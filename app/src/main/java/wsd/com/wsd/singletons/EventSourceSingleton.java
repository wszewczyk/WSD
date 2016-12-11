package wsd.com.wsd.singletons;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.Localization;
import wsd.com.wsd.models.TimeSlot;
import wsd.com.wsd.models.types.Interwal;

public class EventSourceSingleton {
    private static EventSourceSingleton ourInstance = new EventSourceSingleton();
    @Getter
    private List<Event> events = new ArrayList<>();

    public static EventSourceSingleton getInstance() {
        return ourInstance;
    }

    private EventSourceSingleton() {
        addSomeEvents();
    }

    private void addSomeEvents(){
        events.add(new Event("event 1", "desc 1", new Date(), new TimeSlot(Interwal._8, Interwal._10), new Localization(52.10, 21)));
        events.add(new Event("event 1", "desc 1", new Date(), new TimeSlot(Interwal._10, Interwal._12), new Localization(52.10, 21)));
        events.add(new Event("event 1", "desc 1", new Date(), new TimeSlot(Interwal._12, Interwal._18), new Localization(52.10, 21)));
    }
}
