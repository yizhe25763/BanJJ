package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class CouponMoneyResponse implements Serializable {
	private int result;
	private int count;
	private List<VoucherBean> list;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<VoucherBean> getList() {
		return list;
	}

	public void setList(List<VoucherBean> list) {
		this.list = list;
	}

}
