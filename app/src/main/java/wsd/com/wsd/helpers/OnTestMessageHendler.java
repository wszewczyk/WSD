package wsd.com.wsd.helpers;


import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import wsd.com.wsd.app.WSD;
import wsd.com.wsd.comparators.EventComparator;
import wsd.com.wsd.comunication.AgentMessage;
import wsd.com.wsd.comunication.CommunicatParser;
import wsd.com.wsd.comunication.MessageFactory;
import wsd.com.wsd.comunication.handlers.OnMessageHendler;
import wsd.com.wsd.comunication.utils.Performative;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.singletons.DeviceSingleton;
import wsd.com.wsd.singletons.EventSourceSingleton;

public class OnTestMessageHendler implements OnMessageHendler{

    private static final String ALGORITHM_TAG = "ALGORITHM";

    private DeviceSingleton deviceSingleton = DeviceSingleton.getInstance();
    public OnTestMessageHendler() {

    }

    @Override
    public void handle(AgentMessage agentMessage) {
        Performative performative = agentMessage.getPerformative();
        if(performative==Performative.PROPOSE){
            checkProposalAndSend(agentMessage);
        }
        if(performative==Performative.REJECT_PROPOSAL && deviceSingleton.isCoordinator()){
            rejectAndSendNewProposalAsCordinator(agentMessage);
        }
        if(performative==Performative.ACCEPT_PROPOSAL && deviceSingleton.isCoordinator()){
            acceptNegotiation(agentMessage);
        }
    }

    public void checkProposalAndSend(AgentMessage agentMessage) {
        final AgentMessage am = agentMessage;
        Log.e(ALGORITHM_TAG, "PROPOZYCJA");
        boolean isEnable = false;
        EventComparator eventComparator = new EventComparator();
        for (Event event : TestDatabase.getMemoryByDeviceId(agentMessage.getReciverId())) {
            if (eventComparator.compare(event, agentMessage.getEvent()) == 0) {
                isEnable = true;
                break;
            }
        }

         if (isEnable == false) {
             Log.e(ALGORITHM_TAG, "PROPOZYCJA - NIE");
             new Timer().schedule(new TimerTask() {
                 @Override
                 public void run() {
                     String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.onRejectMessage(am, "Error"));
                     WSD.getBcs().sendMessage(DeviceSingleton.getInstance().getMacAddressByDeviceId(1), message);
                 }
             }, 1000);

         } else {
             if (DeviceSingleton.getInstance().isLastDevice()) {
                 Log.e(ALGORITHM_TAG, "PROPOZYCJA - TAK");
                 new Timer().schedule(new TimerTask() {
                     @Override
                     public void run() {
                         String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.lastAgentConfirmToCoordinatorMessage(am, am.getReciverId()));
                         WSD.getBcs().sendMessage(DeviceSingleton.getInstance().getMacAddressByDeviceId(1), message);
                     }
                 }, 1000);

             } else {
                 Log.e(ALGORITHM_TAG, "PROPOZYCJA - DALEJ");
                 new Timer().schedule(new TimerTask() {
                     @Override
                     public void run() {
                         String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.onConfirmToNextDeviceMessage(am, am.getReciverId()+1, am.getReciverId()));
                         WSD.getBcs().sendMessage(DeviceSingleton.getInstance().getMacAddressByDeviceId(am.getReciverId()+1), message);
                     }
                 }, 1000);
             }
         }
    }

    private void rejectAndSendNewProposalAsCordinator(AgentMessage agentMessage) {
        Log.e(ALGORITHM_TAG, "ODRZUCENIE");
        final AgentMessage am = agentMessage;
        if (agentMessage.getEvent().getId() <= TestDatabase.getMemoryByDeviceId(1).size()) {
            Log.e(ALGORITHM_TAG, "ODRZUCENIE - NOWY");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.proposalEventMessage(1, 2, am.getConversationId() + 11, TestDatabase.getMemoryByDeviceId(1).get(am.getEvent().getId() + 1)));
                    WSD.getBcs().sendMessage(DeviceSingleton.getInstance().getMacAddressByDeviceId(2), message);
                }
            }, 1000);

        } else {
            Log.e(ALGORITHM_TAG, "ODRZUCENIE - KONIEC");
        }
    }

    private void acceptNegotiation(AgentMessage agentMessage) {
        Log.e(ALGORITHM_TAG, "KONIEC");
        final AgentMessage am = agentMessage;
        EventSourceSingleton.getInstance().setAcceptedEvent(agentMessage.getEvent());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String message = CommunicatParser.convertACLMessagoToJSON(MessageFactory.eventConfirmMessageAndStop(am.getEvent(), 2, 3003));
                WSD.getBcs().sendMessage(DeviceSingleton.getInstance().getMacAddressByDeviceId(2), message);
            }
        }, 1000);
    }
}
