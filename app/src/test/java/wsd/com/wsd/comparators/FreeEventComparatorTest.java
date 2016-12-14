package wsd.com.wsd.comparators;

import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.Localization;
import wsd.com.wsd.models.TimeSlot;
import wsd.com.wsd.models.types.Interwal;

import static org.junit.Assert.*;


public class FreeEventComparatorTest {

    private FreeEventComparator freeEventComparator = new FreeEventComparator(new Date());

    @Test
    public void getAbsFromDateTest(){

        int DIF = 2;


        long days = FreeEventComparator.getDateDiff(new Date(), dateFromDate(new Date(), DIF), TimeUnit.DAYS);

        assertEquals(Math.abs(DIF), days);
    }

    @Test
    public void someFreeEventComperatorTest(){
        List<Event> testSet = mockEvenst();
        Collections.sort(testSet,new FreeEventComparator(new Date()));

        for(Event e: testSet){
            System.out.println(e);
        }
    }

    private Date getDateByVariables(int y, int m, int d) {
        GregorianCalendar gc = new GregorianCalendar(y, m, d);
        return gc.getTime();
    }

    private List<Event> mockEvenst() {
        return Arrays.asList(
                new Event("event 5", "enevt desc 5", getDateByVariables(2016, 12, 11), new TimeSlot(Interwal._16, Interwal._18), new Localization(12.23, 13.23)),
                new Event("event 3", "enevt desc 3", getDateByVariables(2016, 12, 13), new TimeSlot(Interwal._18, Interwal._20), new Localization(12.23, 13.23)),
                new Event("event 1", "enevt desc 1", getDateByVariables(2016, 12, 13), new TimeSlot(Interwal._8, Interwal._10), new Localization(12.23, 13.23)),
                new Event("event 2", "enevt desc 2", getDateByVariables(2016, 12, 14), new TimeSlot(Interwal._16, Interwal._18), new Localization(12.23, 13.23)),
                new Event("event 4", "enevt desc 4", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._10, Interwal._14), new Localization(12.23, 13.23))
        );
    }

    private Date dateFromDate(Date date, int numberOfDays){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, numberOfDays);
        return calendar.getTime();
    }
}