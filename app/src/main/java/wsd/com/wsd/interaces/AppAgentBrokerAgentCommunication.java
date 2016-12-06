package wsd.com.wsd.interaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wsd.com.wsd.models.Event;


public interface AppAgentBrokerAgentCommunication {
    List<Event> getEvents(Date start, Date end); //TODO zmiana klasy obiektu // start, end - zakres, z ktorego nalezy pobrac wydarzenia
    void saveEvent(Event event); //TODO zmiana klasy obiektu
}
