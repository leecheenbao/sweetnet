package com.sweetNet.dto;

public class ImagesDTO {
	private Integer id;

	private String memUuid;

	private String image_1;

	private String image_2;

	private String image_3;

	private String image_4;

	private String image_5;

	public ImagesDTO(Integer id, String memUuid, String image_1, String image_2, String image_3, String image_4,
			String image_5) {
		super();
		this.id = id;
		this.memUuid = memUuid;
		this.image_1 = image_1;
		this.image_2 = image_2;
		this.image_3 = image_3;
		this.image_4 = image_4;
		this.image_5 = image_5;
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

	public String getImage_1() {
		return image_1;
	}

	public void setImage_1(String image_1) {
		this.image_1 = image_1;
	}

	public String getImage_2() {
		return image_2;
	}

	public void setImage_2(String image_2) {
		this.image_2 = image_2;
	}

	public String getImage_3() {
		return image_3;
	}

	public void setImage_3(String image_3) {
		this.image_3 = image_3;
	}

	public String getImage_4() {
		return image_4;
	}

	public void setImage_4(String image_4) {
		this.image_4 = image_4;
	}

	public String getImage_5() {
		return image_5;
	}

	public void setImage_5(String image_5) {
		this.image_5 = image_5;
	}

}
