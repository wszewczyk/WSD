package wsd.com.wsd.comparators;


import android.icu.util.DateInterval;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import wsd.com.wsd.acivities.MainActivity;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.TimeSlot;

public class FreeEventComparator implements Comparator<Event>{

    private DateComperator dateComperator = new DateComperator();
    private TimeSlotsComparator timeSlotsComparator = new TimeSlotsComparator();

    private Date proposalDate;

    public FreeEventComparator(Date proposalDate) {
        this.proposalDate = proposalDate;
    }

    @Override
    public int compare(Event event, Event t1) {
        Date firstEventDate = event.getDate();
        Date secoundEventDate = t1.getDate();
        long absFirstDate = getDateDiff(firstEventDate, this.proposalDate, TimeUnit.DAYS);
        long absSecDate = getDateDiff(secoundEventDate, this.proposalDate, TimeUnit.DAYS);

        int res =  Long.compare(absFirstDate, absSecDate);


//        return res;
        if(res==0){
            return timeSlotsComparator.compare(event.getTimeSlot(), t1.getTimeSlot());
//            return -1;
        }else {
            return 1;
        }
    }
    static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
//        long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
        long diffInMillies = date2.getTime() - date1.getTime();
//        long dif = timeUnit.toDays(diffInMillies);
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

//    // do wygodnego tworzenia dat typu Date
//    public static Date getDateByVariables(int y, int m, int d) {
//        GregorianCalendar gc = new GregorianCalendar(y, m, d);
//        return gc.getTime();
//    }
}
