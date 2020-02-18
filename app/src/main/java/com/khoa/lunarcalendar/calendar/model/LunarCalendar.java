package com.khoa.lunarcalendar.calendar.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class LunarCalendar {

//    public static void main(String[] args) {
//        Date date = convertSolar2Lunar(29, 2, 2020, 7);
//        System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(date));
//    }

    // Chuyển đổi dương lịch sang Julius jd
    public static int jdFromDate(int dd, int mm, int yy) {
        double a = Math.floor((14 - mm) / 12);
        double y = yy + 4800 - a;
        double m = mm + 12 * a - 3;
        double jd = dd + Math.floor((153 * m + 2) / 5) + 365 * y + Math.floor(y / 4) - Math.floor(y / 100)
                + Math.floor(y / 400) - 32045;
        if (jd < 2299161) {
            jd = dd + Math.floor((153 * m + 2) / 5) + 365 * y + Math.floor(y / 4) - 32083;
        }
        return (int) jd;
    }

    // Tính ngày Sóc
    public static double getNewMoonDay(double k, double timeZone) {
        double T, T2, T3, dr, Jd1, M, Mpr, F, C1, deltat, JdNew;
        T = k / 1236.85; // Time in Julian centuries from 1900 January 0.5
        T2 = T * T;
        T3 = T2 * T;
        dr = Math.PI / 180;
        Jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * T2 - 0.000000155 * T3;
        Jd1 = Jd1 + 0.00033 * Math.sin((166.56 + 132.87 * T - 0.009173 * T2) * dr); // Mean new moon
        M = 359.2242 + 29.10535608 * k - 0.0000333 * T2 - 0.00000347 * T3; // Sun"s mean anomaly
        Mpr = 306.0253 + 385.81691806 * k + 0.0107306 * T2 + 0.00001236 * T3; // Moon"s mean anomaly
        F = 21.2964 + 390.67050646 * k - 0.0016528 * T2 - 0.00000239 * T3; // Moon"s argument of latitude
        C1 = (0.1734 - 0.000393 * T) * Math.sin(M * dr) + 0.0021 * Math.sin(2 * dr * M);
        C1 = C1 - 0.4068 * Math.sin(Mpr * dr) + 0.0161 * Math.sin(dr * 2 * Mpr);
        C1 = C1 - 0.0004 * Math.sin(dr * 3 * Mpr);
        C1 = C1 + 0.0104 * Math.sin(dr * 2 * F) - 0.0051 * Math.sin(dr * (M + Mpr));
        C1 = C1 - 0.0074 * Math.sin(dr * (M - Mpr)) + 0.0004 * Math.sin(dr * (2 * F + M));
        C1 = C1 - 0.0004 * Math.sin(dr * (2 * F - M)) - 0.0006 * Math.sin(dr * (2 * F + Mpr));
        C1 = C1 + 0.0010 * Math.sin(dr * (2 * F - Mpr)) + 0.0005 * Math.sin(dr * (2 * Mpr + M));
        if (T < -11) {
            deltat = 0.001 + 0.000839 * T + 0.0002261 * T2 - 0.00000845 * T3 - 0.000000081 * T * T3;
        } else {
            deltat = -0.000278 + 0.000265 * T + 0.000262 * T2;
        }
        ;
        JdNew = Jd1 + C1 - deltat;
        return Math.floor(JdNew + 0.5 + timeZone / 24);
    }


    // Tính tọa độ mặt trời
    public static double getSunLongitude(double jdn, int timeZone) {
        double T, T2, dr, M, L0, DL, L;
        T = (jdn - 2451545.5 - timeZone / 24) / 36525; // Time in Julian centuries from 2000-01-01 12:00:00 GMT
        T2 = T * T;
        dr = Math.PI / 180; // degree to radian
        M = 357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048 * T * T2; // mean anomaly, degree
        L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2; // mean longitude, degree
        DL = (1.914600 - 0.004817 * T - 0.000014 * T2) * Math.sin(dr * M);
        DL = DL + (0.019993 - 0.000101 * T) * Math.sin(dr * 2 * M) + 0.000290 * Math.sin(dr * 3 * M);
        L = L0 + DL; // true longitude, degree
        double omega = 125.04 - 1934.136 * T;
        L = L - 0.00569 - 0.00478 * Math.sin(omega * dr);
        L = L * dr;
        L = L - Math.PI * 2 * (Math.floor(L / (Math.PI * 2))); // Normalize to (0, 2*PI)
        return Math.floor(L / Math.PI * 6);
    }

    public static double getSunLongitude2(int jdn) {
        double T, T2, dr, M, L0, DL, lambda, theta, omega;
        T = (jdn - 2451545.0) / 36525; // Time in Julian centuries from 2000-01-01 12:00:00 GMT
        T2 = T * T;
        dr = Math.PI / 180; // degree to radian
        M = 357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048 * T * T2; // mean anomaly, degree
        L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2; // mean longitude, degree
        DL = (1.914600 - 0.004817 * T - 0.000014 * T2) * Math.sin(dr * M);
        DL = DL + (0.019993 - 0.000101 * T) * Math.sin(dr * 2 * M) + 0.000290 * Math.sin(dr * 3 * M);
        theta = L0 + DL; // true longitude, degree
        // obtain apparent longitude by correcting for nutation and aberration
        omega = 125.04 - 1934.136 * T;
        lambda = theta - 0.00569 - 0.00478 * Math.sin(omega * dr);
        // Convert to radians
        lambda = lambda * dr;
        lambda = lambda - Math.PI * 2 * (Math.floor(lambda / (Math.PI * 2))); // Normalize to (0, 2*PI)
        return lambda;
    }

    // Tính ngày bắt đầu tháng 11 âm lịch
    public static double getLunarMonth11(int yy, int timeZone) {
        int off = jdFromDate(31, 12, yy) - 2415021;
        int k = (int) Math.floor(off / 29.530588853);
        double nm = getNewMoonDay(k, timeZone);
        double sunLong = getSunLongitude(nm, timeZone); // sun longitude at local midnight
        if (sunLong >= 9) {
            nm = getNewMoonDay(k - 1, timeZone);
        }
        return nm;
    }

    // Xác định tháng nhuận
    public static int getLeapMonthOffset(double a11, int timeZone) {
        int k = (int) Math.floor((a11 - 2415021.076998695) / 29.530588853 + 0.5);
        double last = 0;
        int i = 1; // We start with the month following lunar month 11
        double arc = getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone);
        do {
            last = arc;
            i = i + 1;
            arc = getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone);
        } while (arc != last && i < 14);
        return i - 1;
    }

    // chuyển ngày dương sang ngày âm
    public static Date convertSolar2Lunar(int dd, int mm, int yy, int timeZone) {
        int dayNumber = jdFromDate(dd, mm, yy);
        int k = (int) Math.floor((dayNumber - 2415021.076998695) / 29.530588853);
        double monthStart = getNewMoonDay(k + 1, timeZone);
        if (monthStart > dayNumber) {
            monthStart = getNewMoonDay(k, timeZone);
        }
        double a11 = getLunarMonth11(yy, timeZone);
        double b11 = a11;
        int lunarYear = 0;
        if (a11 >= monthStart) {
            lunarYear = yy;
            a11 = getLunarMonth11(yy - 1, timeZone);
        } else {
            lunarYear = yy + 1;
            b11 = getLunarMonth11(yy + 1, timeZone);
        }
        int lunarDay = (int) (dayNumber - monthStart + 1);
        int diff = (int) Math.floor((monthStart - a11) / 29);
        int lunarLeap = 0;
        int lunarMonth = diff + 11;
        if (b11 - a11 > 365) {
            int leapMonthDiff = getLeapMonthOffset(a11, timeZone);
            if (diff >= leapMonthDiff) {
                lunarMonth = diff + 10;
                if (diff == leapMonthDiff) {
                    lunarLeap = 1;
                }
            }
        }
        if (lunarMonth > 12) {
            lunarMonth = lunarMonth - 12;
        }
        if (lunarMonth >= 11 && diff < 4) {
            lunarYear -= 1;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(lunarYear + "-" + lunarMonth + "-" + lunarDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // chuyển âm lịch sang dương lịch
    public static int[] convertLunar2Solar(int lunarDay, int lunarMonth, int lunarYear) {
        int[] date = new int[3];
        int year1 = lunarYear;

        date = convertLunar2Solar(lunarDay, lunarMonth, year1, isLunarYearLeap(year1), 7);
        if (date[2] <= lunarYear) {
//            System.out.println(date[0] + "/" + date[1] + "/" + date[2]);
            return date;
        }

        int year2 = lunarYear - 1;
        date = convertLunar2Solar(lunarDay, lunarMonth, year2, isLunarYearLeap(year2), 7);
        return date;
    }

    // chuyển âm lịch sang dương lịch
    public static int[] convertLunar2Solar(int lunarDay, int lunarMonth, int lunarYear, boolean lunarLeap, int timeZone) {
        double a11, b11, k;
        if (lunarMonth < 11) {
            a11 = getLunarMonth11(lunarYear - 1, timeZone);
            b11 = getLunarMonth11(lunarYear, timeZone);
        } else {
            a11 = getLunarMonth11(lunarYear, timeZone);
            b11 = getLunarMonth11(lunarYear + 1, timeZone);
        }
        k = (int) Math.floor(0.5 + (a11 - 2415021.076998695) / 29.530588853);
        int off = lunarMonth - 11;
        if (off < 0) {
            off += 12;
        }
        if (b11 - a11 > 365) {
            int leapOff = getLeapMonthOffset(a11, timeZone);
            int leapMonth = leapOff - 2;
            if (leapMonth < 0) {
                leapMonth += 12;
            }
            if (lunarLeap && lunarMonth != leapMonth) {
                System.out.println("Invalid input!");
                return new int[]{0, 0, 0};
            } else if (lunarLeap || off >= leapOff) {
                off += 1;
            }
        }
        int monthStart = (int) getNewMoonDay(k + off, timeZone);
        return jdToDate(monthStart + lunarDay - 1);
    }

    // kiểm tra năm dương nhuận
    public static boolean isSolarYearLeap(int yyyy) {
        if (yyyy % 4 == 0 || (yyyy % 100 == 0 && yyyy % 400 == 0)) {
            return true;
        } else {
            return false;
        }
    }

    // kiểm tra năm âm nhuận
    public static boolean isLunarYearLeap(int yyyy) {
        int arr[] = { 0, 3, 6, 9, 11, 14, 17 };
        int leap = yyyy % 19;
        if (Arrays.asList(arr).contains(leap)) {
            return true;
        } else {
            return false;
        }
    }

    // Chuyển đổi Julius jd sang dương lịch
    public static int[] jdToDate(double jd) {
        double c = 0, b = 0, a = 0;
        if (jd > 2299160) { // After 5/10/1582, Gregorian calendar
            a = jd + 32044;
            b = (int) Math.floor((4 * a + 3) / 146097);
            c = (int) (a - Math.floor((b * 146097) / 4));
        } else {
            b = 0;
            c = jd + 32082;
        }
        int d = (int) Math.floor((4 * c + 3) / 1461);
        int e = (int) (c - Math.floor((1461 * d) / 4));
        int m = (int) Math.floor((5 * e + 2) / 153);
        int day = (int) (e - Math.floor((153 * m + 2) / 5) + 1);
        int month = (int) (m + 3 - 12 * Math.floor(m / 10));
        int year = (int) (b * 100 + d - 4800 + Math.floor(m / 10));
        int kq[] = { day, month, year };
        return kq;
    }

    // Tính Can-Chi theo ngày âm lịch
    public static String[] getListCan() {
        String arr2[] = { "Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ", "Canh", "Tân", "Nhâm", "Quí" };
        return arr2;
    }

    public static String[] getListChi() {
        String arr2[] = { "Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ", "Mùi", "Thân", "Dậu", "Tuất", "Hợi" };
        return arr2;
    }

    public static String getCanChiNam(int nam) {
        String arrCan[] = getListCan();
        String arrChi[] = getListChi();
        int can = (nam + 6) % 10;
        int chi = (nam + 8) % 12;
        return arrCan[can] + " " + arrChi[chi];
    }

    public static String getCanChiThang(int nam, int thang) {
        String arrCan[] = getListCan();
        String arrChi[] = getListChi();
        int can = (nam * 12 + thang + 3) % 10;
        int chi = (thang + 1) % 12;
        String canchithang = arrCan[can] + " " + arrChi[chi];
        return canchithang;
    }

    public static String getCanChiNgay(int jd) {
        String arrCan[] = getListCan();
        String arrChi[] = getListChi();
        int can = (jd + 9) % 10;
        int chi = (jd + 1) % 12;
        String canchingay = arrCan[can] + " " + arrChi[chi];
        return canchingay;
    }

    public static String getChiNgay(int jd) {
        String arrChi[] = getListChi();
        int chi = (jd + 1) % 12;
        String chingay = arrChi[chi];
        return chingay;
    }

    public static String getCanNgay(int jd) {
        String arrChi[] = getListCan();
        int can = (jd + 9) % 10;
        String chingay = arrChi[can];
        return chingay;
    }


    public static String getCanChiGio(int jd) {
        String arrCan[] = getListCan();
        String arrChi[] = getListChi();
        int can = (jd - 1) * 2 % 10;
        String canchigio = arrCan[can] + " " + arrChi[0];
        return canchigio;
    }

    // Tính Tiết khí
    public static String getTietKhi(int jd) {
        String arr[] = { "Xuân phân", "Thanh minh", "Cốc vũ", "Lập hạ", "Tiểu mãn", "Mang chủng", "Hạ chí", "Tiểu thử",
                "Đại thử", "Lập thu", "Xử thử", "Bạch lộ", "Thu phân", "Hàn lộ", "Sương giáng", "Lập đông",
                "Tiểu tuyết", "Đại tuyết", "Đông chí", "Tiểu hàn", "Đại hàn", "Lập xuân", "Vũ thủy", "Kinh trập" };
        int tietkhi = (int) Math.floor(getSunLongitude2((int) (jd + 1 - 0.5 - 7.0 / 24.0)) / Math.PI * 12);
        return arr[tietkhi];
    }

}