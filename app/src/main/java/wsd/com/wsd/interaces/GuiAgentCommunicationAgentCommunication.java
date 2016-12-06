package wsd.com.wsd.interaces;

import java.util.ArrayList;
import java.util.List;

import wsd.com.wsd.models.UserDevice;


public interface GuiAgentCommunicationAgentCommunication {
    List<UserDevice> searchForBTDevices();
//    ArrayList<Object> receiveListOfDevices(); //TODO: zmienic typ obiektow po ustaleniu struktury klasy
}
