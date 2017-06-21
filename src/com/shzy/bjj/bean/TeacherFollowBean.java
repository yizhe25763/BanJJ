package com.shzy.bjj.bean;

import java.io.Serializable;

/**
 * 已关注/已服务 老师列表
 * 
 * @author suntao
 * 
 */
public class TeacherFollowBean implements Serializable {

	private int teacher_id;

	public int getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(int teacher_id) {
		this.teacher_id = teacher_id;
	}

}
