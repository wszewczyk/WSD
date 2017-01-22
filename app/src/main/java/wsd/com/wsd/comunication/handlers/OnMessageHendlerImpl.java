package wsd.com.wsd.comunication.handlers;


import wsd.com.wsd.comunication.AgentMessage;
import wsd.com.wsd.hard.OnHardMessageHendler;
import wsd.com.wsd.singletons.MessagesMemory;

public class OnMessageHendlerImpl implements OnMessageHendler{

    private OnNetworkCreationHendler onNetworkCreationHendler =  new OnNetworkCreationHendler();
    private OnHardMessageHendler onHardMessageHendler = new OnHardMessageHendler();
    private OnConfirmEventHandler confirmEventHandler = new OnConfirmEventHandler();

    MessagesMemory messagesMemory = MessagesMemory.getInstance();

    @Override
    public void handle(AgentMessage agentMessage) {



        switch (agentMessage.getJobType()){
            case NETWORK_PROPAGATION:
                onNetworkCreationHendler.handle(agentMessage);
                break;
            case HANDSHAKING_EVENT_TERM:
                onHardMessageHendler.handle(agentMessage);
                break;
            case CONFIRM_EVENT:
                confirmEventHandler.handle(agentMessage);
                break;
        }

//        messagesMemory.addMessage(agentMessage);
    }



}
