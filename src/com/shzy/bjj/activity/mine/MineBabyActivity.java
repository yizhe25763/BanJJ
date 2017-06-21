package com.shzy.bjj.activity.mine;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shzy.bjj.AppManager;
import com.shzy.bjj.MyApplication;
import com.shzy.bjj.R;
import com.shzy.bjj.activity.base.BaseActivity;
import com.shzy.bjj.bean.BabyBean;
import com.shzy.bjj.constant.Apis;
import com.shzy.bjj.constant.DataTag;
import com.shzy.bjj.handler.StringHandler;
import com.shzy.bjj.tools.DateUtil;
import com.shzy.bjj.tools.DateUtils;
import com.shzy.bjj.tools.DialogUtils;
import com.shzy.bjj.tools.FileUtil;
import com.shzy.bjj.tools.HttpTool;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;
import com.shzy.bjj.view.CircularImage;
import com.shzy.bjj.view.DatePickerDialog;

public class MineBabyActivity extends BaseActivity implements OnClickListener {
	private CircularImage head_image;
	private TextView baby_name;
	private EditText baby_nickEditText;
	private EditText baby_numberEditText;
	private TextView baby_birthdayEditText;
	private ImageView manImageView;
	private ImageView femanImageView;
	private LinearLayout baby_moreLayout;
	private LinearLayout baby_deleteLayout;

	private File cameraFile;
	private File cropFile;
	private String base64 = "";

	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private Calendar babyCalendar = Calendar.getInstance();
	private boolean flag = false;
	private BabyBean baby = null;
	private int sex = 0;

	public static void startActivity(Context context, boolean flag,
			BabyBean bean) {
		context.startActivity(new Intent(context, MineBabyActivity.class)
				.putExtra("tag", flag).putExtra("bean", bean));
	}

	@Override
	public int bindLayout() {
		return R.layout.mine_baby;
	}

