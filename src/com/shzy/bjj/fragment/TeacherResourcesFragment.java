package com.shzy.bjj.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.activity.home.LocationActivity;
import com.shzy.bjj.activity.teacher.TeacherConditionFragment;
import com.shzy.bjj.activity.teacher.TeacherDetailActivity;
import com.shzy.bjj.adapter.TeacherListAdapter;
import com.shzy.bjj.bean.TeacherBean;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.tools.DateTimeTool;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.view.XListView;
import com.shzy.bjj.view.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;

/**
 * 教师列表
 * 
 * @author suntao
 * 
 */
public class TeacherResourcesFragment extends TeacherConditionFragment
		implements OnClickListener, OnItemClickListener, IXListViewListener {
	private TextView locationTextView;
	private XListView listview;

	@Override
	public void initView(View view) {
		super.initView(view);
		action_title.setText(R.string.teacher_title_content);
		locationTextView = $(R.id.location);
		listview = $(R.id.listview);
		listview.setPullLoadEnable(true);
		listview.setXListViewListener((IXListViewListener) this);
		adapter = new TeacherListAdapter(getContext(), false, 0);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
	}

	@Override
	public void initData(Context mContext) {
		super.initData(mContext);
		if (StringTool.isNoBlankAndNoNull(DataConst.mCity)) {
			city = DataConst.mCity;
		}
		city = DataConst.mCity;
		if (!HttpTool.checkNetwork(getActivity())) {
			loading.setVisibility(View.GONE);
			return;
		}
		if (StringTool.isNoBlankAndNoNull(city)) {
			locationTextView.setText(city);
			conditionBean.setCity(city);
			getBusinessCircle(city);
		}
		refreshTeacherList(conditionBean, 100, 1);
		if (HttpTool.checkNetwork(getActivity())) {
			onLoad();
		} else {
		}
	}

	@Override
	public void initListener() {
		super.initListener();
		area_layout.setOnClickListener(this);
		date_layout.setOnClickListener(this);
		skill_layout.setOnClickListener(this);
		action_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent().setClass(getActivity(),
						LocationActivity.class));
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		TextView teacher_nameTextView = (TextView) view
				.findViewById(R.id.teacher_name);
		TeacherBean bean = (TeacherBean) teacher_nameTextView.getTag();
		if (!HttpTool.checkNetwork(getActivity())) {
			loading.setVisibility(View.GONE);
			return;
		}
		if (bean != null) {
			TeacherDetailActivity.startActivity(getContext(), bean);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.area_layout:
			setConditionNUll();
			setConditionArrow(areaTextView, area_arrow, true);
			toggleAreaPopupWindow();
			break;
		case R.id.date_layout:
			setConditionNUll();
			setConditionArrow(dateTextView, date_arrow, true);
			toggleDatePopupWindow();
			break;
		case R.id.skill_layout:
			setConditionNUll();
			setConditionArrow(skillTextView, skill_arrow, true);
			toggleSkillPopupWindow();
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onEvent(getContext(), "A0008");
		if (mApplication.isLogin()) {
			if (!HttpTool.checkNetwork(getActivity())) {
				loading.setVisibility(View.GONE);
				return;
			}
			getFollowedTeacher(mApplication.getLOGIN_KEY());
			getServicedTeacher(mApplication.getLOGIN_KEY());
		} else {
			followedList.clear();
			servicedList.clear();
		}

	}

	@Override
	public void onRefresh() {
		if (HttpTool.checkNetwork(getActivity())) {
			initData(getContext());
			loading.setVisibility(View.GONE);
		} else {
			listview.stopRefresh();
		}

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
}
