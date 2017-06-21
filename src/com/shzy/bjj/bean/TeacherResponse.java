package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class TeacherResponse implements Serializable {
	private int count;
	private List<TeacherBean> list;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<TeacherBean> getList() {
		return list;
	}

	public void setList(List<TeacherBean> list) {
		this.list = list;
	}

}
