package wsd.com.wsd.comunication;


import com.google.gson.Gson;

import jade.lang.acl.ACLMessage;

public class CommunicatParser {
    private static Gson gson = new Gson();
    public static ACLMessage convertStringToACLMessage(String message){
        ACLMessage aclMessage = gson.fromJson(message, ACLMessage.class);
        return aclMessage;
    }

    public static String convertACLMessagoToJSON(ACLMessage aclMessage){
        return gson.toJson(aclMessage);
    }
}
