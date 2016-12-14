package wsd.com.wsd.agents;


import lombok.extern.java.Log;
import wsd.com.wsd.comunication.AgentMessage;
import wsd.com.wsd.interaces.AppAgentCommunicationAgentCommunication;

@Log
public class CommunicationAgent implements AppAgentCommunicationAgentCommunication{
    private static CommunicationAgent ourInstance = new CommunicationAgent();

    public static CommunicationAgent getInstance() {
        return ourInstance;
    }

    private CommunicationAgent() {
    }

    @Override
    public void send(AgentMessage agentMessage) {
        log.info("send some message");
    }
}
