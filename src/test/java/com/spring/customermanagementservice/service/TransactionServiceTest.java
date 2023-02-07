package com.spring.customermanagementservice.service;

import com.spring.customermanagementservice.domain.Response;
import com.spring.customermanagementservice.domain.TransactionRequest;
import com.spring.customermanagementservice.domain.constant.StatusConstant;
import com.spring.customermanagementservice.model.Customer;
import com.spring.customermanagementservice.repository.CustomerRepository;
import com.spring.customermanagementservice.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TransactionServiceTest {

    @Autowired
    TransactionService transactionService;

    @MockBean
    TransactionRepository transactionRepository;

    @MockBean
    CustomerRepository customerRepository;

    @Before
    public void getCustomer(){
        Customer customer = Customer.builder()
                .nik("3428977899987878")
                .fullName("Tiara Norma")
                .address("Jalan Veteran, Yogyakarta")
                .phoneNumber("087676789878")
                .accountNumber("9878987898")
                .balance(BigDecimal.valueOf(100000000))
                .status(StatusConstant.APPROVED)
                .build();

        Mockito.when(customerRepository.findOneByAccountNumber("9878987898")).thenReturn(customer);
    }

    @Test
    public void inquiry() throws Exception {
        TransactionRequest request = TransactionRequest.builder()
                .accountNumber("9878987898")
                .build();

        Response response = transactionService.inquiry(request);
        assertNotNull(response);
    }

    @Test
    public void tarikTunai() throws Exception {
        TransactionRequest request = TransactionRequest.builder()
                .accountNumber("9878987898")
                .amount(1000000)
                .build();

        Response response = transactionService.cashWithdrawal(request);
        assertNotNull(response);
    }

    @Test
    public void setorTunai() throws Exception {
        TransactionRequest request = TransactionRequest.builder()
                .accountNumber("9878987898")
                .amount(3000000)
                .build();

        Response response = transactionService.cashDeposit(request);
        assertNotNull(response);
    }

}
