package wsd.com.wsd.agents;


import lombok.extern.java.Log;
import wsd.com.wsd.models.Event;

@Log
public class AppAgent {
    private static AppAgent ourInstance = new AppAgent();

    public static AppAgent getInstance() {
        return ourInstance;
    }

    private AppAgent() {
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
