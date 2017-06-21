package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 积分
 * 
 * @author Administrator
 * 
 */
public class IntegralBean implements Serializable {
	private int type;
	private int value;
	private String descb;
	private String time;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDescb() {
		return descb;
	}

	public void setDescb(String descb) {
		this.descb = descb;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
