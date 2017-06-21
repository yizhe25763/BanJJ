package com.shzy.bjj.activity.login;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.DateUtil;
import com.shzy.bjj.tools.DateUtils;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.PreferencesTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.view.DatePickerDialog;

/**
 * 
 * @brief 完善信息
 * @author Fanhao Yi
 * @data 2015年6月10日上午11:34:46
 * @version V1.0
 */
public class PerfectInformationActivity extends BaseActivity {
	private Calendar babyCalendar = Calendar.getInstance();
	/**
	 * 用户姓名
	 */
	private String mUserNick;
	/**
	 * 身份证
	 */
	private String mIDCard;
	/**
	 * 宝宝姓名
	 */
	private String mBabyName;
	/**
	 * 宝宝生日
	 */
	private String mBrithday;
	/**
	 * 宝宝性别
	 */
	private int mSexID;
	/**
	 * 宝宝身份证
	 */
	private String mBabyIDCard;
	/**
	 * 宝宝生日控件
	 */
	private EditText birthdayEdt;
	/**
	 * 公主
	 */
	private ImageButton mFamale;
	/**
	 * 王子
	 */
	private ImageButton mMale;
	/**
	 * 用户名
	 */
	private EditText vUserNickEdt;

	/**
	 * 身份证
	 */
	private EditText vIDCardEdt;

	/**
	 * 宝宝姓名
	 */
	private EditText vBabyNameEdt;

	/**
	 * 宝宝身份证
	 */
	private EditText vBabyIDCardEdt;

	@Override
	public int bindLayout() {
		return R.layout.activity_perfectinfomation;
	}

	@Override
	public void initView(View view) {
		birthdayEdt = $(R.id.baby_birthday);
		mFamale = $(R.id.famale_btn);
		mMale = $(R.id.male_btn);
		vUserNickEdt = $(R.id.your_name);
		vIDCardEdt = $(R.id.id_card);
		vBabyNameEdt = $(R.id.baby_name);
		vBabyIDCardEdt = $(R.id.baby_id_card);

	}

	@Override
	public void initData(Context mContext) {
		action_back.setVisibility(View.GONE);
		// 初始化标题
		action_title.setText(R.string.perfect_information_title);
		// 设置右侧按钮文字
		action_right.setText(R.string.perfect_information_next);
	}

	@SuppressLint("NewApi")
	@Override
	public void initListener() {
		mMale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mSexID = 0;
				mMale.setBackgroundResource(
						R.drawable.choose_address_down);
				mFamale.setBackgroundResource(
						R.drawable.choose_address_up);

			}
		});
		mFamale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mSexID = 1;
				mFamale.setBackgroundResource(
						R.drawable.choose_address_down);
				mMale.setBackgroundResource(
						R.drawable.choose_address_up);
			}
		});
		/**
		 * 选择生日
		 */
		birthdayEdt.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				String birth = birthdayEdt.getText().toString().trim();
				setBabyBirthday(birth);
				return false;
			}
		});
		action_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mUserNick = vUserNickEdt.getText().toString().trim();
				mIDCard = vIDCardEdt.getText().toString().trim();
				mBabyName = vBabyNameEdt.getText().toString().trim();
				mBabyIDCard = vBabyIDCardEdt.getText().toString().trim();
				if (StringTool.isNoBlankAndNoNull(mUserNick)
						&& StringTool.isNoBlankAndNoNull(mIDCard)
						&& StringTool.isNoBlankAndNoNull(mBabyName)
						&& StringTool.isNoBlankAndNoNull(mBabyIDCard)
						&& StringTool.isNoBlankAndNoNull(mBrithday)) {
					createFirstInfo(mUserNick, mIDCard, mBabyName, mBrithday,
							mSexID, mBabyIDCard);
				} else {
					Toast.makeText(getContext(), "请完善信息", Toast.LENGTH_LONG)
							.show();
				}

			}

		});
	}

	/**
	 * 选取宝宝生日
	 * 
	 * @param currentBabyBirthday
	 */
	private void setBabyBirthday(String currentBabyBirthday) {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(MineBabyActivity.this.getWindowToken(),
		// 0);
		final Calendar calendar = Calendar.getInstance();
		if (StringTool.isNoBlankAndNoNull(currentBabyBirthday)) {
			calendar.setTime(DateUtil
					.getDate(currentBabyBirthday, "yyyy-MM-dd"));
		}
		DatePickerDialog pickerDialog = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						babyCalendar.set(year, monthOfYear, dayOfMonth);
						birthdayEdt.setText(DateUtils.formatYMD(new Date(
								babyCalendar.getTimeInMillis())));
					}
				}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		pickerDialog.setTitle("设置宝宝生日");
		pickerDialog.setMaxDate(Calendar.getInstance().getTime());
		pickerDialog.show();
	}

	/**
	 * 完善用户资料
	 * 
	 * @param mUserNick
	 *            用户名称
	 * @param mIDCard
	 *            身份证号
	 * @param mBabyName
	 *            宝宝名字
	 * @param mBrithday
	 *            宝宝生日
	 * @param mSexID
	 *            宝宝性别
	 * @param mBabyIDCard
	 *            宝宝身份证号
	 */
	private void createFirstInfo(String mUserNick, String mIDCard,
			String mBabyName, String mBrithday, int mSexID, String mBabyIDCard) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.NAME, mUserNick);
		maps.put(DataTag.IDENTITY, mIDCard);
		maps.put(DataTag.BABYNAME, mBabyName);
		maps.put(DataTag.BABYBIRTHDAY, mBrithday);
		maps.put(DataTag.BABYSEX, mSexID);
		maps.put(DataTag.BABYIDENTITY, mBabyIDCard);
		maps.put(DataTag.LOGINKEY,
				PreferencesTool.getString(getContext(), DataTag.LOGINKEY));
		HttpTool.post(getContext(),Apis.CREATEFIRSTINFO, loading,maps, new StringHandler(loading) {

			@Override
			public void success(String response) {
				DataConst.isFirstRegister = true;
				mApplication.setLOGIN_KEY(PreferencesTool.getString(
						getContext(), DataTag.LOGINKEY));
				mApplication.setLogin(true);
				AppManager.getAppManager().finishActivity();
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				PreferencesTool.putBoolean(getContext(),
						DataTag.IS_FIRST_REGISTER, false);
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});

	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
