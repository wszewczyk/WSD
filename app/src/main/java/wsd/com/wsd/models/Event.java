package wsd.com.wsd.models;


import com.orm.dsl.Table;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
@ToString
public class Event{
    private Long id;
    private String name;
    private String description;
    private Date date;
    private TimeSlot timeSlot;
    private Localization localization;

    public Event(String name, String description, Date date,TimeSlot timeSlot,Localization localization){
        this.name = name;
        this.description = description;
        this.date = date;
        this.timeSlot = timeSlot;
        this.localization = localization;
    }

    public Long getId() {
        return id;
    }
}
