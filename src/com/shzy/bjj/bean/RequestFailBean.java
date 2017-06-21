package com.shzy.bjj.bean;

import com.shzy.bjj.tools.ToastTool;

/**
 * 
 * @brief 服务端响应基类 包含错误编码以及错误信息
 * @author Fanhao Yi
 * @data 2015年6月16日上午11:45:16
 * @version V1.0
 */
public class RequestFailBean {
	/**
	 * 错误详细编码
	 */
	public int error_code;
	public int result;
	public int failue_reason_type;

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getFailue_reason_type() {
		return failue_reason_type;
	}

	public void setFailue_reason_type(int failue_reason_type) {
		this.failue_reason_type = failue_reason_type;
	}

	public static String getFailType(int type) {
		String msg = "";
		switch (type) {
		case 0:
			msg = "系统错误";
			break;
		case 2:
			msg = "金额错误";
			break;
		case 3:
			msg = "老师课程被锁定";
			break;
		case 4:
			msg = "优惠券不存在";
			break;
		case 5:
			msg = "优惠券不可用";
			break;
		case 6:
			msg = "课程被锁定";
			break;
		case 7:
			msg = "课程不存在";
			break;
		case 8:
			msg = "订单明细数量错误";
			break;
		case 9:
			msg = "订单积分错误";
			break;
		case 10:
			msg = "订单商品总数错误";
			break;
		case 11:
			msg = "用户不存在";
			break;
		case 12:
			msg = "优惠券面额错误";
			break;
		case 13:
			msg = "订单原始价格错误";
			break;
		case 14:
			msg = "发布课程不存在";
			break;
		case 15:
			msg = "订单明细统计错误";
			break;
		case 16:
			msg = "用户资源被锁";
			break;
		case 17:
			msg = "课程不可用";
			break;

		case 18:
			msg = "优惠券价格错误";
			break;

		case 19:
			msg = "积分价格错误";
			break;

		case 20:
			msg = "折扣金额错误";
			break;

		case 21:
			msg = "宝宝不存在";
			break;

		case 22:
			msg = "没有权限访问宝宝";
			break;
		case 23:
			msg = "订单地址用户错误";
			break;
		default:
			break;
		}
		return msg;
	}
}
