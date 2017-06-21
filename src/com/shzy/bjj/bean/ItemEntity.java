package com.shzy.bjj.bean;

import java.util.ArrayList;

public class ItemEntity {
	private String avatar; // 用户头像URL
	private String title; // 标题
	private String content; // 内容
	private String userName;
	private String time;
	private int teacher_score;
	private ArrayList<String> imageUrls; // 九宫格图片的URL集合

	public ItemEntity() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getTeacher_score() {
		return teacher_score;
	}

	public void setTeacher_score(int teacher_score) {
		this.teacher_score = teacher_score;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArrayList<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(ArrayList<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	@Override
	public String toString() {
		return "ItemEntity [avatar=" + avatar + ", title=" + title
				+ ", content=" + content + ", imageUrls=" + imageUrls + "]";
	}

}
