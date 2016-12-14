package wsd.com.wsd.agents;


import lombok.extern.java.Log;

@Log
public class AppAgent {
    private static AppAgent ourInstance = new AppAgent();

    public static AppAgent getInstance() {
        return ourInstance;
    }

    private AppAgent() {
    }

    public void sendNextPropolas(){
        log.info("sending new event proposal");
    }
}
