package com.mobileweb.igse.service;

import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.entity.Voucher;
import com.mobileweb.igse.exceptions.GeneralException;
import com.mobileweb.igse.repository.CustomerRepository;
import com.mobileweb.igse.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class RegisterService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    public ResponseEntity register(Customer customer, String evcCode) {
        try{
            if(customerRepository.findById(customer.getCustomer_id()).isPresent()){
                throw new Exception("Email Address already exists");
            }
            Optional<Voucher> voucher = voucherRepository.findById(evcCode);
            if(voucher.isEmpty()){
                throw new Exception("Voucher Code is invalid");
            }
            if(voucher.get().getUsed()==1){
                throw new Exception("Voucher Code has already been redeemed");
            }
            Voucher currentVoucher = voucher.get();
            currentVoucher.setUsed(1);
            voucherRepository.save(currentVoucher);
            customerRepository.save(customer);
            return new ResponseEntity(customer, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
