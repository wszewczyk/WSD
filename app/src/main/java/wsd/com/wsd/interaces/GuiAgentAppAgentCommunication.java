package wsd.com.wsd.interaces;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tobia on 06.12.2016.
 */

public interface GuiAgentAppAgentCommunication {
    void setGroup(ArrayList<Object> users); //TODO: zmienic typ obiektow po ustaleniu formatu klasy user czy devices, kwestia nazewnictwa do ustalenia
    void setProposedDate(Date proposedDate, Date start, Date end); // start, end - parametry definiujace zakres przeszukiwania
    void startNegotiation(); // TODO: punkt 5 - algorytm
}
