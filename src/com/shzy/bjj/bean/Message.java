package com.shzy.bjj.bean;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 7491152915368949244L;

	/**
	 * 消息ID
	 */
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
