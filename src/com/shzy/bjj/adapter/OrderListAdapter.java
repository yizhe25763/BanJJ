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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.bean.OrderBean;
import com.shzy.bjj.bean.TaskDayBean;
import com.shzy.bjj.bean.TaskDayOrderBean;
import com.shzy.bjj.fragment.OrderFragment.OrderOperation;
import com.shzy.bjj.tools.DoubleTool;
import com.shzy.bjj.view.CustomListView;

public class OrderListAdapter extends BaseAdapter {

	private List<TaskDayBean> data = new ArrayList<TaskDayBean>();
	private LayoutInflater inflater;
	private Context context;
	private Resources res;
	private OrderOperation orderOperation;

	public List<TaskDayBean> getData() {
		return data;
	}

	public void setData(List<TaskDayBean> data) {
		this.data = data;
	}

	public OrderListAdapter(Context context, OrderOperation orderOperation) {
		this.context = context;
		this.orderOperation = orderOperation;
		this.res = context.getResources();
		inflater = LayoutInflater.from(context);
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
	public View getView(int position, View contentView, ViewGroup viewGroup) {
		ViewHolder holder = null;
		OrderTaskAdapter adapter = null;
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = inflater.inflate(R.layout.adapter_order, null);
			holder.order_statusTextView = (TextView) contentView
					.findViewById(R.id.order_status);
			holder.listView = (CustomListView) contentView
					.findViewById(R.id.listview);
			holder.layout = (LinearLayout) contentView
					.findViewById(R.id.layout);
			holder.payLayout = (RelativeLayout) contentView
					.findViewById(R.id.payLayout);
			holder.order_priceTextView = (TextView) contentView
					.findViewById(R.id.order_price);
			holder.payBt = (Button) contentView.findViewById(R.id.payBt);
			holder.CancelBt = (TextView) contentView
					.findViewById(R.id.cancelBt);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		if (adapter == null) {
			adapter = new OrderTaskAdapter(context);
		}
		holder.listView.setAdapter(adapter);

		TaskDayBean bean = data.get(position);
		List<TaskDayOrderBean> list = bean.getList();
		final OrderBean orderBean = bean.getOrderBean();

		adapter.setData(list);
		adapter.setOrderOperation(orderOperation);
		adapter.notifyDataSetChanged();

		holder.order_statusTextView.setTag(bean);
		int status = orderBean.getStatus();
		holder.order_statusTextView.setText(OrderBean
				.getOrderStatusName(status));
		Double price = (double) orderBean.getTotal_price();
		holder.order_priceTextView.setText("￥"
				+ DoubleTool.channelPrice(price / 100));
		holder.payBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				orderOperation.onClick(0, orderBean);
			}
		});
		holder.CancelBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				orderOperation.onClick(1, orderBean);
			}
		});
		if (status == 100) {
			holder.layout.setBackgroundColor(res
					.getColor(R.color.order_status_topay_bg));
			holder.order_statusTextView.setTextColor(res
					.getColor(R.color.order_status_topay_text_color));
			holder.payBt.setVisibility(View.VISIBLE);
			holder.CancelBt.setVisibility(View.VISIBLE);
		} else {
			holder.layout.setBackgroundColor(res
					.getColor(R.color.order_status_payed_bg));
			holder.order_statusTextView.setTextColor(res
					.getColor(R.color.order_status_payed_text_color));
			holder.payBt.setVisibility(View.INVISIBLE);
			holder.CancelBt.setVisibility(View.GONE);
		}
		return contentView;
	}

	class ViewHolder {
		private LinearLayout layout;
		private TextView order_statusTextView;
		private CustomListView listView;
		private RelativeLayout payLayout;
		private TextView order_priceTextView;
		private Button payBt;
		private TextView CancelBt;
	}

}
