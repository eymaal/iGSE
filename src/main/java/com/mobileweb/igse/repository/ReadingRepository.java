package com.mobileweb.igse.repository;

import com.mobileweb.igse.entity.Reading;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingRepository extends CrudRepository<Reading,Integer> {

    @Query(value = "SELECT * FROM Reading r WHERE r.customer_id=?1 ORDER BY r.submission_date DESC LIMIT 1", nativeQuery = true)
    public Optional<Reading> findLatestReading(String customer_id);

    @Query(value = "SELECT r FROM Reading r WHERE r.customer_id=?1 ORDER BY r.submission_date DESC")
    public List<Reading> findAllByCustomerId(String customer_id);

}
