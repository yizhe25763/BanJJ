package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class IntegralResponse implements Serializable {
	// 记录数量
	private int count;
	// 总积分
	private int total;
	private List<IntegralBean> list;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<IntegralBean> getList() {
		return list;
	}

	public void setList(List<IntegralBean> list) {
		this.list = list;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
