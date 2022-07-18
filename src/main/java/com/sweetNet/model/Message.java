package com.sweetNet.model;

import java.io.Serializable;
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
@Table(name = "MESSAGE")
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	@ApiModelProperty("更新時間-系統自增")
	@CreationTimestamp
	@Column(name = "pdate")
	private Date pdate;

	@Column(name = "states")
	private Integer states = 0;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPdate() {
		return pdate;
	}

	public void setPdate(Date pdate) {
		this.pdate = pdate;
	}

	public Integer getId() {
		return id;
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

	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}

}