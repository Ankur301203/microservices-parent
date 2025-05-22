package org.ankur.orderservice.service;


import org.ankur.orderservice.dto.InventoryResponse;
import org.ankur.orderservice.dto.OrderLineItemsDto;
import org.ankur.orderservice.dto.OrderRequest;
import org.ankur.orderservice.model.Order;
import org.ankur.orderservice.model.OrderLineItems;
import org.ankur.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    public final WebClient.Builder webClient;

    public OrderService(OrderRepository orderRepository, WebClient.Builder webClient) {
        this.orderRepository = orderRepository;
        this.webClient = webClient;
    }

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes = order.getOrderLineItems().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        InventoryResponse[] inventoryResponseArray = webClient.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        Map<String, Integer> skuToAvailableQty = Arrays.stream(inventoryResponseArray)
                .collect(Collectors.toMap(
                        InventoryResponse::getSkuCode,
                        InventoryResponse::getAvailableQuantity
                ));

        for (OrderLineItems item : orderLineItems) {
            String sku = item.getSkuCode();
            int requestedQty = item.getQuantity();
            int availableQty = skuToAvailableQty.getOrDefault(sku, 0);

            if (requestedQty > availableQty) {
                throw new IllegalArgumentException("Not enough stock for SKU: " + sku +
                        ". Requested: " + requestedQty + ", Available: " + availableQty);
            }
        }
        orderRepository.save(order);
    }


    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSKuCode());
        return orderLineItems;
    }
}
