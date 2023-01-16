package com.mobileweb.igse.repository;

import com.mobileweb.igse.entity.Reading;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingRepository extends CrudRepository<Reading,Integer> {

    @Query(value = "SELECT * FROM Reading r WHERE r.customer_id=?1 ORDER BY r.submission_date DESC LIMIT 1", nativeQuery = true)
    public Optional<Reading> findLatestReading(String customer_id);

    @Query(value = "SELECT r FROM Reading r WHERE r.customer_id=?1 ORDER BY r.submission_date DESC")
    public List<Reading> findAllByCustomerId(String customer_id);

    @Query(value = "SELECT r FROM Reading r WHERE r.customer_id=?1 AND r.status='pending' ORDER BY r.submission_date DESC")
    public List<Reading> findAllPendingByCustomerId(String customer_id);

    @Query(value = "SELECT r FROM Reading r ORDER BY r.customer_id ASC, r.submission_date DESC")
    public  List<Reading> getAllReadingsSorted();

    @Query(value = "SELECT * FROM Reading r WHERE r.customer_id=?1 AND r.status='paid' ORDER BY r.submission_date DESC LIMIT 1", nativeQuery = true)
    public Optional<Reading> findLastPaidReading(String customer_id);

    @Query(value = "SELECT * FROM Reading r WHERE r.customer_id=?1 AND r.status='paid' ORDER BY r.submission_date DESC LIMIT 2", nativeQuery = true)
    public List<Reading> findLatestPaidReadings(String customer_id);

    @Query(value = "SELECT * FROM Reading r WHERE r.customer_id=?1 ORDER BY r.submission_date DESC LIMIT 1", nativeQuery = true)
    public Reading findLatestReadingByCustomerId(String customer_id);

    @Query(value = "SELECT * FROM Reading r WHERE r.customer_id=?1 ORDER BY r.submission_date LIMIT 1", nativeQuery = true)
    public Reading findOldestReadingByCustomerId(String customer_id);

    @Query(value = "SELECT r FROM Reading r WHERE r.customer_id=?1 AND r.submission_date=?2")
    public Reading findReadingByCustomerIdAndSubmissionDate(String customer_id, Date submission_date);

    public List<Reading> findReadingByStatus(String status);
}
