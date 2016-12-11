package wsd.com.wsd.comunication;


import java.util.Map;

import jade.lang.acl.ACLMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import wsd.com.wsd.comunication.utils.Performative;
import wsd.com.wsd.comunication.utils.UserProperties;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode
public class AgentMessage {
    private Performative performative;
    private String senderId;
    private String reciverId;
    private String language;
    private String ontology;
    private String protocol;
    private String conversationId;
    private String content;
    private UserProperties userProperties = new UserProperties();

    public AgentMessage getReplayMessage(Performative performative){
        return AgentMessage.builder()
                .performative(performative)
                .senderId(this.reciverId)
                .reciverId(this.senderId)
                .language(this.language)
                .ontology(this.ontology)
                .protocol(this.protocol)
                .conversationId(this.conversationId)
                .build();
    }

    public void addProperty(String key, String value){
        this.userProperties.addProperty(key, value);
    }

    public String getUserProperty(String key){
        return this.userProperties.getProperty(key);
    }


}
