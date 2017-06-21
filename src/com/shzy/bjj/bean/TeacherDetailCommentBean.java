package com.shzy.bjj.bean;

import java.io.Serializable;

public class TeacherDetailCommentBean implements Serializable {
	// 评论人名称
	private String post_user_name;
	// 评论人头像
	private String post_user_avatar_url;
	// 老师综合评分
	private int teacher_score;
	// 老师专业评分
	private int knowledge_score;
	// 老师服务评分
	private int service_score;
	// 老师守时评分
	private int punctuality_score;
	// 评论内容
	private String post_content;
	// 评论时间
	private String post_time_descb;

	public String getPost_user_name() {
		return post_user_name;
	}

	public void setPost_user_name(String post_user_name) {
		this.post_user_name = post_user_name;
	}

	public String getPost_user_avatar_url() {
		return post_user_avatar_url;
	}

	public void setPost_user_avatar_url(String post_user_avatar_url) {
		this.post_user_avatar_url = post_user_avatar_url;
	}

	public int getTeacher_score() {
		return teacher_score;
	}

	public void setTeacher_score(int teacher_score) {
		this.teacher_score = teacher_score;
	}

	public int getKnowledge_score() {
		return knowledge_score;
	}

	public void setKnowledge_score(int knowledge_score) {
		this.knowledge_score = knowledge_score;
	}

	public int getService_score() {
		return service_score;
	}

	public void setService_score(int service_score) {
		this.service_score = service_score;
	}

	public int getPunctuality_score() {
		return punctuality_score;
	}

	public void setPunctuality_score(int punctuality_score) {
		this.punctuality_score = punctuality_score;
	}

	public String getPost_content() {
		return post_content;
	}

	public void setPost_content(String post_content) {
		this.post_content = post_content;
	}

	public String getPost_time_descb() {
		return post_time_descb;
	}

	public void setPost_time_descb(String post_time_descb) {
		this.post_time_descb = post_time_descb;
	}

}
