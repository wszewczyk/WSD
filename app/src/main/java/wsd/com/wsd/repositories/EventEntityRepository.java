package wsd.com.wsd.repositories;


import java.util.List;

import wsd.com.wsd.models.Event;

public interface EventEntityRepository {
    void save(Event event);
    List<Event> getAllEvents();
}
