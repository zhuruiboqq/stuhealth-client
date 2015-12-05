package com.vastcm.stuhealth.client.utils;

import java.math.BigDecimal;

import org.springframework.util.StringUtils;

public class BigDecimalUtil {

	public static int Default_Scale = 2;
	
	public static final BigDecimal NEGATIVE_ONE = new BigDecimal("-1");
	public static final BigDecimal ZERO = new BigDecimal("0");
	public static final BigDecimal ONE = new BigDecimal("1");
	public static final BigDecimal SEVEN = new BigDecimal("7");
	public static final BigDecimal HUNDRED = new BigDecimal("100");
	public static final BigDecimal THOUSAND = new BigDecimal("1000");

	/**
	 * 获取不为空的大数，如果传入的参数为空，返回0.00的大数
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal getNotNullBigDecimal(BigDecimal value) {
		return value == null ? ZERO : value;
	}

	/**
	 * 获取不为空的大数，如果传入的参数为空，返回0.00的大数
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal getNotNull(BigDecimal value) {
		return value == null ? ZERO : value;
	}

	/**
	 * 如果value值为0，则返回null。否则返回原值
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal getZeroNull(BigDecimal value) {
		if (value == null)
			return null;
		if (isZero(value))
			return null;
		return value;
	}

	/**
	 * 是否等于0。value为空，出错
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isZero(BigDecimal value) {
		return ZERO.compareTo(value) == 0;
	}

	/**
	 * 两个大数的正负号是否相同，两个大数都不能为空。如果任意一个数为0，返回true
	 * 
	 * @param value1
	 * @param value2
	 * @return true，两个大数的正负号相同。
	 */
	public static boolean isSameNegativeSign(BigDecimal value1, BigDecimal value2) {
		if (isZero(value1) || isZero(value2))
			return true;

		if ((value1.compareTo(ZERO) > 0 && value2.compareTo(ZERO) > 0) || (value1.compareTo(ZERO) < 0 && value2.compareTo(ZERO) < 0))
			return true;

		return false;
	}

	/**
	 * 如果value为空，返回默认值
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static BigDecimal getBigDecimal(BigDecimal value, BigDecimal defaultValue) {
		return value == null ? defaultValue : value;
	}

	/**
	 * 把Object转换成大数，如果参数obj为空，返回defaultValue
	 * 
	 * @param obj
	 * @param defaultValue 如果obj为空，默认返回该值
	 * @return
	 */
	public static BigDecimal getBigDecimal(Object obj, BigDecimal defaultValue) {
		if (obj == null)
			return defaultValue;
		if (obj instanceof BigDecimal)
			return (BigDecimal) obj;
		String str = obj.toString();
		if (!StringUtils.hasText(str))
			return defaultValue;
		return new BigDecimal(str);
	}

	/**
	 * 把Object转换成大数，如果参数obj为空，返回ZERO
	 * 
	 * @param obj
	 * @return
	 */
	public static BigDecimal getBigDecimal(Object obj) {
		if (obj == null)
			return ZERO;
		if (obj instanceof BigDecimal)
			return (BigDecimal) obj;
		String str = obj.toString();
		if (!StringUtils.hasText(str))
			return ZERO;
		return new BigDecimal(str);
	}

	public static Object add(Object value1, Object value2) {
		if (value1 == null && value2 == null)
			return 0;
		if (value1 == null)
			return value2;
		if (value2 == null)
			return value1;
		return BigDecimalUtil.getBigDecimal(value1).add(BigDecimalUtil.getBigDecimal(value2));
	}

	public static Object multiply(Object multiplicandObj, Object multiplierObj) {
		if (multiplicandObj == null || multiplierObj == null)
			return null;
		return BigDecimalUtil.getBigDecimal(multiplicandObj).multiply(BigDecimalUtil.getBigDecimal(multiplierObj));
	}

	public static Object divide(Object dividendObj, Object divisorObj) {
		if (dividendObj == null || divisorObj == null)
			return null;
		BigDecimal divisor = BigDecimalUtil.getBigDecimal(divisorObj);
		if (BigDecimalUtil.isZero(divisor))
			return ZERO;
		return BigDecimalUtil.getBigDecimal(dividendObj).divide(divisor, Default_Scale, BigDecimal.ROUND_HALF_UP);
	}
}