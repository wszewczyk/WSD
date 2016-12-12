package wsd.com.wsd.service;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import wsd.com.wsd.models.Event;

import static org.junit.Assert.*;


public class FreeEventProposalServiceTest {

    private FreeEventProposalService proposalService = new FreeEventProposalService(2);

    @Test
    public void genFreeEnetsInDayTest(){
        Set<Event> events = proposalService.generateFreeEventsInDay(new Date());
        for (Event e: events){
            System.out.println(e);
        }

        assertEquals(6, events.size());
    }

    @Test
    public void generateEventInDays(){
        Set<Event> events = proposalService.generateAllFreePeriod(new Date(), dateFromDate(new Date(), 3));

        for (Event e: events){
            System.out.println(e);
        }

        assertEquals(18, events.size());
    }


    private Date dateFromDate(Date date, int numberOfDays){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, numberOfDays);
        return calendar.getTime();
    }


}