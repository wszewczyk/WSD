package wsd.com.wsd.singletons;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.Localization;
import wsd.com.wsd.models.TimeSlot;
import wsd.com.wsd.models.types.Interwal;

public class EventSourceSingleton {
    private static EventSourceSingleton ourInstance = new EventSourceSingleton();
    @Getter
    private List<Event> events = new ArrayList<>();

    @Getter @Setter
    private Event acceptedEvent;

    public static EventSourceSingleton getInstance() {
        return ourInstance;
    }

    private EventSourceSingleton() {
    }
}
