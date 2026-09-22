package com.rohitmunde.trustdesk.entity;

// {"sku": "BG-AIRPODS-01", "name": "BlueBuds Air", "quantity": 1, "category": "audio", "final_sale": false}

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer quantity;

    @Column
    private String category;

    @Column(name = "final_sale",nullable = false)
    private Boolean finalSale;
}
