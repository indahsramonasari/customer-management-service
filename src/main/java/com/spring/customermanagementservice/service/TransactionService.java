package com.spring.customermanagementservice.service;

import com.spring.customermanagementservice.domain.TransactionRequest;
import com.spring.customermanagementservice.domain.TransactionResponse;
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
    public TransactionResponse inquiry(String accountNumber) throws Exception {
        TransactionResponse response = new TransactionResponse();
        try {
            Customer customer = customerRepository.findOneByAccountNumber(accountNumber);
            if (customer == null) throw new Exception("Data nasabah tidak ditemukan");
            response = TransactionResponse.builder()
                    .fullName(customer.getFullName())
                    .accountNumber(customer.getAccountNumber())
                    .balance(customer.getBalance())
                    .build();
        } catch (Exception ex) {
            log.error("Error when get data {}", ex);
            throw new Exception(ex);
        }
        return response;
    }

    //Tarik Tunai
    public TransactionResponse cashWithdrawal(TransactionRequest request) throws Exception {
        TransactionResponse response = new TransactionResponse();
        try {
            TransactionResponse inquiryResponse = inquiry(request.getAccountNumber());
            int existingBalance = inquiryResponse.getBalance().intValueExact();
            if (request.getAmount() > existingBalance) {
                response.setAccountNumber(inquiryResponse.getAccountNumber());
                response.setBalance(inquiryResponse.getBalance());
                response.setMessage("Transaksi gagal, Saldo tidak mencukupi");
            }

            //hitung saldo setelah tarik tunai
            Balance countBalance = new Balance(existingBalance, request.getAmount());
            int finalBalance = countBalance.cashWithdrawal();

            //update saldo nasabah
            updateCustomerData(request, finalBalance);

            //store transaction history
            storeHistory(request, StatusConstant.TARIK_CATEGORY);

            response.setAccountNumber(inquiryResponse.getAccountNumber());
            response.setBalance(BigDecimal.valueOf(finalBalance));
            response.setMessage(StatusConstant.TRANSACTION_MESSAGE_SUCCESS);

        } catch (Exception ex) {
            log.error("Error when save data {}", ex);
            throw new Exception(ex);
        }
        return response;
    }

    //Setor Tunai
    public TransactionResponse cashDeposit(TransactionRequest request) throws Exception {
        TransactionResponse response = new TransactionResponse();
        try {
            TransactionResponse inquiryResponse = inquiry(request.getAccountNumber());
            int existingBalance = inquiryResponse.getBalance().intValueExact();

            //hitung saldo setelah setor tunai
            Balance countBalance = new Balance(existingBalance, request.getAmount());
            int finalBalance = countBalance.cashDeposit();

            //update saldo nasabah
            updateCustomerData(request, finalBalance);

            //store transaction history
            storeHistory(request, StatusConstant.SETOR_CATEGORY);

            response.setAccountNumber(inquiryResponse.getAccountNumber());
            response.setBalance(BigDecimal.valueOf(finalBalance));
            response.setMessage(StatusConstant.TRANSACTION_MESSAGE_SUCCESS);

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
