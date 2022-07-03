package com.sweetNet.service;

import java.util.List;

import com.sweetNet.model.Dashboard;

public interface DashboardService {

	List<Dashboard> findAll();

	void save(Dashboard dashboard);

	Dashboard findOne(String code);
}
