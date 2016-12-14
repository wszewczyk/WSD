package wsd.com.wsd.models.entities;


import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Builder
@Data
@EqualsAndHashCode
@ToString
public class EventEntity {
    private String name;
    private String description;
    private Date date;
    private String timeSlotBeg;
    private String timeSlotEnd;
    private double localizationLatitude;
    private double localizationLongitude;

    public EventEntity(){}

    public EventEntity(String name, String description, Date date, String timeSlotBeg, String timeSlotEnd, double localizationLatitude, double localizationLongitude) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.timeSlotBeg = timeSlotBeg;
        this.timeSlotEnd = timeSlotEnd;
        this.localizationLatitude = localizationLatitude;
        this.localizationLongitude = localizationLongitude;
    }
}
