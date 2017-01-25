package wsd.com.wsd.comunication.handlers;


import wsd.com.wsd.comunication.AgentMessage;
import wsd.com.wsd.helpers.OnTestMessageHendler;
import wsd.com.wsd.singletons.MessagesMemory;

public class OnMessageHendlerImpl implements OnMessageHendler{

    private OnNetworkCreationHendler onNetworkCreationHendler =  new OnNetworkCreationHendler();
    private OnTestMessageHendler onHardMessageHendler = new OnTestMessageHendler();
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
