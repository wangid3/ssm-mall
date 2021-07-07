package com.ssm.mall.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
//本工具采用joda-time工具，用于兼容JDK8之前的JDK，进行时间处理
public class DateTimeUtil {
    //可以代用joda-time工具jar包（用于兼容JDK8之前的JDK版本），实现String->Date
    public static Date jodaStrToDate(String dateTime, String pattern){
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        DateTime result = formatter.parseDateTime(dateTime);
        return result.toDate();
    }
    //使用joda-time实现Date->String
    public static String jodaDateToStr(Date date,String pattern){
        if(date == null) return "";
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(pattern);
    }
    //使用JAVA8的java.time包
    public static Date strToDate(String dateTime,String pattern){
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime,formatter);
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());

    }
    public static String dateToStr(Date dateTime,String pattern){
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(pattern);
        LocalDateTime ldt = LocalDateTime.ofInstant(dateTime.toInstant(),ZoneId.systemDefault());
        return ldt.format(formatter);
    }

    //测试
    public static void main(String[] args) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Date dateTime = jodaStrToDate("2020-01-01 12:01:02",pattern);
        System.out.println("**joda** str->date:\t"+dateTime);
        String dateTimeVo = jodaDateToStr(dateTime,pattern);
        System.out.println("**joda** date->str:\t"+dateTimeVo);
        System.out.println("*******************JAVA8*******************");
        Date result = strToDate("2020-05-01 20:08:08",pattern);
        System.out.println(result);
        String resultStr = dateToStr(new Date(),pattern);
        System.out.println(resultStr);
    }
}
