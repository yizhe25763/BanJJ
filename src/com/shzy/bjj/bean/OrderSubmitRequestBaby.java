package com.shzy.bjj.bean;

import java.io.Serializable;

public class OrderSubmitRequestBaby implements Serializable {
	private int id;

	public OrderSubmitRequestBaby(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
