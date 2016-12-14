package wsd.com.wsd.comunication;


import com.google.gson.Gson;

import wsd.com.wsd.comunication.utils.JobType;
import wsd.com.wsd.comunication.utils.Performative;
import wsd.com.wsd.comunication.utils.UserProperties;
import wsd.com.wsd.comunication.utils.UserPropertyKey;
import wsd.com.wsd.comunication.utils.Utils;
import wsd.com.wsd.dto.DtoConverter;
import wsd.com.wsd.dto.NetworkInfoDto;
import wsd.com.wsd.models.Event;

public class MessageFactory {

    private static Gson gson = new Gson();


    private static final int CORDINATOR_ID =1;

    public static AgentMessage proposalEventMessage(int sender, int revicer,int conversationId, Event propolasEvent){
        return AgentMessage.builder()
                .conversationId(conversationId)
                .protocol(Utils.PROTOCOL_NAME)
                .ontology(Utils.ONTOLOGY_NAME)
                .jobType(JobType.HANDSHAKING_EVENT_TERM)
                .performative(Performative.PROPOSE)
                .senderId(sender)
                .reciverId(revicer)
                .content(gson.toJson(propolasEvent))
                .build();
    }

    public static AgentMessage onRejectMessage(AgentMessage agentMessage, String cause){
        return AgentMessage.builder()
                .conversationId(agentMessage.getConversationId())
                .protocol(Utils.PROTOCOL_NAME)
                .ontology(Utils.ONTOLOGY_NAME)
                .jobType(JobType.HANDSHAKING_EVENT_TERM)
                .performative(Performative.REJECT_PROPOSAL)
                .senderId(agentMessage.getReciverId())
                .reciverId(CORDINATOR_ID)
                .content(agentMessage.getContent())
                .userProperties(new UserProperties(UserPropertyKey.REFUSE_CAUSE, cause))
                .build();
    }

    public static AgentMessage onConfirmToNextDeviceMessage(AgentMessage agentMessage, int reciver, int sender){
        return AgentMessage.builder()
                .conversationId(agentMessage.getConversationId())
                .protocol(Utils.PROTOCOL_NAME)
                .ontology(Utils.ONTOLOGY_NAME)
                .jobType(JobType.HANDSHAKING_EVENT_TERM)
                .performative(Performative.PROPOSE)
                .senderId(sender)
                .reciverId(reciver)
                .content(agentMessage.getContent())
                .build();
    }

    public static AgentMessage networkPropagationMessage(NetworkInfoDto networkInfoDto, int conversationId){
        return AgentMessage.builder()
                .conversationId(conversationId)
                .protocol(Utils.PROTOCOL_NAME)
                .ontology(Utils.ONTOLOGY_NAME)
                .jobType(JobType.NETWORK_PROPAGATION)
                .performative(Performative.INFORM)
                .senderId(CORDINATOR_ID)
                .reciverId(networkInfoDto.getSelfDeviceInfo().getDeviceId())
                .content(DtoConverter.fromNetworkInfoDto(networkInfoDto))
                .build();
    }

    public static AgentMessage eventConfirmMessageAndStop(Event event, int reviver, int conversationId){
        return AgentMessage.builder()
                .conversationId(conversationId)
                .protocol(Utils.PROTOCOL_NAME)
                .ontology(Utils.ONTOLOGY_NAME)
                .jobType(JobType.CONFIRM_EVENT)
                .performative(Performative.CONFIRM)
                .senderId(CORDINATOR_ID)
                .reciverId(reviver)
                .content(DtoConverter.fromEvent(event))
                .build();
    }


    public static AgentMessage lastAgentConfirmToCoordinatorMessage(AgentMessage message, int sender) {
        return AgentMessage.builder()
                .conversationId(message.getConversationId())
                .protocol(Utils.PROTOCOL_NAME)
                .ontology(Utils.ONTOLOGY_NAME)
                .jobType(JobType.HANDSHAKING_EVENT_TERM)
                .performative(Performative.ACCEPT_PROPOSAL)
                .senderId(sender)
                .reciverId(CORDINATOR_ID)
                .content(message.getContent())
                .build();
    }
}
