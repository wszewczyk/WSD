package wsd.com.wsd.agents;


import lombok.extern.java.Log;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.utils.AppUtils;

@Log
public class AppAgent {
    private static AppAgent ourInstance = new AppAgent();
    private CommunicationAgent communicationAgent = CommunicationAgent.getInstance();
    private BrokerAgent brokerAgent = BrokerAgent.getInstance();

    public static AppAgent getInstance() {
        return ourInstance;
    }

    private AppAgent() {
    }


    public void doAlgorythm(Event baseEvent){
        brokerAgent.loadEvents(AppUtils.dateFromDate(baseEvent.getDate(), -AppUtils.SEARCH_RANGE_SIZE),
                AppUtils.dateFromDate(baseEvent.getDate(), AppUtils.SEARCH_RANGE_SIZE));
        buildNetwork();
        propagateNetwork();
//        TODO co teraz?
    }

    private void propagateNetwork() {
        throw new IllegalStateException("add devices to lists");

    }

    private void buildNetwork() {
        throw new IllegalStateException("add devices to lists");
    }

    public void sendNextPropolas(){
//        TODO wygenerować nowy proponowany event i wysłać
        log.info("sending new event proposal");
    }

    public void acceptEventTerm(Event event){
//        TODO wyświetlić użytkownikowi propozycje i potem wysłać wiadomość o akceptacji terminu
        log.info("end of alg Hurraa");
    }
}
