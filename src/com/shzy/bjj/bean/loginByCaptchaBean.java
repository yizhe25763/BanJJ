package com.shzy.bjj.bean;

/**
 * 
 * @brief 手机验证码登录实体Bean
 * @author Fanhao Yi
 * @data 2015年6月17日下午2:58:08
 * @version V1.0
 */
public class loginByCaptchaBean {

	private int result;
	private String login_key;
	private Long user_id;


	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getLogin_key() {
		return login_key;
	}

	public void setLogin_key(String login_key) {
		this.login_key = login_key;
	}

	@Override
	public String toString() {
		return "loginByCaptchaBean [result=" + result + ", login_key="
				+ login_key + "]";
	}

}
