package com.shzy.bjj.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

/**
 * 
 * @brief 自定义验证码发送按钮
 * @author Fanhao Yi
 * @data 2015年6月9日下午5:37:23
 * @version V1.0
 */
public class TimeButton extends Button implements OnClickListener {
	private long lenght = 60 * 1000;
	private String textafter = "秒后重新获取~";
	private String textbefore = "点击获取验证码~";
	private final String TIME = "time";
	private final String CTIME = "ctime";
	private OnClickListener mOnclickListener;
	private Timer t;
	private TimerTask tt;
	private long time;
	Map<String, Long> map = new HashMap<String, Long>();
	private Boolean mIsSend;
	private Context context;
	private String mPhone = "";
	private int num = 0;

	public TimeButton(Context context) {
		super(context);
		setOnClickListener(this);
		this.context = context;

	}

	public TimeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	Handler han = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			TimeButton.this.setText(time / 1000 + textafter);
			time -= 1000;
			if (time < 0) {
				TimeButton.this.setEnabled(true);
				TimeButton.this
						.setBackgroundResource(R.drawable.send_message_down);
				TimeButton.this.setText(textbefore);
				clearTimer();
			}
		};
	};

	private void initTimer() {
		time = lenght;
		t = new Timer();
		tt = new TimerTask() {

			@Override
			public void run() {
				han.sendEmptyMessage(0x01);
			}
		};
	}

	private void clearTimer() {
		if (tt != null) {
			tt.cancel();
			tt = null;
		}
		if (t != null)
			t.cancel();
		t = null;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		if (l instanceof TimeButton) {
			super.setOnClickListener(l);
		} else
			this.mOnclickListener = l;
	}

	/**
	 * 发送验证码
	 * 
	 * @param mPhone
	 *            待发送手机号
	 */
	public void sendMessage(final Context context, String mPhone) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.PHONE, mPhone);
		HttpTool.post(context, Apis.SENDCAPTCHA, maps, new StringHandler(null) {

			@Override
			public void success(String response) {
				initTimer();
				setText(time / 1000 + textafter);
				setEnabled(false);
				TimeButton.this
						.setBackgroundResource(R.drawable.send_message_up);
				t.schedule(tt, 0, 1000);
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				MyApplication.ShowFailMessage(context, responseBody);
			}
		});
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (mOnclickListener != null) {
			mOnclickListener.onClick(v);
		}
		if (num == 2) {
			if(HttpTool.checkNetwork(context)){
				sendMessage(context, mPhone);
			}
		} else {
			if (num == 0) {
				ToastTool.toastMessage(context, "手机号码不能为空");
			} else if (num == 1) {
				ToastTool.toastMessage(context, "抱歉，您输入的手机号码不正确，请检查");
			}
		}
	}

	public void onDestroy() {
		if (DataConst.map == null)
			DataConst.map = new HashMap<String, Long>();
		DataConst.map.put(TIME, time);
		DataConst.map.put(CTIME, System.currentTimeMillis());
		clearTimer();
	}

	@SuppressLint("NewApi")
	public void onCreate(Bundle bundle) {
		if (DataConst.map == null)
			return;
		if (DataConst.map.size() <= 0)
			return;
		long time = System.currentTimeMillis() - DataConst.map.get(CTIME)
				- DataConst.map.get(TIME);
		DataConst.map.clear();
		if (time > 0)
			return;
		else {
			initTimer();
			this.time = Math.abs(time);
			t.schedule(tt, 0, 1000);
			this.setText(time + textafter);
			this.setEnabled(false);
		}
	}

	public TimeButton setTextAfter(String text1) {
		this.textafter = text1;
		return this;
	}

	public TimeButton setTextBefore(String text0) {
		this.textbefore = text0;
		this.setText(textbefore);
		return this;
	}

	public TimeButton setLenght(long lenght) {
		this.lenght = lenght;
		return this;
	}

	public TimeButton setIsSend(Boolean isSend) {
		this.mIsSend = isSend;
		return this;
	}

	public TimeButton setMessage(Context context, String phone, int num) {
		this.num = num;
		this.mPhone = phone;
		this.context = context;
		return this;
	}

}