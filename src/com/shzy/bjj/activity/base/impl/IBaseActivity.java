package com.shzy.bjj.activity.base.impl;

import android.content.Context;
import android.view.View;

/**
 * 
 * @brief Activity接口
 * @author Fanhao.Yi
 * @date 2015年4月27日上午11:23:44
 * @version V1.0
 */
public interface IBaseActivity {

	/**
	 * 绑定渲染视图的布局文件 （onCreate方法中调用）
	 * 
	 * @return 布局文件资源id
	 */
	 public int bindLayout();

	/**
	 * 初始化控件 （onCreate方法中调用）
	 */
	public void initView(final View view);

	/**
	 * 初始化数据（onCreate方法中调用）
	 * 
	 * @param mContext
	 *            当前Activity对象
	 */
	public void initData(Context mContext);

	/**
	 * 初始化点击事件
	 */
	public void initListener();

	/**
	 * 暂停恢复刷新相关操作（onResume方法中调用）
	 */
	public void resume();

	/**
	 * 销毁、释放资源相关操作（onDestroy方法中调用）
	 */
	public void destroy();

}
