package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class AdsResult implements Serializable {
	/**
	 * 1
	 */
	private int method_id;
	/**
	 * 状态(0 成功,5 未知情况)
	 */
	private int result;
	/**
	 * 广告位编号
	 */
	private int position_id;
	/**
	 * 广告位名称
	 */
	private String position_name;
	/**
	 * 界面层级
	 */
	private int screen_level;
	/**
	 * 轮播时间(秒)
	 */
	private int carousel_value;
	/**
	 * 记录条数
	 */
	private int count;
	/**
	 * 是否轮播(0:否 1:是)
	 */
	private int is_carousel;
	/**
	 * 广告内容
	 */
	private List<AdsBean> ads;

	public int getMethod_id() {
		return method_id;
	}

	public void setMethod_id(int method_id) {
		this.method_id = method_id;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getPosition_id() {
		return position_id;
	}

	public void setPosition_id(int position_id) {
		this.position_id = position_id;
	}

	public String getPosition_name() {
		return position_name;
	}

	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}

	public int getScreen_level() {
		return screen_level;
	}

	public void setScreen_level(int screen_level) {
		this.screen_level = screen_level;
	}

	public int getCarousel_value() {
		return carousel_value;
	}

	public void setCarousel_value(int carousel_value) {
		this.carousel_value = carousel_value;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getIs_carousel() {
		return is_carousel;
	}

	public void setIs_carousel(int is_carousel) {
		this.is_carousel = is_carousel;
	}

	public List<AdsBean> getAds() {
		return ads;
	}

	public void setAds(List<AdsBean> ads) {
		this.ads = ads;
	}

	@Override
	public String toString() {
		return "AdsResult [method_id=" + method_id + ", result=" + result
				+ ", position_id=" + position_id + ", position_name="
				+ position_name + ", screen_level=" + screen_level
				+ ", carousel_value=" + carousel_value + ", count=" + count
				+ ", is_carousel=" + is_carousel + ", ads=" + ads + "]";
	}

}
