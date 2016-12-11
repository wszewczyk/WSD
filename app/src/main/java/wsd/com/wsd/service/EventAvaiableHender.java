package wsd.com.wsd.service;

import wsd.com.wsd.models.Event;

public interface EventAvaiableHender {
    public boolean handle(Event event);
}
