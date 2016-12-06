package wsd.com.wsd.interaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public interface AppAgentBrokerAgentCommunication {
    List<Object> getEvents(Date start, Date end); //TODO zmiana klasy obiektu // start, end - zakres, z ktorego nalezy pobrac wydarzenia
    void saveEvent(Object event); //TODO zmiana klasy obiektu
}
