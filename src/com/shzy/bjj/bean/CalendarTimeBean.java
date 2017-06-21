package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CalendarTimeBean implements Serializable {

	public static String getCalendarTime(List<List<MapBean>> dataList,
			List<MapBean> titleList) {
		List<String> data = new ArrayList<String>();
		for (int i = 0, length = dataList.size(); i < length; i++) {
			if (i == 0) {
				continue;
			} else {
				List<MapBean> list = dataList.get(i);
				boolean flag = false;
				for (int j = 0, len = list.size(); j < len; j++) {
					if (list.get(j).isFlag()) {
						flag = true;
						break;
					}
				}
				if (flag) {
					data.addAll(getOrderAndDate(list, titleList, i));
				}
			}
		}
		StringBuilder builder = new StringBuilder("(");
		for (int i = 0, len = data.size(); i < len; i++) {
			builder.append(data.get(i));
			if (i < len - 1) {
				builder.append(" OR ");
			}
		}
		builder.append(")");
		return builder.toString().trim();
	}

	private static List<String> getOrderAndDate(List<MapBean> list,
			List<MapBean> titleList, int position) {
		List<String> dataList = new ArrayList<String>();
		// 上午
		boolean flag = false;
		for (int i = 0; i < 3; i++) {
			if (list.get(i).isFlag()) {
				flag = true;
				break;
			}
		}
		if (flag) {
			dataList.addAll(getTime(list.subList(0, 3), 0, position, titleList));
		}
		// 中午
		boolean tag = false;
		for (int j = 3; j < 7; j++) {
			if (list.get(j).isFlag()) {
				tag = true;
				break;
			}
		}
		if (tag) {
			dataList.addAll(getTime(list.subList(3, 7), 1, position, titleList));
		}

		// 晚上
		boolean tak = false;
		for (int x = 7; x < 9; x++) {
			if (list.get(x).isFlag()) {
				tak = true;
				break;
			}
		}
		if (tak) {
			dataList.addAll(getTime(list.subList(7, 9), 2, position, titleList));
		}
		return dataList;
	}

	private static List<String> getTime(List<MapBean> list, int tag,
			int position, List<MapBean> titleList) {
		List<String> data = new ArrayList<String>();
		switch (tag) {
		// 上午
		case 0:
			// 如果10~11选中
			if (list.get(1).isFlag()) {
				// 如果9~10和11-12 都选中
				if (list.get(0).isFlag() && list.get(2).isFlag()) {
					String[] array = { getDateTime(position, 0, titleList),
							getDateTime(position, 1, titleList),
							getDateTime(position, 2, titleList) };
					data.add(getOrder(array));
					// 如果9~10和11-12 都不选中
				} else if (!list.get(0).isFlag() && !list.get(2).isFlag()) {
					String[] array = { getDateTime(position, 1, titleList) };
					data.add(getOrder(array));
				} else {
					// 如果9~10选中
					if (list.get(0).isFlag()) {
						String[] array = { getDateTime(position, 0, titleList),
								getDateTime(position, 1, titleList) };
						data.add(getOrder(array));
					}
					// 如果11-12 选中
					if (list.get(2).isFlag()) {
						String[] array = { getDateTime(position, 1, titleList),
								getDateTime(position, 2, titleList) };
						data.add(getOrder(array));
					}
				}
				// 如果10~11不选中
			} else {
				if (list.get(0).isFlag()) {
					String[] array = { getDateTime(position, 0, titleList) };
					data.add(getOrder(array));
				}
				if (list.get(2).isFlag()) {
					String[] array = { getDateTime(position, 2, titleList) };
					data.add(getOrder(array));
				}
			}
			break;
		// 中午
		case 1:
			// 如果中间2同时选中
			if (list.get(1).isFlag() && list.get(2).isFlag()) {
				if (list.get(0).isFlag() && list.get(3).isFlag()) {
					String[] array = { getDateTime(position, 3, titleList),
							getDateTime(position, 4, titleList),
							getDateTime(position, 5, titleList),
							getDateTime(position, 6, titleList) };
					data.add(getOrder(array));
				} else if (!list.get(0).isFlag() && !list.get(3).isFlag()) {
					String[] array = { getDateTime(position, 4, titleList),
							getDateTime(position, 5, titleList) };
					data.add(getOrder(array));
				} else {
					// 如果1选中
					if (list.get(0).isFlag()) {
						String[] array = { getDateTime(position, 3, titleList),
								getDateTime(position, 4, titleList),
								getDateTime(position, 5, titleList) };
						data.add(getOrder(array));
					}
					// 如果3选中
					if (list.get(3).isFlag()) {
						String[] array = { getDateTime(position, 4, titleList),
								getDateTime(position, 5, titleList),
								getDateTime(position, 6, titleList) };
						data.add(getOrder(array));
					}
				}
			} else {
				// 如果中间2选中
				if (list.get(1).isFlag()) {
					// 如果1选中
					if (list.get(0).isFlag()) {
						String[] array = { getDateTime(position, 3, titleList),
								getDateTime(position, 4, titleList) };
						data.add(getOrder(array));
					} else {
						String[] array = { getDateTime(position, 4, titleList) };
						data.add(getOrder(array));
					}
				} else {
					if (list.get(0).isFlag()) {
						String[] array = { getDateTime(position, 3, titleList) };
						data.add(getOrder(array));
					}
				}
				// 如果中间3选中
				if (list.get(2).isFlag()) {
					// 如果4选中
					if (list.get(3).isFlag()) {
						String[] array = { getDateTime(position, 5, titleList),
								getDateTime(position, 6, titleList) };
						data.add(getOrder(array));
					} else {
						String[] array = { getDateTime(position, 5, titleList) };
						data.add(getOrder(array));
					}
				} else {
					if (list.get(3).isFlag()) {
						String[] array = { getDateTime(position, 6, titleList) };
						data.add(getOrder(array));
					}
				}
			}
			break;
		// 晚上
		case 2:
			if (list.get(0).isFlag() && list.get(1).isFlag()) {
				String[] array = { getDateTime(position, 7, titleList),
						getDateTime(position, 8, titleList) };
				data.add(getOrder(array));
			} else {
				if (list.get(0).isFlag()) {
					String[] array = { getDateTime(position, 7, titleList) };
					data.add(getOrder(array));
				}
				if (list.get(1).isFlag()) {
					String[] array = { getDateTime(position, 8, titleList) };
					data.add(getOrder(array));
				}
			}
			break;
		default:
			break;
		}
		return data;
	}

	public static String getOrder(String[] array) {
		StringBuilder builder = new StringBuilder("(");
		int length = array.length;
		for (int i = 0; i < length; i++) {
			builder.append(array[i]);
			if (i < length - 1) {
				builder.append(" AND ");
			}
		}
		builder.append(")");
		return builder.toString().trim();
	}

	public static String getDateTime(int i, int j, List<MapBean> titleList) {
		String day = titleList.get(i).getName();
		String time = getTime(day, j);
		return time;
	}

	public static String getTime(String date, int j) {
		String result = date;
		switch (j) {
		case 0:
			result = date + "-09-00-00_" + date + "-10-00-00";
			break;

		case 1:
			result = date + "-10-00-00_" + date + "-11-00-00";
			break;
		case 2:
			result = date + "-11-00-00_" + date + "-12-00-00";
			break;
		case 3:
			result = date + "-14-00-00_" + date + "-15-00-00";
			break;
		case 4:
			result = date + "-15-00-00_" + date + "-16-00-00";
			break;
		case 5:
			result = date + "-16-00-00_" + date + "-17-00-00";
			break;
		case 6:
			result = date + "-17-00-00_" + date + "-18-00-00";
			break;
		case 7:
			result = date + "-19-00-00_" + date + "-20-00-00";
			break;
		case 8:
			result = date + "-20-00-00_" + date + "-21-00-00";
			break;
		default:
			break;
		}
		return result;
	}
}
