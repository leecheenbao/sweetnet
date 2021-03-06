package com.sweetNet.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MEMBERIMAGE")
public class MemberImage implements Serializable {
	private static long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "memUuid")
	private String memUuid;

	@Column(name = "imageUrl")
	private String imageUrl;

	@Column(name = "seq")
	private int seq;

	public MemberImage(Integer id, String memUuid, String imageUrl, int seq) {
		super();
		this.id = id;
		this.memUuid = memUuid;
		this.imageUrl = imageUrl;
		this.seq = seq;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static void setSerialVersionUID(long serialVersionUID) {
		MemberImage.serialVersionUID = serialVersionUID;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public MemberImage() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMemUuid() {
		return memUuid;
	}

	public void setMemUuid(String memUuid) {
		this.memUuid = memUuid;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
