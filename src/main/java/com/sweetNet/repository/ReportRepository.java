package com.sweetNet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sweetNet.model.Report;

public interface ReportRepository extends JpaRepository<Report, String> {
	@Query(value = " SELECT * FROM REPORT WHERE  STATES = 0 ", nativeQuery = true)
	public List<Report> findByStates();

	public Report findById(Integer id);

	@Query(value = "SELECT * FROM REPORT ORDER BY REPORTDATE DESC", nativeQuery = true)
	public List<Report> findAllOrderByPostTime();
}