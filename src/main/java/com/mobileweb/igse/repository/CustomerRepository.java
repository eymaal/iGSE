package com.mobileweb.igse.repository;

import com.mobileweb.igse.entity.Customer;
import com.mobileweb.igse.entity.Reading;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {

    @Query(value = "SELECT COUNT(c) FROM Customer c WHERE c.property_type=?1")
    public int countByPropertyType(String propertyType);

}
