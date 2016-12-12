package wsd.com.wsd.comparators;

import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.types.Interwal;

public class EventComparator implements Comparator<Event> {


    @Override
    public int compare(Event event, Event t1) {
        int res = compareDate(event.getDate(), t1.getDate());
        return res;
    }

    int compareDate(Date a, Date b) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(a);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(b);
        int yearCompare = Integer.compare(calendar1.get(Calendar.YEAR),calendar2.get(Calendar.YEAR));
        if(yearCompare==0){
            int monthComapre = Integer.compare(calendar1.get(Calendar.MONTH),calendar2.get(Calendar.MONTH));
            if(monthComapre==0){
                return Integer.compare(calendar1.get(Calendar.DAY_OF_MONTH),calendar2.get(Calendar.DAY_OF_MONTH));
            }else {
                return monthComapre;
            }
        }else{
            return yearCompare;
        }
    }
}
