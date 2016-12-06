package wsd.com.wsd.interaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wsd.com.wsd.models.UserDevice;



public interface GuiAgentAppAgentCommunication {
    void setGroup(List<UserDevice> users); //TODO: zmienic typ obiektow po ustaleniu formatu klasy user czy devices, kwestia nazewnictwa do ustalenia
    void setProposedDate(Date proposedDate, Date start, Date end); // start, end - parametry definiujace zakres przeszukiwania
    void startNegotiation(); // TODO: punkt 5 - algorytm
}
