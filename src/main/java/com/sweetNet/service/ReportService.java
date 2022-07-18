package com.sweetNet.service;

import java.util.List;

import com.sweetNet.model.Report;

public interface ReportService {

	List<Report> findAll();

	Report findOne(Integer id);

	List<Report> findBySates();

	void save(Report report);

	void delete(Integer id);
}
