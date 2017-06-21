package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class OrderBean implements Serializable {

	private int is_comment;
	private int schedule_status;

	public int getIs_comment() {
		return is_comment;
	}

	public void setIs_comment(int is_comment) {
		this.is_comment = is_comment;
	}

	public int getSchedule_status() {
		return schedule_status;
	}

	public void setSchedule_status(int schedule_status) {
		this.schedule_status = schedule_status;
	}

	private String order_create_time;
	private String baby_name;
	private String bady_id;
	// 订单编号
	private String order_number;
	// 联系人姓名
	private String order_contact_name;
	// 联系人手机号码
	private String order_contact_phone;
	// 联系人座机号码
	private String order_contact_telphone;
	// 上门地址
	private String address;
	// 地址门牌号
	private String address_room;
	// 订单备注
	private String memo;
	// 使用的积分数量
	private int use_score;
	// 使用的代金券 id
	private int use_coupon_id;
	// 支付方式:1:支付宝2:微信
	private int pay_type;
	// 订单原价（单位分）
	private int original_price;
	// 订单折扣金额
	private int discount_money;
	// 积分抵扣金额
	private int score_money;
	// 抵用券抵扣金额
	private int coupon_money;
	// 订单最终总价
	private int total_price;
	// 订单状态
	private int status;
	private int detail_count;
	// 明细数量
	private List<OrderDetailCountBean> detail_list;

	public String getAddress_room() {
		return address_room;
	}

	public void setAddress_room(String address_room) {
		this.address_room = address_room;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getBady_id() {
		return bady_id;
	}

	public void setBady_id(String bady_id) {
		this.bady_id = bady_id;
	}

	public String getOrder_contact_name() {
		return order_contact_name;
	}

	public void setOrder_contact_name(String order_contact_name) {
		this.order_contact_name = order_contact_name;
	}

	public String getOrder_create_time() {
		return order_create_time;
	}

	public void setOrder_create_time(String order_create_time) {
		this.order_create_time = order_create_time;
	}

	public String getBaby_name() {
		return baby_name;
	}

	public void setBaby_name(String baby_name) {
		this.baby_name = baby_name;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getDetail_count() {
		return detail_count;
	}

	public void setDetail_count(int detail_count) {
		this.detail_count = detail_count;
	}

	public List<OrderDetailCountBean> getDetail_list() {
		return detail_list;
	}

	public void setDetail_list(List<OrderDetailCountBean> detail_list) {
		this.detail_list = detail_list;
	}

	public static String getOrderDetailNumber(String number) {
		if (number.contains(",")) {
			int index = number.indexOf(",");
			number = number.substring(0, index);
		}
		return number;
	}

	public static String getOrderStatusName(int status) {
		String result = "";
		if (status == 100) {
			result = "未付款";
		} else if (status == 108) {
			result = "订单支付超时被取消";
		} else if (status == 109) {
			result = "订单已取消";
		} else if (status == 201) {
			result = "付款中";
		} else if (status == 320) {
			result = "订单完成";
		} else if (status == 200) {
			result = "已付款";
		}
		return result;
	}
}
