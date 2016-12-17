package wsd.com.wsd.models.entities;

import java.util.ArrayList;
import java.util.List;

import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.Localization;
import wsd.com.wsd.models.TimeSlot;
import wsd.com.wsd.models.types.Interwal;

public class EntityConverter {
    public static Event fromEventEntity(EventEntity eventEntity){
        Interwal beg = Interwal.valueOf(eventEntity.getTimeSlotBeg());
        Interwal end = Interwal.valueOf(eventEntity.getTimeSlotEnd());
        TimeSlot timeSlot = new TimeSlot(beg,end);
        Localization localization = new Localization(eventEntity.getLocalizationLatitude(), eventEntity.getLocalizationLongitude());
        return Event.builder()
                .timeSlot(timeSlot)
                .name(eventEntity.getName())
                .description(eventEntity.getDescription())
                .date(eventEntity.getDate())
                .localization(localization)
                .build();
    }

    public static EventEntity toEventEntity(Event event){
        return EventEntity.builder()
                .name(event.getName())
                .description(event.getDescription())
                .date(event.getDate())
                .localizationLatitude(event.getLocalization().getLatitude())
                .localizationLongitude(event.getLocalization().getLongitude())
                .timeSlotBeg(event.getTimeSlot().getBegin().name())
                .timeSlotEnd(event.getTimeSlot().getEnd().name())
                .build();
    }

    public static List<Event> toEventList(List<EventEntity> eventEntities){
        List<Event> events = new ArrayList<>();
        for(EventEntity ee: eventEntities){
            events.add(fromEventEntity(ee));
        }
        return events;
    }
}
