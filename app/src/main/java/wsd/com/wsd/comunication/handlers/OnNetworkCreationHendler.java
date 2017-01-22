package wsd.com.wsd.comunication.handlers;


import wsd.com.wsd.comunication.AgentMessage;
import wsd.com.wsd.dto.DtoConverter;
import wsd.com.wsd.dto.NetworkInfoDto;
import wsd.com.wsd.singletons.DeviceSingleton;

public class OnNetworkCreationHendler implements OnMessageHendler {

    private DeviceSingleton deviceSingleton = DeviceSingleton.getInstance();



    @Override
    public void handle(AgentMessage agentMessage) {
        deviceSingleton.updateInfo(agentMessage.getNetworkInfoDto().getDevicesNetowrk(), agentMessage.getNetworkInfoDto().getSelfDeviceInfo());
    }
}
