package com.product_comparator.productcomparator.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class UserAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String userEmail;
    private LocalDate dateAdded;
    @Column(name = "sent")
    private Boolean sent=false;
    @Column(name = "date_sent")
    private LocalDate dateSent=null;
    private BigDecimal priceSetpoint;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    private Product product;

}
