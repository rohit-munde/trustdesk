package com.rohitmunde.trustdesk.entity;

import com.rohitmunde.trustdesk.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

/**
 *   {
 *     "customer_id": "cus_1001",
 *     "name": "Aisha Rao",
 *     "email": "aisha.rao@example.com",
 *     "tier": "gold",
 *     "country": "IN",
 *     "created_at": "2024-05-14",
 *     "verified": true,
 *     "tags": ["loyal_customer", "high_lifetime_value"]
 *   },
 * */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column
    private CustomerTier tier;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column
    private Boolean verified;

    @Column
    private List<String> tags;
}
