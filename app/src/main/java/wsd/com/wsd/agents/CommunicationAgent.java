package wsd.com.wsd.agents;


import lombok.extern.java.Log;
import wsd.com.wsd.app.WSD;
import wsd.com.wsd.comunication.AgentMessage;
import wsd.com.wsd.comunication.CommunicatParser;
import wsd.com.wsd.interaces.AppAgentCommunicationAgentCommunication;
import wsd.com.wsd.singletons.DeviceSingleton;

@Log
public class CommunicationAgent implements AppAgentCommunicationAgentCommunication{

    DeviceSingleton ds = DeviceSingleton.getInstance();

    private static CommunicationAgent ourInstance = new CommunicationAgent();

    public static CommunicationAgent getInstance() {
        return ourInstance;
    }

    private CommunicationAgent() {
        System.out.println("hehe wanczam");
    }

    @Override
    public void send(AgentMessage agentMessage) {
        WSD.getBcs().sendMessage(ds.getMacAddressByDeviceId(agentMessage.getReciverId()), CommunicatParser.convertACLMessagoToJSON(agentMessage));
        log.info("send some message");
    }
}
