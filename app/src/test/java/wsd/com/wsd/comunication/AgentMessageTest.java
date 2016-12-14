package wsd.com.wsd.comunication;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import wsd.com.wsd.comunication.utils.Performative;

import static org.junit.Assert.*;

public class AgentMessageTest {

    private static final String USER_PROP_KEY_1 = "User Prp KEY 1";
    private static final String USER_PROP_VAL_1 = "User Prp VAL 1";
    private static final int SENDER = 1;
    private static final int RECIVER = 2;
    private static final String CONTENT = "CONTENT";
    private static final int CONVERSATION_ID = 123;
    private static final Performative PERFORMATIVE_SEND = Performative.PROPOSE;
    private static final Performative PERFORMATIVE_REVICE = Performative.AGREEE;


    private Gson gson = new Gson();


    private AgentMessage message;
    private String strMessage;


    @Before
    public void setUp() throws Exception {
        message = new AgentMessage();
        message.addProperty(USER_PROP_KEY_1, USER_PROP_VAL_1);
        message.setSenderId(SENDER);
        message.setReciverId(RECIVER);
        message.setContent(CONTENT);
        message.setConversationId(CONVERSATION_ID);
        message.setPerformative(PERFORMATIVE_SEND);

        strMessage = gson.toJson(message);
    }

    @Test
    public void convertACLMessagoToJSONTest(){
        String res = CommunicatParser.convertACLMessagoToJSON(message);
        assertEquals(strMessage, res);
    }

    @Test
    public void convertStringToACLMessageTest(){
        AgentMessage res = CommunicatParser.convertStringToACLMessage(strMessage);
        assertEquals(message, res);
    }
    @Test
    public void replayMessageTest(){
        AgentMessage agentMessage = message.getReplayMessage(PERFORMATIVE_REVICE);

        assertEquals(SENDER, agentMessage.getReciverId());
        assertEquals(RECIVER, agentMessage.getSenderId());
        assertEquals(PERFORMATIVE_REVICE, agentMessage.getPerformative());

    }

}