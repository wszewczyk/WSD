package wsd.com.wsd.service;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import wsd.com.wsd.comparators.DateComperator;
import wsd.com.wsd.comparators.EventComparator;
import wsd.com.wsd.comparators.TimeSlotsComparator;
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
        Set<Event> resEvent = getFreeEvents(events, this.leftLimitDate, this.rightLimitDate);
    }

    public Set<Event> getFreeEvents(Collection<Event> events, Date leftLimitDate, Date rightLimitDate){
        List<Event> eventLis = explodeEvents(events);
        List<Event> genNevEvent = generateAllFreePeriod(leftLimitDate, rightLimitDate, 2, true);

        TimeSlotsComparator timeSlotsComparator = new TimeSlotsComparator();

        Set<Event> curRes = new TreeSet<>(eventComparator);
        curRes.addAll(genNevEvent);
        List<Event> nn = new ArrayList<>();


        curRes.removeAll(eventLis);


        if(meetingPeriod>2){
            return mergeByMettingPeriod(curRes);
        }

        return curRes;
    }

    private Set<Event> mergeByMettingPeriod(Collection<Event> curRes) {
        return null;
    }

    public List<Event> explodeEvents(Collection<Event> events) {
        List<Event> res = new ArrayList<>();
        for(Event e: events){
            int dif = e.getTimeSlot().getEnd().getInterval()-e.getTimeSlot().getBegin().getInterval();
            if(dif>2){
                Interwal cur = e.getTimeSlot().getBegin();
                for (int i = 0; i < dif / 2; i++) {
                    TimeSlot timeSlot = new TimeSlot(cur, Interwal.getNextInterval(cur, 2));
                    cur = timeSlot.getEnd();
                    res.add(new Event(e.getName(), e.getDescription(), e.getDate(), timeSlot , e.getLocalization()));
                }
            }else {
                res.add(e);
            }
        }
        return res;
    }

    public List<Event> generateAllFreePeriod(Date beg, Date end, int period, boolean genForLastOne) {
        List<Event> res = new ArrayList<>();
        Date tmp= beg;
        while (dateComperator.compare(tmp, end)<0){
            res.addAll(generateFreeEventsInDay(tmp, period));
            tmp = dateFromDate(tmp, 1);
        }
        if(genForLastOne){
            res.addAll(generateFreeEventsInDay(end, period));
        }
        return res;
    }

    public  Set<Event> generateFreeEventsInDay(Date tmp, int period) {
        Set<Event> res = new HashSet<>();

//        TODO tak się nie robi ! polecam rdrozdz
        try {
            Interwal cur = Interwal._8;
            while (true){
                Interwal next = Interwal.getNextInterval(cur, period);
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
