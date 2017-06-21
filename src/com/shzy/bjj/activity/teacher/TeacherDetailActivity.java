package com.shzy.bjj.activity.teacher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.home.AppointmentListActivity;
import com.shzy.bjj.activity.home.TeacherResourcesActivity;
import com.shzy.bjj.bean.TeacherBean;
import com.shzy.bjj.bean.TeacherDetailBean;
import com.shzy.bjj.bean.TeacherScheduleResponse;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;

/**
 * 老师详情
 * 
 * @author suntao
 * 
 */
public class TeacherDetailActivity extends TeacherSummarizeActivity implements
		OnClickListener {

	private Boolean tag = false;
	private int position;

	public static void startActivity(Context context, int positon, Boolean tag,
			TeacherBean bean) {
		context.startActivity(new Intent(context, TeacherDetailActivity.class)
				.putExtra("position", positon).putExtra("tag", tag)
				.putExtra("bean", bean));
	}

	public static void startActivity(Context context, TeacherBean bean) {
		context.startActivity(new Intent(context, TeacherDetailActivity.class)
				.putExtra("bean", bean));
	}

	@Override
	public void initView(View view) {
		super.initView(view);
		mViewPager.setVisibility(View.INVISIBLE);
		if (mApplication.isLogin()) {
			if (teacherBean.isFollowed()) {
				action_fav.setBackgroundResource(R.drawable.yy_btn_fav_has);
			} else {
				action_fav.setBackgroundResource(R.drawable.yy_btn_fav_normal);
			}
			action_fav.setVisibility(View.VISIBLE);
		} else {
			action_fav.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void initData(Context mContext) {
		tag = getIntent().getExtras().getBoolean("tag");
		position = position = getIntent().getIntExtra("position", 0);
		if (teacherBean != null) {
			loading.setVisibility(View.VISIBLE);
			getTeacherDetail(teacherBean.getTeacher_id());
			getTeacherScheule(teacherBean.getTeacher_id());
		} else {
			AppManager.getAppManager().finishActivity();
		}
	}

	private void setViewData(TeacherDetailBean teacherDetailBean) {
		initSummarizeData(teacherDetailBean);
		if (teacherDetailBean.getComment_count() > 0) {
			initCommentData(teacherDetailBean.getComment_ist());
		}
		setTeacherDataDetail(teacherDetailBean);
	}

	@Override
	public void initListener() {
		super.initListener();
		action_back.setOnClickListener(this);
		action_title.setOnClickListener(this);
		action_title_right.setOnClickListener(this);
		action_fav.setOnClickListener(this);
		chooseBt.setOnClickListener(this);
		teacher_detail_all_time_arrow.setOnClickListener(this);
		teacher_detail_all_time.setOnClickListener(this);
	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.action_back:
			AppManager.getAppManager().finishActivity();
			break;

		case R.id.action_fav:
			if (mApplication.isLogin()) {
				action_fav.setClickable(false);
				loading.setVisibility(View.VISIBLE);
				if (teacherBean.isFollowed()) {
					teacherCancelFollow(mApplication.getLOGIN_KEY(),
							teacherBean.getTeacher_id());
				} else {
					teacherFollow(mApplication.getLOGIN_KEY(),
							teacherBean.getTeacher_id());
				}
			}
			break;

		case R.id.action_title:
			action_title.setTextColor(resources.getColor(R.color.white));
			action_title_right.setTextColor(resources
					.getColor(R.color.teacher_title));
			currentIndex = 0;
			mViewPager.setCurrentItem(currentIndex);
			break;

		case R.id.action_title_right:
			action_title
					.setTextColor(resources.getColor(R.color.teacher_title));
			action_title_right.setTextColor(resources.getColor(R.color.white));
			currentIndex = 1;
			mViewPager.setCurrentItem(currentIndex);
			break;

		case R.id.choose:
			if (teacherBean != null) {
				if (tag) {
					AppointmentListActivity.data.get(position).setTeacherBean(
							teacherBean);
					AppointmentListActivity.isRefresh = true;
					AppManager.getAppManager().finishActivity();
					AppManager.getAppManager().finishActivity(
							TeacherResourcesActivity.class);

				} else {
					ChooseTeacherActivity.startActivity(this, teacherBean);

				}
			}
			break;

		case R.id.teacher_detail_all_time_arrow:
		case R.id.teacher_detail_all_time:
			if (titleList != null && dataList != null) {
				if (isArrow) {
					// 刷新数据
					if (titleList != null && titleList.size() > 3
							&& dataList != null && dataList.size() > 3) {
						((ImageView) $(R.id.teacher_detail_all_time_arrow))
								.setBackgroundResource(R.drawable.tech_pic_techarrow_down);
						listAdapter.setTitleArray(titleList.subList(0, 3));
						listAdapter.setData(dataList);
						listAdapter.notifyDataSetChanged();
						isArrow = false;

					}
				} else {
					// 刷新数据
					((ImageView) $(R.id.teacher_detail_all_time_arrow))
							.setBackgroundResource(R.drawable.tech_pic_techarrow_up);

					listAdapter.setTitleArray(titleList);
					listAdapter.setData(dataList);
					listAdapter.notifyDataSetChanged();
					isArrow = true;
				}
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onPageScrolled(int i, float v, int i2) {

	}

	@Override
	public void onPageSelected(int i) {
		if (0 == i) {
			action_title.setTextColor(resources.getColor(R.color.white));
			action_title_right.setTextColor(resources
					.getColor(R.color.teacher_title));
		} else {
			action_title
					.setTextColor(resources.getColor(R.color.teacher_title));
			action_title_right.setTextColor(resources.getColor(R.color.white));
		}
		currentIndex = i;
		mViewPager.setCurrentItem(currentIndex);
	}

	@Override
	public void onPageScrollStateChanged(int i) {
	}

	public class ViewPagerAdapter extends PagerAdapter {
		private List<View> listView;

		public ViewPagerAdapter() {
		}

		public ViewPagerAdapter(List<View> listViews) {
			this.listView = listViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(listView.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(listView.get(position), 0);
			return listView.get(position);
		}

		@Override
		public int getCount() {
			return listView.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	protected void teacherFollow(String loginKey, int teacher_id) {
		Map maps = new HashMap<String, String>();
		maps.put("teacher_id", teacher_id);
		maps.put(DataTag.LOGINKEY, loginKey);
		HttpTool.post(getContext(), Apis.TEACHER_FOLLOW,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							action_fav
									.setBackgroundResource(R.drawable.yy_btn_fav_has);
							loading.setVisibility(View.GONE);
							action_fav.setClickable(true);

							teacherBean.setFollowed(true);
						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
						loading.setVisibility(View.GONE);
						action_fav.setClickable(true);

					}
				});
	}

	protected void teacherCancelFollow(String loginKey, int teacher_id) {
		Map maps = new HashMap<String, String>();
		maps.put("teacher_id", teacher_id);
		maps.put(DataTag.LOGINKEY, loginKey);
		HttpTool.post(getContext(), Apis.TEACHER_FOLLOW_CANCEL,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							action_fav
									.setBackgroundResource(R.drawable.yy_btn_fav_normal);
							teacherBean.setFollowed(false);
							loading.setVisibility(View.GONE);
							action_fav.setClickable(true);

						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
						loading.setVisibility(View.GONE);
						action_fav.setClickable(true);

					}
				});
	}

	/**
	 * 老师详情
	 * 
	 * @param id
	 */
	protected void getTeacherDetail(int id) {
		Map maps = new HashMap<String, String>();
		maps.put("teacher_id", id);

		HttpTool.post(getContext(), Apis.TEACHER_DETAIL,loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						loading.setVisibility(View.GONE);
						mViewPager.setVisibility(View.VISIBLE);
						if (StringTool.isNoBlankAndNoNull(response)) {
							TeacherDetailBean teacherDetailBean = JSON.decode(
									response, TeacherDetailBean.class);
							if (teacherDetailBean != null) {
								setViewData(teacherDetailBean);
							}
						}
						mViewPager.setVisibility(View.VISIBLE);
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						mViewPager.setVisibility(View.VISIBLE);
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});
	}

	/**
	 * 老师排班表
	 * 
	 * @param id
	 */
	protected void getTeacherScheule(int id) {
		Map maps = new HashMap<String, String>();
		maps.put("teacher_id", id);

		HttpTool.post(getContext(), Apis.TEACHER_SCHEDULE, loading,maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							TeacherScheduleResponse teacherScheduleResponse = JSON
									.decode(response,
											TeacherScheduleResponse.class);
							if (teacherScheduleResponse != null
									&& teacherScheduleResponse.getDate_count() > 0) {
								initTitleGrideView(teacherScheduleResponse
										.getDate_list());
							}
						}
						mViewPager.setVisibility(View.VISIBLE);
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						mViewPager.setVisibility(View.VISIBLE);
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});
	}

}
