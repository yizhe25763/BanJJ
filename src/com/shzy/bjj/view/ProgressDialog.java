package com.shzy.bjj.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.shzy.bjj.tools.ResourceTool;

/**
 * 
 * @brief 自定义对话框
 * @author Fanhao.Yi
 * @date 2015年4月24日上午11:02:54
 * @version V1.0
 */
public class ProgressDialog extends Dialog {

	/** 消息TextView */
	private TextView tvMsg;

	public ProgressDialog(Context context, String strMessage) {
		this(context, ResourceTool.getStyleId("CustomProgressDialog"),
				strMessage);
	}

	public ProgressDialog(Context context, int theme, String strMessage) {
		super(context, theme);
		this.setContentView(ResourceTool.getLayoutId("view_progress_dialog"));
		this.getWindow().getAttributes().gravity = Gravity.CENTER;
		tvMsg = (TextView) this.findViewById(ResourceTool.getIdId("tv_msg"));
		setMessage(strMessage);
	}

	/**
	 * 设置进度条消息
	 * 
	 * @param strMessage
	 *            消息文本
	 */
	public void setMessage(String strMessage) {
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
	}
}