package com.mobileweb.igse.repository;

import com.mobileweb.igse.entity.Bill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends CrudRepository<Bill, String> {
}
