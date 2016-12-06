package wsd.com.wsd.interaces;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tobia on 06.12.2016.
 */

public interface AppAgentBrokerAgentCommunication {
    ArrayList<Object> getEvents(Date start, Date end); //TODO zmiana klasy obiektu // start, end - zakres, z ktorego nalezy pobrac wydarzenia
    void saveEvent(Object event); //TODO zmiana klasy obiektu
}
