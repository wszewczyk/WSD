package wsd.com.wsd.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.Localization;
import wsd.com.wsd.models.TimeSlot;
import wsd.com.wsd.models.types.Interwal;
import wsd.com.wsd.singletons.EventSourceSingleton;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class EventAvaialbeClientTest {


    private EventSourceSingleton eventSourceSingleton;
    private EventAvaialbeClient eventAvaialbeClient = EventAvaialbeClient.getInstance();

    @Before
    public void setUp() throws Exception {
        eventSourceSingleton = mock(EventSourceSingleton.class);
        when(eventSourceSingleton.getEvents()).thenReturn(mockEvenst());
        eventAvaialbeClient.setEventSourceSingleton(eventSourceSingleton);
    }

    @Test
    public void functionalityTest(){
        boolean res;

        Event testEvent1 = new Event("test 1", "test desc 1", getDateByVariables(2016, 12, 14), new TimeSlot(Interwal._8, Interwal._10), new Localization(12.23, 13.23));
        res = eventAvaialbeClient.isEventAvaiable(testEvent1);
        assertEquals(false, res);

        Event testEvent2 = new Event("test 2", "test desc 2", getDateByVariables(2016, 12, 14), new TimeSlot(Interwal._14, Interwal._18), new Localization(12.23, 13.23));
        res = eventAvaialbeClient.isEventAvaiable(testEvent2);
        assertEquals(true, res);


        Event testEvent3 = new Event("test 3", "test desc 3", getDateByVariables(2016, 12, 14), new TimeSlot(Interwal._12, Interwal._18), new Localization(12.23, 13.23));
        res = eventAvaialbeClient.isEventAvaiable(testEvent3);
        assertEquals(false, res);

        Event testEvent4 = new Event("test 4", "test desc 4", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._8, Interwal._12), new Localization(12.23, 13.23));
        res = eventAvaialbeClient.isEventAvaiable(testEvent4);
        assertEquals(false, res);

        Event testEvent5 = new Event("test 5", "test desc 5", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._18, Interwal._20), new Localization(12.23, 13.23));
        res = eventAvaialbeClient.isEventAvaiable(testEvent5);
        assertEquals(true, res);
    }

    private Date getDateByVariables(int y, int m, int d){
        GregorianCalendar gc = new GregorianCalendar(y,m,d);
        return gc.getTime();
    }

    private List<Event> mockEvenst(){
        return Arrays.asList(
                new Event("event 1", "enevt desc 1", getDateByVariables(2016, 12, 14), new TimeSlot(Interwal._8, Interwal._10), new Localization(12.23, 13.23)),
                new Event("event 2", "enevt desc 2", getDateByVariables(2016, 12, 14), new TimeSlot(Interwal._10, Interwal._14), new Localization(12.23, 13.23)),
                new Event("event 3", "enevt desc 3", getDateByVariables(2016, 12, 14), new TimeSlot(Interwal._18, Interwal._20), new Localization(12.23, 13.23)),
                new Event("event 4", "enevt desc 4", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._10, Interwal._14), new Localization(12.23, 13.23)),
                new Event("event 5", "enevt desc 5", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._16, Interwal._18), new Localization(12.23, 13.23))
        );
    }
}