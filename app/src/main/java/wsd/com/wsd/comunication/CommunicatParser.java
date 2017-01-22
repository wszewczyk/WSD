package wsd.com.wsd.comunication;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jade.lang.acl.ACLMessage;

public class CommunicatParser {
    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    public static AgentMessage convertStringToACLMessage(String message){
        AgentMessage agentMessage = gson.fromJson(message, AgentMessage.class);
        return agentMessage;
    }

    public static String convertACLMessagoToJSON(AgentMessage agentMessage){
        return gson.toJson(agentMessage);
    }

    
}
