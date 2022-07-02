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
@Table(name = "BULLETIN")
public class Bulletin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty("公告ID-系統自增")
	@Column(name = "id")
	private Integer id;

	@ApiModelProperty("標題")
	@NotNull
	@Column(name = "title")
	private String title;

	@ApiModelProperty("內容")
	@NotNull
	@Column(name = "content")
	private String content;

	@ApiModelProperty("上架時間(2022-07-01 10:00:00)")
	@NotNull
	@Column(name = "postTime")
	private Date postTime;

	@ApiModelProperty("更新時間-系統自增")
	@CreationTimestamp
	@Column(name = "updateTime")
	private Date updateTime;

	@ApiModelProperty("公告狀態(0：預設公開、1：隱藏)-系統自增")
	@Column(name = "states")
	private Integer states = 0;

	public Bulletin() {
		super();
	}

	public Bulletin(Integer id, String title, String content, Date postTime, Date updateTime, Integer states) {
		super();

		this.id = id;
		this.title = title;
		this.content = content;
		this.postTime = postTime;
		this.updateTime = updateTime;
		this.states = states;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}

}