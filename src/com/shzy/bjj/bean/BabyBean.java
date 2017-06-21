package com.shzy.bjj.bean;

import java.io.Serializable;

public class BabyBean implements Serializable {
	private int id;
	// 宝宝真实姓名
	private String name;
	// 宝宝昵称
	private String nickname;
	// 宝宝生日（格式为: 2013-10-10）
	private String birthday;
	// 宝宝性别（ 0 男 1 女）
	private int sex;
	// 宝宝身份证号
	private String identity;
	// 宝宝头像
	private String pic_url;

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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

}
