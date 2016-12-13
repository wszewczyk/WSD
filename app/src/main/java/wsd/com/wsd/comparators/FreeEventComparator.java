package wsd.com.wsd.comparators;


import android.icu.util.DateInterval;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

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
        int absFirstDate = getAbsFromDate(firstEventDate, this.proposalDate);
        int absSecDate = getAbsFromDate(secoundEventDate, this.proposalDate);

        int compDateRes = Integer.compare(absFirstDate, absSecDate);

        if(compDateRes==0){
            return timeSlotsComparator.compare(event.getTimeSlot(), t1.getTimeSlot());
        }else {
            return compDateRes;
        }
    }

//    TODO zrobić to lepiej -> Najlepiej w Java 8 !!!!!!!!!!!!!!
    int getAbsFromDate(Date firstEventDate, Date secEventDate) {
        long dis = firstEventDate.getTime() - secEventDate.getTime();
        long diffDays = dis / (24 * 60 * 60 * 1000);
        int  daysdiff = (int) diffDays;
        return Math.abs(daysdiff);
    }
}
