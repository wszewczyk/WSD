package wsd.com.wsd.comunication.handlers;


import wsd.com.wsd.comunication.AgentMessage;

public class OnMessageHendlerImpl implements OnMessageHendler{

    private OnNetworkCreationHendler onNetworkCreationHendler =  new OnNetworkCreationHendler();
    private OnHandshakingEventTermHandler onHandshakingEventTermHandler = new OnHandshakingEventTermHandler();
    private ConfirmEventHandler confirmEventHandler = new ConfirmEventHandler();

    @Override
    public void handle(AgentMessage agentMessage) {
        switch (agentMessage.getJobType()){
            case NETWORK_PROPAGATION:
                onNetworkCreationHendler.handle(agentMessage);
                break;
            case HANDSHAKING_EVENT_TERM:
                onHandshakingEventTermHandler.handle(agentMessage);
                break;
            case CONFIRM_EVENT:
                confirmEventHandler.handle(agentMessage);
        }
    }



}
