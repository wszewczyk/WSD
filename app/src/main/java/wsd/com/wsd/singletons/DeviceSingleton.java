package wsd.com.wsd.singletons;


import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import lombok.Getter;
import lombok.extern.java.Log;
import wsd.com.wsd.app.WSD;
import wsd.com.wsd.comparators.UserDeviceComparator;
import wsd.com.wsd.comunication.CommunicatParser;
import wsd.com.wsd.comunication.MessageFactory;
import wsd.com.wsd.dto.NetworkInfoDto;
import wsd.com.wsd.models.UserDevice;

@Log
public class DeviceSingleton {
    private static DeviceSingleton ourInstance = new DeviceSingleton();

    @Getter
    private UserDevice selfUserDevice;
    @Getter
    private boolean isCoordinator;
    @Getter
    private boolean isLastDevice;
    @Getter
    private UserDevice nextDevice;
    @Getter
    private UserDevice coordinatorDevice;

    @Getter
    private UserDevice lastDevice;

    public Set<UserDevice> devicesInNetwork = new TreeSet<>(new UserDeviceComparator());

    public static DeviceSingleton getInstance() {
        return ourInstance;
    }

    private DeviceSingleton() {
    }

    public void updateInfo(Collection<UserDevice> userDevicesNetwork, UserDevice selfUserDevice){

        log.info(String.format("user device info: %s network info%s", selfUserDevice.toString(), userDevicesNetwork.toString()));

        this.selfUserDevice = selfUserDevice;
        this.devicesInNetwork.addAll(userDevicesNetwork);



        int lastDeviceId = 0;

        for(Iterator<UserDevice> iterator = userDevicesNetwork.iterator(); iterator.hasNext();){
            UserDevice curentDevice = iterator.next();

//            set coordinator
            if(lastDeviceId==0){
                coordinatorDevice = curentDevice;
            }
//            set next device
            if(curentDevice.getDeviceId()==selfUserDevice.getDeviceId()+1){
                nextDevice = curentDevice;
            }
            lastDeviceId = curentDevice.getDeviceId();
            lastDevice = curentDevice;
        }

        if(selfUserDevice.getDeviceId()==1){
            this.isCoordinator = true;
        } else {
            this.isCoordinator = false;
        }
        if(!isCoordinator && lastDeviceId==selfUserDevice.getDeviceId()){
            this.isLastDevice = true;
        } else {
            this.isLastDevice = false;
        }

        System.out.println(toString());
    }

    public String getMacAddressByDeviceId(int id) {
        for (UserDevice device : devicesInNetwork) {
            if (device.getDeviceId() == id) {
                return device.getAddress();
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return "DeviceSingleton{" +
                "lastDevice=" + lastDevice +
                ", coordinatorDevice=" + coordinatorDevice +
                ", isLastDevice=" + isLastDevice +
                ", nextDevice=" + nextDevice +
                ", isCoordinator=" + isCoordinator +
                ", selfUserDevice=" + selfUserDevice +
                '}';
    }
}
