package com.shzy.bjj.bean;

public class LoginSuccessUserChooseBean {

	private String mAddress;
	private String mBaby;

	public String getmAddress() {
		return mAddress;
	}

	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getmBaby() {
		return mBaby;
	}

	public void setmBaby(String mBaby) {
		this.mBaby = mBaby;
	}

	@Override
	public String toString() {
		return "LoginSuccessUserChooseBean [mAddress=" + mAddress + ", mBaby="
				+ mBaby + "]";
	}

}
