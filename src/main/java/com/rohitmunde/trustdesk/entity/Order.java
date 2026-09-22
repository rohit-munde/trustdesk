package com.rohitmunde.trustdesk.entity;

import com.rohitmunde.trustdesk.enums.OrderStatus;
import com.rohitmunde.trustdesk.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *  {
 *     "order_id": "ord_5001",
 *     "customer_id": "cus_1001",
 *     "status": "delivered",
 *     "placed_at": "2026-06-20",
 *     "delivered_at": "2026-06-24",
 *     "eligible_return_until": "2026-07-01",
 *     "total": 8999,
 *     "currency": "INR",
 *     "payment_status": "paid",
 *     "tracking_number": "BLUETRK10001",
 *     "items": [
 *       {"sku": "BG-AIRPODS-01", "name": "BlueBuds Air", "quantity": 1, "category": "audio", "final_sale": false}
 *     ]
 *   },
 * */
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column
    private OrderStatus status;

    @Column(name = "placed_at", nullable = false)
    private OffsetDateTime placedAt;

    @Column(name = "delivered_at")
    private OffsetDateTime deliveredAt;

    @Column(name = "eligible_return_until")
    private OffsetDateTime eligibleReturnUntil;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem orderItem) {
        this.items.add(orderItem);
        orderItem.setOrder(this);
    }
}
