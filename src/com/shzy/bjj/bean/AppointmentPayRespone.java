package com.shzy.bjj.bean;

import java.io.Serializable;

public class AppointmentPayRespone implements Serializable {
	/**
	 * 订单编号
	 */
	private String order_number;
	/**
	 * 支付编号
	 */
	private String pay_number;
	/**
	 * 回调地址
	 */
	private String notify_url;

	private int result;
	/**
	 * 订单最后价格
	 */
	private int total_price;
	/**
	 * 抵用卷折扣金额
	 */
	private int coupon_money;
	/**
	 * 积分抵扣金额
	 */
	private int score_money;
	/**
	 * 订单折扣金额
	 */
	private int discount_money;

	private int original_price;

	public String getPay_number() {
		return pay_number;
	}

	public void setPay_number(String pay_number) {
		this.pay_number = pay_number;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getTotal_price() {
		return total_price;
	}

	public void setTotal_price(int total_price) {
		this.total_price = total_price;
	}

	public int getCoupon_money() {
		return coupon_money;
	}

	public void setCoupon_money(int coupon_money) {
		this.coupon_money = coupon_money;
	}

	public int getScore_money() {
		return score_money;
	}

	public void setScore_money(int score_money) {
		this.score_money = score_money;
	}

	public int getDiscount_money() {
		return discount_money;
	}

	public void setDiscount_money(int discount_money) {
		this.discount_money = discount_money;
	}

	public int getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(int original_price) {
		this.original_price = original_price;
	}

	@Override
	public String toString() {
		return "AppointmentPayRespone [order_number=" + order_number
				+ ", result=" + result + ", total_price=" + total_price
				+ ", coupon_money=" + coupon_money + ", score_money="
				+ score_money + ", discount_money=" + discount_money
				+ ", original_price=" + original_price + "]";
	}

}
