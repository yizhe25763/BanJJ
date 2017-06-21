package com.shzy.bjj.bean;

import java.io.Serializable;

public class TaskDayOrderBean implements Serializable {
	private OrderBean orderBean;
	private OrderDetailCountBean orderDetailCountBean;

	public TaskDayOrderBean(OrderBean orderBean,
			OrderDetailCountBean orderDetailCountBean) {
		this.orderBean = orderBean;
		this.orderDetailCountBean = orderDetailCountBean;
	}

	public OrderBean getOrderBean() {
		return orderBean;
	}

	public void setOrderBean(OrderBean orderBean) {
		this.orderBean = orderBean;
	}

	public OrderDetailCountBean getOrderDetailCountBean() {
		return orderDetailCountBean;
	}

	public void setOrderDetailCountBean(
			OrderDetailCountBean orderDetailCountBean) {
		this.orderDetailCountBean = orderDetailCountBean;
	}

	public static String getOrderDetailStatus(int status, int schedule_status,
			int comment) {
		String result = "";
		if (status == 100 || status == 201) {
			result = "";
		} else if (status == 108 || status == 109) {
			result = "关闭";
		} else if (status == 200 || status == 320) {
			switch (schedule_status) {
			case 1:
				result = "等待服务";
				break;
			case 2:
				result = "开始服务";
				break;
			case 3:
				result = "服务完成";
				break;
			case 4:
				result = "服务结束";
				break;
			case 5:
				result = "投诉中";
				break;
			case 6:
				result = "退款中";
				break;
			case 7:
				result = "已退款";
				break;
			case 8:
				result = "关闭";
				break;
			default:
				break;
			}
			if (comment == 1) {
				result = "已评论，感谢您的支持";
			}
		}
		return result;
	}

}
