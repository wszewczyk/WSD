package wsd.com.wsd.service;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import wsd.com.wsd.comparators.DateComperator;
import wsd.com.wsd.comparators.EventComparator;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.TimeSlot;
import wsd.com.wsd.models.types.Interwal;
import wsd.com.wsd.singletons.EventSourceSingleton;

public class FreeEventProposalService {

    private EventComparator eventComparator = new EventComparator();
    private DateComperator dateComperator =  new DateComperator();

    private Set<Event> bookedEvent = new TreeSet<>(eventComparator);
    private Date preditcedMeetingTime;
    private int meetingPeriod;

    private Event proposalEventInfo;

    private Date leftLimitDate;
    private Date rightLimitDate;


    public FreeEventProposalService(EventSourceSingleton eventSourceSingleton,Event proposalEvent, Date preditcedMeetingTime, int meetingPeriod, int periot) {
        assert eventSourceSingleton != null;
        this.preditcedMeetingTime = preditcedMeetingTime;
        this.meetingPeriod = meetingPeriod;
        this.proposalEventInfo = proposalEvent;
//        this.bookedEvent.addAll(eventSourceSingleton.getEvents());
        this.leftLimitDate = dateFromDate(preditcedMeetingTime, -periot);
        this.rightLimitDate = dateFromDate(preditcedMeetingTime, periot);

        onCreation(eventSourceSingleton.getEvents());
    }

    public FreeEventProposalService(int meetingPeriod) {
        this.meetingPeriod = meetingPeriod;
    }

    private void onCreation(List<Event> allEvents) {
        Set<Event> events = filterEventsMatchingToPeriod(allEvents);
        List<Event> resEvent = getFreeEvents(events);
    }

    private List<Event> getFreeEvents(Set<Event> events){
        List<Event> eventLis = new ArrayList<>(events);
        if(eventLis.isEmpty()){
            return generateAllFreePeriod(this.leftLimitDate, this.rightLimitDate);
        }else{

            return null;
        }
//        return null;
    }

    public List<Event> generateAllFreePeriod(Date beg, Date end) {
        List<Event> res = new ArrayList<>();
        Date tmp= beg;
        while (dateComperator.compare(tmp, end)<0){
            res.addAll(generateFreeEventsInDay(tmp));
            tmp = dateFromDate(tmp, 1);
        }
        return res;
    }

    public  Set<Event> generateFreeEventsInDay(Date tmp) {
        Set<Event> res = new HashSet<>();

//        TODO tak się nie robi ! polecam rdrozdz
        try {
            Interwal cur = Interwal._8;
            while (true){
                Interwal next = Interwal.getNextInterval(cur, meetingPeriod);
                TimeSlot timeSlot = new TimeSlot(cur, next);
                cur = next;
                res.add(Event.builder()
                        .date(tmp)
                        .timeSlot(timeSlot)
                        .build());
            }
        }catch (IllegalArgumentException e){
//            ok
        }catch (Exception e){
            System.out.println("not ok");
            throw e;
        }

        return res;
    }

    private Set<Event> filterEventsMatchingToPeriod(List<Event> allEvents) {
        Set<Event> events = new TreeSet<>(eventComparator);
        for(Event e: allEvents){
            Date eDate = e.getDate();
            if(dateComperator.compare(eDate, this.leftLimitDate) >=0 && dateComperator.compare(eDate, rightLimitDate) <= 0){
                events.add(e);
            }
        }
        return events;
    }

    private Date dateFromDate(Date date, int numberOfDays){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, numberOfDays);
        return calendar.getTime();
    }

    public Event getEventProposal(){
        return null;
    }

    private boolean isWeekendDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return  dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY;
    }


}
