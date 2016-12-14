package wsd.com.wsd.comunication.handlers;


import wsd.com.wsd.comunication.AgentMessage;

public interface OnMessageHendler {
    void handle(AgentMessage agentMessage);
}
