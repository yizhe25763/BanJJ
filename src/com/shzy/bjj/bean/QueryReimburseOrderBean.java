package com.shzy.bjj.bean;

import java.io.Serializable;

public class QueryReimburseOrderBean implements Serializable {

	private int result;
	private int money;
	private int failue_reason_type;
	private int error_code;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getFailue_reason_type() {
		return failue_reason_type;
	}

	public void setFailue_reason_type(int failue_reason_type) {
		this.failue_reason_type = failue_reason_type;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

}
