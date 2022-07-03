package com.sweetNet.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweetNet.model.Dashboard;
import com.sweetNet.repository.DashboardRepository;
import com.sweetNet.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {
	@Autowired
	private DashboardRepository dashboardRepository;

	@Override
	public List<Dashboard> findAll() {
		return dashboardRepository.findAll();
	}

	@Override
	public void save(Dashboard Dashboard) {

		dashboardRepository.save(Dashboard);
	}

	@Override
	public Dashboard findOne(String code) {
		return dashboardRepository.findById(code).get();
	}

}