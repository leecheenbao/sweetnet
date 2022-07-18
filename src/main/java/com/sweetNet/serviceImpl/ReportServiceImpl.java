package com.sweetNet.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweetNet.model.Report;
import com.sweetNet.repository.ReportRepository;
import com.sweetNet.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {
	@Autowired
	private ReportRepository reportRepository;

	@Override
	public List<Report> findAll() {
		return reportRepository.findAllOrderByPostTime();
	}

	@Override
	public void save(Report report) {
		reportRepository.save(report);
	}

	@Override
	public Report findOne(Integer id) {
		return reportRepository.findById(id);
	}

	@Override
	public List<Report> findBySates() {
		return reportRepository.findByStates();
	}

	@Override
	public void delete(Integer id) {
		Report report = reportRepository.findById(id);
		if (report != null) {
			reportRepository.delete(report);
		}
	}

}