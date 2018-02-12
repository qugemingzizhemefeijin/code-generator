package com.tigerjoys.np.cg.databases.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Time;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    /**
     * The empty String <code>""</code>.
     */
    public static final String EMPTY_STRING = "";

    /**
     * The comma String <code>","</code>.
     */
    public static final String COMMA_STRING = ",";

    /**
     * The unknown String <code>未知</code>.
     */
    public static final String UNKNOWN = "未知";

    /**
     * 一年的毫秒数1
     */
    public static final long YEAR_MILLIS = 31104000000l;

    /**
     * 一月的毫秒数，30天。
     */
    public static final long MONTH_MILLIS = 2592000000l;

    /**
     * 一周的毫秒数
     */
    public static final long WEEK_MILLIS = 604800000l;

    /**
     * 一天的毫秒数
     */
    public static final long DAY_MILLIS = 86400000l;

    /**
     * 一小时的毫秒数
     */
    public static final long HOUR_MILLIS = 3600000l;

    /**
     * 一分钟的毫秒数
     */
    public static final long MINUTE_MILLIS = 60000;

    /**
     * 星期的对应数组，请注意：第一位是星期天，通过Calendar.get(Calendar.DAY_OF_WEEK)的值需要-1才能对应上。
     */
    public static final String[] WEEK_TEXT = new String[]{"日", "一", "二", "三", "四", "五", "六"};

    private static ThreadLocal<SimpleDateFormat> DATE_FORMAT_LOCAL = new ThreadLocal<SimpleDateFormat>();
    private static ThreadLocal<SimpleDateFormat> DATE_TIME_FORMAT_LOCAL = new ThreadLocal<SimpleDateFormat>();

    /**
     * 生肖属性
     */
    public static final String[] zodiacArr = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};

    /**
     * 星座
     */
    public static final String[] constellationArr = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};

    /**
     * 星座对应日期
     */
    public static final int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};


    /**
     * 根据日期获取生肖
     *
     * @return
     */
    public static String getZodica(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return zodiacArr[cal.get(Calendar.YEAR) % 12];
    }

    /**
     * 根据日期获取星座
     *
     * @return
     */
    public static String getConstellation(Date date) {
        if (date == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArr[month];
        }
        // default to return 魔羯
        return constellationArr[11];
    }

    /**
     * 获取SimpleDateFormat，格式化时间为：yyyy-MM-dd
     *
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getDateFormat() {
        SimpleDateFormat df = DATE_FORMAT_LOCAL.get();
        if (df == null) {
            df = new SimpleDateFormat("yyyy-MM-dd");
            DATE_FORMAT_LOCAL.set(df);
        }
        return df;
    }

    /**
     * 获取SimpleDateFormat，格式化时间为：yyyy-MM-dd HH:mm:ss
     *
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getDateTimeFormat() {
        SimpleDateFormat df = DATE_TIME_FORMAT_LOCAL.get();
        if (df == null) {
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DATE_TIME_FORMAT_LOCAL.set(df);
        }
        return df;
    }

    /**
     * 获得指定年的第一天日期
     *
     * @param i - 0是今年，1是明年，-1去年
     * @return long
     */
    public static long getYearTimes(int i) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        if (i != 0) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + i);
        }
        return cal.getTimeInMillis();
    }

    /**
     * 获得n年今日的日期
     *
     * @param n - N
     * @return long 毫秒数
     */
    public static long getCurrDayYearTimes(int n) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        if (n != 0) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + n);
        }
        return cal.getTimeInMillis();
    }

    /**
     * 获得n年今日的最后一秒日期
     *
     * @param n - N
     * @return long 毫秒
     */
    public static long getCurrDayLastYearTimes(int n) {
        long c = getCurrDayYearTimes(n);

        return c + Tools.DAY_MILLIS - 1;
    }

    /**
     * 获得n年今日前一天的凌晨时间
     *
     * @param n - N
     * @return long 毫秒
     */
    public static long getCurrDayAndTomorrowYearDayTimes(int n) {
        long c = getCurrDayYearTimes(n);

        return c + Tools.DAY_MILLIS;
    }

    /**
     * 获得n年今日前一年的前一天的凌晨时间
     *
     * @param n N
     * @return 毫秒
     */
    public static long getCurrDayAndTomorrowBeforeYearDayTimes(int n) {
        long c = getCurrDayYearTimes(n - 1);

        return c + Tools.DAY_MILLIS;
    }

    /**
     * 获得今年的第一天的时间
     *
     * @return long
     */
    public static long getThisYearTimes() {
        return getYearTimes(0);
    }

    /**
     * 获得去年的第一天的时间
     *
     * @return long
     */
    public static long getLastYearTimes() {
        return getYearTimes(-1);
    }

    /**
     * 获得今年的年份数字
     *
     * @return String
     */
    public static String getYear() {
        return Tools.getDate().substring(0, 4);
    }

    /**
     * 获得指定的年份数字
     *
     * @param i - 0是今年，1是明年，-1去年
     * @return String
     */
    public static String getYear(int i) {
        return Tools.getDate(getYearTimes(i)).substring(0, 4);
    }

    /**
     * 获得当前的月，如2016-06
     *
     * @return String
     */
    public static String getMonth() {
        return Tools.getDate().substring(0, 7);
    }

    /**
     * 获得指定的年份数字
     *
     * @param i - 0是当前月，1是下月，-1上月
     * @return String
     */
    public static String getMonth(int i) {
        return Tools.getDate(Tools.getMonthTime(-i)).substring(0, 7);
    }

    /**
     * 获得指定Date日期的月
     *
     * @param date - Date
     * @return String
     */
    public static String getMonth(Date date) {
        String d = Tools.getDate(date);
        if (Tools.isNull(d)) {
            return EMPTY_STRING;
        }
        return d.substring(0, 7);
    }

    /**
     * 获得指定时间的所属月份
     *
     * @param time - long
     * @return String
     */
    public static String getMonth(long time) {
        String d = Tools.getDate(time);
        if (Tools.isNull(d)) {
            return EMPTY_STRING;
        }
        return d.substring(0, 7);
    }

    /**
     * 获得当前的一个格式化的时间
     *
     * @param format - 格式化字符串
     * @return String
     */
    public static String getCurrentDate(String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(new Date());
    }

    /**
     * 格式化日期yyyy-MM-dd，返回Date
     *
     * @return long
     */
    public static Date getDate(String date) {
        if (Tools.isNull(date))
            return null;
        try {
            return getDateFormat().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得今天得日期 2011-12-12
     *
     * @return String
     */
    public static String getDate() {
        return getDateFormat().format(new Date());
    }

    /**
     * 获得今天得日期 2011-12-12
     *
     * @param time - 毫秒
     * @return String
     */
    public static String getDate(long time) {
        return getDateFormat().format(new Date(time));
    }

    /**
     * 获得今天得日期 2011-12-12
     *
     * @param date - 日期
     * @return String
     */
    public static String getDate(Date date) {
        if (date == null) {
            return EMPTY_STRING;
        }
        return getDateFormat().format(date);
    }

    /**
     * 将时间的STRING格式化一下
     *
     * @param date
     * @param format1
     * @param format2
     * @return
     */
    public static String getFormatString(String date, String format1, String format2) {
        long ss = getDateTimeMillis(date, format1);
        return getFormatDate(ss, format2);
    }

    /**
     * 获取一个格式化的时间
     *
     * @param time   long
     * @param format 格式化字符串
     * @return String
     */
    public static String getFormatDate(long time, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        Date date = new Date(time);
        return sf.format(date);
    }

    /**
     * 获取一个格式化的时间
     *
     * @param date   - Date
     * @param format 格式化字符串
     * @return String
     */
    public static String getFormatDate(Date date, String format) {
        if (date == null) {
            return EMPTY_STRING;
        }

        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

    /**
     * 获取一个格式化的时间
     *
     * @param format 格式化字符串
     * @return String
     */
    public static String getFormatDate(String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(new Date());
    }

    /**
     * 时间格式化，格式：2011-11-11 11:11:11
     *
     * @return String
     */
    public static String getDateTime() {
        return getDateTime(new Date());
    }

    /**
     * 时间格式化
     *
     * @param time - 时间
     * @return String yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime(long time) {
        return getDateTimeFormat().format(new Date(time));
    }

    /**
     * 时间格式化
     *
     * @param date - 时间毫秒数
     * @return String MM-dd
     */
    public static String getMonthDay(long date) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        return format.format(new Date(date));
    }

    /**
     * 时间格式化
     *
     * @param date - 时间2016-01-01 11:11:11
     * @return String MM-dd
     */
    public static String getMonthDay(String date) {
        if (date == null || date.length() < 10) {
            return EMPTY_STRING;
        }
        return date.substring(5, 10);
    }

    /**
     * 时间格式化
     *
     * @param date - 时间对象
     * @return String
     */
    public static String getDateTime(Date date) {
        if (date == null) {
            return EMPTY_STRING;
        }
        return getDateTimeFormat().format(date);
    }

    /**
     * 格式化日期 yyyy-MM-dd HH:mm:ss，返回Date
     *
     * @return long
     */
    public static Date getDateTime(String date) {
        if (Tools.isNull(date))
            return null;
        try {
            return getDateTimeFormat().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 逆向解析一个时间，返回一个long型的时间
     *
     * @param date   - 字符型时间
     * @param format - 格式化时间
     * @return long 型时间
     */
    public static long getDateTimeMillis(String date, String format) {
        if (isNull(date))
            return 0;

        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            return sf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 逆向解析一个时间，返回一个Date型的时间
     *
     * @param date   - 字符型时间
     * @param format - 格式化时间
     * @return Date
     */
    public static Date getDateTime(String date, String format) {
        if (isNull(date))
            return null;

        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            return sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得当前日期与本周一相差的天数
     *
     * @return int
     */
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return 6;
        } else {
            return dayOfWeek - 2;
        }
    }

    /**
     * 获得上周星期一的日期时间,返回long
     *
     * @return long
     */
    public static long getPreviousMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, -(mondayPlus + 7));
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        return currentDate.getTimeInMillis();
    }

    /**
     * 获得本周星期一的时间,返回long
     *
     * @return long
     */
    public static long getCurrnetMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, -mondayPlus);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        return currentDate.getTimeInMillis();
    }

    /**
     * 得到今天的凌晨的时间
     *
     * @return long
     */
    public static long getDayTime() {
        return getDayTime(0);
    }

    /**
     * 得到今天前几天的凌晨的时间
     *
     * @param dayBefore - 几天前
     * @return long - dayBefore天前凌晨的时间。
     */
    public static long getDayTime(long dayBefore) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis() - dayBefore * 24 * 60 * 60 * 1000);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 得到指定日期的凌晨的时间
     *
     * @param date - Date
     * @return long
     */
    public static long getDayTime(Date date) {
        if (date == null)
            throw new NullPointerException();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 获得当前小时的毫秒数
     *
     * @return long - 当前小时的毫秒数
     */
    public static long getHourTime() {
        return getHourTime(0);
    }

    /**
     * 获得当前V小时的毫秒数
     *
     * @param v - 当前几小时，可以为负数
     * @return long - 当前V小时的毫秒数
     */
    public static long getHourTime(long v) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis() + v * Tools.HOUR_MILLIS);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 按当前时间整取某个点的时间，如5分钟整取，5.4 取 5 5.6取 5.5
     *
     * @param m - 要整取的时间点，必须大于0
     * @return long
     */
    public static long getMinuteTime(int m) {
        return getMinuteTime(m, 0);
    }

    /**
     * 按当前时间整取某个点的时间，如5分钟整取，5.4 取 5 5.6取 5.5
     *
     * @param m           - 要整取的时间点，必须大于0
     * @param delayMinute 延迟分钟数，需要添加的时间，可以为负数，负数为过去的时间，正数为未来的时间
     * @return long
     */
    public static long getMinuteTime(int m, int delayMinute) {
        if (m <= 0)
            throw new RuntimeException("m can not lt zero;");

        Calendar cal = Calendar.getInstance();
        int min = cal.get(Calendar.MINUTE); // 当前的分钟
        cal.set(Calendar.MINUTE, min / m * m); // 取5整数
        cal.set(Calendar.SECOND, 0); // 设置秒数为0
        cal.set(Calendar.MILLISECOND, 0); // 设置毫秒数为0

        return cal.getTimeInMillis() + delayMinute * MINUTE_MILLIS;
    }

    /**
     * 获得本月的第一天的凌晨的时间
     *
     * @return long
     */
    public static long getMonthTime() {
        return getMonthTime(0);
    }

    /**
     * 获得下个月的第一天的凌晨的时间
     *
     * @return long
     */
    public static long getNextMonthTime() {
        return getMonthTime(-1);
    }

    /**
     * 获得本月之前的某月的第一天的凌晨的时间
     *
     * @param month - 0本月，-1下个月，1上个月
     * @return long
     */
    public static long getMonthTime(int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        int m = cal.get(Calendar.MONTH);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.MONTH, m - month);

        return cal.getTimeInMillis();
    }

    /**
     * 根据Date获得年龄
     *
     * @param birthday - 年龄
     * @return int
     */
    public static int getAge(Date birthday) {
        if (birthday == null)
            return 0;

        Calendar now = Calendar.getInstance();
        Calendar born = Calendar.getInstance();
        born.setTime(birthday);

        if (born.after(now)) {
            return 0;
        }

        int age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH), bornMonth = born.get(Calendar.MONTH);
        if (nowMonth <= bornMonth) {
            if (nowMonth < bornMonth) {
                age -= 1;
            } else if (nowMonth == bornMonth) {
                if (now.get(Calendar.DAY_OF_MONTH) < born.get(Calendar.DAY_OF_MONTH)) {
                    age -= 1;
                }
            }
        }

        return age;
    }

    /**
     * 根据Date获得多少天之后的日期
     *
     * @param date - 日期
     * @param day - 天
     * @return int
     */
    public static Date getdate(Date date, int day) {
        return new Date(date.getTime() + Tools.DAY_MILLIS * day);
    }

    /**
     * 匹配字符
     *
     * @param regex - 通配符
     * @param str   - 需要验证的字符
     * @return boolean 布尔值
     */
    public static boolean matches(String regex, String str) {
        if (str == null)
            return false;
        return Pattern.matches(regex, str);
    }

    /**
     * 查看某字符是否在数组中
     *
     * @param array - 数组
     * @param str   - 字符串
     * @return - Ture or False
     */
    public static boolean isInArray(String[] array, String str) {
        if (Tools.isNull(str))
            return false;
        if (array == null || array.length == 0)
            return false;
        for (String s : array) {
            if (s.equals(str))
                return true;
        }
        return false;
    }

    /**
     * 四舍五入返回一个小数
     *
     * @param d     - 要格式化的数字
     * @param scale - 返回小数的位数
     * @return double
     */
    public static double getDouble(double d, int scale) {
        if (d == 0)
            return 0;
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 四舍五入返回一个小数
     *
     * @param f     - 要格式化的数字
     * @param scale - 返回小数的位数
     * @return float
     */
    public static float getFloat(float f, int scale) {
        if (f == 0)
            return 0;
        BigDecimal bd = new BigDecimal(f);
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 四舍五入返回一个小数
     *
     * @param f
     * @param scale
     * @return
     */
    public static float getFloat(Float f, int scale) {
        if (f == null || f.floatValue() == 0)
            return 0;
        BigDecimal bd = new BigDecimal(f.floatValue());
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 字符串不能为空
     *
     * @param str - 字符串
     * @param def - 字符串为空显示的字符
     * @return 不为空的字符串
     */
    public static String formatString(String str, String def) {
        if (Tools.isNull(str))
            return def;
        return str;
    }

    /**
     * 字符串不能为空
     *
     * @param str - 字符串
     * @return 不为空的字符串
     */
    public static String formatString(String str) {
        return formatString(str, EMPTY_STRING);
    }

    /**
     * Integer不能为空
     *
     * @param i - Integer
     * @return Integer
     */
    public static Integer formatInteger(Integer i, Integer def) {
        if (i == null) {
            return def;
        }
        return i;
    }

    /**
     * Integer不能为空
     *
     * @param i - Integer
     * @return i或者0
     */
    public static Integer formatInteger(Integer i) {
        if (i == null) {
            return 0;
        }
        return i;
    }

    /**
     * Double不能为空
     *
     * @param d - Double
     * @return Double
     */
    public static Double formatDouble(Double d, Double def) {
        if (d == null)
            return def;
        return d;
    }

    /**
     * Double不能为空
     *
     * @param d - Double
     * @return d或者0
     */
    public static Double formatDouble(Double d) {
        if (d == null)
            return 0d;
        return d;
    }

    /**
     * Float不能为空
     *
     * @param f - Float
     * @return Float
     */
    public static Float formatFloat(Float f, Float def) {
        if (f == null)
            return def;
        return f;
    }

    /**
     * Float不能为空
     *
     * @param f - Float
     * @return f或者0
     */
    public static Float formatFloat(Float f) {
        if (f == null)
            return 0f;
        return f;
    }

    /**
     * Long不能为空
     *
     * @param l - Long
     * @return Long
     */
    public static Long formatLong(Long l, Long def) {
        if (l == null)
            return def;
        return l;
    }

    /**
     * Long不能为空
     *
     * @param l - Long
     * @return l或者0
     */
    public static Long formatLong(Long l) {
        if (l == null) return 0L;
        return l;
    }

    /**
     * Date不能为空
     *
     * @param d - Date
     * @return Date
     */
    public static Date formatDate(Date d, Date def) {
        if (d == null) return def;
        return d;
    }

    /**
     * Date不能为空
     *
     * @param d - Date
     * @return d或者当前时间
     */
    public static Date formatDate(Date d) {
        if (d == null) return new Date();
        return d;
    }

    /**
     * Object不能为空
     *
     * @param o - Object
     * @return Object
     */
    public static Object formatObject(Object o, Object def) {
        if (o == null)
            return def;
        return o;
    }

    /**
     * 如果字符串存在，则将c拼接并返回，否则返回空字串
     *
     * @param str - 字符串
     * @param c - 要拼接的字符串
     * @return String
     */
    public static String concatString(String str, String c) {
        if (isNull(str)) {
            return EMPTY_STRING;
        }
        return str + c;
    }

    /**
     * 返回一个字符串是否为空，如果字符串为""也为true，空格也为true
     *
     * @param str - String
     * @return true false
     */
    public static boolean isNull(String str) {
        if (str == null || str.trim().length() == 0)
            return true;
        return false;
    }

    /**
     * 如果字符串为空，则返回true，如果字符串为""也为true，空格也为true
     *
     * @param str - String
     * @param s1  - 额外的字符串
     * @return true false
     */
    public static boolean isNull(String str, String... s1) {
        if (str == null || str.trim().length() == 0)
            return true;

        if (s1 != null && s1.length > 0) {
            for (String s : s1) {
                if (s == null || s.trim().length() == 0)
                    return true;
            }
        }
        return false;
    }

    /**
     * 返回一个字符串是否不为空
     *
     * @param str - String
     * @return True or False
     */
    public static boolean isNotNull(String str) {
        if (str == null || str.trim().length() == 0)
            return false;
        return true;
    }

    /**
     * 返回一个字符串的长度
     *
     * @param str - String
     * @return int
     */
    public static int length(String str) {
        if (str == null) return 0;
        return str.length();
    }

    /**
     * 返回List是否为空
     *
     * @param collection - Collection
     * @return True or False
     */
    public static boolean isNull(Collection<?> collection) {
        if (collection == null || collection.isEmpty())
            return true;
        return false;
    }

    /**
     * 返回List是否不为空
     *
     * @param collection - Collection
     * @return True or False
     */
    public static boolean isNotNull(Collection<?> collection) {
        if (collection == null || collection.isEmpty())
            return false;
        return true;
    }

    /**
     * 返回集合的长度
     *
     * @param collection - Collection
     * @return
     */
    public static int length(Collection<?> collection) {
        if (collection == null) return 0;
        return collection.size();
    }

    /**
     * 返回Map是否为空
     *
     * @param map - Map
     * @return True or False
     */
    public static boolean isNull(Map<?, ?> map) {
        if (map == null || map.isEmpty())
            return true;
        return false;
    }

    /**
     * 返回Map是否不为空
     *
     * @param map - Map
     * @return True or False
     */
    public static boolean isNotNull(Map<?, ?> map) {
        if (map == null || map.isEmpty())
            return false;
        return true;
    }

    /**
     * 返回Map的长度
     *
     * @param map - Map
     * @return int
     */
    public static int length(Map<?, ?> map) {
        if (map == null) return 0;
        return map.size();
    }

    /**
     * 判断Object是否为空，注意，此处仅仅判断 null
     *
     * @param obj - Object
     * @return True or False
     */
    public static boolean isNull(Object obj) {
        if (obj == null)
            return true;
        return false;
    }

    /**
     * 判断Object是否不为空，注意，此处仅仅判断 null
     *
     * @param obj - Object
     * @return True or False
     */
    public static boolean isNotNull(Object obj) {
        if (obj == null)
            return false;
        return true;
    }

    /**
     * 返回Set是否不为空
     *
     * @param set - Set
     * @return True or False
     */
    public static boolean isNotNull(Set<?> set) {
        if (set == null || set.isEmpty())
            return false;
        return true;
    }

    /**
     * 返回Set的长度
     *
     * @param set - Set
     * @return int
     */
    public static int length(Set<?> set) {
        if (set == null) return 0;
        return set.size();
    }

    /**
     * 返回Set是否为空
     *
     * @param set - Set
     * @return True or False
     */
    public static boolean isNull(Set<?> set) {
        if (set == null || set.isEmpty())
            return true;
        return false;
    }

    /**
     * 判断数组是否为空
     *
     * @param array - Object[]
     * @return True or False
     */
    public static boolean isNull(Object[] array) {
        if (array == null || array.length == 0)
            return true;
        return false;
    }

    /**
     * 判断数组是否不为空
     *
     * @param array - Object[]
     * @return True or False
     */
    public static boolean isNotNull(Object[] array) {
        if (array == null || array.length == 0)
            return false;
        return true;
    }

    /**
     * 返回数组的长度
     *
     * @param array - Object[]
     * @return int
     */
    public static int length(Object[] array) {
        if (array == null) return 0;
        return array.length;
    }

    /**
     * 判断字符串是否为浮点型数字，包含负数
     *
     * @param str - String
     * @return true or false
     */
    public static boolean isDouble(String str) {
        if (str == null || str.length() == 0)
            return false;
        return Pattern.matches("^[-]?[\\d]+([.]?[\\d]*)$", str);
    }

    /**
     * 判断是否是整型数字，包括负数
     */
    private static final Pattern Pattern_Int = Pattern.compile("^[-]?[\\d]+$");

    /**
     * 判断字符串是否为整型数字，包括负数。
     *
     * @param str - String
     * @return True or False
     */
    public static boolean isInt(String str) {
        if (isNull(str))
            return false;
        Matcher m = Pattern_Int.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否是手机的正则表达式
     */
    private static final Pattern Pattern_Mobile = Pattern.compile("^1[0-9]{10}$");

    /**
     * 判断是否是手机号码
     *
     * @param str - String
     * @return true or false
     */
    public static boolean isMobile(String str) {
        if (str == null || str.length() != 11)
            return false;
        Matcher m = Pattern_Mobile.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否是座机
     *
     * @param str - String
     * @return True or False
     */
    public static boolean isTel(String str) {
        if (str == null)
            return false;

        return Pattern.matches("^(\\d{3,4}-)?\\d{7,9}$", str);
    }

    /**
     * 判断是否是电话[手机和座机都行]
     *
     * @param str - String
     * @return True or False
     */
    public static boolean isPhone(String str) {
        if (isNull(str))
            return false;

        if (isMobile(str) || isTel(str))
            return true;
        return false;
    }

    /**
     * 判断是否是QQ号码
     *
     * @param str - String
     * @return True or False
     */
    public static boolean isQQ(String str) {
        if (isNull(str))
            return false;

        return Pattern.matches("^([1-9]{1})([0-9]{4,13})$", str);
    }

    /**
     * 判断是否是微信号码
     *
     * @param str - String
     * @return True or False
     */
    public static boolean isWeixin(String str) {
        if (isNull(str))
            return false;

        return Pattern.matches("^([a-zA-Z]{1})([0-9A-Za-z_-]{5,19})$", str);
    }

    /**
     * 判断是否是钱的格式，如12或12.12
     *
     * @param str - String
     * @return True or False
     */
    public static boolean isMoney(String str) {
        if (str == null || str.length() == 0)
            return false;
        return Pattern.matches("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,2})?$", str);
    }

    /**
     * 判断是否是汉字的正则表达式
     */
    private static final Pattern Pattern_Chinese = Pattern.compile("[\\u4e00-\\u9fa5]+");

    /**
     * 判断是否是汉字
     *
     * @param ch - String
     * @return True or False
     */
    public static boolean isChinese(String ch) {
        if (ch == null || ch.length() == 0)
            return false;

        Matcher m = Pattern_Chinese.matcher(ch);
        return m.matches();
    }

    /**
     * 看一段字符串是否是字母
     *
     * @param str String 要判断的字符串
     * @return boolean 布尔值
     */
    public static boolean isEnglish(String str) {
        if (isNull(str))
            return false;
        return Pattern.matches("[a-zA-Z]*", str);
    }

    /**
     * 判断是否是数字的正则表达式
     */
    private static final Pattern Pattern_Number = Pattern.compile("\\d+");

    /**
     * 判断一段字符串是否是数字，不包括全角和负数。
     *
     * @param str - 字符串
     * @return boolean 布尔值
     */
    public static boolean isNumber(String str) {
        if (str == null)
            return false;
        Matcher m = Pattern_Number.matcher(str);
        return m.matches();
    }

    /**
     * 判断一段字符串是否是正整数且不含0
     *
     * @param str - 字符串
     * @return True or False
     */
    public static boolean isPositiveNumber(String str) {
        if (!isNumber(str))
            return false;

        return Integer.parseInt(str) > 0;
    }

    /**
     * 判断一段字符串是否是Email
     *
     * @param str String 字符串
     * @return boolean 布尔值
     */
    public static boolean isEmail(String str) {
        if (isNull(str))
            return false;
        return Pattern.matches(
                "^([a-zA-Z0-9_\\-]+\\.*)+@([a-zA-Z0-9_\\-]+\\.*)+(\\.(com|cn|mobi|co|net|so|org|gov|tel|tv|biz|cc|hk|name|info|asia|me|tw|nz|ru|fr|de|au|jp|kr)){1,2}+$",
                str);
    }

    /**
     * 判断是否是日期的正则表达式
     */
    private static final Pattern Pattern_Date = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");

    /**
     * 判断一段字符串是否是日期，2011-07-07
     *
     * @param str - 字符串
     * @return boolean 布尔值
     */
    public static boolean isDate(String str) {
        if (str == null)
            return false;
        Matcher m = Pattern_Date.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否是日期+时间的正则表达式，如2013-01-23 15:07:47
     */
    private static final Pattern Pattern_DateTime = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$");

    /**
     * 判断一段字符串是否是日期+时间，2011-07-07 12:12:12
     *
     * @param str - 字符串
     * @return boolean 布尔值
     */
    public static boolean isDateTime(String str) {
        if (str == null)
            return false;
        Matcher m = Pattern_DateTime.matcher(str);
        return m.matches();
    }

    /**
     * 数组转换成a,b,c格式的字符串
     *
     * @param array        - String[]
     * @param prefix       - 前缀字符
     * @param defaultValue - 如果数组为空，则默认返回此值
     * @return String
     */
    public static String joinString(Object[] array, String prefix, String defaultValue) {
        if (array == null || array.length == 0) {
            return defaultValue;
        }
        StringBuilder sb = new StringBuilder();

        //前缀长度
        int prefixLength;
        if (prefix != null) {
            prefixLength = prefix.length();
            sb.append(prefix);
        } else {
            prefixLength = 0;
        }

        for (Object obj : array) {
            if (sb.length() > prefixLength) {
                sb.append(",");
            }
            sb.append(obj);
        }
        return sb.toString();
    }

    /**
     * 数组转换成a,b,c格式的字符串
     *
     * @param array - String[]
     * @return String
     */
    public static String joinString(Object[] array) {
        return joinString(array, null, EMPTY_STRING);
    }

    /**
     * 列表转换成a,b,c格式的字符串
     *
     * @param list         - List<?>
     * @param prefix       - 前缀字符
     * @param defaultValue - 如果列表为空，则默认返回此值
     * @return String
     */
    public static String joinString(Collection<?> list, String prefix, String defaultValue) {
        if (list == null || list.isEmpty()) {
            return defaultValue;
        }
        StringBuilder sb = new StringBuilder();

        //前缀长度
        int prefixLength;
        if (prefix != null) {
            prefixLength = prefix.length();
            sb.append(prefix);
        } else {
            prefixLength = 0;
        }

        for (Object o : list) {
            if (sb.length() > prefixLength) {
                sb.append(",");
            }
            sb.append(o);
        }
        return sb.toString();
    }

    /**
     * 列表转换成a,b,c格式的字符串
     *
     * @param list - List<?>
     * @return String
     */
    public static String joinString(Collection<?> list) {
        return joinString(list, null, EMPTY_STRING);
    }

    /**
     * map的key转换成a,b,c格式的字符串
     *
     * @param map          - Map<String,Object>
     * @param defaultValue - 如果列表为空，则默认返回此值
     * @return String
     */
    public static String joinString(Map<String, Object> map, String defaultValue) {
        if (map == null || map.isEmpty())
            return defaultValue;
        Iterator<String> keyIt = map.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        int size = map.size();
        int i = 0;
        while (keyIt.hasNext()) {
            sb.append(keyIt.next());
            if (i < size - 1)
                sb.append(",");
        }
        return sb.toString();
    }

    /**
     * map的key转换成a,b,c格式的字符串
     *
     * @param map - Map<String,Object>
     * @return String
     */
    public static String joinString(Map<String, Object> map) {
        return joinString(map, EMPTY_STRING);
    }

    /**
     * 编码URL字符串
     *
     * @param str     - 字符串
     * @param charset - 编码
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String encoder(String str, String charset) throws UnsupportedEncodingException {
        if (isNull(str)) {
            return EMPTY_STRING;
        }
        return URLEncoder.encode(str, charset);
    }

    /**
     * 去掉字符串头尾的空格
     *
     * @param str - 要替换的字符串
     * @return String
     */
    public static String trim(String str) {
        if (str == null) {
            return EMPTY_STRING;
        }
        return str.trim();
    }

    /**
     * 去掉字符串头尾的字符
     *
     * @param str - 要替换的字符串
     * @param s   - 要去掉的头尾字符
     * @return String
     */
    public static String trim(String str, String s) {
        if (Tools.isNull(str)) {
            return EMPTY_STRING;
        }
        return str.replaceAll("^" + s + "+|" + s + "+$", EMPTY_STRING);
    }

    /**
     * 字符串转换成int型,默认值返回0
     *
     * @param s - String
     * @return int
     */
    public static int parseInt(String s) {
        return parseInt(s, 0);
    }

    /**
     * 字符串转换成int型
     *
     * @param s            - String
     * @param defaultValue - 如果转换错误，则返回defaultValue
     * @return int
     */
    public static int parseInt(String s, int defaultValue) {
        if (s == null || s.trim().length() == 0)
            return defaultValue;

        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 字符串转换long型,默认值返回0
     *
     * @param s - String
     * @return long
     */
    public static long parseLong(String s) {
        return parseLong(s, 0);
    }

    /**
     * 字符串转换成long型
     *
     * @param s            - String
     * @param defaultValue - 如果转换错误，则返回defaultValue
     * @return long
     */
    public static long parseLong(String s, long defaultValue) {
        if (s == null || s.trim().length() == 0)
            return defaultValue;

        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 字符串转换long型,默认值返回0
     *
     * @param s - Object
     * @return long
     */
    public static long parseLong(Object s) {
        if (s == null)
            return 0;

        try {
            return Long.parseLong(s.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 字符串抓换成float型，默认值返回0
     *
     * @param s - String
     * @return float
     */
    public static float parseFloat(String s) {
        return parseFloat(s, 0f);
    }

    /**
     * 字符串抓换成float型
     *
     * @param s            - String
     * @param defaultValue - 如果转换错误，则返回defaultValue
     * @return float
     */
    public static float parseFloat(String s, float defaultValue) {
        if (s == null || s.trim().length() == 0)
            return defaultValue;

        try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 字符串转换成double型，默认值返回0
     *
     * @param s - String
     * @return double
     */
    public static double parseDouble(String s) {
        return parseDouble(s, 0d);
    }

    /**
     * 字符串抓换成double型
     *
     * @param s            - String
     * @param defaultValue - 如果转换错误，则返回defaultValue
     * @return double
     */
    public static double parseDouble(String s, double defaultValue) {
        if (s == null || s.trim().length() == 0)
            return defaultValue;

        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 字符串转换成boolean型，默认值返回false
     *
     * @param s - String
     * @return boolean
     */
    public static boolean parseBoolean(String s) {
        return parseBoolean(s, false);
    }

    /**
     * 字符串抓换成boolean型
     *
     * @param s            - String
     * @param defaultValue - 如果转换错误，则返回defaultValue
     * @return boolean
     */
    public static boolean parseBoolean(String s, boolean defaultValue) {
        if (s == null || s.trim().length() == 0)
            return defaultValue;

        try {
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 将Object类型转换成long
     *
     * @param obj - Object
     * @return long
     */
    public static long getLong(Object obj) {
        if (obj == null || obj.toString().trim().length() == 0)
            return 0;
        return Long.parseLong(obj.toString());
    }

    /**
     * 将Object类型转换成int
     *
     * @param obj - Object
     * @return int
     */
    public static int getInt(Object obj) {
        if (obj == null || obj.toString().trim().length() == 0)
            return 0;

        return Integer.parseInt(obj.toString());
    }

    /**
     * Long转换成long
     *
     * @param l            - Long
     * @param defaultValue - 默认显示值
     * @return long
     */
    public static long longValue(Long l, long defaultValue) {
        if (l == null)
            return defaultValue;
        return l.longValue();
    }

    /**
     * Long转换成long，如果为null，则显示0
     *
     * @param l - Long
     * @return long
     */
    public static long longValue(Long l) {
        if (l == null)
            return 0;
        return l.longValue();
    }

    /**
     * number转换成long，如果为null，则显示0
     *
     * @param number - Number
     * @return long
     */
    public static long longValue(Number number) {
        if (number == null)
            return 0;
        return number.longValue();
    }

    /**
     * number转换成long，如果为null，则显示0
     *
     * @param number - Number
     * @return long
     */
    public static long longValue(Object number) {
        if (number == null) {
            return 0;
        } else if (number instanceof Number) {
            return ((Number) number).longValue();
        } else if (number instanceof String) {
            return Tools.parseLong(number.toString(), 0L);
        }

        return 0;
    }

    /**
     * 获得一个正整数long，如果i为负数，则为0
     *
     * @param i - long
     * @return long
     */
    public static long longPositiveValue(long i) {
        if (i < 0)
            return 0;
        return i;
    }

    /**
     * Integer转换成int
     *
     * @param i            - Integer
     * @param defaultValue - 默认显示值
     * @return int
     */
    public static int intValue(Integer i, int defaultValue) {
        if (i == null)
            return defaultValue;
        return i.intValue();
    }

    /**
     * Integer转换成int，如果为null，则显示0
     *
     * @param i - Integer
     * @return int
     */
    public static int intValue(Integer i) {
        if (i == null)
            return 0;
        return i.intValue();
    }

    /**
     * Number转换成int，如果为null，则显示0
     *
     * @param number - Number
     * @return int
     */
    public static int intValue(Number number) {
        if (number == null)
            return 0;
        return number.intValue();
    }

    /**
     * Number转换成int，如果为null，则显示0
     *
     * @param number - Object
     * @return int
     */
    public static int intValue(Object number) {
        if (number == null) {
            return 0;
        } else if (number instanceof Number) {
            return ((Number) number).intValue();
        } else if (number instanceof String) {
            return Tools.parseInt(number.toString(), 0);
        }
        return 0;
    }

    /**
     * 获得一个正整数int，如果i为负数，则为0
     *
     * @param i - int
     * @return int
     */
    public static int intPositiveValue(int i) {
        if (i < 0)
            return 0;
        return i;
    }

    /**
     * Float转换成float
     *
     * @param f            - Float
     * @param defaultValue - 默认显示值
     * @return float
     */
    public static float floatValue(Float f, float defaultValue) {
        if (f == null)
            return defaultValue;
        return f.floatValue();
    }

    /**
     * Float转换成float，如果为null，则显示0
     *
     * @param f - Float
     * @return float
     */
    public static float floatValue(Float f) {
        if (f == null)
            return 0f;
        return f.floatValue();
    }

    /**
     * 获得一个正整数float，如果i为负数，则为0
     *
     * @param i - float
     * @return float
     */
    public static float floatPositiveValue(float i) {
        if (i < 0)
            return 0f;
        return i;
    }

    /**
     * Double转换成double
     *
     * @param d            - Double
     * @param defaultValue - 默认显示值
     * @return double
     */
    public static double doubleValue(Double d, double defaultValue) {
        if (d == null)
            return defaultValue;
        return d.doubleValue();
    }

    /**
     * Double转换成double，如果为null，则显示0
     *
     * @param d - Double
     * @return double
     */
    public static double doubleValue(Double d) {
        if (d == null)
            return 0d;
        return d.doubleValue();
    }

    /**
     * Number转换成double，如果为null，则显示0
     *
     * @param number - Number
     * @return double
     */
    public static double doubleValue(Number number) {
        if (number == null)
            return 0;
        return number.doubleValue();
    }

    /**
     * Number转换成double，如果为null，则显示0
     *
     * @param number - Object
     * @return double
     */
    public static double doubleValue(Object number) {
        if (number == null || !(number instanceof Number))
            return 0;
        return ((Number) number).doubleValue();
    }

    /**
     * 获得一个正整数double，如果i为负数，则为0
     *
     * @param i - double
     * @return double
     */
    public static double doublePositiveValue(double i) {
        if (i < 0)
            return 0d;
        return i;
    }

    /**
     * Boolean转换成Boolean
     *
     * @param b            - Boolean
     * @param defaultValue - 默认显示值
     * @return boolean
     */
    public static boolean booleanValue(Boolean b, boolean defaultValue) {
        if (b == null)
            return defaultValue;
        return b.booleanValue();
    }

    /**
     * Boolean转换成Boolean，如果为null，则显示false
     *
     * @param b - Boolean
     * @return boolean
     */
    public static boolean booleanValue(Boolean b) {
        if (b == null)
            return false;
        return b.booleanValue();
    }

    /**
     * Date转换成long型
     *
     * @param date - 时间
     * @return long
     */
    public static long dateValue(Date date) {
        if (date == null) return 0;
        return date.getTime();
    }


    /**
     * long型转化为Date
     *
     * @param timeMills - 毫秒
     * @return Date
     */
    public static Date dateValue(long timeMills) {
        if (timeMills <= 0) return null;
        return new Date(timeMills);
    }

    /**
     * 首字母小写
     *
     * @param s - String
     * @return String
     */
    public static String uncapFirst(String s) {
        if (s == null || s.length() == 0) {
            return EMPTY_STRING;
        }

        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * 首字母大写
     *
     * @param s - String
     * @return String
     */
    public static String capFirst(String s) {
        if (s == null || s.length() == 0) {
            return EMPTY_STRING;
        }

        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * int 转换成 byte 4位数组
     *
     * @param i - int
     * @return byte[]
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * 4位byte数组转换成int
     *
     * @param b - byte[]
     * @return int
     */
    public static int byteArrayToInt(byte[] b) {
        if (b == null || b.length != 4)
            return 0;

        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }

    /**
     * 当前日期与给定日期比较
     *
     * @param time1 开始日期
     * @param time2 结束日期
     * @return
     */
    public static boolean compareTime(String time1, String time2) {
        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date now = c.getTime();
        boolean b = false;
        try {
            Date startDate = df.parse(time1);
            Date endDate = df.parse(time2);
            if (now.compareTo(startDate) >= 0 && now.compareTo(endDate) <= 0) {
                b = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
    }

    /**
     * 当前时间是否在指定的时间范围内
     *
     * @param beginDate - 开始时间
     * @param endDate   - 结束时间[不含]
     * @return True or False
     */
    public static boolean betweenTime(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null)
            return false;

        long currTime = System.currentTimeMillis();
        if (beginDate.getTime() <= currTime && endDate.getTime() > currTime)
            return true;
        return false;
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s - 要分割的字符串
     * @return String[]
     */
    public static String[] split(String s) {
        return split(s, COMMA_STRING);
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s - 要分割的字符串
     * @return Long[]
     */
    public static Long[] splitToLong(String s) {
        return splitToLong(s, COMMA_STRING);
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s - 要分割的字符串
     * @return Long[]
     */
    public static Integer[] splitToInteger(String s) {
        return splitToInteger(s, COMMA_STRING);
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s              - 要分割的字符串
     * @param separatorChars - 指定分割符
     * @return String[]
     */
    public static String[] split(String s, String separatorChars) {
        return org.apache.commons.lang3.StringUtils.split(s, separatorChars);
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s              - 要分割的字符串
     * @param separatorChars - 指定分割符
     * @return String[]
     */
    public static Long[] splitToLong(String s, String separatorChars) {
        String[] arr = org.apache.commons.lang3.StringUtils.split(s, separatorChars);
        if (arr == null || arr.length == 0) {
            return null;
        }

        Long[] l = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (Tools.isNumber(arr[i])) {
                l[i] = new Long(arr[i]);
            }
        }

        return l;
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s              - 要分割的字符串
     * @param separatorChars - 指定分割符
     * @return String[]
     */
    public static Integer[] splitToInteger(String s, String separatorChars) {
        String[] arr = org.apache.commons.lang3.StringUtils.split(s, separatorChars);
        if (arr == null || arr.length == 0) {
            return null;
        }

        Integer[] l = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (Tools.isNumber(arr[i])) {
                l[i] = new Integer(arr[i]);
            }
        }

        return l;
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s - 要分割的字符串
     * @return Set<Integer>
     */
    public static Set<Long> splitToLongSet(String s) {
        return splitToLongSet(s, COMMA_STRING);
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s              - 要分割的字符串
     * @param separatorChars - 指定分割符
     * @return Set<Integer>
     */
    public static Set<Long> splitToLongSet(String s, String separatorChars) {
        String[] arr = org.apache.commons.lang3.StringUtils.split(s, separatorChars);
        if (arr == null || arr.length == 0) {
            return Collections.emptySet();
        }

        Set<Long> set = new HashSet<>();
        for (int i = 0; i < arr.length; i++) {
            if (Tools.isNumber(arr[i])) {
                set.add(new Long(arr[i]));
            }
        }

        return set;
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s - 要分割的字符串
     * @return Set<Integer>
     */
    public static Set<Integer> splitToIntegerSet(String s) {
        return splitToIntegerSet(s, COMMA_STRING);
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s              - 要分割的字符串
     * @param separatorChars - 指定分割符
     * @return Set<Integer>
     */
    public static Set<Integer> splitToIntegerSet(String s, String separatorChars) {
        String[] arr = org.apache.commons.lang3.StringUtils.split(s, separatorChars);
        if (arr == null || arr.length == 0) {
            return Collections.emptySet();
        }

        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < arr.length; i++) {
            if (Tools.isNumber(arr[i])) {
                set.add(new Integer(arr[i]));
            }
        }

        return set;
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s - 要分割的字符串
     * @return List<Integer>
     */
    public static List<Long> splitToLongList(String s) {
        return splitToLongList(s, COMMA_STRING);
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s              - 要分割的字符串
     * @param separatorChars - 指定分割符
     * @return List<Integer>
     */
    public static List<Long> splitToLongList(String s, String separatorChars) {
        String[] arr = org.apache.commons.lang3.StringUtils.split(s, separatorChars);
        if (arr == null || arr.length == 0) {
            return Collections.emptyList();
        }

        List<Long> list = new ArrayList<>(arr.length);
        for (int i = 0; i < arr.length; i++) {
            if (Tools.isNumber(arr[i])) {
                list.add(new Long(arr[i]));
            }
        }

        return list;
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s - 要分割的字符串
     * @return List<Integer>
     */
    public static List<Integer> splitToIntegerList(String s) {
        return splitToIntegerList(s, COMMA_STRING);
    }

    /**
     * 利用apache的StringUtils.split
     *
     * @param s              - 要分割的字符串
     * @param separatorChars - 指定分割符
     * @return List<Integer>
     */
    public static List<Integer> splitToIntegerList(String s, String separatorChars) {
        String[] arr = org.apache.commons.lang3.StringUtils.split(s, separatorChars);
        if (arr == null || arr.length == 0) {
            return Collections.emptyList();
        }

        List<Integer> list = new ArrayList<>(arr.length);
        for (int i = 0; i < arr.length; i++) {
            if (Tools.isNumber(arr[i])) {
                list.add(new Integer(arr[i]));
            }
        }

        return list;
    }

    /**
     * 过滤XSS攻击代码
     *
     * @param s - String
     * @return String
     */
    public static String cleanXSS(String s) {
        if (s == null || s.length() == 0) {
            return EMPTY_STRING;
        }
        if (s.trim().length() == 0)
            return s;

        s = s.replace("<", EMPTY_STRING).replace(">", EMPTY_STRING);
        s = s.replaceAll("eval\\((.*)\\)", EMPTY_STRING);
        s = s.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        return s;
    }

    /**
     * 去掉串中的html标签
     *
     * @param str - 要替换的字符
     * @return String
     */
    public static String clearHTML(String str) {
        if (Tools.isNull(str)) {
            return EMPTY_STRING;
        }
        String x = str.replaceAll("<.+?>", EMPTY_STRING);
        x = x.replaceAll("\"", EMPTY_STRING);
        x = x.replaceAll("'", EMPTY_STRING);
        x = x.replaceAll("<", EMPTY_STRING);
        x = x.replaceAll(">", EMPTY_STRING);
        x = x.replaceAll("\\&.+?;", EMPTY_STRING);
        return x;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 如果字符串不是数字则返回空，否则返回本身
     *
     * @param s - String
     * @return String
     */
    public static String toNumberOrNull(String s) {
        if (!Tools.isNumber(s))
            return null;
        return s;
    }

    /**
     * 如果字符串不是""或者Null，则返回空，否则返回本身
     *
     * @param s - String
     * @return String
     */
    public static String toStringOrNull(String s) {
        if (Tools.isNull(s))
            return null;
        return s;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    private static ThreadLocal<DecimalFormat> NUMBER_FORMAT_LOCAL = new ThreadLocal<DecimalFormat>();

    // 3位逗号隔开
    private static ThreadLocal<DecimalFormat> MONEY_FORMAT_LOCAL = new ThreadLocal<DecimalFormat>();

    /**
     * 获取DecimalFormat，保留两位小数
     *
     * @return DecimalFormat
     */
    public static DecimalFormat getDecimalFormat() {
        DecimalFormat df = NUMBER_FORMAT_LOCAL.get();
        if (df == null) {
            df = new DecimalFormat("#0.00");
            NUMBER_FORMAT_LOCAL.set(df);
        }
        return df;
    }

    /**
     * 获取DecimalFormat，保留两位小数
     *
     * @return DecimalFormat
     */
    public static DecimalFormat getMoneyFormat() {
        DecimalFormat df = MONEY_FORMAT_LOCAL.get();
        if (df == null) {
            df = new DecimalFormat("#,##0.00");
            MONEY_FORMAT_LOCAL.set(df);
        }
        return df;
    }

    /**
     * 格式化金钱，每3位逗号间隔
     *
     * @param f - float
     * @return String
     */
    public static String moneyToSepara(float f, String symbol) {
        if (symbol == null)
            symbol = "￥";

        if (f == 0)
            return symbol + "0.00";
        DecimalFormat df = getMoneyFormat();
        return symbol + df.format(f);
    }

    /**
     * 格式化金钱，每3位逗号间隔
     *
     * @param d      - double
     * @param symbol - 符号
     * @return String
     */
    public static String moneyToSepara(double d, String symbol) {
        if (symbol == null)
            symbol = "￥";

        if (d == 0)
            return symbol + "0.00";
        DecimalFormat df = getMoneyFormat();
        return symbol + df.format(d);
    }

    /**
     * 获取两位Float
     *
     * @param number - 数字
     * @return String
     */
    public static String getFloat2(float number) {
        DecimalFormat df = getDecimalFormat();
        return df.format(number);
    }

    /**
     * 获取两位Float
     *
     * @param number - 数字
     * @param zero   - 如果小数位数为0，是否显示小数位。
     * @return String
     */
    public static String getFloat2(float number, boolean zero) {
        String r = getFloat2(number);
        if (!zero) {
            int indexOf = -1;
            if (r != null && (indexOf = r.lastIndexOf(".")) > -1) {
                String r0 = r.substring(indexOf);

                int loc = r.length();
                for (int i = r0.length() - 1; i >= 0; i--) {
                    if (r0.charAt(i) == '0' || r0.charAt(i) == '.') {
                        loc--;
                    } else {
                        break;
                    }
                }
                return r.substring(0, loc);
            }
        }

        return r;
    }

    /**
     * 获取两个Double
     *
     * @param number - 数字
     * @return String
     */
    public static String getDouble2(double number) {
        DecimalFormat df = getDecimalFormat();
        return df.format(number);
    }

    /**
     * 获取两位Float
     *
     * @param number - 数字
     * @param zero   - 如果小数位数为0，是否显示小数位。
     * @return String
     */
    public static String getDouble2(double number, boolean zero) {
        String r = getDouble2(number);
        if (!zero) {
            int indexOf = -1;
            if (r != null && (indexOf = r.lastIndexOf(".")) > -1) {
                String r0 = r.substring(indexOf);

                int loc = r.length();
                for (int i = r0.length() - 1; i >= 0; i--) {
                    if (r0.charAt(i) == '0' || r0.charAt(i) == '.') {
                        loc--;
                    } else {
                        break;
                    }
                }
                return r.substring(0, loc);
            }
        }

        return r;
    }

    /**
     * 格式化Double类型的数据
     *
     * @param d - double
     * @return double
     */
    public static double formatDouble2(double d) {
        BigDecimal b = new BigDecimal(d);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将long/100并且格式化数据
     *
     * @param d - long
     * @return double
     */
    public static double formatDouble2Percent(long d) {
        if (d == 0) return 0d;

        BigDecimal b = new BigDecimal((double) d / 100);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将long/100并且格式化数据，显示两位小数
     *
     * @param d - long
     * @return String
     */
    public static String formatDouble2PercentToString(long d) {
        double v = formatDouble2Percent(d);

        return getDouble2(v);
    }

    /**
     * 将int/100并且格式化数据
     *
     * @param d - long
     * @return double
     */
    public static double formatDouble2Percent(int d) {
        if (d == 0) return 0d;

        BigDecimal b = new BigDecimal((double) d / 100);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 将long/100并且格式化数据，显示两位小数
     *
     * @param d - long
     * @return String
     */
    public static String formatDouble2PercentToString(int d) {
        double v = formatDouble2Percent(d);

        return getDouble2(v);
    }

    /**
     * 格式化double类型的字段，保存两位小数
     *
     * @param o - 需要格式化属性的对象
     */
    public static void formatDoubleField(Object o) {
        Map<String, Double> fieldmap = new HashMap<String, Double>();
        Map<String, Double> getfieldmap = new HashMap<String, Double>();
        Map<String, Double> setfieldmap = new HashMap<String, Double>();
        for (Field field : o.getClass().getDeclaredFields()) {
            String fileType = field.getType().toString().toLowerCase();
            if (fileType.equals("double")) {
                try {
                    getfieldmap.put("get" + field.getName().toLowerCase(), 0D);
                    setfieldmap.put("set" + field.getName().toLowerCase(), 0D);
                    fieldmap.put(field.getName().toLowerCase(), 0D);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        Object[] obj = null;
        for (Method m : o.getClass().getDeclaredMethods()) {
            if (getfieldmap.get(m.getName().toString().toLowerCase()) != null) {
                Object tmp = null;
                try {
                    tmp = m.invoke(o, obj);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                double val = Double.parseDouble(tmp.toString());
                setfieldmap.put("set" + m.getName().substring(3).toLowerCase(), val);
                getfieldmap.put("get" + m.getName().substring(3).toLowerCase(), val);
                fieldmap.put(m.getName().substring(3).toLowerCase(), val);
            }
        }

        for (Method m : o.getClass().getDeclaredMethods()) {
            if (setfieldmap.get(m.getName().toString().toLowerCase()) != null) {
                BigDecimal b = new BigDecimal(setfieldmap.get(m.getName().toString().toLowerCase()));
                double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                try {
                    m.invoke(o, f1);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 日期天数增加
     *
     * @param date 日期实例
     * @param days 增加的天数
     * @return 增加后的日期实例
     */
    public static Date addDay(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * 获取昨天的日期
     *
     * @return
     */
    public static String getYesterday() {
        return getDate(addDay(new Date(), -1));
    }

    /**
     * 5分钟整取，例如--5.4 取 5 5.6取 5.5 类似，然后再加上15分钟，代表未来15分钟对应的数据
     *
     * @param delayMinute 延迟分钟数
     * @return 未来时间，格式："yyyy_MM_dd_HH_mm"
     */
    public static String getMinuteDate(int delayMinute) {
        Calendar cal = Calendar.getInstance();
        int min = cal.get(Calendar.MINUTE); // 当前的分钟
        cal.set(Calendar.MINUTE, min / 5 * 5); // 取5整数
        cal.set(Calendar.SECOND, 0); // 设置秒数为0

        Date afterDate = new Date(cal.getTime().getTime() + delayMinute * MINUTE_MILLIS);
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
        return format.format(afterDate);
    }

    /**
     * 去除对象的字符属性左右空格
     *
     * @param o - Object
     */
    public static void trimStringField(Object o) {
        Map<String, String> fieldmap = new HashMap<String, String>();
        Map<String, String> getfieldmap = new HashMap<String, String>();
        Map<String, String> setfieldmap = new HashMap<String, String>();
        for (Field field : o.getClass().getDeclaredFields()) {
            if (field.getType().getSimpleName().toString().toLowerCase().equals("string")) {
                try {
                    getfieldmap.put("get" + field.getName().toLowerCase(), EMPTY_STRING);
                    setfieldmap.put("set" + field.getName().toLowerCase(), EMPTY_STRING);
                    fieldmap.put(field.getName().toLowerCase(), EMPTY_STRING);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Method m : o.getClass().getDeclaredMethods()) {
            if (getfieldmap.get(m.getName().toString().toLowerCase()) != null) {
                Object tmp = null;
                try {
                    tmp = m.invoke(o);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                String val = null;
                if (tmp != null) {
                    val = tmp.toString();
                }
                setfieldmap.put("set" + m.getName().substring(3).toLowerCase(), val);
                getfieldmap.put("get" + m.getName().substring(3).toLowerCase(), val);
                fieldmap.put(m.getName().substring(3).toLowerCase(), val);
            }
        }

        for (Method m : o.getClass().getDeclaredMethods()) {
            if (setfieldmap.get(m.getName().toString().toLowerCase()) != null) {
                String f1 = setfieldmap.get(m.getName().toString().toLowerCase());
                if (f1 != null)
                    f1 = f1.trim();
                try {
                    m.invoke(o, f1);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 随机指定范围内N个不重复的数 利用HashSet的特征，只能存放不同的值
     *
     * @param min              指定范围最小值
     * @param max              指定范围最大值
     * @param n                随机数个数
     * @param set set 随机数结果集
     * @throws Exception
     */
    public static void randomSet(int min, int max, int n, HashSet<Integer> set) throws Exception {
        if (n > (max - min + 1) || max < min) {
            throw new Exception("随机参数错误！");
        }
        for (int i = 0; i < n; i++) {
            // 调用Math.random()方法
            int num = (int) (Math.random() * (max - min + 1)) + min;
            set.add(num);// 将不同的数存入HashSet中
        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (setSize < n) {
            randomSet(min, max, n, set);// 递归
        }
    }

    /**
     * 得到今天后几天的凌晨的时间
     *
     * @param dayAfter - 几天后
     * @return long - dayAfter天后凌晨的时间。
     */
    public static long getNextDayTime(long dayAfter) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis() + dayAfter * 24 * 60 * 60 * 1000);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 将时分秒（01:09:35）转换成毫秒
     *
     * @param time - Time
     * @return long
     */
    public static long getTotalMills(Time time) {
        return getTotalMills(time.toString());
    }

    /**
     * 将时分秒（01:09:35）转换成毫秒
     *
     * @param time - String
     * @return long
     */
    public static long getTotalMills(String time) {
        String[] my = time.split(":");
        int hour = Integer.parseInt(my[0]);
        int min = Integer.parseInt(my[1]);
        int sec = Integer.parseInt(my[2]);

        return hour * HOUR_MILLIS + min * MINUTE_MILLIS + sec * 1000;
    }

    /**
     * 将浮点小数转换成百分数，例如：1.2355--》123.55%
     *
     * @param decimal
     * @return
     */
    public static String getDecimalPercent(double decimal) {
        NumberFormat num = NumberFormat.getPercentInstance();
        num.setMaximumIntegerDigits(3);
        num.setMaximumFractionDigits(2);
        return num.format(decimal);
    }

    public static void main(String[] args) {
        System.err.println(Tools.getCurrDayAndTomorrowBeforeYearDayTimes(1));
        System.err.println(Tools.getDateTime(Tools.getCurrDayAndTomorrowBeforeYearDayTimes(1)));
    }

}
