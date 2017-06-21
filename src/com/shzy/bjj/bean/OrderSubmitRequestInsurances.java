package com.shzy.bjj.bean;

import java.io.Serializable;

public class OrderSubmitRequestInsurances implements Serializable {
	private String name;
	private String identity;

	public OrderSubmitRequestInsurances(String name, String identity) {
		this.name = name;
		this.identity = identity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

}
