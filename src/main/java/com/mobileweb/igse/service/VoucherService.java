package com.mobileweb.igse.service;

import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.entity.Voucher;
import com.mobileweb.igse.repository.CustomerRepository;
import com.mobileweb.igse.repository.VoucherRepository;
import com.mobileweb.igse.utility.Responses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoucherService {

    @Autowired
    LoginService loginService;

    @Autowired
    VoucherRepository voucherRepository;
    @Autowired
    CustomerRepository customerRepository;

    public ResponseEntity redeemCode(Customer customer, String evcCode) {
        try{
            Optional<Customer> optionalCustomer = customerRepository.findById(customer.getCustomer_id());
            if(optionalCustomer.isEmpty()){
                throw new Exception("Customer does not exist. Register");
            }
            Optional<Voucher> optionalVoucher = voucherRepository.findById(evcCode);
            if(optionalVoucher.isEmpty()){
                throw new Exception("Voucher code is invalid.");
            }
            if(optionalVoucher.get().getUsed()==1){
                throw new Exception("Voucher code is already redeemed.");
            }
            Voucher voucher = optionalVoucher.get();
            customer = optionalCustomer.get();
            customer.setBalance(customer.getBalance()+200);
            customerRepository.save(customer);
            voucher.setUsed(1);
            voucherRepository.save(voucher);
            return ResponseEntity.ok(customer);
        } catch (Exception e){
            return Responses.makeBadRequest(e.getMessage());
        }
    }
}
