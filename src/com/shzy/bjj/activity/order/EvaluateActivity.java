package com.shzy.bjj.activity.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.arnx.jsonic.JSON;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.bean.ImageBean;
import com.shzy.bjj.bean.PicBean;
import com.shzy.bjj.bean.TaskDayOrderBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.ButtonTool;
import com.shzy.bjj.tools.DialogUtils;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.tools.image.AlbumActivity;
import com.shzy.bjj.tools.image.Bimp;
import com.shzy.bjj.tools.image.FileUtils;
import com.shzy.bjj.tools.image.GalleryActivity;
import com.shzy.bjj.tools.image.ImageItem;
import com.shzy.bjj.tools.image.PublicWay;
import com.shzy.bjj.tools.image.Res;

/**
 * 
 * 
 * @brief 评价界面
 * @author Fanhao Yi
 * @data 2015年7月31日上午9:57:25
 * @version V1.0
 */
public class EvaluateActivity extends BaseActivity {
	/**
	 * 综合评分 初始默认为5星
	 */
	private RatingBar vComprehensiveScore;
	private int mComprehensiveScore = 50;
	/**
	 * 服务 初始默认为5星
	 */
	private RatingBar vService;
	private int mService = 50;

	/**
	 * 专业 初始默认为5星
	 */
	private RatingBar vProfessional;
	private int mProfessional = 50;

	/**
	 * 守时 初始默认为5星
	 */
	private RatingBar vPunctual;
	private int mPunctual = 50;

	/**
	 * 备注文字
	 */
	private EditText vNote;
	private String mNote;
	/**
	 * 身份识别 key
	 */
	private static String mLoginKey;
	/**
	 * 订单编号
	 */
	private static String mOrderNumber;
	/**
	 * 订单明细编号
	 */
	private static String mOrderDetailNumber;
	/**
	 * 老师 id
	 */
	private static int mTeacherID;
	/**
	 * 订单Bean
	 */
	private  static TaskDayOrderBean bean;
	/**
	 * 备注文本框字数显示
	 */
	private TextView mContentSize;
	public static String content;

	/**
	 * 选择照片
	 */
	private Button imagebtn;
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap=null;

	RelativeLayout parent;
	Button takeCameraBtn;
	Button takePicBtn;
	Button cannelBtn;
	private List<ImageBean> imageList = new ArrayList<ImageBean>();
	private String image_url;
	int num = 0;
	private int imageNum;

	public static void startActivity(Context context, TaskDayOrderBean bean) {
		context.startActivity(new Intent(context, EvaluateActivity.class)
				.putExtra("bean", bean));
	}

	@Override
	public int bindLayout() {
		return R.layout.activity_evaluate;
	}

	@Override
	public void initView(View view) {
		((TextView) $(R.id.config_hidden)).requestFocus();
		vComprehensiveScore = $(R.id.comprehensive_score);
		vService = $(R.id.service);
		vProfessional = $(R.id.professional);
		vPunctual = $(R.id.punctual);
		vNote = $(R.id.note);
		mContentSize = $(R.id.order_num_text);
		imagebtn = $(R.id.pic_btn);
		Res.init(getContext());
		parentView = getLayoutInflater().inflate(R.layout.activity_evaluate,
				null);
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_addpic_unfocused);
		PublicWay.activityList.add(this);
		pop = new PopupWindow(getContext());

