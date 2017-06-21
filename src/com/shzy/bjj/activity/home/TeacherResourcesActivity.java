package com.shzy.bjj.activity.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.activity.teacher.TeacherDetailActivity;
import com.shzy.bjj.adapter.TeacherListAdapter;
import com.shzy.bjj.bean.TeacherBean;
import com.shzy.bjj.bean.TeacherConditionBean;
import com.shzy.bjj.bean.TimeBean;
import com.shzy.bjj.tools.ButtonTool;
import com.shzy.bjj.tools.DateTimeTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.view.XListView;
import com.shzy.bjj.view.XListView.IXListViewListener;

/**
 * 选择老师
 * 
 * @author suntao
 * 
 */
public class TeacherResourcesActivity extends ConditionActivity implements
		OnClickListener, OnItemClickListener, IXListViewListener {
	private TextView locationTextView;
	private XListView listview;
	private int position;
	private TimeBean timeBean;

	public static void startActivity(Context context, int position) {
		// flag true返回时间选择列表 false 生成订单页面
		context.startActivity(new Intent(context,
				TeacherResourcesActivity.class).putExtra("position", position));
	}

	public static void startActivity(Context context, int position,
			TimeBean timeBean) {
		// flag true返回时间选择列表 false 生成订单页面
		context.startActivity(new Intent(context,
				TeacherResourcesActivity.class).putExtra("position", position)
				.putExtra("bean", timeBean));
	}

	@Override
	public void initView(View view) {
		super.initView(view);
		position = getIntent().getIntExtra("position", 0);
		timeBean = (TimeBean) getIntent().getSerializableExtra("bean");
		action_title.setText(R.string.teacher_title_select);
		locationTextView = $(R.id.location);
		action_back.setVisibility(View.GONE);
		locationTextView.setVisibility(View.GONE);
		listview = $(R.id.listview);
		adapter = new TeacherListAdapter(getContext(), true, position);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		listview.setXListViewListener((IXListViewListener) this);
	}

	@Override
	public void initData(Context mContext) {
		super.initData(mContext);
		// 获取已服务/已关注 老师列表
		if (mApplication.isLogin()) {
			getFollowedTeacher(mApplication.getLOGIN_KEY());
			getServicedTeacher(mApplication.getLOGIN_KEY());
		}
		if (mApplication.getLocation() != null) {
			city = mApplication.getLocation().getCity();
		}
		if (StringTool.isNoBlankAndNoNull(city)) {
			locationTextView.setText(city);
			conditionBean.setCity(city);
			conditionBean.setCondition_time(timeBean.getOrderTime());
			conditionBean.setCondition_district(timeBean.getAddress()
					.getBusiness_district());
			// getBusinessCircle(city);
		}
		// getTeacherData(conditionBean, 10, 1);
		refreshTeacherList(conditionBean, 100, 1);
		onLoad();
	}

	@Override
	public void onRefresh() {
		initData(getContext());
		loading.setVisibility(View.GONE);

	}

	@Override
	public void onLoadMore() {

	}

	private void onLoad() {
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime(DateTimeTool.formatFriendly(DateTimeTool
				.parseDate(DateTimeTool.gainCurrentDate("yyyy-MM-dd HH:mm:ss"))));

	}

	@Override
	public void initListener() {
		super.initListener();
		area_layout.setOnClickListener(this);
		sort_layout.setOnClickListener(this);
		skill_layout.setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int arg2,
			long arg3) {
		if (ButtonTool.isFastClick()) {
			return;
		}
		TextView teacher_nameTextView = (TextView) view
				.findViewById(R.id.teacher_name);
		TeacherBean bean = (TeacherBean) teacher_nameTextView.getTag();
		if (bean != null) {
			TeacherDetailActivity.startActivity(getContext(), position, true,
					bean);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.area_layout:
			if (ButtonTool.isFastClick()) {
				return;
			}
			setConditionNUll();
			setConditionArrow(areaTextView, area_arrow, true);
			toggleAreaPopupWindow();
			break;
		case R.id.date_layout:
			if (ButtonTool.isFastClick()) {
				return;
			}
			setConditionNUll();
			setConditionArrow(sortTextView, sort_arrow, true);
			toggleSortPopupWindow();
			break;
		case R.id.skill_layout:
			if (ButtonTool.isFastClick()) {
				return;
			}
			setConditionNUll();
			setConditionArrow(skillTextView, skill_arrow, true);
			toggleSkillPopupWindow();
			break;
		default:
			break;
		}
	}

	/**
	 * 刷新教师列表数据
	 */
	protected void refreshTeacherList(TeacherConditionBean conditionBean,
			int size, int page) {
		loading.setVisibility(View.VISIBLE);
		getTeacherData(conditionBean, size, page);
	}

}
