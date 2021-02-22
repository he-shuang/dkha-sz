

package com.dkha.commons.tools.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 日期处理工具类
 * @since 1.0.0
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @return  返回yyyy-MM-dd格式日期
     */
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @param pattern  格式，如：DateUtils.DATE_TIME_PATTERN
     * @return  返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    /**
     * 日期解析
     * @param date  日期
     * @param pattern  格式，如：DateUtils.DATE_TIME_PATTERN
     * @return  返回Date
     */
    public static Date parse(String date, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转换成日期
     * @param strDate 日期字符串
     * @param pattern 日期的格式，如：DateUtils.DATE_TIME_PATTERN
     */
    public static Date stringToDate(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)){
            return null;
        }

        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        return fmt.parseLocalDateTime(strDate).toDate();
    }

    /**
     * 根据周数，获取开始日期、结束日期
     * @param week  周期  0本周，-1上周，-2上上周，1下周，2下下周
     * @return  返回date[0]开始日期、date[1]结束日期
     */
    public static Date[] getWeekStartAndEnd(int week) {
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusWeeks(week));

        date = date.dayOfWeek().withMinimumValue();
        Date beginDate = date.toDate();
        Date endDate = date.plusDays(6).toDate();
        return new Date[]{beginDate, endDate};
    }

    /**
     * 秒转换为时间
     * @param second
     * @return
     */
    public static Date secondToDate(long second) {
        Calendar calendar = Calendar.getInstance();
        //转换为毫秒
        calendar.setTimeInMillis(second * 1000);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 对日期的【秒】进行加/减
     *
     * @param date 日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分钟】进行加/减
     *
     * @param date 日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     *
     * @param date 日期
     * @param hours 小时数，负数为减
     * @return 加/减几小时后的日期
     */
    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    /**
     * 对日期的【天】进行加/减
     *
     * @param date 日期
     * @param days 天数，负数为减
     * @return 加/减几天后的日期
     */
    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    /**
     * 对日期的【周】进行加/减
     *
     * @param date 日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     *
     * @param date 日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    /**
     * 对日期的【年】进行加/减
     *
     * @param date 日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

    // 06. java.time.LocalTime --> java.util.Date
    public static java.util.Date LocalTimeToUdate(LocalTime localTime) {
        java.time.LocalDate localDate = java.time.LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        java.util.Date date = Date.from(instant);
        return  date;
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param LocalstartTime 开始时间
     * @param LocalendTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, LocalTime LocalstartTime, LocalTime LocalendTime) {
        Date startTime=LocalTimeToUdate(LocalstartTime);
        Date endTime=LocalTimeToUdate(LocalendTime);
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     *  获取某一段时间中包含的月份
     * @param dBegin 开始时间
     * @param dEnd  结束时间
     * @return
     */
  public static List<String> getRangeMonths( String dBegin , String dEnd){
      List<String> list = new ArrayList<String>();
      try {
          Date startDate = new SimpleDateFormat("yyyy-MM").parse(dBegin);
          Date endDate = new SimpleDateFormat("yyyy-MM").parse(dEnd);
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(startDate);
          // 获取开始年份和开始月份
          int startYear = calendar.get(Calendar.YEAR);
          int startMonth = calendar.get(Calendar.MONTH);
          // 获取结束年份和结束月份
          calendar.setTime(endDate);
          int endYear = calendar.get(Calendar.YEAR);
          int endMonth = calendar.get(Calendar.MONTH);
          for (int i = startYear; i <= endYear; i++) {
              String date = "";
              if (startYear == endYear) {
                  for (int j = startMonth; j <= endMonth; j++) {
                      if (j < 9) {
                          date = i + "年0" + (j + 1)+"月";
                      } else {
                          date = i + "年" + (j + 1)+"月";
                      }
                      list.add(date);
                  }
              } else {
                  if (i == startYear) {
                      for (int j = startMonth; j < 12; j++) {
                          if (j < 9) {
                              date = i + "年0" + (j + 1)+"月";
                          } else {
                              date = i + "年" + (j + 1)+"月";
                          }
                          list.add(date);
                      }
                  } else if (i == endYear) {
                      for (int j = 0; j <= endMonth; j++) {
                          if (j < 9) {
                              date = i + "年0" + (j + 1)+"月";
                          } else {
                              date = i + "年" + (j + 1)+"月";
                          }
                          list.add(date);
                      }
                  } else {
                      for (int j = 0; j < 12; j++) {
                          if (j < 9) {
                              date = i + "年0" + (j + 1)+"月";
                          } else {
                              date = i + "年" + (j + 1)+"月";
                          }
                          list.add(date);
                      }
                  }
              }
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      return list;
  }

    /**
     * 获取两个日期之间的所有日期(字符串格式, 按天计算)
     *
     * @param startTime String 开始时间 yyyy-MM-dd
     * @param endTime  String 结束时间 yyyy-MM-dd
     * @return
     */
    public static List<String> getBetweenDays(String startTime, String endTime) throws ParseException {
        if(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)){
            return null;
        }
        //1、定义转换格式
        SimpleDateFormat df  = new SimpleDateFormat("yyyy-MM-dd");

        Date start = df.parse(startTime);
        Date end = df.parse(endTime);
        if(start == null || end == null){
            return null;
        }
        List<String> result = new ArrayList<String>();

        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);

        tempStart.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        result.add(sdf.format(start));
        while (tempStart.before(tempEnd)) {
            result.add(sdf.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

    /**
     *  计算一个日在年终某一周
     * @param dateStr
     * @return
     */
    public static Map<String,Object> week(String dateStr){
        Map<String,Object> map = new HashMap<>();
        java.time.LocalDate localDate = java.time.LocalDate.parse(dateStr);
        java.time.LocalDate localDate1 = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        java.time.LocalDate weekStart = localDate1.with(TemporalAdjusters.previousOrSame( DayOfWeek.MONDAY));
        java.time.LocalDate weekEnd = localDate1.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY ));

        int week = localDate1.get ( IsoFields.WEEK_OF_WEEK_BASED_YEAR );
        String weekStr = week > 9 ? String.valueOf(week) : "0" + week;
        int weekYear = localDate1.get ( IsoFields.WEEK_BASED_YEAR );
        map.put("week",weekYear+"年第"+weekStr+"周");
        map.put("weekStart",weekStart);
        map.put("weekEnd",weekEnd);
        return map;
    }

    /**
     * 根据时间段 获取包含所有周列表
     * @param startTime
     * @param endTime
     * @return
     */
    public static LinkedHashSet<Map<String,Object>> weeks(String startTime, String endTime) {
        LinkedHashSet<Map<String,Object>> objects = new LinkedHashSet<>();
        try {
            List<String> betweenDays = getBetweenDays(startTime, endTime);
            if(betweenDays != null && betweenDays.size() > 0){
                for (String betweenDay : betweenDays) {
                    Map<String, Object> week = week(betweenDay);
                    objects.add(week);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return objects;
    }

    public static Map<String,Object> weekDate(String startTime, String endTime){
        Map<String,Object> weekDate = new HashMap<>();
        LinkedHashSet<Map<String, Object>> weeks = weeks(startTime, endTime);

        List<String> weekList = new ArrayList<>();
        Map<String,String> dates = new HashMap<>();
        for (Map<String, Object> week : weeks) {
            weekList.add(week.get("week").toString());
            dates.put(week.get("week").toString(),week.get("weekStart").toString() + " ~ " + week.get("weekEnd").toString());
        }
        weekDate.put("week",weekList);
        weekDate.put("dates",dates);
        return weekDate;
    }

    /**
     * 获取给定星期几的开始日期
     * @param year
     * @param weekNo
     * @return
     */
    public static String getStartDayOfWeekNo(int year,int weekNo){
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        int month = cal.get(Calendar.MONTH) + 1;
        String monthStr = month > 9 ? String.valueOf(month) : "0"+month;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String dayStr = day > 9 ? String.valueOf(day) : "0"+day;
        return cal.get(Calendar.YEAR) + "-" + monthStr + "-" + dayStr;

    }

    /**
     * 获取给定年份的日历
     * @param year
     * @return
     */
    private static Calendar getCalendarFormYear(int year){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        return cal;
    }

    /**
     * 获取一年中任意一周的结束日期
     * @param year
     * @param weekNo
     * @return
     */
    public static String getEndDayOfWeekNo(int year,int weekNo){
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        int month = cal.get(Calendar.MONTH) + 1;
        String monthStr = month > 9 ? String.valueOf(month) : "0"+month;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String dayStr = day > 9 ? String.valueOf(day) : "0"+day;
        return cal.get(Calendar.YEAR) + "-" + monthStr + "-" + dayStr;
    }

    /**
     *  获取一年中任意一周的开/结束日期
     * @return
     */
    public static String getDateOfWeekStartAndEnd(int year,int weekNo){
        String startDayOfWeekNo = getStartDayOfWeekNo(year, weekNo);
        String endDayOfWeekNo = getEndDayOfWeekNo(year, weekNo);
        return startDayOfWeekNo + " ~ " + endDayOfWeekNo;

    }

    private static java.time.LocalDate parseWeekBegin(int year, int week) {
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY,7);
        return java.time.LocalDate.parse(year + " " + week,
                new java.time.format.DateTimeFormatterBuilder().appendPattern("YYYY w").parseDefaulting(WeekFields.ISO.dayOfWeek(), 1).toFormatter());
    }

    private static java.time.LocalDate parseWeekEnd(java.time.LocalDate begin) {
        return begin.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
    }


    /**
     * 判断两个时间是否为同一天
     * @param time
     * @param time2
     * @return
     */
    public static Boolean dateCompare(long time,long time2){
        String format = format(new Date(time), DATE_PATTERN);
        String format2 = format(new Date(time2), DATE_PATTERN);
        if(format.equals(format2)){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args) {

        LinkedHashSet<Map<String, Object>> weeks = weeks("2020-12-21", "2021-01-05");
        for (Map<String, Object> week : weeks) {
            System.out.println(week);
        }
    }


}
