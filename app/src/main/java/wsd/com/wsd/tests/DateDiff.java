package wsd.com.wsd.tests;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mikolaj on 12/14/16.
 */

public class DateDiff {

    public static void main(String args[]) {

        // proste testy
        Date date1 = getDateByVariables(2015, 12, 1);
        Date date2 = getDateByVariables(2014, 12, 9);
        System.out.println(getDateDiff(date1,date2,TimeUnit.DAYS));

        Date date3 = getDateByVariables(2013, 5, 8);
        Date date4 = getDateByVariables(2014, 5, 11);
        System.out.println(getDateDiff(date1,date2,TimeUnit.DAYS));

        Date date5 = getDateByVariables(2012, 2, 2);
        Date date6 = getDateByVariables(2012, 2, 4);
        System.out.println(getDateDiff(date1,date2,TimeUnit.DAYS));
    }

    // wartość bezwględna różnicy w milisekundach, konwertowana przy wywoływaniu na dni
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    // do wygodnego tworzenia dat typu Date
    public static Date getDateByVariables(int y, int m, int d) {
        GregorianCalendar gc = new GregorianCalendar(y, m, d);
        return gc.getTime();
    }
}
