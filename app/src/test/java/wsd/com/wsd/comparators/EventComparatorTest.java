package wsd.com.wsd.comparators;

import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.Localization;
import wsd.com.wsd.models.TimeSlot;
import wsd.com.wsd.models.types.Interwal;

import static org.junit.Assert.*;


public class EventComparatorTest {
    private EventComparator eventComparator = new EventComparator();

    @Test
    public void eventComparaorEq() {
        Set<Event> events = new TreeSet<>(eventComparator);
        List<Event> eventList = mockEvenst();

        events.addAll(mockEvenst());


        Event first = new Event("event 1", "enevt desc 1", getDateByVariables(2016, 12, 14), new TimeSlot(Interwal._8, Interwal._10), new Localization(12.23, 13.23));
        Iterator<Event> eventIterator = events.iterator();


        int[] testArr = {2,3,1,4,0};
        int i = 0;
        while (eventIterator.hasNext()){
            assertEquals(eventList.get(testArr[i++]), eventIterator.next());
        }
    }

    private Date getDateByVariables(int y, int m, int d) {
        GregorianCalendar gc = new GregorianCalendar(y, m, d);
        return gc.getTime();
    }

    private List<Event> mockEvenst() {
        return Arrays.asList(
                new Event("event 5", "enevt desc 5", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._16, Interwal._18), new Localization(12.23, 13.23)),
                new Event("event 3", "enevt desc 3", getDateByVariables(2016, 12, 14), new TimeSlot(Interwal._18, Interwal._20), new Localization(12.23, 13.23)),
                new Event("event 1", "enevt desc 1", getDateByVariables(2016, 12, 14), new TimeSlot(Interwal._8, Interwal._10), new Localization(12.23, 13.23)),
                new Event("event 2", "enevt desc 2", getDateByVariables(2016, 12, 14), new TimeSlot(Interwal._10, Interwal._14), new Localization(12.23, 13.23)),
                new Event("event 4", "enevt desc 4", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._10, Interwal._14), new Localization(12.23, 13.23))
        );
    }
}