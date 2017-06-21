package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.activity.teacher.GetCalendarData;
import com.shzy.bjj.bean.MapBean;
import com.shzy.bjj.tools.ToastTool;

public class ConditionListViewAdapter extends BaseAdapter {
	private List<MapBean> data = new ArrayList<MapBean>();
	private int tag = 0;
	private LayoutInflater inflater;
	private Resources resources;
	private boolean flag = false;
	private GetCalendarData getCalendarData;
	private int max;
	private int total;
	private boolean isMax;
	private Context context;
	// 判断当前时间对应的时间段，24小时内不可预约
	private int dayTime_position = -1;

	public ConditionListViewAdapter() {
	}

	public ConditionListViewAdapter(Context context, boolean flag, int max,
			boolean isMax, int dayTime_position) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		resources = context.getResources();
		this.flag = flag;
		this.max = max;
		this.isMax = isMax;
		this.dayTime_position = dayTime_position;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public GetCalendarData getGetCalendarData() {
		return getCalendarData;
	}

	public void setGetCalendarData(GetCalendarData getCalendarData) {
		this.getCalendarData = getCalendarData;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public List<MapBean> getData() {
		return data;
	}

	public void setData(List<MapBean> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = inflater.inflate(R.layout.condition_item, null);
			holder.nameTextView = (TextView) contentView
					.findViewById(R.id.name);
			holder.titleTextView = (TextView) contentView
					.findViewById(R.id.title);
			holder.layout = (LinearLayout) contentView
					.findViewById(R.id.layout);
			holder.line = (LinearLayout) contentView.findViewById(R.id.line);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		final MapBean mapBean = data.get(position);
		holder.nameTextView.setTag(mapBean);
		if (tag == 0) {
			reSetView(false, holder);
			holder.nameTextView.setText(mapBean.getTitle());
			holder.titleTextView.setText(mapBean.getName());
			holder.nameTextView.setTextColor(resources
					.getColor(R.color.calendar_title_clor));
			holder.titleTextView.setTextColor(resources
					.getColor(R.color.calendar_title_clor));
			holder.layout.setBackgroundResource(R.drawable.calendar_bg_select);
		} else {

			reSetView(true, holder);
			if (isMax) {
				if (tag == 1) {
					mapBean.setEnable(false);
				}
				if (tag == 2 && dayTime_position > -1
						&& position <= dayTime_position) {
					mapBean.setEnable(false);
				}
			}
			// 允许选
			if (mapBean.isEnable()) {

				if (flag) {
					// 老师端 选择 时间
					holder.nameTextView
							.setText(String.valueOf(mapBean.getId()));
				}
				// 已经选中
				if (mapBean.isFlag()) {
					holder.layout
							.setBackgroundResource(R.drawable.calendar_bg_selected);

				} else {
					holder.layout
							.setBackgroundResource(R.drawable.calendar_bg_select);
				}

				// 不允许
			} else {
				holder.layout
						.setBackgroundResource(R.drawable.calendar_bg_diaable);
			}
		}

		if (position == 3 || position == 7) {
			holder.line.setVisibility(View.INVISIBLE);
		} else {
			holder.line.setVisibility(View.GONE);
		}
		holder.layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (isMax && total >= max && mapBean.isEnable()
						&& mapBean.isFlag() == false) {
					ToastTool
							.toastMessage(context, "一次订单最多只能选" + max + "个时间段！");
					return;
				}
				if (mapBean.isEnable()) {
					if (mapBean.isFlag()) {
						mapBean.setFlag(false);
					} else {
						mapBean.setFlag(true);
					}
					getCalendarData.onClick(tag, position, mapBean);
				}
			}
		});
		return contentView;
	}

	private void reSetView(boolean flag, ViewHolder holder) {
		if (flag) {
			holder.nameTextView.setVisibility(View.GONE);
			holder.titleTextView.setVisibility(View.GONE);
		} else {
			holder.nameTextView.setVisibility(View.VISIBLE);
			holder.titleTextView.setVisibility(View.VISIBLE);
		}
	}

	class ViewHolder {
		private LinearLayout line;
		private LinearLayout layout;
		private TextView nameTextView;
		private TextView titleTextView;
	}

}
