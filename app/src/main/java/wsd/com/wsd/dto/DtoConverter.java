package wsd.com.wsd.dto;


import com.google.gson.Gson;

public class DtoConverter {
    private static Gson gson = new Gson();

    public static String fromNetworkInfoDto(NetworkInfoDto networkInfoDto){
        return gson.toJson(networkInfoDto);
    }

    public static NetworkInfoDto toNetworkInfoDto(String content){
        return gson.fromJson(content, NetworkInfoDto.class);
    }
}
