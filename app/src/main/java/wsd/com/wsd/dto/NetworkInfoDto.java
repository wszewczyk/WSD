package wsd.com.wsd.dto;


import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wsd.com.wsd.models.UserDevice;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class NetworkInfoDto {
    private UserDevice selfDeviceInfo;
    private List<UserDevice> devicesNetowrk;
}
