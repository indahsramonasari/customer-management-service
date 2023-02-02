package com.spring.customermanagementservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse implements Serializable {

    private static final long serialVersionUID = -5442352405843653647L;

    private String fullName;
    private String accountNumber;
    private BigDecimal balance;
    private String message;

}
