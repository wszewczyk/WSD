package wsd.com.wsd.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class UserDevice {
    private String name;
    private int deviceId;
    private String deviceName;
}
