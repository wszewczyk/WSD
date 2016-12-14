package wsd.com.wsd.comunication.handlers;


import wsd.com.wsd.agents.BrokerAgent;
import wsd.com.wsd.comunication.AgentMessage;
import wsd.com.wsd.dto.DtoConverter;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.singletons.MessagesMemory;

public class OnConfirmEventHandler implements OnMessageHendler{

    private BrokerAgent brokerAgent = BrokerAgent.getInstance();
    private MessagesMemory messagesMemory = MessagesMemory.getInstance();

    @Override
    public void handle(AgentMessage agentMessage) {
        AgentMessage lastMessage = messagesMemory.getLastMessage();
        assert lastMessage!=null;

        int currConversationId = agentMessage.getConversationId();
        int lastConversationId = lastMessage.getConversationId();

        if(currConversationId!=lastConversationId){
            throw new IllegalStateException(String.format("illegal converstation ID: %s, required: %s",currConversationId, lastConversationId));
        }

        Event event = DtoConverter.toEvent(agentMessage.getContent());

        brokerAgent.saveEvent(event);

    }
}
