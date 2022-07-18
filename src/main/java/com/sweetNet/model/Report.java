package com.sweetNet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "REPORT")
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@Column(name = "sendId")
	private String sendId;

	@NotNull
	@Column(name = "recId")
	private String recId;

	@NotNull
	@Column(name = "content")
	private String content;

	@Column(name = "url")
	private String url;

	@Column(name = "states")
	private Integer states = 0;

	@ApiModelProperty("更新時間-系統自增")
	@CreationTimestamp
	@Column(name = "reportDate")
	private Date reportDate;

	public Integer getId() {
		return id;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getRecId() {
		return recId;
	}

	public void setRecId(String recId) {
		this.recId = recId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}
}