	@Override
	public void initView(View view) {
		flag = getIntent().getBooleanExtra("tag", false);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.mine_head_img_baby)
				.showImageOnFail(R.drawable.mine_head_img_baby)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		cropFile = new File(Environment.getExternalStorageDirectory(), "jpg");
		action_title.setText(R.string.baby_title);
		action_right.setText(R.string.save);
		head_image = $(R.id.head_img);
		baby_name = $(R.id.baby_name);
		baby_nickEditText = $(R.id.baby_nick);
		baby_numberEditText = $(R.id.baby_number);
		baby_birthdayEditText = $(R.id.baby_birthday);
		manImageView = $(R.id.man);
		femanImageView = $(R.id.feman);
		manImageView.setEnabled(false);
		femanImageView.setEnabled(true);
		baby_moreLayout = $(R.id.baby_more);
		baby_deleteLayout = $(R.id.baby_delete);
		loading.setVisibility(View.GONE);
		if (flag) {
			baby_deleteLayout.setVisibility(View.GONE);
		} else {
			baby = (BabyBean) getIntent().getSerializableExtra("bean");
			if (baby != null) {
				baby_name.setVisibility(View.INVISIBLE);
				baby_nickEditText.setText(baby.getName());
				baby_numberEditText.setText(baby.getIdentity());
				baby_birthdayEditText.setText(baby.getBirthday());
				sex = baby.getSex();
				if (sex == 0) {
					manImageView.setEnabled(false);
					femanImageView.setEnabled(true);
				} else {
					manImageView.setEnabled(true);
					femanImageView.setEnabled(false);
				}
				String imageUrl = baby.getPic_url();
				if (StringTool.isNoBlankAndNoNull(imageUrl)) {
					imageLoader.displayImage(imageUrl, head_image, options,
							new SimpleImageLoadingListener() {
								@Override
								public void onLoadingStarted(String imageUri,
										View view) {
								}

								@Override
								public void onLoadingFailed(String imageUri,
										View view, FailReason failReason) {
								}

								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage) {
								}
							}, null);
				} else {
					head_image.setImageResource(R.drawable.mine_head_img_baby);
					baby_name.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	@Override
	public void initData(Context mContext) {

	}

	@Override
	public void initListener() {
		action_back.setOnClickListener(this);
		action_right.setOnClickListener(this);
		baby_moreLayout.setOnClickListener(this);
		baby_deleteLayout.setOnClickListener(this);
		head_image.setOnClickListener(this);
		baby_birthdayEditText.setOnClickListener(this);
		manImageView.setOnClickListener(this);
		femanImageView.setOnClickListener(this);
	}

	@Override
	public void resume() {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.action_back:
			AppManager.getAppManager().finishActivity();
			break;

		case R.id.head_img:
			cameraFile = DialogUtils.showImageDialog(this);
			break;

		case R.id.action_right:
			String nick = baby_nickEditText.getText().toString().trim();
			// String identity =
			// baby_numberEditText.getText().toString().trim();
			String birthday = baby_birthdayEditText.getText().toString().trim();
			if (!StringTool.isNoBlankAndNoNull(nick)) {
				ToastTool.toastMessage(this, R.string.toast_baby_name_empty);
				return;
			}
			// if (identity.length() > 0 && identity.length() != 18) {
			// ToastTool.toastMessage(getContext(), "请输入正确的身份证号码");
			// return;
			// } else {
			// identity = "";
			// }
			if (!StringTool.isNoBlankAndNoNull(birthday)) {
				ToastTool.toastMessage(this, R.string.toast_baby_birth_empty);
				return;
			}
			if (mApplication.isLogin()) {
				loading.setVisibility(View.VISIBLE);
				if (flag) {
					createBaby(mApplication.getLOGIN_KEY(), nick, sex,
							birthday, base64, nick, "");
				} else {
					if (baby != null)
						modifyBaby(mApplication.getLOGIN_KEY(), baby.getId(),
								nick, sex, birthday, base64, nick, "");
				}
			}
			break;
		case R.id.baby_more:

			break;
		case R.id.baby_delete:
			if (baby != null && mApplication.isLogin()) {
				Intent intent = new  Intent();
				intent.setClass(this, BabyDeleteDialog.class);
				startActivityForResult(intent, 0);
				
				
//				BabyDeleteDialog.startActivity(getContext(), baby);
			}
			break;

		case R.id.baby_birthday:
			String birth = baby_birthdayEditText.getText().toString().trim();
			setBabyBirthday(birth);
			break;

		case R.id.man:
			sex = 0;
			manImageView.setEnabled(false);
			femanImageView.setEnabled(true);
			break;
		case R.id.feman:
			sex = 1;
			femanImageView.setEnabled(false);
			manImageView.setEnabled(true);
			break;
		default:
			break;
		}
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
						baby_birthdayEditText.setText(DateUtils
								.formatYMD(new Date(babyCalendar
										.getTimeInMillis())));
					}
				}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		pickerDialog.setTitle("设置宝宝生日");
		pickerDialog.setMaxDate(Calendar.getInstance().getTime());
		pickerDialog.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 拍照

		
		
		
		if (requestCode == DialogUtils.REQUEST_CODE_CAMERA
				&& FileUtil.isValid(cameraFile) && resultCode == RESULT_OK) {
			DialogUtils.startPhotoCrop(this, Uri.fromFile(cameraFile), 80,
					cropFile);
			cameraFile = null;
			// 系统图片库-选择图片
		} else if (requestCode == DialogUtils.REQUEST_CODE_IMAGE
				&& data != null && resultCode == RESULT_OK) {
			DialogUtils.startPhotoCrop(this, data.getData(), 80, cropFile);
			// 裁剪图片
		} else if (requestCode == DialogUtils.REQUEST_CODE_CROP && data != null
				&& resultCode == RESULT_OK) {
			Bitmap bitmap = BitmapFactory
					.decodeFile(cropFile.getAbsolutePath());
			if (bitmap != null) {
				// 设置妈妈头像
				head_image.setImageBitmap(bitmap);
				base64 = DialogUtils.imgToBase64(bitmap);
			} else {
				ToastTool.toastMessage(this, "用户存储空间不足，请整理空间后上传!");
			}
		}else if(resultCode==1){
			deleteBaby(mApplication.getLOGIN_KEY(), baby.getId());
		}
	}

	private void createBaby(String loginKey, String nickname, int sex,
			String birthday, String pic, String name, String identity) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put("nickname", nickname);
		maps.put("sex", sex);
		maps.put("birthday", birthday);
		maps.put("pic", pic);
		maps.put("name", name);
		maps.put("identity", identity);

		HttpTool.post(getContext(),Apis.USER_BABY_CREATE, loading,maps, new StringHandler(loading) {

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					ToastTool.toastMessage(MineBabyActivity.this,
							R.string.toast_baby_create);
					AppManager.getAppManager().finishActivity();
					MineMamaActivity.isRefresh = true;
				}
				loading.setVisibility(View.GONE);
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});
	}

	private void modifyBaby(String loginKey, int baby_id, String nickname,
			int sex, String birthday, String pic, String name, String identity) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put("nickname", nickname);
		maps.put("sex", sex);
		maps.put("birthday", birthday);
		maps.put("pic", pic);
		maps.put("name", name);
		maps.put("identity", identity);
		maps.put("baby_id", baby_id);

		HttpTool.post(getContext(),Apis.USER_BABY_MODIFY, loading,maps, new StringHandler(loading) {

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					ToastTool.toastMessage(MineBabyActivity.this,
							R.string.toast_baby_update);
					AppManager.getAppManager().finishActivity();
					MineMamaActivity.isRefresh = true;
				}
				loading.setVisibility(View.GONE);
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});
	}
	private void deleteBaby(String loginKey, int baby_id) {
		Map maps = new HashMap<String, String>();
		maps.put(DataTag.LOGINKEY, loginKey);
		maps.put("baby_id", baby_id);

		HttpTool.post(getContext(),Apis.USER_BABY_DELETE,loading, maps, new StringHandler(loading) {

			@Override
			public void success(String response) {
				if (StringTool.isNoBlankAndNoNull(response)) {
					ToastTool.toastMessage(MineBabyActivity.this,
							R.string.toast_baby_delete);
					AppManager.getAppManager().finishActivity();
					AppManager.getAppManager().finishActivity(
							MineBabyActivity.class);
					MineMamaActivity.isRefresh = true;
				}
				loading.setVisibility(View.GONE);
			}

			@Override
			public void failure(int statusCode, String responseBody, Throwable e) {
				MyApplication.ShowFailMessage(getContext(), responseBody);
			}
		});
	}
}
