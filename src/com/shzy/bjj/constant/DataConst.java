package com.shzy.bjj.constant;

import java.util.Map;

import android.os.Environment;

/**
 * 
 * @brief 数据常量
 * @author Fanhao.Yi
 * @date 2015年5月4日下午5:22:35
 * @version V1.0
 */
public class DataConst {

	/**
	 * 极光推送标签组
	 */
	public static String tags = "dev";
	/**
	 * 折扣比率
	 */

	public static int aggressive = 0;
	/**
	 * 轮播时间
	 */
	public static int BannerTime = 4000;
	/**
	 * 服务声明URL
	 */
	public static final String URLS = "www.banjiajia.cn/agreement.html";
	/***************************************** 支付宝支付 *****************************************/
	/**
	 * 商户PID
	 */
	public static final String PARTNER = "2088501776889412";
	/**
	 * 商户收款账号
	 */
	public static final String SELLER = "2088501776889412";
	/**
	 * 商户私钥，pkcs8格式
	 */
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMLfqmRTK73HRUUNJrbfiXoZITBPknK/xOjhn9CuzZZVppM6Nb/1A0FLbGlZ8YR9e05pb/xS4v1DJ7qhzREv4NgC855XFIBNC1U/GGkb0ace2UQgVL/D3Zun6lDA2uBG0T6dL9Ee6qtKbf/QLvBBQziqAzrbP6BNwFGhaH7wfFhdAgMBAAECgYEAsWFx8dwa5qXqEY6E/NTAiJkzMoxbm4OfMvOcyxEyJXNVKpJDtrTI/7gtuD+craKiiJ9ExgKUhz+HfEQnK0vPdnZq431/0Fh2dn9OSUkYMqSrh5xC7YtXOejoe+UB/BNQ0+ukSvflgWPF/LRfrTISLzbSZoJG4yd2hEDfNxu/w4ECQQD9cXCnynJB90T7AXAuATchj2IzwhhZZWLGQ8dIO8RGskU3pS7aNua7rSdKSgye+7H7SowaUHcxKpVwRf1FjYStAkEAxNb1xBRyenRKQtm21dPR+c/TLgGLgN4T7FfSCFdrw4FT/rWzKsnjiUEfaDRXn5HkCsy1kMUdoonbksitwOjocQJAUip36n4QwuAmhfX2Z4PPWtsAbGVNvZXICx0oO3vc60qadCriYK6R6WB+r3AIXXaeQijt+Q4DRnZW0npuXSE/VQJATTzcKxiq4woGqbysVbNMTW4TO5Y3m1WJE9BSBhvnmUuCHgxGRZ/6vLzlCgqf52ljxikVhb0TJ9hYZHyCUZQSwQJBAPWs2iAqIz2tCvrXuK7a/OuCCAOuYiWbxrQbiWb9GMRiiVvfJ+Eeazfo+U1TfQwjoIf/84wdOJVXss8r5GmAsLc=";
	/**
	 * 支付宝公钥
	 */
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	/**
	 * 支付编号
	 */
	public static final int SDK_PAY_FLAG = 1;
	/**
	 * 检查账号
	 */
	public static final int SDK_CHECK_FLAG = 2;
	/**
	 * 支付成功编号
	 */
	public static final String ALIPAYSUCCESSCODE = "9000";
	/**
	 * 支付失败编号
	 */
	public static final String ALIPAYFAILCODE = "8000";
	/**
	 * 
	 */
	public static final int TIMESTAMP = 1000;

	/***************************************** END *****************************************/

	/***************************************** 微信支付 *****************************************/
	public static final String wechatPayUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/***************************************** END *****************************************/

	/***************************************** 时间转换 *****************************************/

	public static final String TODAY = "今天";
	public static final String YESTERDAY = "昨天";
	public static final String TOMORROW = "明天";
	public static final String BEFORE_YESTERDAY = "前天";
	public static final String AFTER_TOMORROW = "后天";
	public static final String SUNDAY = "星期日";
	public static final String MONDAY = "星期一";
	public static final String TUESDAY = "星期二";
	public static final String WEDNESDAY = "星期三";
	public static final String THURSDAY = "星期四";
	public static final String FRIDAY = "星期五";
	public static final String SATURDAY = "星期六";
	/***************************************** END *****************************************/
	/**
	 * 初始化注册
	 */
	public static Boolean isFirstRegister = false;
	/**
	 * 初始化Banner标示
	 */
	public static Boolean firstInitBanner = true;
	/**
	 * 双击退出函数
	 */
	public static Boolean isExit = false;
	/**
	 * 手机号码长度
	 */
	public static final int phoneLength = 11;
	/**
	 * 验证号码长度
	 */
	public static final int authorCodeLength = 6;

	/**
	 * 用于存放倒计时时间
	 */
	public static Map<String, Long> map;
	/**
	 * 接口返回成功
	 */
	public static final int SUCCESS = 0;
	/**
	 * 接口返回失败
	 */
	public static final int FAIL = 5;
	/**
	 * 
	 */
	public static final int REQUESTCODEOK = 1;
	/**
	 * 地址登录入口
	 */
	public static final String ADDRESSID = "0";
	/**
	 * 宝宝信息入口
	 */
	public static final String BABYID = "1";
	/**
	 * 个人中心登录入口
	 */
	public static final String MINEID = "2";
	/*
	 * 本地保存路径
	 */
	public static String PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/BanJiaJia/";

	/**
	 * 初始化定位城市
	 */
	public static String mCity = "";

	/******************************** 服务端错误代码 **********************************/
	public static final int ERROR_ONE = 10000;
	public static final int ERROR_TWO = 10001;
	public static final int ERROR_THREE = 10002;
	public static final int ERROR_FOUR = 10003;
	public static final int ERROR_FIVE = 10013;
	public static final int ERROR_SIX = 10016;
	public static final int ERROR_SEVEN = 10017;
	public static final int ERROR_EIGHT = 10018;
	public static final int ERROR_NINE = 10019;
	public static final int ERROR_TEN = 10020;
	public static final int ERROR_EVEVEN = 10021;
	public static final int ERROR_TWELVE = 20001;
	public static final int ERROR_THIRTEEN = 20004;
	public static final int ERROR_FOURTEEN = 20006;
	public static final int ERROR_FIFTEEN = 20008;
	public static final int ERROR_SIXTEEN = 20009;
	public static final int ERROR_SEVENTEEN = 20010;
	public static final int ERROR_EIGHTEEN = 20102;
	public static final int ERROR_NINETEEN = 20201;
	public static final int ERROR_TWENTY = 20202;
	public static final int ERROR_TWENTY_ONE = 20203;
	public static final int ERROR_TWENTY_TWO = 20204;
	public static final int ERROR_TWENTY_THREE = 20205;
	public static final int ERROR_TWENTY_FOUR = 20208;
	public static final int ERROR_TWENTY_FIVE = 20209;
	public static final int ERROR_TWENTY_SIX = 20210;
	public static final int ERROR_TWENTY_SEVEN = 20214;
	public static final int ERROR_TWENTY_EIGHT = 20215;
	/******************************** End **********************************/
	// 初始化参数
	public static final String CONFIG = "CONFIG";
}
