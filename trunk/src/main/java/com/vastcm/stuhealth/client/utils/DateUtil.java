package com.vastcm.stuhealth.client.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**日期处理的常用功能
 * @author bob
 * 
 * @version 1.0
 */
public class DateUtil {
	public final static String DEFAULT_PATTERN = "yyyy-MM-dd";
	public final static SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_PATTERN);

	/**
	 * 以默认格式yyyy-MM-dd，转换对象为日期
	 * 
	 * @param obj
	 * @return
	 */
	public static Date getDate(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Date)
			return (Date) obj;
		String str;
		if (obj instanceof String) {
			str = (String) obj;
		} else {
			str = obj.toString();
		}
		try {
			return dateFormat.parse(str);
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * 以pattern格式，转换对象为日期
	 * 
	 * @author 祝瑞柏(bob)
	 * @param obj
	 * @param pattern
	 * @return
	 */
	public static Date getDate(Object obj, String pattern) {
		if (obj == null)
			return null;
		if (obj instanceof Date)
			return (Date) obj;
		String str;
		if (obj instanceof String) {
			str = (String) obj;
		} else {
			str = obj.toString();
		}
		try {
			return new SimpleDateFormat(pattern).parse(str);
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * 按默认返加日期格式，"yyyy-MM-dd"
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateString(Date date) {
		return dateFormat.format(date);
	}

	/**
	 * 按指定的格式，返回日期字符串
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getDateString(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 本月第一个星期有几天，如果本月第一天为星期一，那返回7。从星期一到星期天为一周
	 * 
	 * @param date
	 * @return
	 */
	public static int getFirstWeekCountInMonth(Calendar date) {
		Calendar tempDate = (Calendar) date.clone();
		tempDate.setFirstDayOfWeek(Calendar.MONDAY);
		tempDate.set(Calendar.DAY_OF_MONTH, 1);
		int dayIndex = tempDate.get(Calendar.DAY_OF_WEEK);
		if (dayIndex == Calendar.SUNDAY)
			return 1;
		return 9 - dayIndex;
	}

	/**
	 * 本月最后一个星期有几天，如果本月最后一天为星期天，返回7。从星期一到星期天为一周
	 * 
	 * @param date
	 * @return
	 */
	public static int getLastWeekCountInMonth(Calendar date) {
		Calendar tempDate = (Calendar) date.clone();
		tempDate.setFirstDayOfWeek(Calendar.MONDAY);
		tempDate.set(Calendar.DAY_OF_MONTH, tempDate.getActualMaximum(Calendar.DAY_OF_MONTH));
		int dayIndex = tempDate.get(Calendar.DAY_OF_WEEK);
		if (dayIndex == Calendar.SUNDAY)
			return 7;
		return dayIndex - 1;
	}

	/**
	 * 两个日期间，相差几天。例如：2011年06月01日与2011年06月23日相差22天，
	 * 而2011年06月24日0点0分1秒与2011年06月23日23点59分59秒相差1天。只按日期计算，不按时分秒
	 * 
	 * @author 祝瑞柏(bob)
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int dayMargin(Date date1, Date date2) {
		return Math.abs(dayMarginWithSigned(date1, date2));
	}

	/**
	 * 两个日期间，相差几天(有符号)。例如：2011年06月01日与2011年06月23日相差22天，
	 * 而2011年06月24日0点0分1秒与2011年06月23日23点59分59秒相差-1天。只按日期计算，不按时分秒
	 * 
	 * @author 祝瑞柏(bob)
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return
	 */
	public static int dayMarginWithSigned(Date startDate, Date endDate) {
		/*
		 * 与Calendar.HOUR不一样，Calendar.HOUR是12小时制的，Calendar.HOUR_OF_DAY是24小时。
		 * 则用Calendar.HOUR，如果是上午，设置值为0，表示凌晨0点。
		 * 如果是下午，设置为0，表示中午12点。影响后面的计算
		 */
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		end.set(Calendar.HOUR_OF_DAY, 0);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);
		end.set(Calendar.MILLISECOND, 0);
		//用startDate的getTime()减去endDate的getTime()就是这两天相差的毫秒数，1秒=1000毫秒，1分钟=60秒，1小时=60分钟，1天=24小时，然后除除除就得到天数了。
		long time = start.getTimeInMillis() - end.getTimeInMillis();
		time = time / 1000 / 60 / 60 / 24;
		return Integer.parseInt(String.valueOf(time));
	}

	/**
	 * 两个日期间，相差几个月。忽略月份天数，例如：2010年06月01日与2010年06月23日相差0个月份，
	 * 而2010年06月23日与2010年07月01日相差1个月份
	 * 
	 * @author 祝瑞柏(bob)
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int monthMargin(Date date1, Date date2) {
		return Math.abs(monthMarginWithSigned(date1, date2));
	}

	/**
	 * 两个日期间，相差几个月(有符号)。忽略月份天数，例如：2010年06月01日与2010年06月23日相差0个月份，
	 * 而2010年07月01日与2010年06月23日相差-1个月份
	 * 
	 * @author 祝瑞柏(bob)
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @return
	 */
	public static int monthMarginWithSigned(Date startDate, Date endDate) {
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		return (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12 + (end.get(Calendar.MONTH) - start.get(Calendar.MONTH));
	}

	/**
	 * 检查比较日期是否在开始日期与结束日期之间。如果在两个日期之间或与其中一个日期相等，返回true；否则返回false。
	 * <ul>
	 * <b>判断过程</b>
	 * <li>先与开始日期比较，如果早于开始日期，如：比较日期20100706早于开始日期20100705，返回false。此时，结束日期可以为空</li>
	 * <li>再与结束日期比较，如果晚于结束日期，则返回false</li>
	 * <li>最后返回true</li>
	 * </ul>
	 * 
	 * @author 祝瑞柏(bob)
	 * @param startDate 开始日期
	 * @param endDate 结束日期
	 * @param compareDate 比较日期
	 * @return
	 */
	public static boolean isBetween(Date startDate, Date endDate, Date compareDate) {
		if (compareDate.compareTo(startDate) < 0)
			return false;// 比开始日期要早
		if (compareDate.compareTo(endDate) > 0)
			return false;// 比结束日期要晚
		return true;
	}

	/**
	 * 开始日期与结束日期组成一组，检查日期组相互之间是否有时间上的交集。
	 * <p>
	 * 如：
	 * <ul>
	 * <li>开始日期：2000年0101,2002年0101</li>
	 * <li>结束日期：2001年0101,2003年0101</li>
	 * 没有交集，返回空
	 * </ul>
	 * <ul>
	 * <li>开始日期：2000年0101,2001年0101</li>
	 * <li>结束日期：2002年0101,2003年0101</li>
	 * 有交集2001年，返回数据{0,1}
	 * </ul>
	 * </p>
	 * @author 祝瑞柏(bob)
	 * @param startDates 所有开始日期
	 * @param endDates 所有结束日期，与开始日期一一对应
	 * @return
	 */
	public static int[] isBetween(List startDates, List endDates) {
		int size = startDates.size();
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (!isBetween((Date) startDates.get(j), (Date) endDates.get(j), (Date) startDates.get(i)))
					return new int[] { i, j };//开始日期与下一组日期期间比较
				if (!isBetween((Date) startDates.get(j), (Date) endDates.get(j), (Date) endDates.get(i)))
					return new int[] { i, j };//结束日期与下一组日期期间比较
			}
		}
		return null;
	}
}
