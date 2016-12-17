package wsd.com.wsd.agents;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.java.Log;
import wsd.com.wsd.interaces.AppAgentBrokerAgentCommunication;
import wsd.com.wsd.models.Event;

@Log
public class BrokerAgent implements AppAgentBrokerAgentCommunication {
    private static BrokerAgent ourInstance = new BrokerAgent();



    public static BrokerAgent getInstance() {
        return ourInstance;
    }

    private BrokerAgent() {
    }

    @Override
    public void loadEvents(Date start, Date end) {
       log.info("add events to EventSourceSingleton");
    }

    @Override
    public void saveEvent(Event event) {
        log.info("save some event : "+event.toString());
    }
}
