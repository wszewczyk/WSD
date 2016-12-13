package wsd.com.wsd.models;

import com.orm.SugarRecord;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wsd.com.wsd.comparators.DateComperator;

@Data
//@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Event extends SugarRecord {
    private String name;
    private String description;
    private Date date;
    private TimeSlot timeSlot;
    private Localization localization;

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
