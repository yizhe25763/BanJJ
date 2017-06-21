package com.shzy.bjj.activity.login;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.arnx.jsonic.JSON;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.WebViewActivity;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.activity.home.AppointmentAddressAddActivity;
import com.shzy.bjj.activity.home.ChooseAddressActivity;
import com.shzy.bjj.activity.home.ChooseBabyActivity;
import com.shzy.bjj.activity.mine.MineBabyActivity;
import com.shzy.bjj.bean.AppointmentResponse;
import com.shzy.bjj.bean.BabyBean;
import com.shzy.bjj.bean.InfoBean;
import com.shzy.bjj.bean.InfoResponse;
import com.shzy.bjj.bean.UserBean;
import com.shzy.bjj.bean.loginByCaptchaBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.ClassTag;
import com.shzy.bjj.constant.DataConst;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.PreferencesTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.tools.ValidTool;
import com.shzy.bjj.view.MainBottomTabLayout;
import com.shzy.bjj.view.TimeButton;

/**
 * 
 * @brief 登录界面
 * @author Fanhao Yi
 * @data 2015年6月8日上午9:57:58
 * @version V1.0
 */
public class LoginActivity extends BaseActivity {

	/**
	 * Phone
	 */
	private String mPhone;
	/**
	 * 发送验证码按钮
	 */
	private TimeButton sendBtn;

	/**
	 * 验证码发送前文字信息
	 */
	private String sendBefore = "获取验证";
	/**
	 * 验证码发送后文字信息
	 */
	private String sendAfter = "秒后重新获取";

	/**
	 * 验证码
	 */
	private String authorCode;
	/**
	 * 电话号码
	 */
	private EditText mPhoneEdt;
	/**
	 * 验证码
	 */
	private EditText mCradEdt;

	/**
	 * 1.注册流程 2.我的流程
	 */
	private String mFromID;

	private TextView loginService;

	public static String URLS = "";
	private String urls;
	private String channel;

	private BroadcastReceiver smsReceiver;
	private IntentFilter filter2;
	private Handler handler;
	private EditText et;
	private String strContent;
	private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";

	public static void startActivity(Context context) {
		context.startActivity(new Intent(context, LoginActivity.class)
				.putExtra(DataTag.IS_FROM, DataConst.MINEID));
	}

	public static void startActivity(Context context, String formID, String urls) {
		context.startActivity(new Intent(context, LoginActivity.class)
				.putExtra(DataTag.IS_FROM, formID).putExtra("urlss", urls));
	}

	@Override
	public int bindLayout() {
		return R.layout.activity_login;
	}

	@Override
	public void initView(View view) {
		sendBtn = $(R.id.send_btn);
		mPhoneEdt = $(R.id.phone_edt);
		mCradEdt = $(R.id.auth_code_edt);
		loginService = $(R.id.login_services);

	}

