package com.shzy.bjj.constant;

//import org.apache.http.message.BasicNameValuePair;

/**
 * 
 * @brief API接口
 * @author Fanhao Yi
 * @data 2015年6月15日上午10:54:15
 * @version V1.0
 */
public class Apis {
	/**
	 * 测试独立Host
	 */
	// public static String ONLYHOST = "http://test5.eachbaby.com/";
	/**
	 * 测试Host
	 */
	// public static String HOST = "http://test5.eachbaby.com/api/c/";
	/**
	 * 
	 * 测试key
	 */
	// public static String KEY = "fc1d2e28b2a6c836e1b0adadd5c5e963";
	/**
	 * 提交订单测试KEY
	 */
	// public static String ORDERKEY = "adf31dc1e4738c5919964ffbfd4e17ce";

	/**
	 * 正式独立Host
	 */
	public static String ONLYHOST = "http://gw.banjiajia.cn//";
	/**
	 * Host 正式key
	 * 
	 */
	public static String HOST = "http://gw.banjiajia.cn/api/c/";
	// /**
	// * 提交订单正式KEY
	// */
	public static String ORDERKEY = "0d92a1a673c7ec7f1bdd9862f4a16768";
	// /**
	// * 正式key
	// */
	public static String KEY = "beafb4f471254bbb2e3a90bb291c983a";
	/**
	 * 广告位数据测试地址
	 */
	public static String BANNERHOST = "http://gw.eachbaby.com/ad/datas/ad_list_by_position";
	/**
	 * 广告key
	 */
	public static String BANNERKEY = "c7c9d6da3a5003a7ba72ec37f848413f";

	/**
	 * 发送手机验证码
	 */
	public static String SENDCAPTCHA = "/user/send_captcha";

	/**
	 * 手机验证码登录
	 */
	public static String LOGINBYCAPTCHA = "/user/login_by_captcha";
	/**
	 * 用户订单列表
	 */
	public static String GET_USER_ORDER = "/user/get_order_list";
	/**
	 * 用户订单取消
	 */
	public static String GET_USER_ORDER_CANCEL = "/billing/cancel_order";
	/**
	 * 完善用户信息
	 */
	public static String CREATEFIRSTINFO = "/user/create_first_info";
	/**
	 * 用户代金券列表
	 */
	public static String GET_USER_VOUCHE = "/user/get_coupon_list";
	/**
	 * 用户可用代金券列表
	 */
	public static String GET_USER_VOUCHE_AVAILABLE = "/user/get_coupon_list_available";
	/**
	 * 用户消息中心
	 */
	public static String GET_USER_INFO = "/user/get_message_list";
	/**
	 * 用户积分列表
	 */
	public static String GET_USER_INTEGRAL = "/user/get_score_info_list";
	/**
	 * 获取预约人列表
	 */
	public static String GET_USER_APPOINTMENT = "/user/get_contact_info_list";
	/**
	 * 代金券兑换确认
	 */
	public static String CDK_VOUCHE_EXCHANGE_CONFIRM = "/cdk/exchange_confirm";
	/**
	 * 代金券兑换
	 */
	public static String CDK_VOUCHE_EXCHANGE_SUBMIT = "/cdk/exchange_submit";
	/**
	 * 获取用户信息
	 */
	public static String GET_USER = "/user/get_info";
	/**
	 * 修改用户信息
	 */
	public static String MODIFY_USER = "/user/modify_delux_info";
	/**
	 * 创建宝宝信息
	 */
	public static String USER_BABY_CREATE = "/user/create_baby_info";
	/**
	 * 修改宝宝信息
	 */
	public static String USER_BABY_MODIFY = "/user/modify_baby_info";
	/**
	 * 删除宝宝信息
	 */
	public static String USER_BABY_DELETE = "/user/delete_baby_info";
	/**
	 * 添加预约人
	 */
	public static String GET_USER_APPOINTMENT_CREATE = "/user/create_contact_info";
	/**
	 * 修改预约人
	 */
	public static String GET_USER_APPOINTMENT_MODIFY = "/user/modify_contact_info";
	/**
	 * 删除预约人
	 */
	public static String GET_USER_APPOINTMENT_DELETE = "/user/delete_contact_info";

