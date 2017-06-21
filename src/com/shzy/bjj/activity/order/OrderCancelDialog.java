package com.shzy.bjj.activity.order;

import java.util.HashMap;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.TabFragmentActivity;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.home.CallPhoneActivity;
import com.shzy.bjj.bean.CannelOrderBean;
import com.shzy.bjj.bean.OrderDetailCountBean;
import com.shzy.bjj.bean.QueryReimburseOrderBean;
import com.shzy.bjj.bean.TaskDayOrderBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

/**
 * 取消订单
 * 
 * @author Administrator
 * 
 */
public class OrderCancelDialog extends BaseActivity implements OnClickListener {
	private TextView toastTextView;

	private TextView cancelBt;
	private TextView okBt;
	private TextView phoneBt;

	private View line_View;
	private View line_hView;
	private ImageButton deleteButton;
	private ImageView toastImageView;
	private TaskDayOrderBean orderetailBean;

	/**
	 * 订单编号
	 */
	private String mOrderNumber;
	/**
	 * 订单明细编号(如有多个以逗号分开)
	 */
	private String mOrderDetailNumber;
	/**
	 * 服务时间
	 */
	private String times;
	/**
	 * 退款金额
	 */
	private TextView vMoney;
	private int mMoney;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			toastTextView.setVisibility(View.GONE);
			cancelBt.setVisibility(View.GONE);
			phoneBt.setVisibility(View.GONE);
			line_View.setVisibility(View.GONE);
			deleteButton.setVisibility(View.GONE);
			okBt.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
			toastImageView
					.setImageResource(R.drawable.dialog_cancel_order_success);
		}

	};

	public static void startActivity(Context context, TaskDayOrderBean orderBean) {
		context.startActivity(new Intent(context, OrderCancelDialog.class)
				.putExtra("bean", orderBean));
	}

	@Override
	public int bindLayout() {
		return R.layout.order_cancel_dialog;
	}

	@Override
	public void initView(View view) {
		orderetailBean = (TaskDayOrderBean) getIntent().getSerializableExtra(
				"bean");
		toastTextView = $(R.id.date);
		vMoney = $(R.id.money);
		cancelBt = $(R.id.cancel);
		okBt = $(R.id.ok);
		phoneBt = $(R.id.phone);
		line_View = $(R.id.line_v);
		deleteButton = $(R.id.delete);
		toastImageView = $(R.id.toast);
	}

	@Override
	public void initData(Context mContext) {
		if (orderetailBean != null) {
			mOrderNumber = orderetailBean.getOrderBean().getOrder_number();
			mOrderDetailNumber = orderetailBean.getOrderDetailCountBean()
					.getOrder_detail_number();
			toastTextView.setText("服务时间为："
					+ OrderDetailCountBean.getDate(orderetailBean
							.getOrderDetailCountBean().getService_start_time())
					+ orderetailBean.getOrderDetailCountBean()
							.getService_start_time().substring(11, 13) + "点");
		}
		if (mApplication.isLogin()
				&& StringTool.isNoBlankAndNoNull(mOrderNumber)
				&& StringTool.isNoBlankAndNoNull(mOrderDetailNumber)) {
			getQueryReimburseOrder(mApplication.getLOGIN_KEY(), mOrderNumber,
					mOrderDetailNumber);
		}

	}

	/**
	 * 查询订单退款金额
	 * 
	 * @param login_KEY
	 * @param mOrderDetailNumber
	 *            订单明细
	 * @param mOrderNumber
	 *            订单号
	 */
	private void getQueryReimburseOrder(String login_KEY, String mOrderNumber,
			String mOrderDetailNumber) {

		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, login_KEY);
		maps.put(DataTag.ORDERNUMBER, mOrderNumber);
		maps.put(DataTag.ORDERDETAILNUMBER, mOrderDetailNumber);
		HttpTool.post(getContext(),Apis.QUERYREIMBURSEORDER, loading,maps, new StringHandler(null) {

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					QueryReimburseOrderBean result = JSON.decode(response,
							QueryReimburseOrderBean.class);
					if (result != null) {
						mMoney = result.getMoney();
						Double mPrice = (double) mMoney;
						vMoney.setText(mPrice / 100 + "元");
					}
				}
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				QueryReimburseOrderBean bean = JSON.decode(responseBody,
						QueryReimburseOrderBean.class);
				if (bean != null) {
					if (bean.getResult() == 1) {
						ToastTool.toastMessage(getContext(),
								showFailMessage(bean.getFailue_reason_type()));
					} else {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				}
			}

			private String showFailMessage(int failue_reason_type) {
				switch (failue_reason_type) {
				case 0:
					return "系统异常";
				case -1:
					return "退款期限超时";
				case -2:
					return "超出最大可退金额";
				case -3:
					return "子订单状态不符合退款条件";
				case 2:
					return "订单不存在";
				case 3:
					return "子订单不存在";

				}
				return "";
			}
		});

	}

	@Override
	public void initListener() {
		phoneBt.setOnClickListener(this);
		cancelBt.setOnClickListener(this);
		okBt.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
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
		case R.id.cancel:
			if (mApplication.isLogin() && orderetailBean != null && mMoney > 0) {
				loading.setVisibility(View.VISIBLE);
				cancelOrder(mApplication.getLOGIN_KEY(),orderetailBean.getOrderBean().getOrder_number(), orderetailBean
						.getOrderDetailCountBean().getOrder_detail_number());
			} else {
				AppManager.getAppManager().finishActivity();
			}
			break;

		case R.id.phone:
			CallPhoneActivity.startActivity(getContext());
			break;

		case R.id.ok:
			AppManager.getAppManager().finishActivity();
			break;

		case R.id.delete:
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}

	private void cancelOrder(String loginKey, String orderNumber,String detailNumber) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.ORDERNUMBER, orderNumber);
		maps.put(DataTag.ORDERDETAILNUMBER, detailNumber);
		maps.put(DataTag.MONEY, mMoney);
		HttpTool.post(getContext(),Apis.SUBMITREIMBURSEORDER,loading, maps, new StringHandler(
				loading) {

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					loading.setVisibility(View.GONE);
					ToastTool.toastMessage(getContext(), "订单取消成功");
					TabFragmentActivity.startActivity(getContext(), 2);
					AppManager.getAppManager().finishActivity();
				}
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				CannelOrderBean bean = JSON.decode(responseBody,
						CannelOrderBean.class);
				if (bean != null) {
					if (bean.getResult() == 1) {
						ToastTool.toastMessage(getContext(),
								showFailMessage(bean.getFailue_reason_type()));
					} else {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				}
			}

			private String showFailMessage(int failue_reason_type) {
				switch (failue_reason_type) {
				case 0:
					return "系统异常";
				case -1:

					return "退款期限超时";
				case -3:
					return "子订单状态不符合退款条件";
				case 2:

					return "订单不存在";
				case 3:

					return "子订单不存在";
				case 4:

					return "退款金额不一致";
				}
				return "";
			}
		});
	}

}
