package com.spring.customermanagementservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStatus {

    private String  responseCode;
    private String  responseStatus;
    private String  message;

}