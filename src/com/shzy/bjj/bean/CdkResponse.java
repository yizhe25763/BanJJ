package com.shzy.bjj.bean;

import java.io.Serializable;

public class CdkResponse implements Serializable{
	// 兑换物品类型
	private int goods_type;
	// 兑换物品名称
	private String goods_name;
	// 物品数量
	private int goods_amount;
	// 实际兑换者的昵称
	private String nickname;

	public int getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(int goods_type) {
		this.goods_type = goods_type;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public int getGoods_amount() {
		return goods_amount;
	}

	public void setGoods_amount(int goods_amount) {
		this.goods_amount = goods_amount;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
