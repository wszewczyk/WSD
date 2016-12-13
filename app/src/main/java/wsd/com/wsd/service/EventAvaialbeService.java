package wsd.com.wsd.service;


import lombok.Setter;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.singletons.EventSourceSingleton;

public class EventAvaialbeService {
    private static EventAvaialbeService ourInstance = new EventAvaialbeService();

    @Setter
    private EventSourceSingleton eventSourceSingleton;
    private EventAvaiableHender eventAvaiableHender;


    public static EventAvaialbeService getInstance() {
        return ourInstance;
    }

    private EventAvaialbeService() {
    }

    public boolean isEventAvaiable(Event event){
        onCreate();
        return eventAvaiableHender.handle(event);
    }

    private void onCreate() {
        this.eventAvaiableHender= new FindInDBEventAvaiableHendler(eventSourceSingleton);
    }

}
