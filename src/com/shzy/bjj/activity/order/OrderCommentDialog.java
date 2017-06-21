package com.shzy.bjj.activity.order;

import java.util.HashMap;
import java.util.Map;

import net.arnx.jsonic.JSON;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.bean.CannelOrderBean;
import com.shzy.bjj.bean.OrderBean;
import com.shzy.bjj.bean.TaskDayOrderBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

public class OrderCommentDialog extends BaseActivity implements OnClickListener {
	private TextView cancelBt;
	private TextView okBt;
	private TextView messageTextView;
	private TextView titleTextView;
	private LinearLayout layout;
	private TaskDayOrderBean taskDayOrderBean;

	public static void startActivity(Context context, TaskDayOrderBean bean) {
		context.startActivity(new Intent(context, OrderCommentDialog.class)
				.putExtra("bean", bean));
	}

	@Override
	public int bindLayout() {
		return R.layout.order_comment_dialog;
	}

	@Override
	public void initView(View view) {
		taskDayOrderBean = (TaskDayOrderBean) getIntent().getSerializableExtra(
				"bean");
		if (taskDayOrderBean == null) {
			AppManager.getAppManager().finishActivity();
		}
		cancelBt = $(R.id.cancel);
		okBt = $(R.id.ok);
	}

	@Override
	public void initData(Context mContext) {
	}

	@Override
	public void initListener() {
		cancelBt.setOnClickListener(this);
		okBt.setOnClickListener(this);
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
			AppManager.getAppManager().finishActivity();
			break;

		case R.id.ok:
			EvaluateActivity.startActivity(getContext(), taskDayOrderBean);
			AppManager.getAppManager().finishActivity();
			break;

		default:
			break;
		}
	}

}
