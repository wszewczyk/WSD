package wsd.com.wsd.singletons;


import java.util.LinkedList;
import java.util.List;

import wsd.com.wsd.comunication.AgentMessage;

public class MessagesMemory {
    private static MessagesMemory ourInstance = new MessagesMemory();

    public static MessagesMemory getInstance() {
        return ourInstance;
    }


    public LinkedList<AgentMessage> messagesMemory = new LinkedList<>();

    private MessagesMemory() {
    }

    public void addMessage(AgentMessage message){
        this.messagesMemory.add(message);
    }

    public AgentMessage getLastMessage(){
        return messagesMemory.peekLast();
    }
}
