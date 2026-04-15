package com.lingx.jt808.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Helpers for safe dynamic SQL fragments (parameter binding + validation).
 */
public final class SqlSafe {

	private SqlSafe() {
	}

	/** Escape for LIKE ... ESCAPE '\\' when binding a user-supplied pattern. */
	public static String escapeLike(String s) {
		if (s == null) {
			return "";
		}
		return s.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
	}

	public static String placeholders(int n) {
		if (n <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			if (i > 0) {
				sb.append(',');
			}
			sb.append('?');
		}
		return sb.toString();
	}

	/**
	 * Device / car ids from comma-separated list (alphanumeric, dash, underscore).
	 */
	public static List<String> splitDeviceIds(String csv) {
		List<String> out = new ArrayList<>();
		if (csv == null || csv.isEmpty()) {
			return out;
		}
		for (String part : csv.split(",")) {
			String s = part.replace(" ", "").trim();
			if (s.isEmpty()) {
				continue;
			}
			if (!s.matches("[a-zA-Z0-9\\-_]{1,128}")) {
				continue;
			}
			out.add(s);
		}
		return out;
	}

	public static List<String> splitGroupNames(String csv) {
		List<String> out = new ArrayList<>();
		if (csv == null || csv.isEmpty()) {
			return out;
		}
		for (String part : csv.split(",")) {
			String s = part.trim();
			if (s.isEmpty()) {
				continue;
			}
			if (s.length() > 200) {
				s = s.substring(0, 200);
			}
			out.add(s);
		}
		return out;
	}

	public static List<Integer> parseIntList(String csv) {
		List<Integer> out = new ArrayList<>();
		if (csv == null || csv.isEmpty()) {
			return out;
		}
		for (String part : csv.split(",")) {
			String s = part.replace(" ", "").trim();
			if (s.isEmpty()) {
				continue;
			}
			try {
				out.add(Integer.parseInt(s));
			} catch (NumberFormatException e) {
				// skip invalid token
			}
		}
		return out;
	}

	public static List<Long> parseLongIds(String csv) {
		List<Long> out = new ArrayList<>();
		if (csv == null || csv.isEmpty()) {
			return out;
		}
		for (String part : csv.split(",")) {
			String s = part.trim();
			if (s.isEmpty()) {
				continue;
			}
			try {
				out.add(Long.parseLong(s));
			} catch (NumberFormatException e) {
				// skip
			}
		}
		return out;
	}

	public static Double parseDouble(String s) {
		if (s == null) {
			return null;
		}
		s = s.replace(" ", "").trim();
		if (s.isEmpty()) {
			return null;
		}
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * First 8 chars of a gpstime string must be digits (yyyyMMdd) for dynamic table names like
	 * tgps_data_yyyyMMdd.
	 */
	public static String sanitizeTableDate8FromGpstime(String gpstime) {
		if (gpstime == null || gpstime.length() < 8) {
			return null;
		}
		String d = gpstime.substring(0, 8);
		if (!d.matches("\\d{8}")) {
			return null;
		}
		return d;
	}

	/** gpstime etc.: digit-only time string */
	public static String sanitizeDigitTime(String s) {
		if (s == null) {
			return null;
		}
		s = s.replace(" ", "").trim();
		if (s.isEmpty()) {
			return null;
		}
		if (!s.matches("\\d{1,20}")) {
			return null;
		}
		return s;
	}

	public static int clampPage(int page) {
		return page < 1 ? 1 : page;
	}

	public static int clampRows(int rows, int maxRows) {
		if (rows < 1) {
			return 20;
		}
		return Math.min(rows, maxRows);
	}

	public static int offset(int page, int rows) {
		return (clampPage(page) - 1) * clampRows(rows, 500);
	}

	/**
	 * Filters for tsb_adas / tsb_dsm / tsb_bsd / tsb_tpms style queries.
	 */
	public static void appendTsbFilters(StringBuilder vif, List<Object> args, String deviceId, String type,
			String speed, String stime, String etime) {
		List<String> ids = splitDeviceIds(deviceId);
		if (!ids.isEmpty()) {
			vif.append(" and car_id in (").append(placeholders(ids.size())).append(")");
			args.addAll(ids);
		}
		List<Integer> types = parseIntList(type);
		if (!types.isEmpty()) {
			vif.append(" and bjlx in (").append(placeholders(types.size())).append(")");
			args.addAll(types);
		}
		Double sp = parseDouble(speed);
		if (sp != null) {
			vif.append(" and speed>?");
			args.add(sp);
		}
		String st = sanitizeDigitTime(stime);
		if (st != null) {
			vif.append(" and gpstime>?");
			args.add(st);
		}
		String et = sanitizeDigitTime(etime);
		if (et != null) {
			vif.append(" and gpstime<?");
			args.add(et);
		}
	}

	/**
	 * Filters for tgps_car_alarm joined query (prefix t.).
	 */
	public static void appendAlarmListFilters(StringBuilder vif, List<Object> args, String deviceId, String name,
			String speed, String stime, String etime) {
		List<String> ids = splitDeviceIds(deviceId);
		if (!ids.isEmpty()) {
			vif.append(" and t.car_id in (").append(placeholders(ids.size())).append(")");
			args.addAll(ids);
		}
		if (name != null && !name.replace(" ", "").isEmpty()) {
			String n = name.replace(" ", "");
			vif.append(" and t.name like ? escape '\\'");
			args.add(escapeLike(n) + "%");
		}
		Double sp = parseDouble(speed);
		if (sp != null) {
			vif.append(" and t.speed>?");
			args.add(sp);
		}
		String st = sanitizeDigitTime(stime);
		if (st != null) {
			vif.append(" and t.gpstime>?");
			args.add(st);
		}
		String et = sanitizeDigitTime(etime);
		if (et != null) {
			vif.append(" and t.gpstime<?");
			args.add(et);
		}
	}
}
