package org.ankur.inventoryservice.service;

import lombok.extern.slf4j.Slf4j;
import org.ankur.inventoryservice.dto.InventoryResponse;
import org.ankur.inventoryservice.model.Inventory;
import org.ankur.inventoryservice.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;


    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        List<Inventory> inventoryList = inventoryRepository.findBySkuCodeIn(skuCode);

        inventoryList.forEach(inventory ->
                logger.info("SkuCode: {}, Quantity: {}", inventory.getSkuCode(), inventory.getQuantity())
        );

        return inventoryList.stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .availableQuantity(inventory.getQuantity())  // <- include quantity
                                .build()
                ).toList();
    }

}
