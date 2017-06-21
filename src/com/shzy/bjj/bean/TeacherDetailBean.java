package com.shzy.bjj.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.shzy.bjj.tools.StringTool;

/**
 * 教师详情
 * 
 * @author suntao
 * 
 */
public class TeacherDetailBean implements Serializable {
	private String teacher_name;
	// 老师性别（ 0 男 1 女）
	private int teacher_sex;
	private String teacher_pic_url;
	private String teacher_descb;
	// 老师综合评分
	private int teacher_score;
	// 老师专业评分
	private int knowledge_score;
	private int service_score;
	// 老师守时评分
	private int punctuality_score;
	// 老师接单次数
	private int order_count;
	private String work_start_time;
	private String teacher_birthday;
	private String district;
	// 能力数量
	private int skill_count;
	private List<TeacherDetailSkillBean> skill_ist;
	// 证书数量
	private int profile_count;
	private List<TeacherDetailSkillBean> profile_ist;
	// 履历数量
	private int resume_count;
	private List<TeacherDetailResumeBean> resume_ist;
	// 评论数量
	private int comment_count;
	private List<TeacherDetailCommentBean> comment_ist;

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public int getTeacher_sex() {
		return teacher_sex;
	}

	public void setTeacher_sex(int teacher_sex) {
		this.teacher_sex = teacher_sex;
	}

	public String getWork_start_time() {
		return work_start_time;
	}

	public void setWork_start_time(String work_start_time) {
		this.work_start_time = work_start_time;
	}

	public String getTeacher_birthday() {
		return teacher_birthday;
	}

	public void setTeacher_birthday(String teacher_birthday) {
		this.teacher_birthday = teacher_birthday;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getTeacher_pic_url() {
		return teacher_pic_url;
	}

	public void setTeacher_pic_url(String teacher_pic_url) {
		this.teacher_pic_url = teacher_pic_url;
	}

	public String getTeacher_descb() {
		return teacher_descb;
	}

	public void setTeacher_descb(String teacher_descb) {
		this.teacher_descb = teacher_descb;
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

	public int getOrder_count() {
		return order_count;
	}

	public void setOrder_count(int order_count) {
		this.order_count = order_count;
	}

	public int getSkill_count() {
		return skill_count;
	}

	public void setSkill_count(int skill_count) {
		this.skill_count = skill_count;
	}

	public List<TeacherDetailSkillBean> getSkill_ist() {
		return skill_ist;
	}

	public void setSkill_ist(List<TeacherDetailSkillBean> skill_ist) {
		this.skill_ist = skill_ist;
	}

	public int getProfile_count() {
		return profile_count;
	}

	public void setProfile_count(int profile_count) {
		this.profile_count = profile_count;
	}

	public List<TeacherDetailSkillBean> getProfile_ist() {
		return profile_ist;
	}

	public void setProfile_ist(List<TeacherDetailSkillBean> profile_ist) {
		this.profile_ist = profile_ist;
	}

	public int getResume_count() {
		return resume_count;
	}

	public void setResume_count(int resume_count) {
		this.resume_count = resume_count;
	}

	public List<TeacherDetailResumeBean> getResume_ist() {
		return resume_ist;
	}

	public void setResume_ist(List<TeacherDetailResumeBean> resume_ist) {
		this.resume_ist = resume_ist;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public List<TeacherDetailCommentBean> getComment_ist() {
		return comment_ist;
	}

	public void setComment_ist(List<TeacherDetailCommentBean> comment_ist) {
		this.comment_ist = comment_ist;
	}

	public static float getMyScore(float input) {
		float result = (input / 10);
		return result;
	}
	
	public static float getRatingScore(float input) {
		input = (float) (input + 0.9);
		float result = (input / 10);
		return result;
	}

	public static int getAgeByBirthDay(String birth) {
		if (StringTool.isNoBlankAndNoNull(birth)) {
			Date nowDate = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date birthDate;
			try {
				birthDate = format.parse(birth);
			} catch (ParseException e) {
				birthDate = new Date();
				e.printStackTrace();
			}
			Calendar flightCal = Calendar.getInstance();
			flightCal.setTime(nowDate);
			Calendar birthCal = Calendar.getInstance();
			birthCal.setTime(birthDate);
			int y = flightCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
			int m = flightCal.get(Calendar.MONTH)
					- birthCal.get(Calendar.MONTH);
			int d = flightCal.get(Calendar.DATE) - birthCal.get(Calendar.DATE);
			if (y < 0) {
				y = 0;
			} else {
				if (m < 0) {
					y--;
				}
				if (m >= 0 && d < 0) {
					y--;
				}
			}
			return y;
		} else {

			return 0;
		}
	}
}
