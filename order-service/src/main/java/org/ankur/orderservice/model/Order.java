package org.ankur.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "t_orders")
@Data
//@AllArgsConstructor
//@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String orderNumber;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderLineItems> orderLineItems;

    public Order(Long id, String orderNumber, List<OrderLineItems> orderLineItems) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.orderLineItems = orderLineItems;
    }
    public Order(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<OrderLineItems> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(List<OrderLineItems> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
