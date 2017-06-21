package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class UserBean implements Serializable {
	// 电话号码
	private String phone;
	// 头像
	private String pic_url;
	// 真实姓名
	private String name;
	// 身份证号
	private String identity;
	// 性别（ 0 男 1 女）
	private int sex;
	// 宝宝数量
	private int baby_count;
	// 宝宝信息
	private List<BabyBean> baby_list;

	public String getPhone() {
		if (phone == null) {
			phone = "";
		}
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public String getName() {
		if (name == null) {
			name = "";
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentity() {
		if (identity == null) {
			identity = "";
		}
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getBaby_count() {
		return baby_count;
	}

	public void setBaby_count(int baby_count) {
		this.baby_count = baby_count;
	}

	public List<BabyBean> getBaby_list() {
		return baby_list;
	}

	public void setBaby_list(List<BabyBean> baby_list) {
		this.baby_list = baby_list;
	}

}
