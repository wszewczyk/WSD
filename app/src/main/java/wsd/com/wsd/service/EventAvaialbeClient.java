package wsd.com.wsd.service;


import lombok.Setter;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.singletons.EventSourceSingleton;

public class EventAvaialbeClient {
    private static EventAvaialbeClient ourInstance = new EventAvaialbeClient();

    @Setter
    private EventSourceSingleton eventSourceSingleton;
    private EventAvaiableHender eventAvaiableHender;


    public static EventAvaialbeClient getInstance() {
        return ourInstance;
    }

    private EventAvaialbeClient() {
    }

    public boolean isEventAvaiable(Event event){
        onCreate();
        return eventAvaiableHender.handle(event);
    }

    private void onCreate() {
        this.eventAvaiableHender= new FindInDBEventAvaiableHendler(eventSourceSingleton);
    }

}
