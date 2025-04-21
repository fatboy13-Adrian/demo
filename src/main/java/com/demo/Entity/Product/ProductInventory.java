package com.demo.Entity.Product;

import com.demo.Entity.Inventory.Inventory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "psid")
    private Long psid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "pid", nullable = false)
    private Product pid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "sid", nullable = false)
    private Inventory sid;
}
