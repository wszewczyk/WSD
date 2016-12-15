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
    public int compare(Event event, Event event2)
    {
        Date firstEventDate = event.getDate();
        Date secoundEventDate = event2.getDate();
        long absFirstDate = getDateDiff(firstEventDate, this.proposalDate, TimeUnit.DAYS);
        long absSecDate = getDateDiff(secoundEventDate, this.proposalDate, TimeUnit.DAYS);

        //int res =  Long.compare(absFirstDate, absSecDate);
        if((absSecDate - absFirstDate) > 0) //
        {
            return -1;
        }
        else if((absSecDate - absFirstDate) < 0)
        {
            return 1;
        }
        else
        {
            return 1;
        }
    }

    static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit)
    {
        long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
        //return diffInMillies;
    }

    public Date getProposalDate()
    {
        return proposalDate;
    }
}
