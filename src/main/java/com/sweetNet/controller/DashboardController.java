package com.sweetNet.controller;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.sweetNet.dto.MemberDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "DashboardController")
@RestController
public class DashboardController {

	@ApiOperation("取得公告訊息")
	@GetMapping(value = "/getBulletin")
	protected String doPost(@RequestBody MemberDTO loginDTO) throws ServletException, IOException {

		Gson gson = new Gson();

		return gson.toJson("");
	}
}
