package wsd.com.wsd.comunication.handlers;


import wsd.com.wsd.agents.AppAgent;
import wsd.com.wsd.agents.CommunicationAgent;
import wsd.com.wsd.comunication.AgentMessage;
import wsd.com.wsd.comunication.MessageFactory;
import wsd.com.wsd.comunication.utils.Performative;
import wsd.com.wsd.dto.DtoConverter;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.service.EventAvaialbeService;
import wsd.com.wsd.singletons.DeviceSingleton;
import wsd.com.wsd.singletons.EventSourceSingleton;

public class OnHandshakingEventTermHandler implements OnMessageHendler {

    private DeviceSingleton deviceSingleton = DeviceSingleton.getInstance();
    private AppAgent appAgent = AppAgent.getInstance();
    private CommunicationAgent communicationAgent = CommunicationAgent.getInstance();
    private EventAvaialbeService eventAvaialbeService = EventAvaialbeService.getInstance();

    public OnHandshakingEventTermHandler() {
        eventAvaialbeService.setEventSourceSingleton(EventSourceSingleton.getInstance());
    }

    @Override
    public void handle(AgentMessage agentMessage) {
        Performative performative = agentMessage.getPerformative();
        if(performative==Performative.PROPOSE && (agentMessage.getSenderId()+1==deviceSingleton.getSelfUserDevice().getDeviceId())){
            checkProposalAndSend(agentMessage);
        }
        if(performative==Performative.REJECT_PROPOSAL && deviceSingleton.isCoordinator()){
            rejectAndSendNewProposalAsCordinator(agentMessage);
        }
    }

    private void rejectAndSendNewProposalAsCordinator(AgentMessage agentMessage) {
        appAgent.sendNextPropolas();
    }

    private void checkProposalAndSend(AgentMessage agentMessage) {
        Event event = DtoConverter.toEvent(agentMessage.getContent());
        boolean isEventAvaiable = eventAvaialbeService.isEventAvaiable(event);
        AgentMessage response;
        if(isEventAvaiable){
            if(deviceSingleton.isLastDevice()){
                response = MessageFactory.lastAgentConfirmToCoordinatorMessage(agentMessage, deviceSingleton.getSelfUserDevice().getDeviceId());
            }else {
                response = MessageFactory.onConfirmToNextDeviceMessage(agentMessage, deviceSingleton.getNextDevice().getDeviceId(), deviceSingleton.getSelfUserDevice().getDeviceId());
            }
        }else {
             response = MessageFactory.onRejectMessage(agentMessage, "not avaiable");
        }
        communicationAgent.send(response);
    }
}
