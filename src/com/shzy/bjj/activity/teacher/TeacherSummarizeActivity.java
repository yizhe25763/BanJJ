package com.shzy.bjj.activity.teacher;

import java.util.ArrayList;
import java.util.List;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shzy.bjj.R;
import com.shzy.bjj.adapter.TeacherCommentAdapter;
import com.shzy.bjj.adapter.TeacherDetailTimeAdapter;
import com.shzy.bjj.adapter.TeacherDetaildateAdapter;
import com.shzy.bjj.bean.ConfigResponse;
import com.shzy.bjj.bean.ImageBean;
import com.shzy.bjj.bean.ItemEntity;
import com.shzy.bjj.bean.MapBean;
import com.shzy.bjj.bean.PicBean;
import com.shzy.bjj.bean.TeacherDetailBean;
import com.shzy.bjj.bean.TeacherDetailCommentBean;
import com.shzy.bjj.bean.TeacherScheduleData;
import com.shzy.bjj.tools.DateTimeTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.view.CircularImage;
import com.shzy.bjj.view.CustomListView;

/**
 * 老师概述
 * 
 * @author suntao
 * 
 */
public class TeacherSummarizeActivity extends TeacherDataActivity {
	protected CircularImage circularImage;
	protected TextView user_name;
	protected TextView user_age;
	protected TextView user_experience;
	protected TextView user_order_number;
	protected TextView knowledge_score;
	protected TextView service_score;
	protected TextView punctuality_score;

	protected GridView grideview_title;
	protected CustomListView listview_date;
	protected TextView teacher_detail_all_time;
	protected ImageView teacher_detail_all_time_arrow;
	protected boolean isArrow = false;

	protected TeacherDetailTimeAdapter titleAdapter;
	protected TeacherDetaildateAdapter listAdapter;

	protected TextView comment_count;
	protected TextView comment_more;
	protected CustomListView listview_comment;
	protected Button chooseBt;

	// 日历数据
	protected List<List<MapBean>> dataList;
	// 日历表头
	protected List<MapBean> titleList;

	private ItemEntity entity;

	private ArrayList<ItemEntity> itemEntities;

	private ArrayList<String> imageList;
	TeacherCommentAdapter adapter;

	@Override
	public void initView(View view) {
		super.initView(view);
		grideview_title = (GridView) pageOne.findViewById(R.id.grideview_title);
		listview_date = (CustomListView) pageOne
				.findViewById(R.id.listview_date);

		circularImage = (CircularImage) pageOne.findViewById(R.id.head_img);
		user_name = (TextView) pageOne.findViewById(R.id.user_name);
		user_age = (TextView) pageOne.findViewById(R.id.user_age);
		user_experience = (TextView) pageOne.findViewById(R.id.user_experience);
		user_order_number = (TextView) pageOne
				.findViewById(R.id.user_order_number);
		knowledge_score = (TextView) pageOne.findViewById(R.id.knowledge_score);
		service_score = (TextView) pageOne.findViewById(R.id.service_score);
		punctuality_score = (TextView) pageOne
				.findViewById(R.id.punctuality_score);

		teacher_detail_all_time = (TextView) pageOne
				.findViewById(R.id.teacher_detail_all_time);
		teacher_detail_all_time_arrow = (ImageView) pageOne
				.findViewById(R.id.teacher_detail_all_time_arrow);
		comment_count = (TextView) pageOne.findViewById(R.id.comment_count);
		comment_more = (TextView) pageOne.findViewById(R.id.comment_more);
		listview_comment = (CustomListView) pageOne
				.findViewById(R.id.listview_comment);
		chooseBt = (Button) pageOne.findViewById(R.id.choose);
		listview_comment.setFocusable(false);
	}

