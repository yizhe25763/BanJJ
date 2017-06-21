package com.shzy.bjj.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class ValidTool {
	public static boolean isPhoneNumberValid(String phoneNumber) {
		String telRegex = "[1][34578]\\d{9}";
		if (TextUtils.isEmpty(phoneNumber)) {
			return false;
		} else {
			return phoneNumber.matches(telRegex);
		}
	}

	public static boolean isAddressValid(String address) {
		Pattern p = Pattern.compile(".*\\d+.*");
		if (TextUtils.isEmpty(address)) {
			return false;
		} else {
			Matcher m = p.matcher(address);
			if (m.matches()) {
				return true;
			} else {
				return false;
			}

		}
	}
}
