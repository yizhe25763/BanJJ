package com.shzy.bjj.activity.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.annotation.SuppressLint;
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
import com.shzy.bjj.adapter.BusinessAdapter;
import com.shzy.bjj.adapter.BusinessAreaAdapter;
import com.shzy.bjj.adapter.ConditionGrideViewAdapter;
import com.shzy.bjj.adapter.ConditionTitleListViewAdapter;
import com.shzy.bjj.adapter.TeacherConditionAdapter;
import com.shzy.bjj.adapter.TeacherListAdapter;
import com.shzy.bjj.bean.BusinessAreaBean;
import com.shzy.bjj.bean.BusinessDistrictBean;
import com.shzy.bjj.bean.BusinessResponse;
import com.shzy.bjj.bean.CalendarTimeBean;
import com.shzy.bjj.bean.ConfigResponse;
import com.shzy.bjj.bean.MapBean;
import com.shzy.bjj.bean.TeacherBean;
import com.shzy.bjj.bean.TeacherConditionBean;
import com.shzy.bjj.bean.TeacherFollowBean;
import com.shzy.bjj.bean.TeacherFollowResponse;
import com.shzy.bjj.bean.TeacherResponse;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.fragment.base.BaseFragment;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.AlertTool;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

public class TeacherConditionFragment extends BaseFragment implements
		PopupWindow.OnDismissListener {
	// 老师列表
	protected TeacherListAdapter adapter;
	protected ConditionGrideViewAdapter conditionGrideViewAdapter;

	protected PopupWindow addressPopWindow;
	protected PopupWindow datePopWindow;
	protected PopupWindow skillPopWindow;
	protected int areaPosition = -1;
	protected int addressPosition = -1;
	protected View line;

	protected LinearLayout layout;
	protected LinearLayout area_layout;
	protected LinearLayout date_layout;
	protected LinearLayout skill_layout;

	protected TextView areaTextView;
	protected ImageView area_arrow;
	protected TextView dateTextView;
	protected ImageView date_arrow;
	protected TextView skillTextView;
	protected ImageView skill_arrow;

	protected TeacherConditionBean conditionBean = new TeacherConditionBean();
	protected List<List<MapBean>> dataList = MapBean.getData(null, null,
			getContext());
	protected List<MapBean> titleList;
	// 商圈
	protected BusinessAreaAdapter areaAdapter;
	protected BusinessAdapter businessAdapter;
	protected String city = DataConst.mCity;

	// 已关注老师列表
	protected List<TeacherFollowBean> followedList = new ArrayList<TeacherFollowBean>();
	// 已服务老师列表
	protected List<TeacherFollowBean> servicedList = new ArrayList<TeacherFollowBean>();
	protected List<MapBean> skillData;

	protected TeacherConditionBean getTeacherRequest() {
		conditionBean = new TeacherConditionBean();
		if (StringTool.isNoBlankAndNoNull(city)) {
			conditionBean.setCity(city);
		} else {
			conditionBean.setCity(null);
		}
		conditionBean.setCondition_serviec_type(1);
		conditionBean.setCondition_serviec_id(1);
		conditionBean.setCondition_time("");
		conditionBean.setCondition_skill_id("");
		conditionBean.setCondition_order_id(2);
		// （ asc:升序 desc:降序）
		conditionBean.setCondition_order_type("asc");
		// 商圈名称
		conditionBean.setCondition_district("");
		return conditionBean;
	}

	private boolean isSkill = false;
	private boolean isTime = false;
	private boolean isAddress = false;

	@Override
	public int bindLayout() {
		return R.layout.fragment_teacher;
	}

	@Override
	public void onResume() {
		super.onResume();
		initData(getContext());
	}

	@Override
	public void initView(View view) {
		businessAdapter = new BusinessAdapter(getContext());
		areaAdapter = new BusinessAreaAdapter(getContext());
		line = $(R.id.line);
		layout = $(R.id.layout);
		area_layout = $(R.id.area_layout);
		date_layout = $(R.id.date_layout);
		skill_layout = $(R.id.skill_layout);

		areaTextView = $(R.id.area);
		area_arrow = $(R.id.area_arrow);
		dateTextView = $(R.id.date);
		date_arrow = $(R.id.date_arrow);
		skillTextView = $(R.id.skill);
		skill_arrow = $(R.id.skill_arrow);
	}

	@Override
	public void initData(Context mContext) {
		skillData = MapBean.parseSkill();
		conditionBean = getTeacherRequest();
		dataList = MapBean.getData(null, null, getContext());
		titleList = MapBean.getCalendarHead(ConfigResponse
				.getMyServerTime(mApplication));

		// getBusinessCircle(city);
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
		ListView listview_address = (ListView) contentView
				.findViewById(R.id.listview_address);
		listview_area.setAdapter(areaAdapter);
		listview_address.setAdapter(businessAdapter);
		LinearLayout layout_area = (LinearLayout) contentView
				.findViewById(R.id.title_area);
		layout_area.setVisibility(View.VISIBLE);
		final LinearLayout layout_address = (LinearLayout) contentView
				.findViewById(R.id.title_address);

		areaAdapter.setLastPosition(areaPosition);
		areaAdapter.notifyDataSetChanged();
		businessAdapter.setLastPosition(addressPosition);
		businessAdapter.notifyDataSetChanged();

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
				BusinessAreaBean bean = (BusinessAreaBean) nameTextView
						.getTag();
				if (bean != null) {
					areaPosition = position;
					areaAdapter.setLastPosition(areaPosition);
					areaAdapter.notifyDataSetChanged();
					List<BusinessDistrictBean> list = new ArrayList<BusinessDistrictBean>();
					if (bean.getDistrict_count() > 0) {
						list.addAll(bean.getDistrict_list());
					}
					businessAdapter.setData(list);
					businessAdapter.notifyDataSetChanged();
					layout_address.setVisibility(View.VISIBLE);
				}
			}
		});

		listview_address.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				TextView nameTextView = (TextView) view.findViewById(R.id.name);
				BusinessDistrictBean bean = (BusinessDistrictBean) nameTextView
						.getTag();
				if (bean != null) {
					addressPosition = position;
					businessAdapter.setLastPosition(addressPosition);
					businessAdapter.notifyDataSetChanged();
					conditionBean.setCondition_district(bean.getDistrict_name());
					areaTextView.setText(bean.getDistrict_name());
					refreshTeacherList(conditionBean, 10, 1);
				}
				addressPopWindow.dismiss();
				isAddress = true;
				areaTextView.setTextColor(getResources().getColor(
						R.color.voucher_h_color));

			}
		});

		OnClickListener clickListener = new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.title_area:
				case R.id.title_address:
					areaPosition = -1;
					areaAdapter.setLastPosition(areaPosition);
					areaAdapter.notifyDataSetChanged();

					addressPosition = -1;
					businessAdapter.setLastPosition(addressPosition);
					businessAdapter
							.setData(new ArrayList<BusinessDistrictBean>());
					businessAdapter.notifyDataSetChanged();
					conditionBean.setCondition_district("");
					areaTextView.setText("不限");
					refreshTeacherList(conditionBean, 100, 1);
					addressPopWindow.dismiss();
					isAddress = true;
					areaTextView.setTextColor(getResources().getColor(
							R.color.voucher_h_color));
					layout_address.setVisibility(View.GONE);
					break;

				default:
					break;
				}
			}
		};

		layout_area.setOnClickListener(clickListener);
		layout_address.setOnClickListener(clickListener);
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

			@SuppressLint("ResourceAsColor")
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
					conditionBean.setCondition_skill_id("");
					skillTextView.setText("擅长");
					skillPopWindow.dismiss();
					refreshTeacherList(conditionBean, 100, 1);
					setConditionArrow(skillTextView, skill_arrow, false);
					skillTextView.setTextColor(getResources().getColor(
							R.color.voucher_up_color));
					isSkill = false;
					break;

				case R.id.ok:
					//
					StringBuilder builder = new StringBuilder("");
					StringBuilder content = new StringBuilder("");
					for (int i = 0, len = skillData.size(); i < len; i++) {
						MapBean bean = skillData.get(i);
						if (bean.isFlag()) {
							builder.append(bean.getTitle() + ",");
							content.append(bean.getName() + ",");
						}
					}
					String result = builder.toString().trim();
					if (result.length() > 0) {
						result = result.substring(0, result.length() - 1);
					} else {
						result = "";
					}

					String contents = content.toString().trim();
					if (contents.length() > 0) {
						contents = contents.substring(0, contents.length() - 1);
						if (contents.contains(",")) {
							contents = contents.substring(0,
									contents.indexOf(","))
									+ "等";
						}
					} else {
						contents = "擅长";

					}

					skillTextView.setText(contents);
					conditionBean.setCondition_skill_id(result);
					skillPopWindow.dismiss();
					if (contents.equals("擅长")) {
						skillTextView.setTextColor(getResources().getColor(
								R.color.voucher_up_color));
						isSkill = false;
					} else {
						skillTextView.setTextColor(getResources().getColor(
								R.color.voucher_h_color));
						isSkill = true;
					}
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

	protected void toggleDatePopupWindow() {
		if (datePopWindow == null) {
			datePopWindow = PopupWindowDialog.createPopupWindowFullScreen(
					getContext(), R.layout.teacher_condition_dialog_date);
		}
		datePopWindow.setOnDismissListener(this);
		View contentView = datePopWindow.getContentView();
		GridView titleGridView = (GridView) contentView
				.findViewById(R.id.grideview_title);
		GridView gridView = (GridView) contentView.findViewById(R.id.grideview);
		TextView cancelTextView = (TextView) contentView
				.findViewById(R.id.cancel);
		TextView clearTextView = (TextView) contentView
				.findViewById(R.id.clear);
		TextView okTextView = (TextView) contentView.findViewById(R.id.ok);
		// isMAX=是否限制选择数量 flag:是否初始化timeID
		conditionGrideViewAdapter = new ConditionGrideViewAdapter(getContext(),
				new GetCalendar(), false, 0, 0, false, 0);
		gridView.setAdapter(conditionGrideViewAdapter);
		conditionGrideViewAdapter.setData(dataList);
		conditionGrideViewAdapter.notifyDataSetChanged();

		ConditionTitleListViewAdapter titleAdapter = new ConditionTitleListViewAdapter(
				getContext());
		titleGridView.setAdapter(titleAdapter);
		titleAdapter.setData(titleList);
		titleAdapter.notifyDataSetChanged();
		// 时间
		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.cancel:
					datePopWindow.dismiss();
					break;

				case R.id.clear:
					dataList = MapBean.getData(null, null, getContext());
					conditionGrideViewAdapter.setData(dataList);
					conditionGrideViewAdapter.notifyDataSetChanged();
					conditionBean.setCondition_time("");
					datePopWindow.dismiss();
					refreshTeacherList(conditionBean, 100, 1);
					dateTextView.setTextColor(getResources().getColor(
							R.color.voucher_up_color));
					isTime = false;
					break;
				case R.id.ok:
					// 转化时间
					String time = CalendarTimeBean
							.getCalendarTime(dataList, titleList).toString()
							.trim();
					if (time.length() < 5) {
						time = "";
						dateTextView.setTextColor(getResources().getColor(
								R.color.voucher_up_color));
					}
					conditionBean.setCondition_time(time);
					datePopWindow.dismiss();
					refreshTeacherList(conditionBean, 100, 1);
					if (time.length() < 5) {
						dateTextView.setTextColor(getResources().getColor(
								R.color.voucher_up_color));
						isTime = false;
					} else {
						dateTextView.setTextColor(getResources().getColor(
								R.color.voucher_h_color));
						isTime = true;
					}

					break;

				default:
					break;
				}
			}

		};
		cancelTextView.setOnClickListener(clickListener);
		clearTextView.setOnClickListener(clickListener);
		okTextView.setOnClickListener(clickListener);
		if (datePopWindow.isShowing()) {
			datePopWindow.dismiss();
		} else {
			datePopWindow.showAsDropDown(line);
		}
	}

	class GetCalendar implements GetCalendarData {

		@Override
		public void onClick(int tag, int position, MapBean bean) {
			dataList.get(tag).set(position, bean);
			conditionGrideViewAdapter.setData(dataList);
			conditionGrideViewAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onDismiss() {
		setConditionNUll();
	}

	protected void setConditionNUll() {
		setConditionArrow(areaTextView, area_arrow, false);
		setConditionArrow(dateTextView, date_arrow, false);
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
		if (isAddress) {
			areaTextView.setTextColor(getResources().getColor(
					R.color.voucher_h_color));
		}
		// else {
		// areaTextView.setTextColor(getResources().getColor(
		// R.color.teacher_condition_selected));
		// }
		if (isSkill) {
			skillTextView.setTextColor(getResources().getColor(
					R.color.voucher_h_color));
		} else {

		}
		if (isTime) {
			dateTextView.setTextColor(getResources().getColor(
					R.color.voucher_h_color));
		}

		// else {
		// dateTextView.setTextColor(getResources().getColor(
		// R.color.teacher_condition_selected));
		// }

	}

	protected void getBusinessCircle(String city) {
		Map maps = new HashMap<String, String>();
		maps.put("city", city);
		HttpTool.post(getContext(), Apis.CLIENT_BUSINESS_LIST, loading, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							BusinessResponse businessResponse = JSON.decode(
									response, BusinessResponse.class);
							int count = businessResponse.getArea_count();
							if (count > 0) {
								List<BusinessAreaBean> data = businessResponse
										.getArea_list();
								// data.add(0, new BusinessAreaBean("不限", 0));
								areaAdapter.setData(data);
								areaAdapter.notifyDataSetChanged();
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
		if (StringTool.isNoBlankAndNoNull(conditionBean.getCity())) {
			getTeacherData(conditionBean, size, page);
		} else {
			ToastTool.toastMessage(getActivity(), "定位失败，请手动选择城市！");
		}
	}

	protected void getTeacherData(TeacherConditionBean request, int size,
			int page) {
		Map maps = new HashMap<String, String>();
		maps.put("city", request.getCity());
		maps.put("condition_time", request.getCondition_time());
		maps.put("condition_skill_id", request.getCondition_skill_id());
		maps.put("condition_order_id", request.getCondition_order_id());
		maps.put("condition_order_type", request.getCondition_order_type());
		maps.put("condition_district", request.getCondition_district());
		maps.put("page", page);
		maps.put("size", 1000);

		HttpTool.post(getContext(), Apis.TEACHER_SEACHER_LIST, loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							TeacherResponse teacherResponse = JSON.decode(
									response, TeacherResponse.class);
							int count = teacherResponse.getCount();
							List<TeacherBean> list;
							if (teacherResponse.getCount() <= 0) {
								loading.setVisibility(View.GONE);
								$(R.id.no_message).setVisibility(View.VISIBLE);
							} else {
								$(R.id.no_message).setVisibility(View.GONE);
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
						loading.setVisibility(View.GONE);
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

		HttpTool.post(getContext(), Apis.GET_TEACHER_FOllOWED, loading, maps,
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

		HttpTool.post(getContext(), Apis.GET_TEACHER_SERVICED, loading, maps,
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
}
