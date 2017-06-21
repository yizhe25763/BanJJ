package com.shzy.bjj.bean;

import java.io.Serializable;

/**
 * 百度地图商圈转换
 */
public class BaiDuMapBusiness implements Serializable {

	// 成功状态"OK"
	private String status;
	private Business result;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Business getResult() {
		return result;
	}

	public void setResult(Business result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "BaiDuMapBusiness [status=" + status + ", result=" + result
				+ "]";
	}

	public class Business {
		private String business;
		private City addressComponent;

		@Override
		public String toString() {
			return "Business [business=" + business + ", addressComponent="
					+ addressComponent + "]";
		}

		public String getBusiness() {
			return business;
		}

		public void setBusiness(String business) {
			this.business = business;
		}

		public City getAddressComponent() {
			return addressComponent;
		}

		public void setAddressComponent(City addressComponent) {
			this.addressComponent = addressComponent;
		}

		public class City {
			private String city;

			public String getCity() {
				return city;
			}

			public void setCity(String city) {
				this.city = city;
			}

			@Override
			public String toString() {
				return "City [city=" + city + "]";
			}

		}

	}

}
