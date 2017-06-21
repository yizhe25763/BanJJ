package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class TeacherFollowResponse implements Serializable {
	private List<TeacherFollowBean> list;
	private int count;

	public List<TeacherFollowBean> getList() {
		return list;
	}

	public void setList(List<TeacherFollowBean> list) {
		this.list = list;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
