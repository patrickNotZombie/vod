package com.dcampus.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtils {

	/**
	 * 四舍五入
	 * @param d
	 * @param n  n==0取整；n>0取n位小数；n<0取(-n)位整数
	 * @return
	 */
	public static Double getRoundDouble(Double d, int n) {
		if (d == null) {
			return 0d;
		}
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(n, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	/**
	 * 四舍五入
	 * @param d
	 * @param n  n==0取整；n>0取n位整数；n<0取(-n)位整数
	 * @return
	 */
	public static Long getRoundLong(Long l, int n) {
		if (l == null) {
			return 0L;
		}
		BigDecimal bd = new BigDecimal(l);
		bd = bd.setScale((n >= 0 ? -n : n), BigDecimal.ROUND_HALF_UP);
		return bd.longValue();
	}
	
	public static String getFormatNumber(Number number, String pattern) {
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(number);
	}
	
	public static void main(String[] args) {
		System.out.println(getFormatNumber(11d, "0.#"));
		System.out.println(getFormatNumber(11.1d, "0.#"));
	}
}
