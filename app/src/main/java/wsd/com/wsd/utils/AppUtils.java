package wsd.com.wsd.utils;


import java.util.Calendar;
import java.util.Date;

public class AppUtils {
    public static final int SEARCH_RANGE_SIZE = 3;

    public static Date dateFromDate(Date date, int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, numberOfDays);
        return calendar.getTime();
    }
}
