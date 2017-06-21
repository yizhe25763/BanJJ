package com.shzy.bjj.bean;

import java.io.Serializable;

public class CannelOrderBean implements Serializable {

	private int result;
	private int failue_reason_type;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getFailue_reason_type() {
		return failue_reason_type;
	}

	public void setFailue_reason_type(int failue_reason_type) {
		this.failue_reason_type = failue_reason_type;
	}

}
