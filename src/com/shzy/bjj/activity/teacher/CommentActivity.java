package com.shzy.bjj.activity.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.adapter.TeacherCommentListAdapter;
import com.shzy.bjj.bean.CommentListBean;
import com.shzy.bjj.bean.ImageBean;
import com.shzy.bjj.bean.ItemEntity;
import com.shzy.bjj.bean.PicBean;
import com.shzy.bjj.bean.commentList;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

public class CommentActivity extends BaseActivity {

	private ListView listView;
	// private TeacherCommentListAdapter mAdapter;
	private List<commentList> list = new ArrayList<commentList>();
	private ItemEntity entity;

	private ArrayList<ItemEntity> itemEntities;
	private ArrayList<String> imageList;

	@Override
	public int bindLayout() {
		return R.layout.activity_comment;
	}

	@Override
	public void initView(View view) {
		listView = $(R.id.listview);
	}

	@Override
	public void initData(Context mContext) {
		loading.setVisibility(View.VISIBLE);
		if (StringTool.isNoBlankAndNoNull(getIntent().getExtras().getString(
				"id"))) {
			getComment(getIntent().getExtras().getString("id"));
		}
	}

	private void getComment(String id) {
		itemEntities = new ArrayList<ItemEntity>();

		Map maps = new HashMap<String, String>();
		maps.put(DataTag.TEACHERID, Integer.parseInt(id));
		maps.put(DataTag.PAGE, 1);
		maps.put(DataTag.SIZE, 100);

		HttpTool.post(getContext(), Apis.TEACHER_COMMENT,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							CommentListBean appointmentResponse = JSON.decode(
									response, CommentListBean.class);
							int count = appointmentResponse.getComment_count();
							if (count > 0) {
								$(R.id.no_message).setVisibility(View.GONE);
								loading.setVisibility(View.GONE);
								list = appointmentResponse.getComment_ist();
								for (int i = 0; i < list.size(); i++) {
									entity = new ItemEntity();
									entity.setAvatar(list.get(i)
											.getPost_user_avatar_url());
									entity.setUserName(list.get(i)
											.getPost_user_name());
									entity.setTeacher_score(list.get(i)
											.getTeacher_score());
									entity.setTime(list.get(i)
											.getPost_time_descb());
									String content = list.get(i)
											.getPost_content();
									if (content.contains("{")
											&& content.contains("}")) {
										PicBean picBean = JSON.decode(content,
												PicBean.class);
										if (picBean.getImageBean() == null
												|| picBean.getImageBean()
														.size() == 0) {
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
										if (StringTool
												.isNoBlankAndNoNull(picBean
														.getMemo())) {
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
								TeacherCommentListAdapter mAdapter = new TeacherCommentListAdapter(
										getContext());
								listView.setAdapter(mAdapter);
								listView.setDivider(null);
								mAdapter.setData(itemEntities);
								mAdapter.notifyDataSetChanged();
							} else {
								loading.setVisibility(View.GONE);
								$(R.id.no_message).setVisibility(View.VISIBLE);
							}
						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						loading.setVisibility(View.GONE);
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}

	@Override
	public void initListener() {

		action_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity();
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
