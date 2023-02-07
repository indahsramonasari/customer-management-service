package com.spring.customermanagementservice.webcontroller;

import com.spring.customermanagementservice.domain.TransactionRequest;
import com.spring.customermanagementservice.domain.CustomerRequest;
import com.spring.customermanagementservice.domain.Response;
import com.spring.customermanagementservice.service.TransactionService;
import com.spring.customermanagementservice.service.CustomerManagementService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
@RequestMapping("/v1")
public class CustomerWebController {

    @Autowired
    CustomerManagementService customerManagementService;

    @Autowired
    TransactionService transactionService;

    @PostMapping(value = "/pendaftarannasabah", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response addCustomer(@RequestBody CustomerRequest customerRequest) throws Exception {
        return customerManagementService.addCustomer(customerRequest);
    }

    @PostMapping(value = "/approvalnasabah", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response approvalCustomer(@RequestBody CustomerRequest customerRequest) throws Exception {
        return customerManagementService.approvalCustomer(customerRequest);
    }

    @PostMapping(value = "/rejectnasabah", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response rejectCustomer(@RequestBody CustomerRequest customerRequest) throws Exception {
        return customerManagementService.rejectCustomer(customerRequest);
    }

    @PostMapping(value = "/tariktunai", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response cashWithdrawal(@RequestBody TransactionRequest transactionRequest) throws Exception {
        return transactionService.cashWithdrawal(transactionRequest);
    }

    @PostMapping(value = "/setortunai", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response cashDeposit(@RequestBody TransactionRequest transactionRequest) throws Exception {
        return transactionService.cashDeposit(transactionRequest);
    }

    @PostMapping(value = "/ceksaldo", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response inquiry(@RequestBody TransactionRequest transactionRequest) throws Exception {
        return transactionService.inquiry(transactionRequest);
    }


}
