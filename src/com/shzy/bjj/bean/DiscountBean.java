package com.shzy.bjj.bean;

import java.io.Serializable;

public class DiscountBean implements Serializable {

	/**
	 * 折扣名称
	 */
	private String name;
	/**
	 * 折扣类型(1 直接抵扣金额(单 位分) 2 打折 0-100)
	 */
	private int type;
	/**
	 * 折扣力度(比如:当 type=1 时 500 表示便宜 5 元;当 type=2 时 80表示打8折)
	 */
	private int aggressive;
	/**
	 * 服务 id(0 表示不限)
	 */
	private int service_id;
	/**
	 * 折扣所在城市名(如:西安市) ALL 表示不限
	 */
	private String city;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAggressive() {
		return aggressive;
	}

	public void setAggressive(int aggressive) {
		this.aggressive = aggressive;
	}

	public int getService_id() {
		return service_id;
	}

	public void setService_id(int service_id) {
		this.service_id = service_id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
