package wsd.com.wsd.service;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import jade.domain.introspection.EventRecord;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.Localization;
import wsd.com.wsd.models.TimeSlot;
import wsd.com.wsd.models.types.Interwal;

import static org.junit.Assert.*;


public class FreeEventProposalServiceTest {

    private FreeEventProposalService proposalService = new FreeEventProposalService(2);

    @Test
    public void genFreeEnetsInDayTest(){
        Set<Event> events = proposalService.generateFreeEventsInDay(new Date(),2);
//        for (Event e: events){
//            System.out.println(e);
//        }

        assertEquals(6, events.size());
    }

    @Test
    public void generateEventInDays(){
        List<Event> events = proposalService.generateAllFreePeriod(new Date(), dateFromDate(new Date(), 3),2,false);

//        for (Event e: events){
//            System.out.println(e);
//        }

        assertEquals(18, events.size());
    }

    @Test
    public void explodeEventsTest(){
        List<Event> eventsToTest = Arrays.asList(
                new Event("", "", new Date(), new TimeSlot(Interwal._8, Interwal._12), new Localization(1,2)),
                new Event("", "", new Date(), new TimeSlot(Interwal._14, Interwal._18), new Localization(1,2))
        );

        List<Event> res = proposalService.explodeEvents(eventsToTest);

//        for (Event e: res){
//            System.out.println(e);
//        }

        assertEquals(4, res.size());
    }

    @Test
    public void getFreeEventsTest(){
        List<Event> eventsToTest = Arrays.asList(
                new Event("", "", new Date(), new TimeSlot(Interwal._8, Interwal._12), new Localization(1,2)),
                new Event("", "", new Date(), new TimeSlot(Interwal._14, Interwal._18), new Localization(1,2))
        );

        Date today = new Date();

        Set<Event> res = proposalService.getFreeEvents(eventsToTest,dateFromDate(today, -1), dateFromDate(today, 1));

//        for (Event e: res){
//            System.out.println(e);
//        }k
        assertEquals(14, res.size());
    }

    @Test
    public void mergeByMettingPeriodTest(){

        List<Event> eventsToTest = Arrays.asList(
                new Event("", "", new Date(), new TimeSlot(Interwal._8, Interwal._10), new Localization(1,2)),
                new Event("", "", new Date(), new TimeSlot(Interwal._10, Interwal._12), new Localization(1,2)),
                new Event("", "", new Date(), new TimeSlot(Interwal._12, Interwal._14), new Localization(1,2)),
                new Event("", "", new Date(), new TimeSlot(Interwal._14, Interwal._16), new Localization(1,2)),
                new Event("", "", new Date(), new TimeSlot(Interwal._16, Interwal._18), new Localization(1,2))
        );

        proposalService.setMeetingPeriod(6);

        Set<Event> res = proposalService.mergeByMettingPeriod(eventsToTest);

        for (Event e: res){
            System.out.println(e);
        }

        assertEquals(1, res.size());
    }


    private Date dateFromDate(Date date, int numberOfDays){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, numberOfDays);
        return calendar.getTime();
    }


}