package com.spring.customermanagementservice.service;

import com.spring.customermanagementservice.domain.Response;
import com.spring.customermanagementservice.domain.TransactionRequest;
import com.spring.customermanagementservice.domain.constant.StatusConstant;
import com.spring.customermanagementservice.model.TransactionHistory;
import com.spring.customermanagementservice.model.Customer;
import com.spring.customermanagementservice.repository.TransactionRepository;
import com.spring.customermanagementservice.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    //Inquiry
    public Response inquiry(TransactionRequest request) throws Exception {
        Response response = new Response();
        try {
            //validasi request
            if(request.getAccountNumber().isEmpty()) {
                response.setResponseCode(StatusConstant.RESPONSE_CODE_FAILED);
                response.setResponseStatus(StatusConstant.STATUS_FAILED);
                response.setMessage("Input tidak sesuai");
            } else {
                Customer customer = customerRepository.findOneByAccountNumber(request.getAccountNumber());
                if (customer == null) {
                    response.setResponseCode(StatusConstant.RESPONSE_CODE_FAILED);
                    response.setResponseStatus(StatusConstant.STATUS_FAILED);
                    response.setMessage("Data nasabah tidak ditemukan");
                } else {
                    response = Response.builder()
                            .responseCode(StatusConstant.RESPONSE_CODE_SUCCESS)
                            .responseStatus(StatusConstant.STATUS_SUCCESS)
                            .fullName(customer.getFullName())
                            .accountNumber(customer.getAccountNumber())
                            .balance(customer.getBalance())
                            .message(StatusConstant.TRANSACTION_MESSAGE_SUCCESS)
                            .build();
                }
            }
        } catch (Exception ex) {
            log.error("Error when get data {}", ex);
            throw new Exception(ex);
        }
        return response;
    }

    //Tarik Tunai
    public Response cashWithdrawal(TransactionRequest request) throws Exception {
        Response response = new Response();
        try {
            //validasi request
            if (request.getAmount() <= 0 || request.getAccountNumber().isEmpty()) {
                response.setResponseCode(StatusConstant.RESPONSE_CODE_FAILED);
                response.setResponseStatus(StatusConstant.STATUS_FAILED);
                response.setMessage("Transaksi gagal, input tidak sesuai");
            } else {
                Response inquiryResponse = inquiry(request);
                int existingBalance = inquiryResponse.getBalance().intValueExact();
                if (request.getAmount() > existingBalance) {
                    response.setResponseCode(StatusConstant.RESPONSE_CODE_FAILED);
                    response.setResponseStatus(StatusConstant.STATUS_FAILED);
                    response.setAccountNumber(inquiryResponse.getAccountNumber());
                    response.setBalance(inquiryResponse.getBalance());
                    response.setMessage("Transaksi gagal, Saldo tidak mencukupi");
                } else {
                    //hitung saldo setelah tarik tunai
                    Balance countBalance = new Balance(existingBalance, request.getAmount());
                    int finalBalance = countBalance.cashWithdrawal();

                    //update saldo nasabah
                    updateCustomerData(request, finalBalance);

                    //store transaction history
                    storeHistory(request, StatusConstant.TARIK_CATEGORY);

                    response.setResponseCode(StatusConstant.RESPONSE_CODE_SUCCESS);
                    response.setResponseStatus(StatusConstant.STATUS_SUCCESS);
                    response.setAccountNumber(inquiryResponse.getAccountNumber());
                    response.setBalance(BigDecimal.valueOf(finalBalance));
                    response.setMessage(StatusConstant.TRANSACTION_MESSAGE_SUCCESS);
                }
            }

        } catch (Exception ex) {
            log.error("Error when save data {}", ex);
            throw new Exception(ex);
        }
        return response;
    }

    //Setor Tunai
    public Response cashDeposit(TransactionRequest request) throws Exception {
        Response response = new Response();
        try {
            //validasi request
            if (request.getAmount() <= 0 || request.getAccountNumber().isEmpty()) {
                response.setResponseCode(StatusConstant.RESPONSE_CODE_FAILED);
                response.setResponseStatus(StatusConstant.STATUS_FAILED);
                response.setMessage("Transaksi gagal, input tidak sesuai");
            } else {
                Response inquiryResponse = inquiry(request);
                int existingBalance = inquiryResponse.getBalance().intValueExact();

                //hitung saldo setelah setor tunai
                Balance countBalance = new Balance(existingBalance, request.getAmount());
                int finalBalance = countBalance.cashDeposit();

                //update saldo nasabah
                updateCustomerData(request, finalBalance);

                //store transaction history
                storeHistory(request, StatusConstant.SETOR_CATEGORY);

                response.setResponseCode(StatusConstant.RESPONSE_CODE_SUCCESS);
                response.setResponseStatus(StatusConstant.STATUS_SUCCESS);
                response.setAccountNumber(inquiryResponse.getAccountNumber());
                response.setBalance(BigDecimal.valueOf(finalBalance));
                response.setMessage(StatusConstant.TRANSACTION_MESSAGE_SUCCESS);
            }
        } catch (Exception ex) {
            log.error("Error when save data {}", ex);
            throw new Exception(ex);
        }
        return response;
    }

    private void updateCustomerData(TransactionRequest request, int balance) {
        Customer customer = customerRepository.findOneByAccountNumber(request.getAccountNumber());
        customer.setBalance(BigDecimal.valueOf(balance));
        customerRepository.save(customer);
    }

    private void storeHistory(TransactionRequest request, String status) {
        TransactionHistory transHistory = new TransactionHistory();
        transHistory.setDate(new Date());
        transHistory.setAccountNumber(request.getAccountNumber());
        transHistory.setAmount(BigDecimal.valueOf(request.getAmount()));
        transHistory.setTransactionType(status);
        transactionRepository.save(transHistory);
    }
}
