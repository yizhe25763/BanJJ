package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class OrderSubmitRequest implements Serializable {
	private int contact_id;
	private String order_contact_name;
	private String order_contact_phone;
	private String order_contact_telphone;
	private String memo;
	private int use_score;
	private int use_coupon_id;
	private int pay_type;
	private int original_price;
	private int discount_money;
	private int score_money;
	private int coupon_money;
	private int total_price;
	private List<OrderSubmitRequestBaby> babys;
	private List<OrderSubmitRequestService> services;
	private List<OrderSubmitRequestInsurances> baby_insurances;

	public int getContact_id() {
		return contact_id;
	}

	public void setContact_id(int contact_id) {
		this.contact_id = contact_id;
	}

	public String getOrder_contact_name() {
		return order_contact_name;
	}

	public void setOrder_contact_name(String order_contact_name) {
		this.order_contact_name = order_contact_name;
	}

	public String getOrder_contact_phone() {
		return order_contact_phone;
	}

	public void setOrder_contact_phone(String order_contact_phone) {
		this.order_contact_phone = order_contact_phone;
	}

	public String getOrder_contact_telphone() {
		return order_contact_telphone;
	}

	public void setOrder_contact_telphone(String order_contact_telphone) {
		this.order_contact_telphone = order_contact_telphone;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getUse_score() {
		return use_score;
	}

	public void setUse_score(int use_score) {
		this.use_score = use_score;
	}

	public int getUse_coupon_id() {
		return use_coupon_id;
	}

	public void setUse_coupon_id(int use_coupon_id) {
		this.use_coupon_id = use_coupon_id;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	public int getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(int original_price) {
		this.original_price = original_price;
	}

	public int getDiscount_money() {
		return discount_money;
	}

	public void setDiscount_money(int discount_money) {
		this.discount_money = discount_money;
	}

	public int getScore_money() {
		return score_money;
	}

	public void setScore_money(int score_money) {
		this.score_money = score_money;
	}

	public int getCoupon_money() {
		return coupon_money;
	}

	public void setCoupon_money(int coupon_money) {
		this.coupon_money = coupon_money;
	}

	public int getTotal_price() {
		return total_price;
	}

	public void setTotal_price(int total_price) {
		this.total_price = total_price;
	}

	public List<OrderSubmitRequestBaby> getBabys() {
		return babys;
	}

	public void setBabys(List<OrderSubmitRequestBaby> babys) {
		this.babys = babys;
	}

	public List<OrderSubmitRequestService> getServices() {
		return services;
	}

	public void setServices(List<OrderSubmitRequestService> services) {
		this.services = services;
	}

	public List<OrderSubmitRequestInsurances> getBaby_insurances() {
		return baby_insurances;
	}

	public void setBaby_insurances(
			List<OrderSubmitRequestInsurances> baby_insurances) {
		this.baby_insurances = baby_insurances;
	}

}
