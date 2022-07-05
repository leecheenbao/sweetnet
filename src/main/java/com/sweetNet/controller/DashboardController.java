package com.sweetNet.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sweetNet.model.Bulletin;
import com.sweetNet.model.Dashboard;
import com.sweetNet.service.BulletinService;
import com.sweetNet.service.DashboardService;
import com.sweetNet.until.ImageUntil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "DashboardController")
@RestController
@RequestMapping("/api")
public class DashboardController {

	@Autowired
	private BulletinService bulletinService;
	@Autowired
	private DashboardService dashboardService;

	@ApiOperation("取得全部公告訊息（含不顯示）")
	@GetMapping(value = "/bulletin")
	protected List<Bulletin> getAllBulletin() {
		bulletinService.findAll();
		return bulletinService.findAll();
	}

	@ApiOperation("僅顯示公開訊息")
	@GetMapping(value = "/publicbulletin")
	protected List<Bulletin> getBulletinByStates() {
		return bulletinService.findBySates(0);
	}

	@ApiOperation("取得單一筆公告")
	@GetMapping(value = "/bulletin/{id}")
	protected Bulletin getOneBulletinById(@PathVariable Integer id) {
		return bulletinService.findOne(id);
	}

	@ApiOperation("新增公告訊息(id、updateTime、states不填其他為必填)")
	@PostMapping(value = "/bulletin")
	protected List<Bulletin> insertBulletin(@RequestBody Bulletin bulletin) {
		bulletinService.save(bulletin);
		return bulletinService.findAll();
	}

	@ApiOperation("編輯公告訊息")
	@PutMapping(value = "/bulletin")
	protected Bulletin updateBulletin(@RequestBody Bulletin bulletin) {
		bulletin.setUpdateTime(new Date());
		bulletinService.save(bulletin);
		return bulletin;
	}

	@ApiOperation("取得面板顯示")
	@GetMapping(value = "/dashboard")
	protected List<Dashboard> getDashboard() {
		return dashboardService.findAll();
	}

	@ApiOperation("更新面板顯示")
	@PutMapping(value = "/dashboard")
	protected List<Dashboard> updateDashboard(@RequestBody Dashboard d) {
		Dashboard dashboard = dashboardService.findOne(d.getCode());

		if (d.getName().contains("IMG")) {
			String URL = ImageUntil.GenerateDashboardImage(d.getCode(), d.getContent());
			dashboard.setContent(URL);
		} else {
			dashboard.setContent(d.getContent());
		}
		dashboard.setName(d.getName());
		dashboardService.save(dashboard);
		return dashboardService.findAll();

	}
	
	@ApiOperation("刪除公告")
	@DeleteMapping(value = "/bulletin/{id}")
	protected List<Bulletin> deleteBulletinById(@PathVariable Integer id) {
		 bulletinService.delete(id);
		return bulletinService.findAll();
	}
}
