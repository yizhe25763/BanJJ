package com.shzy.bjj.bean;

import java.io.Serializable;

public class UserVoucherBeanVoucherBean implements Serializable {

	/**
	 * 代金券 id
	 */
	private int id;
	/**
	 * 代金券名称
	 */
	private String name;
	/**
	 * 代金券面额(单位分)
	 */
	private int denomination;
	/**
	 * 代金券􏰁述
	 */
	private String descb;
	/**
	 * 代金券创建时间(格式为: 2015-10-10 12:50:01)
	 */
	private String create_time;
	/**
	 * 代金券过期时间(格式为: 2016-10-10 )
	 */
	private String expire_date;
	/**
	 * 代金券状态 1 未使用 2 已使用 ￼￼5 过期 ￼6 锁定中
	 */
	private int status;
	/**
	 * 当前代金券是否被选中
	 */
	private Boolean isSelector = false;

	public Boolean getIsSelector() {
		return isSelector;
	}

	public void setIsSelector(Boolean isSelector) {
		this.isSelector = isSelector;
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

	public int getDenomination() {
		return denomination;
	}

	public void setDenomination(int denomination) {
		this.denomination = denomination;
	}

	public String getDescb() {
		return descb;
	}

	public void setDescb(String descb) {
		this.descb = descb;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getExpire_date() {
		return expire_date;
	}

	public void setExpire_date(String expire_date) {
		this.expire_date = expire_date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
