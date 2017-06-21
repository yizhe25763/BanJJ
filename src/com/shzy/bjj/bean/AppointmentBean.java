package com.shzy.bjj.bean;

import java.io.Serializable;

public class AppointmentBean implements Serializable {
	private int id;
	private String name;
	private String mobile;
	private String telphone;
	private String address;
	private String address_room;

	private String business_district;

	public String getBusiness_district() {
		return business_district;
	}

	public void setBusiness_district(String business_district) {
		this.business_district = business_district;
	}

	// 是否默认 0：否 1：是
	private int is_default;
	
	public String getAddress_room() {
		return address_room;
	}

	public void setAddress_room(String address_room) {
		this.address_room = address_room;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getIs_default() {
		return is_default;
	}

	public void setIs_default(int is_default) {
		this.is_default = is_default;
	}

}
