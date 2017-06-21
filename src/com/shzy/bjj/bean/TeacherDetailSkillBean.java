package com.shzy.bjj.bean;

import java.io.Serializable;

public class TeacherDetailSkillBean implements Serializable {

	private String descb;
	/**
	 * 证书类型
	 */
	private int type;
	/**
	 * 正式编号
	 */
	private String num;
	/**
	 * 正式名称
	 */
	private String name;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescb() {
		return descb;
	}

	public void setDescb(String descb) {
		this.descb = descb;
	}


}
