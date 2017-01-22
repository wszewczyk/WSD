package wsd.com.wsd.comunication;


import java.util.Map;

import jade.lang.acl.ACLMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import wsd.com.wsd.comunication.utils.JobType;
import wsd.com.wsd.comunication.utils.Performative;
import wsd.com.wsd.comunication.utils.UserProperties;
import wsd.com.wsd.dto.NetworkInfoDto;
import wsd.com.wsd.models.Event;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AgentMessage {
    private Performative performative;
    private int senderId;
    private int reciverId;
    private String language;
    private String ontology;
    private JobType jobType;
    private String protocol;
    private int conversationId;
    private String content;
    private NetworkInfoDto networkInfoDto;
    private Event event;
    private UserProperties userProperties = new UserProperties();

    public AgentMessage(Performative performative, int senderId, int reciverId,
                        String language, String ontology, String protocol,
                        int conversationId, NetworkInfoDto networkInfoDto, Event event) {
        this.performative = performative;
        this.senderId = senderId;
        this.reciverId = reciverId;
        this.language = language;
        this.ontology = ontology;
        this.protocol = protocol;
        this.conversationId = conversationId;
        this.networkInfoDto = networkInfoDto;
        this.event = event;
    }

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
