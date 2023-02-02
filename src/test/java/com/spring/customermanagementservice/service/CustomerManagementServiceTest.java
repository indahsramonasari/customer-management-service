package com.spring.customermanagementservice.service;

import com.spring.customermanagementservice.domain.CustomerRequest;
import com.spring.customermanagementservice.domain.ResponseStatus;
import com.spring.customermanagementservice.domain.constant.StatusConstant;
import com.spring.customermanagementservice.model.Customer;
import com.spring.customermanagementservice.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles()
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerManagementServiceTest {

    @Autowired
    CustomerManagementService customerManagementService;

    @MockBean
    CustomerRepository customerRepository;

    @Test
    public void addCustomer() throws Exception {
        CustomerRequest request = CustomerRequest.builder()
                .fullName("Budianto")
                .address("Jalan Cempaka, Sleman, Yogyakarta")
                .nik("3403109877897779")
                .phoneNumber("085767677876")
                .build();

        ResponseStatus response = customerManagementService.addCustomer(request);
        assertNotNull(response);
    }

    @Test
    public void approveCustomer() throws Exception {
        com.spring.customermanagementservice.model.Customer customer = Customer.builder()
                .nik("3403109877897779")
                .fullName("Tia Dina")
                .address("Jalan Veteran, Yogyakarta")
                .phoneNumber("087676789878")
                .accountNumber("9876567898")
                .balance(BigDecimal.valueOf(100000000))
                .status(StatusConstant.APPROVED)
                .build();

        Mockito.when(customerRepository.findOneByNik("3403109877897779")).thenReturn(customer);
        ResponseStatus response = customerManagementService.approvalCustomer("3403109877897779");
        assertNotNull(response);
    }

    @Test
    public void rejectCustomer() throws Exception {
        com.spring.customermanagementservice.model.Customer customer = Customer.builder()
                .nik("3403109877897779")
                .fullName("Tia Dina")
                .address("Jalan Veteran, Yogyakarta")
                .phoneNumber("087676789878")
                .accountNumber("9876567898")
                .balance(BigDecimal.valueOf(100000000))
                .status(StatusConstant.PENDING)
                .build();

        Mockito.when(customerRepository.findOneByNik("3403109877897779")).thenReturn(customer);

        ResponseStatus response = customerManagementService.rejectCustomer("3403109877897779");
        assertNotNull(response);
    }
}
