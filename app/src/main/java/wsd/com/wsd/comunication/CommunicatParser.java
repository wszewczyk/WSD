package wsd.com.wsd.comunication;


import com.google.gson.Gson;

import jade.lang.acl.ACLMessage;

public class CommunicatParser {
    private static Gson gson = new Gson();
    public static AgentMessage convertStringToACLMessage(String message){
        AgentMessage agentMessage = gson.fromJson(message, AgentMessage.class);
        return agentMessage;
    }

    public static String convertACLMessagoToJSON(AgentMessage agentMessage){
        return gson.toJson(agentMessage);
    }
}
