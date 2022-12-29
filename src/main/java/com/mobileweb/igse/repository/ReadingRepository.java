package com.mobileweb.igse.repository;

import com.mobileweb.igse.entity.Reading;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ReadingRepository extends CrudRepository<Reading,Integer> {

    @Query(value = "SELECT MAX(r.submission_date) FROM Reading r WHERE  r.customer_id=?1")
    public Optional<Date> findLatestReadingDate(String customer_id);
}
