package xyz.yunzhongyan.www.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
    public static LocalDateTime convertDate2LocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date convertLocalDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date localDate2Date(LocalDate localDate) {
        if(null == localDate) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static String convertDateToStringys(Object o) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(o);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertDateToStringyd(Object o) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").format(o);
        } catch (Exception e) {
            return null;
        }
    }
    public static String convertDateToStringyd2(Object o) {
        try {
            return new SimpleDateFormat("yyyy/MM/dd").format(o);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date convertString2Date(String text) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(text);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getMongoDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, 8);
        return ca.getTime();
    }

    public static String formartDateToString(Date date){
        StringBuffer stringBuffer = new StringBuffer();
        LocalDateTime localDateTime = TimeUtils.convertDate2LocalDateTime(date);
        LocalDateTime now = LocalDateTime.now();
        Duration between = Duration.between(localDateTime, now);
        long minutes = between.toMinutes();
        if (minutes<=1){
            stringBuffer.append("刚刚");
        }else if (minutes<60){
            stringBuffer.append(between.toMinutes()).append("分钟前");
        }else if (minutes<24*60){
            stringBuffer.append(between.toHours()).append("小时前");
        }else if (minutes<24*60*30){
            stringBuffer.append(between.toDays()).append("天前");
        }else{
            stringBuffer.append(TimeUtils.convertDateToStringyd(date));
        }
        return stringBuffer.toString();
    }

}
