package com.shzy.bjj.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.shzy.bjj.R;

public class DatePickerDialog extends AlertDialog implements
		DialogInterface.OnClickListener, DatePicker.OnDateChangedListener {

	private final DatePicker mDatePicker;
	private final OnDateSetListener mCallBack;

	/**
	 * @param context
	 *            The context the dialog is to run in.
	 * @param callBack
	 *            How the parent is notified that the date is set.
	 * @param year
	 *            The initial year of the dialog.
	 * @param monthOfYear
	 *            The initial month of the dialog.
	 * @param dayOfMonth
	 *            The initial day of the dialog.
	 */
	public DatePickerDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		super(context, 0);

		mCallBack = callBack;

		Context themeContext = getContext();
		setButton(BUTTON_POSITIVE,
				themeContext.getText(R.string.date_time_done), this);
		setIcon(0);
		setTitle(R.string.date_picker_dialog_title);

		LayoutInflater inflater = (LayoutInflater) themeContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.date_picker_dialog, null);
		setView(view);
		mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
		// resizePikcer(mDatePicker);
		mDatePicker.init(year, monthOfYear, dayOfMonth, this);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == BUTTON_POSITIVE && mCallBack != null) {
			mDatePicker.clearFocus();
			mCallBack.onDateSet(mDatePicker, mDatePicker.getYear(),
					mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
		}
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mDatePicker.init(year, monthOfYear, dayOfMonth, this);
	}

	public void setMaxDate(Date date) {
		if (date != null) {
			mDatePicker.setMaxDate(date.getTime());
		}
	}

	public void setMinDate(Date date) {
		if (date != null) {
			mDatePicker.setMinDate(date.getTime());
		}
	}

	/**
	 * 调整FrameLayout大小
	 * 
	 * @param tp
	 */
	private void resizePikcer(FrameLayout tp) {
		List<NumberPicker> npList = findNumberPicker(tp);
		for (NumberPicker np : npList) {
			resizeNumberPicker(np);
		}
	}

	/**
	 * 得到viewGroup里面的numberpicker组件
	 * 
	 * @param viewGroup
	 * @return
	 */
	private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
		List<NumberPicker> npList = new ArrayList<NumberPicker>();
		View child = null;
		if (null != viewGroup) {
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				child = viewGroup.getChildAt(i);
				if (child instanceof NumberPicker) {
					npList.add((NumberPicker) child);
				} else if (child instanceof LinearLayout) {
					List<NumberPicker> result = findNumberPicker((ViewGroup) child);
					if (result.size() > 0) {
						return result;
					}
				}
			}
		}
		return npList;
	}

	/*
	 * 调整numberpicker大小
	 */
	private void resizeNumberPicker(NumberPicker np) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1);
		params.setMargins(5, 0, 5, 0);
		np.setLayoutParams(params);
	}

	/**
	 * The callback used to indicate the user is done filling in the date.
	 */
	public interface OnDateSetListener {

		/**
		 * @param view
		 *            The view associated with this listener.
		 * @param year
		 *            The year that was set.
		 * @param monthOfYear
		 *            The month that was set (0-11) for compatibility with
		 *            {@link java.util.Calendar}.
		 * @param dayOfMonth
		 *            The day of the month that was set.
		 */
		void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth);
	}
}
