package wsd.com.wsd.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import wsd.com.wsd.models.types.CloudCalendarType;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {
    private String userName;
    private String password;
    private CloudCalendarType calendarType;
}
