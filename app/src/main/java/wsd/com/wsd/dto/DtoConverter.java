package wsd.com.wsd.dto;


import com.google.gson.Gson;

import wsd.com.wsd.models.Event;

public class DtoConverter {
    private static Gson gson = new Gson();

    public static String fromNetworkInfoDto(NetworkInfoDto networkInfoDto){
        return gson.toJson(networkInfoDto);
    }

    public static NetworkInfoDto toNetworkInfoDto(String content){
        return gson.fromJson(content, NetworkInfoDto.class);
    }

    public static String fromEvent(Event event){
        return gson.toJson(event);
    }

    public static Event toEvent(String content){
        return gson.fromJson(content, Event.class);
    }
}
