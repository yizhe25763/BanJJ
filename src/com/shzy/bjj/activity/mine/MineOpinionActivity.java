package com.shzy.bjj.activity.mine;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

/**
 * 
 * 
 * @brief
 * @author Fanhao Yi
 * @data 2015年8月17日下午4:01:49
 * @version V1.0
 */
public class MineOpinionActivity extends BaseActivity {

	private EditText mContentEdt;
	private String mContent;
	/**
	 * 备注文本框字数显示
	 */
	private TextView mContentSize;

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, MineOpinionActivity.class));
	}

	@Override
	public int bindLayout() {
		return R.layout.mine_opinion;
	}

	@Override
	public void initView(View view) {
		mContentEdt = $(R.id.memo);
		mContentSize = $(R.id.order_num_text);

	}

	@Override
	public void initData(Context mContext) {
		action_title.setText("意见反馈");
		action_right.setText("提交");
	}

	@Override
	public void initListener() {
		/**
		 * 备注文本框
		 */
		mContentEdt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mContentSize.setText(300 - s.length() + "字");
			}
		});
		action_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mContent = mContentEdt.getText().toString().trim();
				if (StringTool.isNoBlankAndNoNull(mContent)) {
					loading.setVisibility(View.VISIBLE);
					submitMemo(mContent);
				}else{
					ToastTool.toastMessage(getContext(), "请输入反馈信息");
				}

			}
		});
		action_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AppManager.getAppManager().finishActivity();

			}
		});
	}

	private void submitMemo(String memo) {
		Map maps = new HashMap<String, String>();
		maps.put("content", memo);
		HttpTool.post(getContext(), Apis.FEEDBACK, loading,maps, new StringHandler(
				loading) {

			@Override
			public void success(String response) {
				ToastTool.toastMessage(getContext(), "感谢您的支持，我们会努力做的更好！");
				loading.setVisibility(View.GONE);
				AppManager.getAppManager().finishActivity();
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});
	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

}
