package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeacherBean implements Serializable {
	//
	private String auto_match_condition_times;
	private List<TimeId> auto_match_service_time_id_list;
	//
	private int teacher_id;
	private String teacher_name;
	// 老师性别（ 0 男 1 女）
	private int teacher_sex;
	// 老师头像
	private String teacher_pic_url;
	private String teacher_descb;
	// 老师综合评分
	private float teacher_score;
	// 专业评分
	private float knowledge_score;
	// 服务评分
	private float service_score;
	// 守时评分
	private float punctuality_score;
	// 老师接单次数
	private int order_count;
	// 距离
	private String distance;

	private int skill_count;
	private List<TeacherSkillBean> skill_list;
	private int district_count;
	private List<TeacherDistrictBean> district_list;
	private String date;
	private int service_time_count;
	private List<ServiceTimeBean> service_time_list;
	private int service_time_id_count;
	private List<ServiceTimeIdBean> service_time_id_list;

	// 老师可供的服务类型数量
	private int service_type_count;
	// 老师可ᨀ供的服务类型列表
	private List<TeacherServiceTypeBean> service_type_list;
	// 老师可ᨀ供的服务数量
	private int service_count;
	// 老师可ᨀ供的服务列表
	private List<TeacherServiceBean> service_list;

	private boolean isFollowed = false;
	private boolean isServiced = false;

	public String getAuto_match_condition_times() {
		return auto_match_condition_times;
	}

	public void setAuto_match_condition_times(String auto_match_condition_times) {
		this.auto_match_condition_times = auto_match_condition_times;
	}

	public List<TimeId> getAuto_match_service_time_id_list() {
		return auto_match_service_time_id_list;
	}

	public void setAuto_match_service_time_id_list(
			List<TimeId> auto_match_service_time_id_list) {
		this.auto_match_service_time_id_list = auto_match_service_time_id_list;
	}

	public int getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(int teacher_id) {
		this.teacher_id = teacher_id;
	}

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

	public float getTeacher_score() {
		return teacher_score;
	}

	public void setTeacher_score(float teacher_score) {
		this.teacher_score = teacher_score;
	}

	public float getKnowledge_score() {
		return knowledge_score;
	}

	public void setKnowledge_score(float knowledge_score) {
		this.knowledge_score = knowledge_score;
	}

	public float getService_score() {
		return service_score;
	}

	public void setService_score(float service_score) {
		this.service_score = service_score;
	}

	public float getPunctuality_score() {
		return punctuality_score;
	}

	public void setPunctuality_score(float punctuality_score) {
		this.punctuality_score = punctuality_score;
	}

	public int getOrder_count() {
		return order_count;
	}

	public void setOrder_count(int order_count) {
		this.order_count = order_count;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getSkill_count() {
		return skill_count;
	}

	public void setSkill_count(int skill_count) {
		this.skill_count = skill_count;
	}

	public List<TeacherSkillBean> getSkill_list() {
		return skill_list;
	}

	public void setSkill_list(List<TeacherSkillBean> skill_list) {
		this.skill_list = skill_list;
	}

	public int getDistrict_count() {
		return district_count;
	}

	public void setDistrict_count(int district_count) {
		this.district_count = district_count;
	}

	public List<TeacherDistrictBean> getDistrict_list() {
		return district_list;
	}

	public void setDistrict_list(List<TeacherDistrictBean> district_list) {
		this.district_list = district_list;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getService_time_count() {
		return service_time_count;
	}

	public void setService_time_count(int service_time_count) {
		this.service_time_count = service_time_count;
	}

	public int getService_time_id_count() {
		return service_time_id_count;
	}

	public void setService_time_id_count(int service_time_id_count) {
		this.service_time_id_count = service_time_id_count;
	}

	public List<ServiceTimeBean> getService_time_list() {
		return service_time_list;
	}

	public void setService_time_list(List<ServiceTimeBean> service_time_list) {
		this.service_time_list = service_time_list;
	}

	public List<ServiceTimeIdBean> getService_time_id_list() {
		return service_time_id_list;
	}

	public void setService_time_id_list(
			List<ServiceTimeIdBean> service_time_id_list) {
		this.service_time_id_list = service_time_id_list;
	}

	public int getService_type_count() {
		return service_type_count;
	}

	public void setService_type_count(int service_type_count) {
		this.service_type_count = service_type_count;
	}

	public List<TeacherServiceTypeBean> getService_type_list() {
		return service_type_list;
	}

	public void setService_type_list(
			List<TeacherServiceTypeBean> service_type_list) {
		this.service_type_list = service_type_list;
	}

	public int getService_count() {
		return service_count;
	}

	public void setService_count(int service_count) {
		this.service_count = service_count;
	}

	public List<TeacherServiceBean> getService_list() {
		return service_list;
	}

	public void setService_list(List<TeacherServiceBean> service_list) {
		this.service_list = service_list;
	}

	public boolean isFollowed() {
		return isFollowed;
	}

	public void setFollowed(boolean isFollowed) {
		this.isFollowed = isFollowed;
	}

	public boolean isServiced() {
		return isServiced;
	}

	public void setServiced(boolean isServiced) {
		this.isServiced = isServiced;
	}

	/**
	 * 匹配 老师是否已关注，已服务过
	 * 
	 * @param list
	 * @param followedList
	 * @param servicedList
	 * @return
	 */
	public static List<TeacherBean> getTeacherShowData(List<TeacherBean> list,
			List<TeacherFollowBean> followedList,
			List<TeacherFollowBean> servicedList) {
		Set<Integer> fset = new HashSet<Integer>();
		for (TeacherFollowBean teacherFollowBean : followedList) {
			fset.add(teacherFollowBean.getTeacher_id());
		}

		Set<Integer> sset = new HashSet<Integer>();
		for (TeacherFollowBean teacherFollowBean : servicedList) {
			sset.add(teacherFollowBean.getTeacher_id());
		}

		for (int i = 0, len = list.size(); i < len; i++) {
			int t = list.get(i).getTeacher_id();
			if (fset.contains(t)) {
				list.get(i).setFollowed(true);
			}
			if (sset.contains(t)) {
				list.get(i).setServiced(true);
			}
		}
		return list;
	}

}