		View views = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);
		ll_popup = (LinearLayout) views.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(views);

		parent = (RelativeLayout) views.findViewById(R.id.parent);
		takeCameraBtn = (Button) views
				.findViewById(R.id.item_popupwindows_camera);
		takeCameraBtn.getBackground().setAlpha(200);
		takePicBtn = (Button) views.findViewById(R.id.item_popupwindows_Photo);
		cannelBtn = (Button) views.findViewById(R.id.item_popupwindows_cancel);
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		imagebtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				ll_popup.startAnimation(AnimationUtils.loadAnimation(
						EvaluateActivity.this, R.anim.activity_translate_in));
				pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				return imm.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), 0);
			}
		});
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		takeCameraBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		takePicBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(EvaluateActivity.this,
						AlbumActivity.class);
				content = vNote.getText().toString().trim();
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in,
						R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();

			}
		});
		cannelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					HideKeyboard();
					ll_popup.startAnimation(AnimationUtils
							.loadAnimation(EvaluateActivity.this,
									R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(EvaluateActivity.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}

	/**
	 * 隐藏软键盘
	 */
	private void HideKeyboard() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager manager = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
				manager.hideSoftInputFromWindow(EvaluateActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 10);
	}

	@Override
	public void initData(Context mContext) {
		action_title.setText("评价");
		action_right.setText("发送");
		vNote.setText(content);
		bean = (TaskDayOrderBean) getIntent().getSerializableExtra("bean");
		if (bean != null) {
			mOrderNumber = bean.getOrderBean().getOrder_number();
			mOrderDetailNumber = bean.getOrderDetailCountBean()
					.getOrder_detail_number();
			mTeacherID = Integer.valueOf(bean.getOrderDetailCountBean()
					.getTeacher_id());
		}
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
//		adapter.update();
		noScrollgridview.setAdapter(adapter);
	}

	@Override
	public void initListener() {
		/**
		 * 备注文本框
		 */
		vNote.addTextChangedListener(new TextWatcher() {
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
				mContentSize.setText(200 - s.length() + "字");
			}
		});
		vService.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {

				mService = ((int) arg1) * 10;
			}
		});
		vProfessional
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar arg0, float arg1,
							boolean arg2) {
						mProfessional = ((int) arg1) * 10;
					}
				});
		vPunctual.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
				mPunctual = ((int) arg1) * 10;
			}
		});

		action_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loading.setVisibility(View.VISIBLE);
				if (ButtonTool.isFastClick()) {
					return;
				}
				ArrayList<ImageItem> tempSelectBitmaps = Bimp.tempSelectBitmap;
				ImageItem bean = null;
				imageNum = tempSelectBitmaps.size();
				if (imageNum > 0) {
					for (int i = 0; i < imageNum; i++) {
						bean = tempSelectBitmaps.get(i);
						String base64 = DialogUtils.imgToBase64(bean
								.getBitmap());
						if (StringTool.isNoBlankAndNoNull(base64)
								&& StringTool.isNoBlankAndNoNull(mLoginKey)
								&& StringTool.isNoBlankAndNoNull(mOrderNumber)
								&& StringTool
										.isNoBlankAndNoNull(mOrderDetailNumber)
								&& mTeacherID > 0) {
							uploadImage(mApplication.getLOGIN_KEY(), base64);
						}
					}
				} else {
					if (StringTool.isNoBlankAndNoNull(mLoginKey)
							&& StringTool.isNoBlankAndNoNull(mOrderNumber)
							&& StringTool
									.isNoBlankAndNoNull(mOrderDetailNumber)
							&& mTeacherID > 0) {
						if (StringTool.isNoBlankAndNoNull(vNote.getText()
								.toString().trim())) {
							mNote = vNote.getText().toString().trim();
						} else {
							mNote = "";
						}
						PicBean beans = new PicBean();
						beans.setMemo(mNote);
						getEvaluteTeacher(getOrderInfo(beans));
					} else {
						loading.setVisibility(View.GONE);
					}
				}
			}
		});
		action_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (ButtonTool.isFastClick()) {
					return;
				}
				for (int i = 0; i < PublicWay.activityList.size(); i++) {
					if (null != PublicWay.activityList.get(i)) {
						PublicWay.activityList.get(i).finish();
						Bimp.tempSelectBitmap.clear();
						mOrderNumber = null;
						mOrderDetailNumber = null;
						mTeacherID = 0;
						content = null;
					}
				}
				// System.exit(0);
			}
		});
		vComprehensiveScore
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar arg0, float arg1,
							boolean arg2) {
						mComprehensiveScore = ((int) arg1) * 10;
					}
				});
	}

	/**
	 * 用户评价老师
	 * 
	 * @param mNote
	 *            评论内容
	 */
	private void getEvaluteTeacher(String mNote) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, mLoginKey);
		maps.put(DataTag.ORDERNUMBER, mOrderNumber);
		maps.put(DataTag.ORDERDETAILNUMBER, mOrderDetailNumber);
		maps.put(DataTag.TEACHERID, mTeacherID);
		maps.put(DataTag.TEACHERSCORE, mComprehensiveScore);
		maps.put(DataTag.KNOWLEDGESCORE, mProfessional);
		maps.put(DataTag.SERVICESCORE, mService);
		maps.put(DataTag.PUNCTUALITYSCORE, mPunctual);
		maps.put(DataTag.COMMENTCONTENT, mNote);

		HttpTool.post(getContext(), Apis.EVALUATETEACHER, loading, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						if (StringTool.isNoBlankAndNoNull(response)) {
							loading.setVisibility(View.GONE);
							ToastTool.toastMessage(getContext(), "获得10积分");
							// AppManager.getAppManager().finishActivity(
							// EvaluateActivity.class);
							// Bimp.tempSelectBitmap.clear();
							// mOrderNumber = null;
							// mOrderDetailNumber = null;
							// mTeacherID = 0;
							// content = null;

							for (int i = 0; i < PublicWay.activityList.size(); i++) {
								if (null != PublicWay.activityList.get(i)) {
									PublicWay.activityList.get(i).finish();
									Bimp.tempSelectBitmap.clear();
									mOrderNumber = null;
									mOrderDetailNumber = null;
									mTeacherID = 0;
									content = null;
								}
							}
							// System.exit(0);

						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						loading.setVisibility(View.GONE);
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}

	private String getOrderInfo(PicBean request) {
		Gson gson = new GsonBuilder().create();
		String result = gson.toJson(request);
		return result;
	}

	@Override
	public void resume() {
		if (mApplication.isLogin()) {
			mLoginKey = mApplication.getLOGIN_KEY();
		}
		if (Bimp.tempSelectBitmap.size() > 0) {
			noScrollgridview.setVisibility(View.VISIBLE);
		} else {
			noScrollgridview.setVisibility(View.GONE);
		}
	}

	/**
	 * 上传图片资源
	 * 
	 * @param login_KEY
	 * @param base64
	 * @param mTeacherID2
	 * @param mOrderDetailNumber2
	 * @param mOrderNumber2
	 */
	private void uploadImage(String login_KEY, String base64) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, mLoginKey);
		maps.put(DataTag.PIC, base64);

		HttpTool.imagePost(getContext(), Apis.IMAGEUPLOAD, maps,
				new StringHandler(null) {

					@Override
					public void success(String response) {
						ImageBean bean = null;
						PicBean beans = new PicBean();
						if (StringTool.isNoBlankAndNoNull(response)) {
							bean = JSON.decode(response, ImageBean.class);
							imageList.add(bean);
							beans.setImageBean(imageList);
							num++;
							if (num == imageNum) {
								if (StringTool.isNoBlankAndNoNull(vNote
										.getText().toString().trim())) {
									mNote = vNote.getText().toString().trim();
								} else {
									mNote = "";
								}
								beans.setMemo(mNote);
								getEvaluteTeacher(getOrderInfo(beans));
							}
						}
					}

					@Override
					public void failure(int statusCode, String responseBody,
							Throwable e) {
						num = 0;
						loading.setVisibility(View.GONE);
						MyApplication.ShowFailMessage(getContext(),
								responseBody);
					}
				});

	}

	@Override
	public void destroy() {
		Bimp.tempSelectBitmap.clear();
		mOrderNumber = null;
		mOrderDetailNumber = null;
		mTeacherID = 0;
		content = null;

	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == 9) {
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.evaluate_btn_addphoto_naomal));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
						.getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if(Bimp.tempSelectBitmap!=null){
							if (Bimp.max == Bimp.tempSelectBitmap.size()) {
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
								break;
							} else {
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							}
						}
					
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();

		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);

				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < PublicWay.activityList.size(); i++) {
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
					Bimp.tempSelectBitmap.clear();
					mOrderNumber = null;
					mOrderDetailNumber = null;
					mTeacherID = 0;
					content = null;
				}
			}
		}
		return true;
	}

}
