package com.spring.customermanagementservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest implements Serializable {

    private static final long serialVersionUID = 3412341191167496142L;

    private String accountNumber;
    private int amount;

}
