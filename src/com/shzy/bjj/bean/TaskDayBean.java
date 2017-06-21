package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class TaskDayBean implements Serializable {
	private List<TaskDayOrderBean> list;
	private OrderBean orderBean;

	public TaskDayBean(List<TaskDayOrderBean> list, OrderBean orderBean) {
		this.list = list;
		this.orderBean = orderBean;
	}

	public List<TaskDayOrderBean> getList() {
		return list;
	}

	public void setList(List<TaskDayOrderBean> list) {
		this.list = list;
	}

	public OrderBean getOrderBean() {
		return orderBean;
	}

	public void setOrderBean(OrderBean orderBean) {
		this.orderBean = orderBean;
	}

}
