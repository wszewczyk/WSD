package wsd.com.wsd.comparators;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;


public class FreeEventComparatorTest {

    private FreeEventComparator freeEventComparator = new FreeEventComparator(new Date());

    @Test
    public void getAbsFromDateTest(){

        int DIF = 2;


        int days = freeEventComparator.getAbsFromDate(new Date(), dateFromDate(new Date(), DIF));

        assertEquals(Math.abs(DIF), days);
    }

    private Date dateFromDate(Date date, int numberOfDays){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, numberOfDays);
        return calendar.getTime();
    }
}