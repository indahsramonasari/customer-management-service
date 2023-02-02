package com.spring.customermanagementservice.webcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.customermanagementservice.CustomerManagementServiceApplication;
import com.spring.customermanagementservice.domain.ResponseStatus;
import com.spring.customermanagementservice.domain.TransactionRequest;
import com.spring.customermanagementservice.domain.CustomerRequest;
import com.spring.customermanagementservice.domain.TransactionResponse;
import com.spring.customermanagementservice.domain.constant.StatusConstant;
import com.spring.customermanagementservice.service.TransactionService;
import com.spring.customermanagementservice.service.CustomerManagementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerManagementServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomerWebControllerTest {

    @Autowired
    CustomerWebController customerWebController;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerManagementService customerManagementService;

    @MockBean
    TransactionService transactionService;

    @Test
    public void addCustomer() throws Exception {
        CustomerRequest request = CustomerRequest.builder()
                .fullName("Budianto")
                .address("Jalan Cempaka, Sleman, Yogyakarta")
                .nik("3403109877897779")
                .phoneNumber("085767677876")
                .build();

        ResponseStatus response = new ResponseStatus("00", "SUCCESS",
                "Pendaftaran berhasil dilakukan, status nasabah pending menunggu proses aprroval");

        Mockito.when(customerManagementService.addCustomer(request)).thenReturn(response);

        this.mockMvc.perform(post("/v1/pendaftarannasabah")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    public void approvalCustomer() throws Exception {
        ResponseStatus response = new ResponseStatus("00", "SUCCESS",
                "Proses approval berhasil, rekening nasabah berhasil dibuat");

        Mockito.when(customerManagementService.approvalCustomer("3403109877897779")).thenReturn(response);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/approvalnasabah/3403109877897779"))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    public void rejectCustomer() throws Exception {
        ResponseStatus response = new ResponseStatus("00", "SUCCESS",
                "Proses reject berhasil, status nasabah reject");

        Mockito.when(customerManagementService.rejectCustomer("3403109877897767")).thenReturn(response);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/rejectnasabah/3403109877897767"))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    public void cashWidrawal() throws Exception {
        TransactionRequest request = TransactionRequest.builder()
                .accountNumber("1234987652")
                .amount(1000000)
                .build();

        TransactionResponse response = new TransactionResponse("Budianto","1234987652",
                BigDecimal.valueOf(11000000), StatusConstant.TRANSACTION_MESSAGE_SUCCESS);

        Mockito.when(transactionService.cashWithdrawal(request)).thenReturn(response);

        this.mockMvc.perform(post("/v1/tariktunai")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    public void cashDeposit() throws Exception {
        TransactionRequest request = TransactionRequest.builder()
                .accountNumber("1234987652")
                .amount(2000000)
                .build();

        TransactionResponse response = new TransactionResponse("Budianto","1234987652",
                BigDecimal.valueOf(13000000), StatusConstant.TRANSACTION_MESSAGE_SUCCESS);

        Mockito.when(transactionService.cashDeposit(request)).thenReturn(response);

        this.mockMvc.perform(post("/v1/setortunai")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void inquiry() throws Exception {
        TransactionResponse response = new TransactionResponse("Budianto","1234565432",
                BigDecimal.valueOf(1000000),null);

        Mockito.when(transactionService.inquiry("8978673458")).thenReturn(response);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/ceksaldo/8978673458"))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(response)))
                .andExpect(status().isOk());
    }

}
