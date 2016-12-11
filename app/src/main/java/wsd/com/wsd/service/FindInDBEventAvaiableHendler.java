package wsd.com.wsd.service;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.Setter;
import wsd.com.wsd.comparators.TimeSlotsComparator;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.TimeSlot;
import wsd.com.wsd.singletons.EventSourceSingleton;

public class FindInDBEventAvaiableHendler implements EventAvaiableHender {

    private EventAvaiableHender eventAvaiableHender;
    @Setter
    private EventSourceSingleton eventSourceSingleton;
    private TimeSlotsComparator slotsComparator = new TimeSlotsComparator();

    public FindInDBEventAvaiableHendler(EventAvaiableHender eventAvaiableHender) {
        this.eventAvaiableHender = eventAvaiableHender;
    }

    public FindInDBEventAvaiableHendler() {
    }

    public FindInDBEventAvaiableHendler(EventSourceSingleton eventSourceSingleton) {
        this.eventSourceSingleton = eventSourceSingleton;
    }

    @Override
    public boolean handle(Event event) {
        boolean result = isEventAvaiable(event);
        if (eventAvaiableHender != null && result) {
            return eventAvaiableHender.handle(event);
        } else {
            return result;
        }
    }

    private boolean isEventAvaiable(Event event) {
        if(eventSourceSingleton==null){
            eventSourceSingleton = EventSourceSingleton.getInstance();
        }
        List<Event> events = eventSourceSingleton.getEvents();
        for (Event e : events) {
            if (isSameDay(e.getDate(), event.getDate())) {
                if (isTheSameSlotTime(e.getTimeSlot(), event.getTimeSlot())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isTheSameSlotTime(TimeSlot currentEvent, TimeSlot proposalEvent) {
        int res = slotsComparator.compare(currentEvent, proposalEvent);
        return res == 100 || res == 0;
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        boolean sameYear = calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
        boolean sameMonth = calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
        boolean sameDay = calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
        return (sameDay && sameMonth && sameYear);
    }
}
