package com.sweetNet.dto;

public class ImagesDTO {
	private Integer id;

	private String image;

	public ImagesDTO(Integer id, String image) {
		super();
		this.id = id;
		this.image = image;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
