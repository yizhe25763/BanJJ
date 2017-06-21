package com.shzy.bjj.listener;
/**
 * 
 * @brief 主要用来实时ScrollView的onScrollChanged方法中的值
 * @author Fanhao.Yi
 * @date 2015年6月4日下午3:46:39
 * @version V1.0
 */
public abstract interface OnScrollChangedListener {
	public abstract void onScrollChanged(int top, int oldTop);
}
