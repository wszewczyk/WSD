package wsd.com.wsd.models;


import com.orm.dsl.Table;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wsd.com.wsd.comparators.DateComperator;

@Data
//@EqualsAndHashCode
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        DateComperator dateComperator = new DateComperator();
        if(dateComperator.compare(this.date, event.getDate())!=0) return false;
        return timeSlot != null ? timeSlot.equals(event.timeSlot) : event.timeSlot == null;

    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (timeSlot != null ? timeSlot.hashCode() : 0);
        return result;
    }
}
