package com.mobileweb.igse.repository;

import com.mobileweb.igse.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String> {

    @Query(value = "SELECT COUNT(c) FROM Customer c WHERE c.property_type=?1")
    public int countByPropertyType(String propertyType);

    @Query(value = "SELECT c FROM Customer c WHERE c.property_type=?1 AND c.bedroom_num=?2")
    public List<Customer> findCustomersByPropertyTypeAndBedrooms(String propertyType, int bedroomNum);

}
