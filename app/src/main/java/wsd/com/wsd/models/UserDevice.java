package wsd.com.wsd.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class UserDevice {
    private String name;
    private int deviceId;
    private String deviceName;
}
