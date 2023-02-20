package com.spring.customermanagementservice.domain.constant;

public class StatusConstant {

    private StatusConstant() {
        throw new IllegalStateException("StatusConstant class");
    }

    public static final String RESPONSE_CODE_SUCCESS = "00";
    public static final String RESPONSE_CODE_FAILED = "01";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAILED = "FAILED";
    public static final String TARIK_CATEGORY = "TT";
    public static final String SETOR_CATEGORY = "ST";
    public static final String TRANSACTION_MESSAGE_SUCCESS = "Transaksi berhasil";
    public static final String PENDING = "PENDING";
    public static final String APPROVED = "APPROVED";
    public static final String REJECT = "REJECT";

}
