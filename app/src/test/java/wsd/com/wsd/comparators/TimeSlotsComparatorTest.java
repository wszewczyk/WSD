package wsd.com.wsd.comparators;

import org.junit.Before;
import org.junit.Test;

import wsd.com.wsd.models.TimeSlot;
import wsd.com.wsd.models.types.Interwal;

import static org.junit.Assert.*;


public class TimeSlotsComparatorTest {
    private TimeSlotsComparator timeSlotsComparator = new TimeSlotsComparator();

    @Test
    public void areSlotsEqual(){
        TimeSlot t1 = new TimeSlot(Interwal._8, Interwal._10);
        TimeSlot t2 = new TimeSlot(Interwal._8, Interwal._10);

        int res = timeSlotsComparator.compare(t1, t2);
        assertEquals(0, res);
    }

    @Test
    public void isFisrtSlotBeforeSecond(){
        TimeSlot t1 = new TimeSlot(Interwal._8, Interwal._10);
        TimeSlot t2 = new TimeSlot(Interwal._10, Interwal._12);

        int res = timeSlotsComparator.compare(t1, t2);

        assertEquals(-1, res);
    }

    @Test
    public void isFirstSlotAfterSecond(){
        TimeSlot t2 = new TimeSlot(Interwal._8, Interwal._10);
        TimeSlot t1 = new TimeSlot(Interwal._10, Interwal._12);

        int res = timeSlotsComparator.compare(t1, t2);
        assertEquals(1, res);
    }

    @Test
    public void isOneSlotInAnother(){
        TimeSlot t1 = new TimeSlot(Interwal._8, Interwal._10);
        TimeSlot t2 = new TimeSlot(Interwal._8, Interwal._14);

        int res = timeSlotsComparator.compare(t1, t2);

        assertEquals(100, res);
    }

    @Test
    public void inOneDuringAnother(){
        TimeSlot t1 = new TimeSlot(Interwal._8, Interwal._14);
        TimeSlot t2 = new TimeSlot(Interwal._12, Interwal._16);

        int res = timeSlotsComparator.compare(t1, t2);

        assertEquals(100, res);


        TimeSlot t3 = new TimeSlot(Interwal._8, Interwal._12);
        TimeSlot t4 = new TimeSlot(Interwal._10, Interwal._16);

        res = timeSlotsComparator.compare(t1, t2);

        assertEquals(100, res);
    }
}