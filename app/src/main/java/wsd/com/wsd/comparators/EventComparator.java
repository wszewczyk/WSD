package wsd.com.wsd.comparators;

import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.types.Interwal;

public class EventComparator implements Comparator<Event> {

    private TimeSlotsComparator timeSlotsComparator = new TimeSlotsComparator();
    private DateComperator dateComperator = new DateComperator();

    @Override
    public int compare(Event event, Event t1) {
        int res = dateComperator.compare(event.getDate(), t1.getDate());
        if(res==0){
            return timeSlotsComparator.compare(event.getTimeSlot(), t1.getTimeSlot());
        }else {
            return res;
        }
    }


}
