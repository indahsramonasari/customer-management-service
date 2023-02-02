package com.spring.customermanagementservice.webcontroller;

import com.spring.customermanagementservice.domain.TransactionRequest;
import com.spring.customermanagementservice.domain.CustomerRequest;
import com.spring.customermanagementservice.domain.ResponseStatus;
import com.spring.customermanagementservice.domain.TransactionResponse;
import com.spring.customermanagementservice.service.TransactionService;
import com.spring.customermanagementservice.service.CustomerManagementService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Api
@Slf4j
@RestController
@RequestMapping("/v1")
public class CustomerWebController {

    @Autowired
    CustomerManagementService customerManagementService;

    @Autowired
    TransactionService transactionService;

    @PostMapping(value = "/pendaftarannasabah", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseStatus addCustomer(@RequestBody CustomerRequest customerRequest) throws Exception {
        return customerManagementService.addCustomer(customerRequest);
    }

    @RequestMapping(value = "/approvalnasabah/{nik}", method = RequestMethod.GET)
    public ResponseStatus approvalCustomer(@PathVariable String nik) throws Exception {
        return customerManagementService.approvalCustomer(nik);
    }

    @RequestMapping(value = "/rejectnasabah/{nik}", method = RequestMethod.GET)
    public ResponseStatus rejectCustomer(@PathVariable String nik) throws Exception {
        return customerManagementService.rejectCustomer(nik);
    }

    @PostMapping(value = "/tariktunai", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionResponse cashWithdrawal(@RequestBody TransactionRequest transactionRequest) throws Exception {
        return transactionService.cashWithdrawal(transactionRequest);
    }

    @PostMapping(value = "/setortunai", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionResponse cashDeposit(@RequestBody TransactionRequest transactionRequest) throws Exception {
        return transactionService.cashDeposit(transactionRequest);
    }

    @RequestMapping(value = "/ceksaldo/{accountNumber}", method = RequestMethod.GET)
    public TransactionResponse inquiry(@PathVariable String accountNumber) throws Exception {
        return transactionService.inquiry(accountNumber);
    }


}
