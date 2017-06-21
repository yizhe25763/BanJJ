package com.shzy.bjj.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.shzy.bjj.MyApplication;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.tools.StringTool;



public class ConfigResponse implements Serializable {
	// 一级服务数量
	private int service_grade_1_count;
	private List<ConfigServiceTypeBean> service_grade_1_list;
	private int skill_count;
	private List<ConfigSkillBean> skill_list;
	private int time_content;
	private List<ConfigTimeBean> time_list;
	private int order_time_max_limit;
	private int discount_count;
	private List<DiscountBean> discount_list;

	private String server_time;

	public int getService_grade_1_count() {
		return service_grade_1_count;
	}

	public void setService_grade_1_count(int service_grade_1_count) {
		this.service_grade_1_count = service_grade_1_count;
	}

	public List<ConfigServiceTypeBean> getService_grade_1_list() {
		return service_grade_1_list;
	}

	public void setService_grade_1_list(
			List<ConfigServiceTypeBean> service_grade_1_list) {
		this.service_grade_1_list = service_grade_1_list;
	}

	public int getSkill_count() {
		return skill_count;
	}

	public void setSkill_count(int skill_count) {
		this.skill_count = skill_count;
	}

	public List<ConfigSkillBean> getSkill_list() {
		return skill_list;
	}

	public void setSkill_list(List<ConfigSkillBean> skill_list) {
		this.skill_list = skill_list;
	}

	public int getTime_content() {
		return time_content;
	}

	public void setTime_content(int time_content) {
		this.time_content = time_content;
	}

	public List<ConfigTimeBean> getTime_list() {
		return time_list;
	}

	public void setTime_list(List<ConfigTimeBean> time_list) {
		this.time_list = time_list;
	}

	public int getOrder_time_max_limit() {
		return order_time_max_limit;
	}

	public void setOrder_time_max_limit(int order_time_max_limit) {
		this.order_time_max_limit = order_time_max_limit;
	}

	public int getDiscount_count() {
		return discount_count;
	}

	public void setDiscount_count(int discount_count) {
		this.discount_count = discount_count;
	}

	public List<DiscountBean> getDiscount_list() {
		return discount_list;
	}

	public void setDiscount_list(List<DiscountBean> discount_list) {
		this.discount_list = discount_list;
	}

	public String getServer_time() {
		return server_time;
	}

	public void setServer_time(String server_time) {
		this.server_time = server_time;
	}

	public static Date getMyServerTime(MyApplication mApplication) {
		String serverTime = "";
		Date nowDate = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (mApplication.isReadDataCache(DataConst.CONFIG)) {
			ConfigResponse configResponse = (ConfigResponse) mApplication
					.readObject(DataConst.CONFIG);
			if (configResponse != null) {
				serverTime = configResponse.getServer_time();
			}
		}

		if (StringTool.isNoBlankAndNoNull(serverTime)) {
				try {
					nowDate = format.parse(serverTime);
				} catch (java.text.ParseException e) {
					nowDate = new Date();
					e.printStackTrace();
				}
		} else {
			nowDate = new Date();
		}
		return nowDate;
	}
}