	/**
	 * 老师搜索列表
	 */
	public static String TEACHER_SEACHER_LIST = "/teacher/get_search_list";

	/**
	 * 老师搜索列表
	 */
	public static String GET_TEACHER_DETAIL = "/teacher/get_detail";
	/**
	 * 老师自动匹配列表
	 */
	public static String GET_AUTOMATCH_SEARCH_LIST = "/teacher/get_automatch_search_list";

	/**
	 * 用户退出
	 */
	public static String USER_LOGOUT = "/user/logout";

	/**
	 * 获取城市的商圈列表
	 */
	public static String CLIENT_BUSINESS_LIST = "/client/get_business_circle_list";
	/**
	 * 获取已关注老师列表
	 */
	public static String GET_TEACHER_FOllOWED = "/user/get_followed_teacher_list";
	/**
	 * 获取已服务老师列表
	 */
	public static String GET_TEACHER_SERVICED = "/user/get_serviced_teacher_list";
	/**
	 * 老师详情
	 */
	public static String TEACHER_DETAIL = "/teacher/v1/get_detail";
	/**
	 * 老师排班表
	 */
	public static String TEACHER_SCHEDULE = "/teacher/get_schedule_info";
	/**
	 * 老师评价列表
	 */
	public static String TEACHER_COMMENT = "/teacher/v1/get_teacher_comment_list";
	/**
	 * 用户关注
	 */
	public static String TEACHER_FOLLOW = "/user/follow_teacher";
	/**
	 * 用户取消关注
	 */
	public static String TEACHER_FOLLOW_CANCEL = "/user/unfollow_teacher";
	/**
	 * 用户提交订单
	 */
	public static String BILLING_SUBMIT_ORDER = "/billing/submit_order";
	/**
	 * 用户确认订单
	 */
	public static String GET_USER_ORDER_CONFIRM = "/billing/confirm_order";
	/**
	 * 配置信息
	 */
	public static String CLIENT_CONFIG = "/client/config";
	/**
	 * 获取继续支付参数
	 */
	public static String GET_RESUME_PAY_INFO = "/billing/get_resume_pay_info";
	/**
	 * 意见反馈
	 */
	public static String FEEDBACK = "/client/feedback";
	/**
	 * 用户评价老师
	 */
	public static String EVALUATETEACHER = "/user/evaluate_teacher";
	/**
	 * 查询订单退款金额
	 */
	public static String QUERYREIMBURSEORDER = "/billing/query_reimburse_order";
	/**
	 * 􏱎􏱏􏰝􏰞􏰻􏰼
	 */
	public static String SUBMITREIMBURSEORDER = "/billing/submit_reimburse_order";
	/**
	 * 用户确认付款
	 */
	public static String CONFIRMORDER = "/billing/confirm_order";
	/**
	 * 用户投诉订单
	 */
	public static String COMPLAINORDER = "/billing/complain_order";

	/**
	 * 订单详情
	 */
	public static String ORDERINFOLIST = "/user/get_order_detail_list";
	/**
	 * 图片资源上传
	 */
	public static String IMAGEUPLOAD = "api/common/resource/image/upload";

	/**
	 * 优惠券兑换
	 */
	public static String CASHCOUPON = "user/exchange_cash_coupon";

	/**
	 * 用户推送绑定
	 */
	public static String PUSHBIND = "user/push_bind";
	/**
	 * 消息已读接口
	 */
	public static String MESSAGEREAD = "api/common/message_read";

	/***************************************** 支付宝支付 *****************************************/
	public static String PREPAY_ID = "prepay_id";
	public static String APPID = "appid";
	public static String CONCESTR = "noncestr";
	public static String PACKAGE = "package";
	public static String PARTNERID = "partnerid";
	public static String PREPAYID = "prepayid";
	public static String TIMESTAMP = "timestamp";

}
