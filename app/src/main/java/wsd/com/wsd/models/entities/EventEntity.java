package wsd.com.wsd.models.entities;


import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class EventEntity {
    private String name;
    private String description;
    private Date date;
    private String timeSlotBeg;
    private String timeSlotEnd;
    private double localizationLatitude;
    private double localizationLongitude;
}
