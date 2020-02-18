package com.khoa.lunarcalendar.calendar.ultis;

import android.util.Log;

import com.khoa.lunarcalendar.calendar.model.LunarCalendar;
import com.khoa.lunarcalendar.calendar.model.MyDate;
import com.khoa.lunarcalendar.calendar.model.MyMonth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarUltis {

    public static MyMonth getMonth(int month, int year){
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, 1);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        ArrayList<MyDate> dateList = new ArrayList();
        for (int i = 1; i <= maxDay; i++) {
            MyDate myDate = new MyDate(i, month, year);
            dateList.add(myDate);
        }

        MyMonth myMonth = new MyMonth(year, month, dateList);
        myMonth.addDay();
        return myMonth;
    }

    public static String getDayName(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek==1) dayOfWeek = 8;
        return getDayName(dayOfWeek);
    }

    public static String getDayName(int dayOfWeek) {
        String dayName = "";
        switch (dayOfWeek) {
            case 2:
                dayName = "Thứ 2";
                break;
            case 3:
                dayName = "Thứ 3";
                break;
            case 4:
                dayName = "Thứ 4";
                break;
            case 5:
                dayName = "Thứ 5";
                break;
            case 6:
                dayName = "Thứ 6";
                break;
            case 7:
                dayName = "Thứ 7";
                break;
            case 8:
                dayName = "Chủ nhật";
                break;
            default:
                break;
        }
        return dayName;
    }


    // 1971-12-23 00:00:00
    // 0123456789012345678
    public static int[] parseTime(String strTime) {
        int year=0, month=0, day=0, hour=0, min=0;
        try {
            year = Integer.valueOf(strTime.substring(0, 4));
            month = Integer.valueOf(strTime.substring(5, 7));
            day = Integer.valueOf(strTime.substring(8, 10));
            hour = Integer.valueOf(strTime.substring(11, 13));
            min = Integer.valueOf(strTime.substring(14, 16));
        }catch (Exception e){
            Log.e("Loi", year + "-" + month + "-" + day + " " + hour + "h" + min);
            e.printStackTrace();
        }
        return new int[]{year, month, day, hour, min};
    }

    public static String[] convertDate(String strStartDate, String strEndDate, boolean solarDay, int year) {
        String[] result = new String[3];
        String strSolarDay = "";
        String strLunarDay = "";
        String strDuration = "";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = df.parse(strStartDate);
            Date endDate = df.parse(strEndDate);
            long diff = endDate.getTime() - startDate.getTime();

            long amountDay = diff / (1000 * 60 * 60 * 24) + 1;
            long amountHour = (diff / (1000 * 60 * 60)) % 24;
            long amountMin = (diff / (1000 * 60)) % 60;


            if (solarDay) {
                String strMyDate = year + strStartDate.substring(4);
                MyDate myDate = new MyDate(df.parse(strMyDate));

                strSolarDay = CalendarUltis.getDayName(myDate.getDayOfWeek()) + ", " + myDate.getDay() + " tháng " + myDate.getMonth();
                if(!strStartDate.startsWith("1970")) {
                    strSolarDay += " " + strStartDate.substring(0, 4);
                }

                strLunarDay = myDate.getLunarDay() + " tháng " + myDate.getLunarMonth() + " âm lịch";
            } else {
                int[] startTime = CalendarUltis.parseTime(strStartDate);
                int[] dMY = LunarCalendar.convertLunar2Solar(startTime[2], startTime[1], year);

                Date solarDate = new SimpleDateFormat("dd-MM-yyyy").parse(String.format("%02d-%02d-%02d", dMY[0], dMY[1],dMY[2]));

                strSolarDay = CalendarUltis.getDayName(solarDate) + ", " + dMY[0] + " tháng " + dMY[1];
                strLunarDay = startTime[2]+ " tháng " + startTime[1] + " âm lịch";
            }


            if (amountDay == 1 && amountHour == 0 && amountMin == 0) {
                strDuration = "Cả ngày";
            } else {
                if (amountDay != 0) strDuration += amountDay + " ngày ";
                if (amountHour != 0) strDuration += amountHour + " giờ ";
                if (amountMin != 0) strDuration += amountMin + " phút";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        result[0] = strSolarDay;
        result[1] = strLunarDay;
        result[2] = strDuration;
        return result;
    }
}
