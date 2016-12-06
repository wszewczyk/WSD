package wsd.com.wsd.interaces;

import java.util.ArrayList;

/**
 * Created by tobia on 06.12.2016.
 */

public interface AppAgentCommunicationAgentCommunication {
    void send(String message);
    String receiveMessage();
    boolean connect(ArrayList<Object> devices);
}
