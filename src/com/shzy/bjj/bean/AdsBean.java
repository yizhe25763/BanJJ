package com.shzy.bjj.bean;

import java.io.Serializable;

public class AdsBean implements Serializable {
	/**
	 * 是否有奖励(0 否 1 是)
	 */
	private int is_havereword;
	/**
	 * 金币奖励额度
	 */
	private int gold_reward;
	/**
	 * vip 奖励额度
	 */
	private int vip_reward;
	/**
	 * vip 名称
	 */
	private String vip_memo;
	/**
	 * 广告功能类型编码
	 */
	private String type_bm;
	/**
	 * 详情页面链接
	 */
	private String page_url;
	/**
	 * 图片地址
	 */
	private String pic_url;
	/**
	 * 广告名称
	 */
	private String name;
	/**
	 * 广告编号
	 */
	private String id;

	public int getIs_havereword() {
		return is_havereword;
	}

	public void setIs_havereword(int is_havereword) {
		this.is_havereword = is_havereword;
	}

	public int getGold_reward() {
		return gold_reward;
	}

	public void setGold_reward(int gold_reward) {
		this.gold_reward = gold_reward;
	}

	public int getVip_reward() {
		return vip_reward;
	}

	public void setVip_reward(int vip_reward) {
		this.vip_reward = vip_reward;
	}

	public String getVip_memo() {
		return vip_memo;
	}

	public void setVip_memo(String vip_memo) {
		this.vip_memo = vip_memo;
	}

	public String getType_bm() {
		return type_bm;
	}

	public void setType_bm(String type_bm) {
		this.type_bm = type_bm;
	}

	public String getPage_url() {
		return page_url;
	}

	public void setPage_url(String page_url) {
		this.page_url = page_url;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "AdsBean [is_havereword=" + is_havereword + ", gold_reward="
				+ gold_reward + ", vip_reward=" + vip_reward + ", vip_memo="
				+ vip_memo + ", type_bm=" + type_bm + ", page_url=" + page_url
				+ ", pic_url=" + pic_url + ", name=" + name + ", id=" + id
				+ "]";
	}

}
