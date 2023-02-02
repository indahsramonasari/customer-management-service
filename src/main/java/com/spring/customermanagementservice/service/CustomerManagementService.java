package com.spring.customermanagementservice.service;

import com.spring.customermanagementservice.domain.CustomerRequest;
import com.spring.customermanagementservice.domain.ResponseStatus;
import com.spring.customermanagementservice.domain.constant.StatusConstant;
import com.spring.customermanagementservice.model.Customer;
import com.spring.customermanagementservice.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
@Slf4j
public class CustomerManagementService {

    @Autowired
    private CustomerRepository customerRepository;

    // Pendaftaran nasabah
    public ResponseStatus addCustomer(CustomerRequest request) throws Exception {
        try {
            Customer customerExisting = customerRepository.findOneByNikAndPhoneNumber(request.getNik(), request.getPhoneNumber());
            if (customerExisting != null) {
                return ResponseStatus.builder()
                        .responseCode(StatusConstant.RESPONSE_CODE_FAILED)
                        .responseStatus(StatusConstant.STATUS_FAILED)
                        .message("Pendaftaran gagal, nik dan nomor telepon telah terdaftar")
                        .build();
            }

            Customer customer = Customer.builder()
                    .fullName(request.getFullName())
                    .address(request.getAddress())
                    .nik(request.getNik())
                    .phoneNumber(request.getPhoneNumber())
                    .status(StatusConstant.PENDING)
                    .build();

            customerRepository.save(customer);

        } catch (Exception ex) {
            log.error("Error when save data {}", ex);
            throw new Exception(ex);
        }

        return ResponseStatus.builder()
                .responseCode(StatusConstant.RESPONSE_CODE_SUCCESS)
                .responseStatus(StatusConstant.STATUS_SUCCESS)
                .message("Pendaftaran berhasil dilakukan, status nasabah pending menunggu proses aprroval")
                .build();
    }

    // Approval dan update data nasabah
    public ResponseStatus approvalCustomer(String nik) throws Exception {
        String accountNumber = "";
        try {
            Customer customer = customerRepository.findOneByNik(nik);
            if (customer == null ) {
                return ResponseStatus.builder()
                        .responseCode(StatusConstant.RESPONSE_CODE_FAILED)
                        .responseStatus(StatusConstant.STATUS_FAILED)
                        .message("NIK belum terdaftar, silahkan melakukan pendaftaran nasabah")
                        .build();
            }

            //generate random account number
            accountNumber = String.valueOf(generateAccountNumber());

            //update status dan data nasabah
            customer.setStatus(StatusConstant.APPROVED);
            customer.setBalance(BigDecimal.valueOf(0));
            customer.setAccountNumber(accountNumber);
            customerRepository.save(customer);

        } catch (Exception ex) {
            log.error("Error when update data {}", ex);
            throw new Exception(ex);
        }

        return ResponseStatus.builder()
                .responseCode(StatusConstant.RESPONSE_CODE_SUCCESS)
                .responseStatus(StatusConstant.STATUS_SUCCESS)
                .message("Proses approval berhasil, nomor rekening nasabah : ".concat(accountNumber))
                .build();
    }

    // Reject dan update data nasabah
    public ResponseStatus rejectCustomer(String nik) throws Exception {
        try {
            Customer customer = customerRepository.findOneByNik(nik);
            if (customer == null) {
                return ResponseStatus.builder()
                        .responseCode(StatusConstant.RESPONSE_CODE_FAILED)
                        .responseStatus(StatusConstant.STATUS_FAILED)
                        .message("NIK belum terdaftar, data nasabah tidak ditemukan")
                        .build();
            }

            if (customer.getStatus() != "PENDING") {
                return ResponseStatus.builder()
                        .responseCode(StatusConstant.RESPONSE_CODE_FAILED)
                        .responseStatus(StatusConstant.STATUS_FAILED)
                        .message("Status nasabah Approved, status nasabah tidak dapat diubah")
                        .build();
            }

            //update status nasabah menjadi REJECT
            customer.setStatus(StatusConstant.REJECT);
            customerRepository.save(customer);
        } catch (Exception ex) {
            log.error("Error when update data {}", ex);
            throw new Exception(ex);
        }

        return ResponseStatus.builder()
                .responseCode(StatusConstant.RESPONSE_CODE_SUCCESS)
                .responseStatus(StatusConstant.STATUS_SUCCESS)
                .message("Proses reject berhasil, status nasabah reject")
                .build();
    }

    private int generateAccountNumber() {
        Random random = new Random(System.currentTimeMillis());
        return 1000000000 + random.nextInt(2000000000);
    }

}
