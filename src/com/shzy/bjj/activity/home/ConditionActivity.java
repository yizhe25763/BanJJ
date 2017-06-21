package com.shzy.bjj.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.teacher.PopupWindowDialog;
import com.shzy.bjj.adapter.TeacherConditionAdapter;
import com.shzy.bjj.adapter.TeacherListAdapter;
import com.shzy.bjj.bean.MapBean;
import com.shzy.bjj.bean.TeacherBean;
import com.shzy.bjj.bean.TeacherConditionBean;
import com.shzy.bjj.bean.TeacherFollowBean;
import com.shzy.bjj.bean.TeacherFollowResponse;
import com.shzy.bjj.bean.TeacherResponse;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

public class ConditionActivity extends BaseActivity implements
		PopupWindow.OnDismissListener {
	// 老师列表
	protected TeacherListAdapter adapter;

	protected PopupWindow addressPopWindow;
	protected PopupWindow sortPopWindow;
	protected PopupWindow skillPopWindow;
	protected int addressPosition = -1;
	protected int skillPosition = -1;
	protected int sortPosition = -1;
	protected View line;

	protected LinearLayout layout;
	protected LinearLayout area_layout;
	protected LinearLayout sort_layout;
	protected LinearLayout skill_layout;

	protected TextView areaTextView;
	protected ImageView area_arrow;
	protected TextView sortTextView;
	protected ImageView sort_arrow;
	protected TextView skillTextView;
	protected ImageView skill_arrow;

	protected TeacherConditionBean conditionBean = new TeacherConditionBean();
	// 商圈
	protected TeacherConditionAdapter areaAdapter;
	protected String city =DataConst.mCity;

	protected List<MapBean> sortData;
	protected List<MapBean> skillData;
	// 已关注老师列表
	protected List<TeacherFollowBean> followedList = new ArrayList<TeacherFollowBean>();
	// 已服务老师列表
	protected List<TeacherFollowBean> servicedList = new ArrayList<TeacherFollowBean>();

	@Override
	public int bindLayout() {
		return R.layout.fragment_teacher;
	}

	protected TeacherConditionBean getTeacherRequest() {
		conditionBean = new TeacherConditionBean();
		conditionBean.setCity(city);
		conditionBean.setCondition_serviec_type(1);
		conditionBean.setCondition_serviec_id(1);
		conditionBean.setCondition_time("");
		conditionBean.setCondition_skill_id("");
		conditionBean.setCondition_order_id(2);
		// （ asc:升序 desc:降序）
		conditionBean.setCondition_order_type("desc");
		// 商圈名称
		conditionBean.setCondition_district("");
		return conditionBean;
	}

	@Override
	public void initView(View view) {
		areaAdapter = new TeacherConditionAdapter(1, getContext());
		line = $(R.id.line);
		layout = $(R.id.layout);
		area_layout = $(R.id.area_layout);
		sort_layout = $(R.id.date_layout);
		skill_layout = $(R.id.skill_layout);
		area_layout.setVisibility(View.GONE);

		areaTextView = $(R.id.area);
		area_arrow = $(R.id.area_arrow);
		sortTextView = $(R.id.date);
		sort_arrow = $(R.id.date_arrow);
		skillTextView = $(R.id.skill);
		skill_arrow = $(R.id.skill_arrow);
		sortTextView.setText(R.string.teacher_sort);
	}

	@Override
	public void initData(Context mContext) {
		skillData = MapBean.parseSkill();
		sortData = MapBean.parseSort();
		conditionBean = getTeacherRequest();

	}

	@Override
	public void initListener() {
	}

	/**
	 * 显示or隐藏PopupWindow
	 */
	protected void toggleAreaPopupWindow() {
		if (addressPopWindow == null) {
			addressPopWindow = PopupWindowDialog.createPopupWindowFullScreen(
					getContext(), R.layout.teacher_condition_dialog_area);
		}
		addressPopWindow.setOnDismissListener(this);

		View contentView = addressPopWindow.getContentView();
		ListView listview_area = (ListView) contentView
				.findViewById(R.id.listview_area);
		listview_area.setAdapter(areaAdapter);

		areaAdapter.setLastPosition(addressPosition);
		areaAdapter.notifyDataSetChanged();

		if (addressPopWindow.isShowing()) {
			addressPopWindow.dismiss();
		} else {
			addressPopWindow.showAsDropDown(line);
		}

		listview_area.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				TextView nameTextView = (TextView) view.findViewById(R.id.name);
				MapBean bean = (MapBean) nameTextView.getTag();
				if (bean != null) {
					addressPosition = position;
					conditionBean.setCondition_district(bean.getName());
					areaAdapter.setLastPosition(addressPosition);
					areaAdapter.notifyDataSetChanged();
				}
				addressPopWindow.dismiss();
				refreshTeacherList(conditionBean, 100, 1);

				// getTeacherData(conditionBean, 10, 1);
			}
		});
	}

	protected void toggleSkillPopupWindow() {
		if (skillPopWindow == null) {
			skillPopWindow = PopupWindowDialog.createPopupWindowFullScreen(
					getContext(), R.layout.teacher_condition_dialog_skill);
		}
		skillPopWindow.setOnDismissListener(this);
		View contentView = skillPopWindow.getContentView();
		GridView gridView = (GridView) contentView.findViewById(R.id.grideview);
		TextView cancelTextView = (TextView) contentView
				.findViewById(R.id.cancel);
		TextView clearTextView = (TextView) contentView
				.findViewById(R.id.clear);
		TextView okTextView = (TextView) contentView.findViewById(R.id.ok);

		final TeacherConditionAdapter adapter = new TeacherConditionAdapter(0,
				getContext());
		gridView.setAdapter(adapter);
		adapter.setData(skillData);
		adapter.notifyDataSetChanged();

		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.cancel:
					skillPopWindow.dismiss();
					break;

				case R.id.clear:
					skillData = MapBean.parseSkill();
					adapter.setData(skillData);
					adapter.notifyDataSetChanged();
					break;

				case R.id.ok:
					StringBuilder builder = new StringBuilder();
					for (int i = 0, len = skillData.size(); i < len; i++) {
						MapBean bean = skillData.get(i);
						if (bean.isFlag()) {
							builder.append(bean.getTitle() + ",");
						}
					}
					String result = builder.toString().trim();
					if (result.length() > 0) {
						result = result.substring(0, result.length() - 1);
					}
					conditionBean.setCondition_skill_id(result);
					skillPopWindow.dismiss();
					refreshTeacherList(conditionBean, 100, 1);
					break;

				default:
					break;
				}
			}
		};
		cancelTextView.setOnClickListener(clickListener);
		clearTextView.setOnClickListener(clickListener);
		okTextView.setOnClickListener(clickListener);

		if (skillPopWindow.isShowing()) {
			skillPopWindow.dismiss();
		} else {
			skillPopWindow.showAsDropDown(line);
		}
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				int count = 0;
				for (int i = 0, len = skillData.size(); i < len; i++) {
					MapBean bean = skillData.get(i);
					if (bean.isFlag()) {
						count += 1;
					}
				}
				if (count >= 2 && skillData.get(position).isFlag() == false) {
					ToastTool.toastMessage(getContext(),
							R.string.toast_skill_two);
					return;
				}
				if (skillData.get(position).isFlag()) {
					skillData.get(position).setFlag(false);
				} else {
					skillData.get(position).setFlag(true);
				}
				adapter.setData(skillData);
				adapter.notifyDataSetChanged();
			}

		});
	}

	protected void toggleSortPopupWindow() {
		if (sortPopWindow == null) {
			sortPopWindow = PopupWindowDialog.createPopupWindowFullScreen(
					getContext(), R.layout.teacher_condition_dialog_sort);
		}
		sortPopWindow.setOnDismissListener(this);

		View contentView = sortPopWindow.getContentView();
		ListView listView = (ListView) contentView.findViewById(R.id.listview);
		final TeacherConditionAdapter adapter = new TeacherConditionAdapter(2,
				getContext());
		listView.setAdapter(adapter);

		adapter.setData(sortData);
		adapter.setLastPosition(sortPosition);
		adapter.notifyDataSetChanged();

		if (sortPopWindow.isShowing()) {
			sortPopWindow.dismiss();
		} else {
			sortPopWindow.showAsDropDown(line);
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				sortPosition = position;
				conditionBean.setCondition_order_id(position + 1);
				adapter.setLastPosition(sortPosition);
				adapter.notifyDataSetChanged();
				sortPopWindow.dismiss();

				refreshTeacherList(conditionBean, 100, 1);
			}
		});
	}

	@Override
	public void onDismiss() {
		setConditionNUll();
	}

	protected void setConditionNUll() {
		setConditionArrow(areaTextView, area_arrow, false);
		setConditionArrow(sortTextView, sort_arrow, false);
		setConditionArrow(skillTextView, skill_arrow, false);
	}

	protected void setConditionArrow(TextView textView, ImageView img,
			boolean flag) {
		if (flag) {
			textView.setTextColor(getResources().getColor(
					R.color.teacher_condition_selected));
			img.setImageResource(R.drawable.tech_pic_techarrow_up);
		} else {
			textView.setTextColor(getResources().getColor(
					R.color.teacher_condition_normal));
			img.setImageResource(R.drawable.tech_pic_techarrow_down);
		}
	}

	@Override
	public void resume() {
	}

	@Override
	public void destroy() {
	}

	protected void getTeacherData(TeacherConditionBean request, int size,
			int page) {
		Map maps = new HashMap<String, String>();
		maps.put("city", request.getCity());
		maps.put("condition_serviec_type", request.getCondition_serviec_type());
		maps.put("condition_serviec_id", request.getCondition_serviec_id());
		maps.put("condition_time", DateChannel.chooseTeacherDate(request));
		maps.put("condition_skill_id", request.getCondition_skill_id());
		maps.put("condition_order_id", request.getCondition_order_id());
		maps.put("condition_order_type", request.getCondition_order_type());
		maps.put("condition_district", request.getCondition_district());
		maps.put("page", page);
		maps.put("size", size);

		HttpTool.post(getContext(), Apis.TEACHER_SEACHER_LIST, loading,maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {

						if (StringTool.isNoBlankAndNoNull(response)) {
							TeacherResponse teacherResponse = JSON.decode(
									response, TeacherResponse.class);
							int count = teacherResponse.getCount();
							List<TeacherBean> list;
							if (teacherResponse.getCount() <= 0) {
								ToastTool.toastMessage(getContext(),
										"未筛选出老师信息，请重新选择");
								loading.setVisibility(View.GONE);
							}
							if (teacherResponse != null) {
								list = TeacherBean.getTeacherShowData(
										teacherResponse.getList(),
										followedList, servicedList);
								adapter.setData(list);
								loading.setVisibility(View.GONE);
							}
							adapter.notifyDataSetChanged();
						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}

	/**
	 * 已关注老师列表
	 * 
	 * @param loginKey
	 */
	protected void getFollowedTeacher(String loginKey) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.PAGE, 1);
		maps.put(DataTag.SIZE, 10000);

		HttpTool.post(getContext(), Apis.GET_TEACHER_FOllOWED,loading, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							TeacherFollowResponse teacherFollowResponse = JSON
									.decode(response,
											TeacherFollowResponse.class);

							int count = teacherFollowResponse.getCount();
							if (count > 0) {
								followedList = teacherFollowResponse.getList();
							}
						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});
	}

	/**
	 * 已服务老师列表
	 * 
	 * @param loginKey
	 */
	protected void getServicedTeacher(String loginKey) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.PAGE, 1);
		maps.put(DataTag.SIZE, 10000);

		HttpTool.post(getContext(), Apis.GET_TEACHER_SERVICED, loading,maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {

							TeacherFollowResponse teacherFollowResponse = JSON
									.decode(response,
											TeacherFollowResponse.class);
							int count = teacherFollowResponse.getCount();
							if (count > 0) {
								servicedList = teacherFollowResponse.getList();
							}
						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});
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
