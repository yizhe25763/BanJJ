package com.shzy.bjj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shzy.bjj.R;

public class Switch extends LinearLayout {

	private ImageView imageView;
	private boolean open = true;
	private boolean isAninFinish = true;
	private float x;
	private boolean isChangedByTouch = false;
	private OnSwitchChangeListener switchChangeListener;

	public interface OnSwitchChangeListener {
		void onSwitchChanged(boolean open);
	}

	public Switch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Switch(Context context) {
		super(context);
		init();
	}

	private void init() {
		on();
		imageView = new ImageView(getContext());
		LayoutParams layoutParams = (new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layoutParams.gravity = Gravity.CENTER_VERTICAL;
		int margin = getResources().getDimensionPixelSize(R.dimen.size_dip_2);
		layoutParams.setMargins(margin, margin, margin, margin);
		imageView.setLayoutParams(layoutParams);
		imageView.setImageResource(R.drawable.switch_mask);
		addView(imageView);
	}

	public boolean getSwitchStatus() {
		return open;
	}

	public void setSwitchStatus(boolean isOpen) {
		this.open = isOpen;
		if (isOpen) {
			on();
		} else {
			off();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			x = event.getX();
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			if (event.getX() - x > 5 && !open) {
				changeStatus();
			} else if (event.getX() - x < -5 && open) {
				changeStatus();
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			if (Math.abs(event.getX() - x) <= 5) {
				changeStatus();
			}
			isChangedByTouch = false;
			break;
		}
		case MotionEvent.ACTION_CANCEL: {
			isChangedByTouch = false;
			break;
		}
		}
		return true;
	}

	private void changeStatus() {
		if (isAninFinish && !isChangedByTouch) {
			isChangedByTouch = true;
			open = !open;
			isAninFinish = false;
			if (switchChangeListener != null) {
				switchChangeListener.onSwitchChanged(open);
			}
			changeOpenStatusWithAnim(open);
		}
	}

	private void changeOpenStatusWithAnim(boolean open) {
		if (open) {
			Animation leftToRight = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE,
					getWidth() - imageView.getWidth(),
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					0);
			leftToRight.setDuration(300);
			leftToRight.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					imageView.clearAnimation();
					on();
					isAninFinish = true;
				}
			});
			imageView.startAnimation(leftToRight);
		} else {
			Animation rightToLeft = new TranslateAnimation(
					Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE,
					imageView.getWidth() - getWidth(),
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
					0);
			rightToLeft.setDuration(300);
			rightToLeft.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					imageView.clearAnimation();
					off();
					isAninFinish = true;
				}
			});
			imageView.startAnimation(rightToLeft);
		}
	}

	private void off() {
		setGravity(Gravity.LEFT);
		setBackgroundResource(R.drawable.switch_background_off);
	}

	private void on() {
		setGravity(Gravity.RIGHT);
		setBackgroundResource(R.drawable.switch_background_on);
	}

	public OnSwitchChangeListener getSwitchChangeListener() {
		return switchChangeListener;
	}

	public void setOnSwitchChangeListener(
			OnSwitchChangeListener switchChangeListener) {
		this.switchChangeListener = switchChangeListener;
	}

}