	@Override
	public void initData(Context mContext) {
		Bundle bundle = MyApplication.getMetaData(getContext());
		channel = String.valueOf(bundle.get("UMENG_CHANNEL"));
		if (!StringTool.isNoBlankAndNoNull(channel)) {
			channel = "";
		} else {
		}
		urls = getIntent().getStringExtra("urlss");
		mFromID = getIntent().getStringExtra(DataTag.IS_FROM);

		// 隐藏右侧按钮
		action_right.setVisibility(View.GONE);
		// 初始化标题
		action_title.setText(R.string.login_title_content);
		// 初始化验证码发送按钮发送前以及发送中文字显示以及时间周期
		sendBtn.setTextAfter(sendAfter).setTextBefore(sendBefore);
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				((EditText) $(R.id.auth_code_edt)).setText(strContent);
			};
		};
		filter2 = new IntentFilter();
		filter2.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter2.setPriority(Integer.MAX_VALUE);
		smsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Object[] objs = (Object[]) intent.getExtras().get("pdus");
				for (Object obj : objs) {
					byte[] pdu = (byte[]) obj;
					SmsMessage sms = SmsMessage.createFromPdu(pdu);
					// 短信的内容
					String message = sms.getMessageBody();
					String from = sms.getOriginatingAddress();
					if (!TextUtils.isEmpty(from)) {
						String code = patternCode(message);
						if (!TextUtils.isEmpty(code)) {
							strContent = code;
							handler.sendEmptyMessage(1);
						}
					}
				}
			}
		};
		registerReceiver(smsReceiver, filter2);

	}

	@SuppressLint("NewApi")
	@Override
	public void initListener() {
		loginService.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				WebViewActivity.startActivity(getContext(), DataConst.URLS, 0);

			}
		});

		mCradEdt.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				mCradEdt.setBackgroundResource(R.drawable.edit_down);
				mPhoneEdt.setBackgroundResource(R.drawable.edit_up);
				return false;
			}
		});
		mPhoneEdt.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				mPhoneEdt.setBackgroundResource(R.drawable.edit_down);
				mCradEdt.setBackgroundResource(R.drawable.edit_up);

				return false;
			}
		});
		action_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AppManager.getAppManager().finishActivity();
			}
		});
		// 发送验证码
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mPhone = ((EditText) $(R.id.phone_edt)).getText().toString()
						.trim();
				if (StringTool.isNoBlankAndNoNull(mPhone)
						&& mPhone.length() == DataConst.phoneLength) {// 手机号码长度不足11位，弹出Toast提示

					sendBtn.setMessage(getContext(), mPhone,2);
					sendBtn.setIsSend(true);
				} else {

					if (mPhone.length() == 0) {
						Toast.makeText(getContext(), "手机号码不能为空",
								Toast.LENGTH_LONG).show();
						sendBtn.setMessage(getContext(), "",0);

					} else {
						Toast.makeText(getContext(), R.string.phone_lack,
								Toast.LENGTH_LONG).show();
						sendBtn.setMessage(getContext(), "",1);

					}
					sendBtn.setIsSend(false);

				}
			}

		});
		// 登录
		$(R.id.login_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mPhone = ((EditText) $(R.id.phone_edt)).getText().toString()
						.trim();
				authorCode = ((EditText) $(R.id.auth_code_edt)).getText()
						.toString().trim();
				if (mPhone.length() == 0) {
					ToastTool.toastMessage(getContext(), "手机号码不能为空");
					return;
				}
				if (!ValidTool.isPhoneNumberValid(mPhone)) {
					ToastTool.toastMessage(getContext(), R.string.phone_lack);
					return;
				}
				if (authorCode.length() == 0) {
					ToastTool.toastMessage(getContext(), "验证码不能为空");
					return;
				}
				if (authorCode.length() != DataConst.authorCodeLength) {
					ToastTool.toastMessage(getContext(),
							R.string.author_code_lack);
					return;
				}

				if (StringTool.isNoBlankAndNoNull(mPhone)
						&& StringTool.isNoBlankAndNoNull(authorCode)
						&& authorCode.length() == DataConst.authorCodeLength) {// 验证码长度不足6位，弹出Toast提示
					loading.setVisibility(View.VISIBLE);
					// 手机验证码登录
					loginByCaptcha(mPhone, authorCode);
				}
			}
		});

	}

	HashSet h = new HashSet();

	/**
	 * 手机验证码登录
	 * 
	 * @param mPhone
	 *            手机号码
	 * @param authorCode
	 *            验证码
	 */
	private void loginByCaptcha(String mPhone, String authorCode) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.PHONE, mPhone);
		maps.put(DataTag.CAPTCHA, authorCode);
		maps.put(DataTag.PLATFORM, "android");
		maps.put(DataTag.CHANNEL, channel);

		HttpTool.post(getContext(), Apis.LOGINBYCAPTCHA, maps, loading,
				new StringHandler(loading) {
					@Override
					public void success(String response) {
						String login_key = JSON.decode(response,
								loginByCaptchaBean.class).getLogin_key();
						Long user_id = JSON.decode(response,
								loginByCaptchaBean.class).getUser_id();
						PreferencesTool.putString(getContext(),
								DataTag.LOGINKEY, login_key);
						PreferencesTool.putLong(getContext(), DataTag.USERID,
								user_id);
						// getUserInfo(login_key);
						if (mFromID.equals(DataConst.ADDRESSID)) {
							loginSuccess(login_key, user_id);
							getUserAddress(login_key);
						} else if (mFromID.equals(DataConst.BABYID)) {
							loginSuccess(login_key, user_id);
							getUserBaby(login_key);
						} else if (mFromID.equals(DataConst.MINEID)) {
							AppManager.getAppManager().finishActivity();
						} else if (mFromID.equals("WebViewActivity")) {
							AppManager.getAppManager().finishActivity();
							AppManager.getAppManager().finishActivity(
									WebViewActivity.class);
							WebViewActivity
									.startActivity(getContext(), URLS, 5);
						}
						h.add(DataConst.tags);
						JPushInterface.setAliasAndTags(
								getContext(),
								DataConst.tags
										+ "_"
										+ PreferencesTool.getLong(getContext(),
												DataTag.USERID), h,
								new TagAliasCallback() {

									@Override
									public void gotResult(int arg0,
											String arg1, Set<String> arg2) {
										if (arg0 == 0) {
											pushBind(
													mApplication.getLOGIN_KEY(),
													JPushInterface
															.getRegistrationID(getContext()),
													DataConst.tags,
													DataConst.tags
															+ "_"
															+ PreferencesTool
																	.getLong(
																			getContext(),
																			DataTag.USERID));
										}
									}
								});

						loading.setVisibility(View.GONE);
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
						loading.setVisibility(View.GONE);
					}
				});

	}

	private void loginSuccess(String login_key, Long user_id) {
		mApplication.setLOGIN_KEY(login_key);
		mApplication.setUSER_ID(user_id);
		mApplication.setLogin(true);
	}

	/**
	 * 用户推送绑定
	 * 
	 * @param login_KEY
	 *            身份识别key
	 * @param registrationId2
	 *            推送注册id
	 * @param tags
	 *            标签组 dev（开发环境） ,prod （正式环境）
	 * @param alias2
	 *            别名 开发环境+下划线+用户id
	 */
	private void pushBind(String login_KEY, String registrationId2,
			String tags, String alias2) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, login_KEY);
		maps.put(DataTag.REGISTRATIONID, registrationId2);
		maps.put(DataTag.TAGS, tags);
		maps.put(DataTag.ALIAS, alias2);
		HttpTool.post(getContext(), Apis.PUSHBIND, loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}

	private void getInformationData(String loginKey, int page, int size,
			String ids) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put(DataTag.PAGE, page);
		maps.put(DataTag.SIZE, size);
		maps.put(DataTag.IDS, ids);
		HttpTool.post(getContext(), Apis.GET_USER_INFO, loading, maps,
				new StringHandler(loading) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							InfoResponse infoResponse = JSON.decode(response,
									InfoResponse.class);
							int count = infoResponse.getCount();
							if (count > 0) {
								List<InfoBean> list = infoResponse.getList();
								MainBottomTabLayout.hasMineMessage = true;
								((MainBottomTabLayout) $(R.id.main_bottom_tablayout))
										.setViewPager(((ViewPager) $(R.id.tab_pager)));
							}
						}
						loading.setVisibility(View.GONE);
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}

	/***
	 * 
	 * @param loginKey
	 */
	private void getUserInfo(final String loginKey) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		HttpTool.post(getContext(), Apis.GET_USER, loading, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							UserBean bean = JSON.decode(response,
									UserBean.class);
							if (StringTool.isNoBlankAndNoNull(bean
									.getIdentity())) {
								// loginSuccess(loginKey);
								// AppManager.getAppManager().finishActivity();
							} else {
								getIntentTool().forward(
										ClassTag.PERFECTINFORMATIONACTIVITY);
							}
						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
					}
				});
	}

	/**
	 * 获取宝宝信息
	 * 
	 * @param login_key
	 */
	private void getInfo(String login_key) {

		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, login_key);
		HttpTool.post(getContext(), Apis.GET_USER, loading, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							UserBean bean = JSON.decode(response,
									UserBean.class);
							if (bean != null) {
								List<BabyBean> list = bean.getBaby_list();
								if (list != null && list.size() > 0) {
									Intent intent = new Intent();
									intent.setClass(getContext(),
											ChooseBabyActivity.class);
									startActivityForResult(intent,
											DataConst.REQUESTCODEOK);
								} else {
									MineBabyActivity.startActivity(
											getContext(), true, null);
									AppManager.getAppManager().finishActivity();
								}
							}
						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}

	/**
	 * 获取预约人信息列表
	 * 
	 * @param mLoginKey
	 */
	private void getContactInfo(String mLoginKey) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, mLoginKey);
		HttpTool.post(getContext(), Apis.GET_USER_APPOINTMENT, loading, maps,
				new StringHandler(null) {
					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							AppointmentResponse bean = JSON.decode(response,
									AppointmentResponse.class);
							int num = bean.getCount();
							// if (num > 0) {
							Intent intent = new Intent();
							intent.setClass(getContext(),
									ChooseAddressActivity.class);
							startActivityForResult(intent,
									DataConst.REQUESTCODEOK);
							// } else {
							// getIntentTool().forward(
							// MineAppointmentAddActivity.class);
							// AppManager.getAppManager().finishActivity();
							//
							// }

						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}

	/**
	 * 获取宝宝信息
	 * 
	 * @param login_KEY
	 */
	private void getUserBaby(String login_KEY) {

		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, login_KEY);
		HttpTool.post(getContext(), Apis.GET_USER, loading, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							UserBean userBean = JSON.decode(response,
									UserBean.class);
							int babyNum = userBean.getBaby_count();
							if (babyNum > 0) {
								getIntentTool().forward(
										ChooseBabyActivity.class);
								AppManager.getAppManager().finishActivity();

							} else {
								MineBabyActivity.startActivity(getContext(),
										true, null);
								AppManager.getAppManager().finishActivity();

							}

						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}

	/**
	 * 获取用户地址信息
	 * 
	 * @param login_KEY
	 */
	private void getUserAddress(String login_KEY) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, login_KEY);
		HttpTool.post(getContext(), Apis.GET_USER_APPOINTMENT, loading, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							AppointmentResponse appointmentResponse = JSON
									.decode(response, AppointmentResponse.class);
							int addressNum = appointmentResponse.getCount();
							if (addressNum > 0) {
								getIntentTool().forward(
										ChooseAddressActivity.class);
								AppManager.getAppManager().finishActivity();

							} else {
								AppointmentAddressAddActivity.startActivity(
										getContext(), true, null);
								AppManager.getAppManager().finishActivity();

							}
						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			switch (resultCode) {
			case 0:
				setResult(3, data);
				AppManager.getAppManager().finishActivity();
				break;
			case 1:
				setResult(4, data);
				AppManager.getAppManager().finishActivity();
				break;

			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		sendBtn.onDestroy();
		unregisterReceiver(smsReceiver);

	}

	/**
	 * 匹配短信中间的6个数字（验证码等）
	 * 
	 * @param patternContent
	 * @return
	 */
	private String patternCode(String patternContent) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		}
		Pattern p = Pattern.compile(patternCoder);
		Matcher matcher = p.matcher(patternContent);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

}