	protected void initSummarizeData(TeacherDetailBean bean) {
		user_name.setText(bean.getTeacher_name());
		if (bean.getTeacher_sex() == 0) {// 男
			chooseBt.setText("选他");
		} else if (bean.getTeacher_sex() == 1) {// 女
			chooseBt.setText("选她");

		}
		user_age.setText(DateTimeTool.getWork(bean.getTeacher_birthday()) + "岁");
		user_experience.setText(DateTimeTool.getWork(bean.getWork_start_time())
				+ "年工作经验");
		user_order_number.setText(bean.getOrder_count() + "单");
		knowledge_score.setText(TeacherDetailBean.getMyScore(bean
				.getKnowledge_score()) + "");
		service_score.setText(TeacherDetailBean.getMyScore(bean
				.getService_score()) + "");
		punctuality_score.setText(TeacherDetailBean.getMyScore(bean
				.getPunctuality_score()) + "");
		// comment_count.setText("(" + bean.getComment_count() + ")");
		String imageUrl = bean.getTeacher_pic_url();
		if (StringTool.isNoBlankAndNoNull(imageUrl)) {
			imageLoader.displayImage(imageUrl, circularImage, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {

						}
					}, null);
		}

	}

	protected void initTitleGrideView(List<TeacherScheduleData> list) {
		int positionByTime = MapBean.getTimePositionByTime(MapBean
				.getCurrentTime(mApplication));
		// 老师排班表
		titleAdapter = new TeacherDetailTimeAdapter(true, this, positionByTime);
		grideview_title.setAdapter(titleAdapter);
		titleAdapter.setData(MapBean.parseTeacherTime());
		titleAdapter.notifyDataSetChanged();
		listAdapter = new TeacherDetaildateAdapter(this, positionByTime);
		listview_date.setAdapter(listAdapter);
		titleList = MapBean.getCalendarHead(ConfigResponse
				.getMyServerTime(mApplication));

		titleList.remove(0);
		dataList = MapBean.getScheduleData(list, titleList, this);
		// 刷新数据
		if (titleList != null && titleList.size() > 3 && dataList != null
				&& dataList.size() > 3) {
			listAdapter.setTitleArray(titleList.subList(0, 3));
			listAdapter.setData(dataList);
		}
		listAdapter.notifyDataSetChanged();
	}

	protected void initCommentData(List<TeacherDetailCommentBean> list) {
		itemEntities = new ArrayList<ItemEntity>();
		for (int i = 0; i < list.size(); i++) {
			entity = new ItemEntity();
			entity.setAvatar(list.get(i).getPost_user_avatar_url());
			entity.setUserName(list.get(i).getPost_user_name());
			entity.setTeacher_score(list.get(i).getTeacher_score());
			entity.setTime(list.get(i).getPost_time_descb());
			String content = list.get(i).getPost_content();
			if (content.contains("{") && content.contains("}")) {
				PicBean picBean = JSON.decode(content, PicBean.class);
				if (picBean.getImageBean() == null
						|| picBean.getImageBean().size() == 0) {
					entity.setImageUrls(null);

				} else {
					int size = picBean.getImageBean().size();
					ImageBean beans;
					imageList = new ArrayList<String>();
					for (int j = 0; j < size; j++) {
						beans = new ImageBean();
						beans = picBean.getImageBean().get(j);
						imageList.add(beans.getImage_url());
					}
					entity.setImageUrls(imageList);
				}
				if (StringTool.isNoBlankAndNoNull(picBean.getMemo())) {
					entity.setContent(picBean.getMemo());
				} else {
					entity.setContent("");
				}
			} else {
				entity.setContent(content);
				entity.setImageUrls(null);
			}
			itemEntities.add(entity);
		}
		TeacherCommentAdapter adapter = new TeacherCommentAdapter(this);
		listview_comment.setAdapter(adapter);
		adapter.setData(itemEntities);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void initData(Context mContext) {

	}

	@Override
	public void initListener() {
		comment_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getContext(), CommentActivity.class);
				intent.putExtra("id", teacherBean.getTeacher_id() + "");
				startActivity(intent);

			}
		});
	}

	@Override
	public void resume() {
	}

	@Override
	public void destroy() {
	}

}
