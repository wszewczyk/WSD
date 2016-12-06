package wsd.com.wsd.interaces;

import java.util.ArrayList;

import wsd.com.wsd.models.UserDevice;


public interface AppAgentCommunicationAgentCommunication {
    void send(String deviceName, String message);
    String receiveMessage();
    boolean connect(ArrayList<UserDevice> devices);
}
