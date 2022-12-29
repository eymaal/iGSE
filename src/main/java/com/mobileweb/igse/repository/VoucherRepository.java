package com.mobileweb.igse.repository;

import com.mobileweb.igse.entity.Voucher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends CrudRepository<Voucher, String> {
}
