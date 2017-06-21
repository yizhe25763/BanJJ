package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class OrderResponse implements Serializable {
	private int count;
	private List<OrderBean> list;
	private int order_status;
	

	public int getOrder_status() {
		return order_status;
	}

	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<OrderBean> getList() {
		return list;
	}

	public void setList(List<OrderBean> list) {
		this.list = list;
	}

}
