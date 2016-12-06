package wsd.com.wsd.interaces;

import java.util.ArrayList;

/**
 * Created by tobia on 06.12.2016.
 */

public interface GuiAgentCommunicationAgentCommunication {
    void searchForBTDevices();
    ArrayList<Object> receiveListOfDevices(); //TODO: zmienic typ obiektow po ustaleniu struktury klasy
}
