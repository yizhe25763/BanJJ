package com.shzy.bjj.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shzy.bjj.R;
import com.shzy.bjj.bean.OrderBean;
import com.shzy.bjj.bean.OrderDetailCountBean;
import com.shzy.bjj.bean.TaskDayOrderBean;
import com.shzy.bjj.fragment.OrderFragment.OrderOperation;
import com.shzy.bjj.tools.DoubleTool;

public class OrderTaskAdapter extends BaseAdapter {

	private List<TaskDayOrderBean> data = new ArrayList<TaskDayOrderBean>();
	private LayoutInflater inflater;
	private Context context;
	private OrderOperation orderOperation;

	public OrderOperation getOrderOperation() {
		return orderOperation;
	}

	public void setOrderOperation(OrderOperation orderOperation) {
		this.orderOperation = orderOperation;
	}

	public List<TaskDayOrderBean> getData() {
		return data;
	}

	public void setData(List<TaskDayOrderBean> data) {
		this.data = data;
	}

	public OrderTaskAdapter(Context context) {
		this.context = context;
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
		if (contentView == null) {
			holder = new ViewHolder();
			contentView = inflater.inflate(R.layout.adapter_order_task, null);
			holder.order_numberTextView = (TextView) contentView
					.findViewById(R.id.order_number);
			holder.order_teacherTextView = (TextView) contentView
					.findViewById(R.id.order_teacher);
			holder.order_serviceTextView = (TextView) contentView
					.findViewById(R.id.service_name);
			holder.order_timeTextView = (TextView) contentView
					.findViewById(R.id.order_shijian);
			holder.order_addressTextView = (TextView) contentView
					.findViewById(R.id.order_address);
			holder.order_statusTextView = (TextView) contentView
					.findViewById(R.id.order_status);
			holder.order_priceTextView = (TextView) contentView
					.findViewById(R.id.order_price);
			holder.layout = (LinearLayout) contentView
					.findViewById(R.id.layout);
			holder.orderCancelBt = (TextView) contentView
					.findViewById(R.id.order_cancel);
			holder.orderOkBt = (TextView) contentView
					.findViewById(R.id.order_ok);
			holder.orderCommentBt = (TextView) contentView
					.findViewById(R.id.order_comment);
			holder.orderComplainBt = (TextView) contentView
					.findViewById(R.id.order_complain);
			holder.orderCommentBtImage = (ImageView) contentView
					.findViewById(R.id.order_comment_image);

			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		final TaskDayOrderBean bean = data.get(position);
		OrderBean orderBean = bean.getOrderBean();
		OrderDetailCountBean orderDetailBean = bean.getOrderDetailCountBean();

		holder.order_teacherTextView.setText(orderDetailBean.getTeacher_name());
		holder.order_teacherTextView.setTag(bean);
		int type = orderDetailBean.getService_grade_1_id();
		if (type == 1) {
			holder.order_serviceTextView
					.setText(R.string.teacher_order_course_eachbaby);
		} else {
			holder.order_serviceTextView
					.setText(R.string.teacher_order_course_personal);
		}
		String orderTime = OrderDetailCountBean.getOrderTime(
				orderDetailBean.getService_start_time(),
				orderDetailBean.getService_end_time());
		holder.order_timeTextView.setText(orderTime);
		holder.order_addressTextView.setText(orderBean.getAddress()
				+ orderBean.getAddress_room());
		Double price = (double) orderDetailBean.getService_unit_price();
		holder.order_priceTextView.setText("￥"
				+ DoubleTool.channelPrice(price / 100));
		holder.order_numberTextView.setText(OrderBean.getOrderDetailNumber(bean
				.getOrderDetailCountBean().getOrder_detail_number()));

		int status = orderBean.getStatus();
		int schedule_status = orderDetailBean.getSchedule_status();
		int comment = bean.getOrderDetailCountBean().getIs_comment();
		holder.order_statusTextView.setText(TaskDayOrderBean
				.getOrderDetailStatus(status, schedule_status, comment));

		holder.layout.setOnClickListener(new Click(0, bean));
		holder.orderOkBt.setOnClickListener(new Click(1, bean));
		holder.orderCancelBt.setOnClickListener(new Click(2, bean));
		holder.orderCommentBt.setOnClickListener(new Click(3, bean));
		holder.orderComplainBt.setOnClickListener(new Click(4, bean));
		setOrderStatus(status, schedule_status, holder, comment);
		return contentView;
	}

	private void setOrderStatus(int status, int schedule_status,
			ViewHolder holder, int comment) {
		holder.orderOkBt.setVisibility(View.GONE);
		holder.orderCancelBt.setVisibility(View.INVISIBLE);
		holder.orderCommentBt.setVisibility(View.GONE);
		holder.orderComplainBt.setVisibility(View.GONE);
		if (status == 201) {

		} else if (status >= 200 && status < 400) {
			if (schedule_status == 1) {
				holder.orderCancelBt.setVisibility(View.VISIBLE);
				holder.orderCommentBtImage.setVisibility(View.GONE);
			} else if (schedule_status == 3) {
				holder.orderOkBt.setVisibility(View.VISIBLE);
				holder.orderComplainBt.setVisibility(View.VISIBLE);
			} else if (schedule_status == 4) {
				if (comment == 1) {
					holder.orderCommentBtImage.setVisibility(View.GONE);
					holder.orderCommentBt.setVisibility(View.GONE);
				} else {
					holder.orderCommentBtImage.setVisibility(View.VISIBLE);
					holder.orderCommentBt.setVisibility(View.VISIBLE);
				}
			}else{
				holder.orderCommentBtImage.setVisibility(View.GONE);
			}
		}

	}

	class Click implements OnClickListener {
		private int tag;
		private TaskDayOrderBean bean;

		public Click(int tag, TaskDayOrderBean bean) {
			this.tag = tag;
			this.bean = bean;
		}

		@Override
		public void onClick(View arg0) {
			orderOperation.onClick(tag, bean);
		}

	}

	class ViewHolder {
		private LinearLayout layout;
		private TextView order_statusTextView;
		private TextView order_numberTextView;
		private TextView order_teacherTextView;
		private TextView order_serviceTextView;
		private TextView order_timeTextView;
		private TextView order_addressTextView;
		private TextView order_priceTextView;
		// 订单确认
		private TextView orderOkBt;
		// 订单取消
		private TextView orderCancelBt;
		// 订单投诉
		private TextView orderComplainBt;
		// 订单评价
		private TextView orderCommentBt;
		//
		private ImageView orderCommentBtImage;
	}

}
