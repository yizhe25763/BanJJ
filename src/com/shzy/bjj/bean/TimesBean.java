package com.shzy.bjj.bean;

import java.io.Serializable;

public class TimesBean implements Serializable {
	private TeacherBean teacherBean;
	private TimeBean timeBean;
	
	public TimeBean getTimeBean() {
		return timeBean;
	}

	public void setTimeBean(TimeBean timeBean) {
		this.timeBean = timeBean;
	}

	public TeacherBean getTeacherBean() {
		return teacherBean;
	}

	public void setTeacherBean(TeacherBean teacherBean) {
		this.teacherBean = teacherBean;
	}

}
