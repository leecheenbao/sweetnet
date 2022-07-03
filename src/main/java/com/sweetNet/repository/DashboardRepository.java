package com.sweetNet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sweetNet.model.Dashboard;

public interface DashboardRepository extends JpaRepository<Dashboard, String> {

	public Dashboard findByCode(String code);
}