package wsd.com.wsd.hard;


import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import wsd.com.wsd.agents.AppAgent;
import wsd.com.wsd.agents.CommunicationAgent;
import wsd.com.wsd.app.WSD;
import wsd.com.wsd.comparators.EventComparator;
import wsd.com.wsd.comunication.AgentMessage;
import wsd.com.wsd.comunication.CommunicatParser;
import wsd.com.wsd.comunication.MessageFactory;
import wsd.com.wsd.comunication.handlers.OnConfirmEventHandler;
import wsd.com.wsd.comunication.handlers.OnHandshakingEventTermHandler;
import wsd.com.wsd.comunication.handlers.OnMessageHendler;
import wsd.com.wsd.comunication.handlers.OnNetworkCreationHendler;
import wsd.com.wsd.comunication.utils.Performative;
import wsd.com.wsd.dto.DtoConverter;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.service.EventAvaialbeService;
import wsd.com.wsd.singletons.DeviceSingleton;
import wsd.com.wsd.singletons.EventSourceSingleton;
import wsd.com.wsd.singletons.MessagesMemory;

public class OnHardMessageHendler implements OnMessageHendler{
    private DeviceSingleton deviceSingleton = DeviceSingleton.getInstance();
    public OnHardMessageHendler() {

    }

    @Override
    public void handle(AgentMessage agentMessage) {
        Performative performative = agentMessage.getPerformative();
        if(performative==Performative.PROPOSE){ // sprawdzenie eventu i przekazanie dalej albo odrzucenie |sprawdzenie czy jestem ostatni|
            checkProposalAndSend(agentMessage);
        }
        if(performative==Performative.REJECT_PROPOSAL && deviceSingleton.isCoordinator()){ // ogarniecie odrzucenia przez koordynatora
            rejectAndSendNewProposalAsCordinator(agentMessage);
        }
        if(performative==Performative.ACCEPT_PROPOSAL && deviceSingleton.isCoordinator()){
            acceptNegotiation(agentMessage);
        }
    }

    public void checkProposalAndSend(AgentMessage agentMessage) {
        final AgentMessage am = agentMessage;
        Log.e("Hard", "PROPOZYCJA");
        boolean isEnable = false;
        EventComparator eventComparator = new EventComparator();
        for (Event event : HardMemory.getMemoryByDeviceId(agentMessage.getReciverId())) {
            if (eventComparator.compare(event, agentMessage.getEvent()) == 0) {
                isEnable = true;
                break;
            }
        }

         if (isEnable == false) {
             Log.e("Hard", "PROPOZYCJA - NIE");
             new Timer().schedule(new TimerTask() {
                 @Override
                 public void run() {
                     String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.onRejectMessage(am, "KTO WIE"));
                     WSD.getBcs().sendMessage(DeviceSingleton.getInstance().getMacAddressByDeviceId(1), message);
                 }
             }, 1000);

         } else {
             if (DeviceSingleton.getInstance().isLastDevice()) {
                 Log.e("Hard", "PROPOZYCJA - TAK");
                 new Timer().schedule(new TimerTask() {
                     @Override
                     public void run() {
                         String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.lastAgentConfirmToCoordinatorMessage(am, am.getReciverId()));
                         WSD.getBcs().sendMessage(DeviceSingleton.getInstance().getMacAddressByDeviceId(1), message);
                     }
                 }, 1000);

             } else {
                 Log.e("Hard", "PROPOZYCJA - DALEJ");
                 new Timer().schedule(new TimerTask() {
                     @Override
                     public void run() {
                         String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.onConfirmToNextDeviceMessage(am, am.getReciverId()+1, am.getReciverId()));
                         WSD.getBcs().sendMessage(DeviceSingleton.getInstance().getMacAddressByDeviceId(am.getReciverId()+1), message);
                     }
                 }, 1000);
             }
         }
        // 1. sprawdzenie wolnego terminu
        //2. jesli nie wolny to wyslij odrzucenie,
        //3. jesli wolny
        //a. jezeli jestem ostatni to wyslij do koordynatora wiadomosc z proposal_accept
        //b. przeslij dalej
    }

    private void rejectAndSendNewProposalAsCordinator(AgentMessage agentMessage) {
        Log.e("Hard", "ODRZUCENIE");
        final AgentMessage am = agentMessage;
        if (agentMessage.getEvent().getId() <= HardMemory.getMemoryByDeviceId(1).size()) {
            Log.e("Hard", "ODRZUCENIE - NOWY");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.proposalEventMessage(1, 2, am.getConversationId() + 11, HardMemory.getMemoryByDeviceId(1).get(am.getEvent().getId() + 1)));
                    WSD.getBcs().sendMessage(DeviceSingleton.getInstance().getMacAddressByDeviceId(2), message);
                }
            }, 1000);

        } else {
            Log.e("Hard", "ODRZUCENIE - KONIEC");
        }

        // 1. pobierz nastepny event z hardcoded list i wyslij dalej
    }

    private void acceptNegotiation(AgentMessage agentMessage) {
        Log.e("Hard", "KONIEC");
        final AgentMessage am = agentMessage;
        EventSourceSingleton.getInstance().setAcceptedEvent(agentMessage.getEvent());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.eventConfirmMessageAndStop(am.getEvent(), 2, 3003));
                WSD.getBcs().sendMessage(DeviceSingleton.getInstance().getMacAddressByDeviceId(2), message);
            }
        }, 1000);
        //1. wyswietl good job fuck yeah!
    }
}
