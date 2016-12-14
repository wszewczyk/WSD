package wsd.com.wsd.service;


import org.mockito.internal.matchers.Null;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;
import wsd.com.wsd.comparators.DateComperator;
import wsd.com.wsd.comparators.EventComparator;
import wsd.com.wsd.comparators.TimeSlotsComparator;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.TimeSlot;
import wsd.com.wsd.models.types.Interwal;
import wsd.com.wsd.singletons.EventSourceSingleton;

public class FreeEventProposalService {

    private EventComparator eventComparator = new EventComparator();
    private DateComperator dateComperator = new DateComperator();

    private Date preditcedMeetingTime;
    @Setter
    private int meetingPeriod;

    @Getter
    private Iterator<Event> iterator;


    private Date leftLimitDate;
    private Date rightLimitDate;


    public FreeEventProposalService(EventSourceSingleton eventSourceSingleton, Date preditcedMeetingTime, int meetingPeriod, int periot) {
        assert eventSourceSingleton != null;
        this.preditcedMeetingTime = preditcedMeetingTime;
        this.meetingPeriod = meetingPeriod;
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
        Set<Event> sortedEvent = new TreeSet<>();
        sortedEvent.addAll(resEvent);
        iterator = sortedEvent.iterator();
    }

     Set<Event> getFreeEvents(Collection<Event> events, Date leftLimitDate, Date rightLimitDate) {
        List<Event> eventLis = explodeEvents(events);
        List<Event> genNevEvent = generateAllFreePeriod(leftLimitDate, rightLimitDate, 2, true);

        TimeSlotsComparator timeSlotsComparator = new TimeSlotsComparator();

        Set<Event> curRes = new TreeSet<>(eventComparator);
        curRes.addAll(genNevEvent);
        List<Event> nn = new ArrayList<>();


        curRes.removeAll(eventLis);


        if (meetingPeriod > 2) {
            return mergeByMettingPeriod(curRes);
        }

        return curRes;
    }

     Set<Event> mergeByMettingPeriod(Collection<Event> es) {
        LinkedList<Event> events = new LinkedList<>(es);
        Set<Event> result = new HashSet<>();
        Event cur;
        Event nextEvent;

        while (true) {
            cur = events.pollFirst();
            if (cur == null){
                break;
            }else {
                while (true){
                    nextEvent = events.pollFirst();
                    if(nextEvent==null){
                        break;
                    }else {
                        if(canAddEvents(cur, nextEvent)){
                            cur = addEvents(cur, nextEvent);
                            if(isPeriodComplite(cur)){
                                result.add(cur);
                                break;
                            }
                        }else {
                            cur = nextEvent;
                        }
                    }
                }
            }
        }

        return result;
    }

    private boolean isPeriodComplite(Event cur) {
        int beg = cur.getTimeSlot().getBegin().getInterval();
        int end = cur.getTimeSlot().getEnd().getInterval();
        int difference = end-beg;
        return difference == this.meetingPeriod;
    }

    private Event addEvents(Event cur, Event nextEvent) {
        TimeSlot timeSlot = new TimeSlot(cur.getTimeSlot().getBegin(), nextEvent.getTimeSlot().getEnd());
        cur.setTimeSlot(timeSlot);
        return cur;
    }

    private boolean canAddEvents(Event cur, Event nextEvent) {
        int compRes = dateComperator.compare(cur.getDate(), nextEvent.getDate());
        if(compRes==0){
            return cur.getTimeSlot().getEnd()==nextEvent.getTimeSlot().getBegin();
        }
        return false;
    }

     List<Event> explodeEvents(Collection<Event> events) {
        List<Event> res = new ArrayList<>();
        for (Event e : events) {
            int dif = e.getTimeSlot().getEnd().getInterval() - e.getTimeSlot().getBegin().getInterval();
            if (dif > 2) {
                Interwal cur = e.getTimeSlot().getBegin();
                for (int i = 0; i < dif / 2; i++) {
                    TimeSlot timeSlot = new TimeSlot(cur, Interwal.getNextInterval(cur, 2));
                    cur = timeSlot.getEnd();
//                    res.add(new Event( e.getName(), e.getDescription(), e.getDate(), timeSlot, e.getLocalization()));
                }
            } else {
                res.add(e);
            }
        }
        return res;
    }

     List<Event> generateAllFreePeriod(Date beg, Date end, int period, boolean genForLastOne) {
        List<Event> res = new ArrayList<>();
        Date tmp = beg;
        while (dateComperator.compare(tmp, end) < 0) {
            res.addAll(generateFreeEventsInDay(tmp, period));
            tmp = dateFromDate(tmp, 1);
        }
        if (genForLastOne) {
            res.addAll(generateFreeEventsInDay(end, period));
        }
        return res;
    }

     Set<Event> generateFreeEventsInDay(Date tmp, int period) {
        Set<Event> res = new HashSet<>();

//        TODO tak się nie robi ! polecam rdrozdz
        try {
            Interwal cur = Interwal._8;
            while (true) {
                Interwal next = Interwal.getNextInterval(cur, period);
                TimeSlot timeSlot = new TimeSlot(cur, next);
                cur = next;
                res.add(Event.builder()
                        .date(tmp)
                        .timeSlot(timeSlot)
                        .build());
            }
        } catch (IllegalArgumentException e) {
//            ok
        } catch (Exception e) {
            System.out.println("not ok");
            throw e;
        }

        return res;
    }

    private Set<Event> filterEventsMatchingToPeriod(List<Event> allEvents) {
        Set<Event> events = new TreeSet<>(eventComparator);
        for (Event e : allEvents) {
            Date eDate = e.getDate();
            if (dateComperator.compare(eDate, this.leftLimitDate) >= 0 && dateComperator.compare(eDate, rightLimitDate) <= 0) {
                events.add(e);
            }
        }
        return events;
    }

    private Date dateFromDate(Date date, int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, numberOfDays);
        return calendar.getTime();
    }

    public Event getEventProposal() {
        return null;
    }

    private boolean isWeekendDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY;
    }


}
