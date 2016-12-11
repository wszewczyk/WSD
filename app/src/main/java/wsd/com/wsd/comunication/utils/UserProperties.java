package wsd.com.wsd.comunication.utils;


import java.util.HashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class UserProperties {
    Map<String, String> propMap = new HashMap<>();

    public String addProperty(String key, String val){
        return propMap.put(key, val);
    }

    public String getProperty(String key){
        return propMap.get(key);
    }
}
