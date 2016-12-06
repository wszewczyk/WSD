package wsd.com.wsd.interaces;

import wsd.com.wsd.models.UserCredentials;


public interface GuiAgentBrokerAgentCommunication {
    void createCalendarAgent(UserCredentials credentials); //TODO zmiana klas obiektow // credentials - dane logowania, type - typ kalendarza {Google, Microsoft)
}
