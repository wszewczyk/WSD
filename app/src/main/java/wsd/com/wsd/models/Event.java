package wsd.com.wsd.models;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Event{
    private String name;
    private String description;
    private Date date;
    private TimeSlot timeSlot;
    private Localization localization;
}
