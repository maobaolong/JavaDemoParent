package net.mbl.demo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mbl on 22/06/2018.
 */
public class DateDemo {
  public static void main(String[] args) {
    DateDemo dd = new DateDemo();
    dd.isBelong("22:00", "06:00", "06:00");
    dd.isBelong("22:00", "06:00", "23:00");
    dd.isBelong("22:00", "06:00", "21:00");
    dd.isBelong("22:00", "21:00", "23:00");
    dd.isBelong("22:00", "23:00", "3:00");
    dd.isBelong("2:00", "23:00", "3:00");
    dd.isBelong("4:00", "23:00", "3:00");
  }
  public void isBelong(String nowStr, String beginStr, String endStr){

    SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置日期格式
    Date now =null;
    Date beginTime = null;
    Date endTime = null;
    try {
      now = df.parse(nowStr);
      System.out.println(nowStr);
      beginTime = df.parse(beginStr);
      endTime = df.parse(endStr);
    } catch (Exception e) {
      e.printStackTrace();
    }

    Boolean flag = belongCalendar(now, beginTime, endTime);
    System.out.println(flag);
  }


  /**
   * 判断时间是否在时间段内， 支持跨夜
   * @param nowTime
   * @param beginTime
   * @param endTime
   * @return
   */
  public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
    Calendar date = Calendar.getInstance();
    date.setTime(nowTime);

    Calendar begin = Calendar.getInstance();
    begin.setTime(beginTime);

    Calendar end = Calendar.getInstance();
    end.setTime(endTime);
    boolean throughNight = end.before(begin);
    if (throughNight) {
      return date.after(begin) || date.before(end);
    } else {
      return date.after(begin) && date.before(end);
    }
  }
}
