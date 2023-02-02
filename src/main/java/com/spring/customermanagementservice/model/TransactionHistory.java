package com.spring.customermanagementservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transaction_history")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private String id;

    @Column(name = "date")
    private Date date;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "transaction_type")
    private String transactionType;
}
