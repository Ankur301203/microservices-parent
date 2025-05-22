package org.ankur.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ankur.orderservice.model.OrderLineItems;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {
    private Long id;
    private String sKuCode;
    private BigDecimal price;
    private Integer quantity;
}
