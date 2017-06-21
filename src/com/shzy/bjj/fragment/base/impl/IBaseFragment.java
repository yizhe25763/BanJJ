package com.shzy.bjj.fragment.base.impl;

import android.content.Context;
import android.view.View;

/**
 * 
 * @brief Fragment接口
 * @author Fanhao.Yi
 * @date 2015年4月29日上午10:52:32
 * @version V1.0
 */
public interface IBaseFragment {

	/**
	 * 绑定渲染视图的布局文件
	 * 
	 * @return 布局文件资源id
	 */
	public int bindLayout();

	/**
	 * 初始化控件
	 */
	public void initView(final View view);

	/**
	 * 业务处理操作（onCreateView方法中调用）
	 * 
	 * @param mContext
	 *            当前Activity对象
	 */
	public void initData(Context mContext);

	/**
	 * 初始化控件点击
	 */
	public void initListener();
}
