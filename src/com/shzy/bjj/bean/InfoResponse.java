package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class InfoResponse implements Serializable {
	private int count;
	private List<InfoBean> list;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<InfoBean> getList() {
		return list;
	}

	public void setList(List<InfoBean> list) {
		this.list = list;
	}

}
