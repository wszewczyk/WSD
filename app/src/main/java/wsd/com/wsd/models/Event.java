package wsd.com.wsd.models;


import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
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
public class Event extends RealmObject {
    private String name;
    private String description;
    private Date date;
    @Ignore
    private TimeSlot timeSlot;
    @Ignore
    private Localization localization;

    @Ignore
    private int sessionId;

    public Event(String name, String description, Date date, TimeSlot timeSlot, Localization localization) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.timeSlot = timeSlot;
        this.localization = localization;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        DateComperator dateComperator = new DateComperator();
        if (dateComperator.compare(this.date, event.getDate()) != 0) return false;
        return timeSlot != null ? timeSlot.equals(event.timeSlot) : event.timeSlot == null;

    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (timeSlot != null ? timeSlot.hashCode() : 0);
        return result;
    }
}
