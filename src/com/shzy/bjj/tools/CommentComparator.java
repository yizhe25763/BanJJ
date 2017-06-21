package com.shzy.bjj.tools;

import java.util.Comparator;

import com.shzy.bjj.bean.IntegralBean;

public class CommentComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		IntegralBean p1 = (IntegralBean) o1; // 强制转换
		IntegralBean p2 = (IntegralBean) o2;

		return p2.getTime().compareTo(p1.getTime());
	}
}