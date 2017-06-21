package com.shzy.bjj.bean;

import java.util.List;

public class PicBean {

	private List<ImageBean> imageBean;
	private String memo;

	public List<ImageBean> getImageBean() {
		return imageBean;
	}

	public void setImageBean(List<ImageBean> imageBean) {
		this.imageBean = imageBean;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
