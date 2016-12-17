package wsd.com.wsd.repositories;

import io.realm.RealmResults;
import wsd.com.wsd.models.entities.EntityConverter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.entities.EventEntity;

public class EventEntityRepositoryImpl implements EventEntityRepository {

    private static EventEntityRepository eventEntityRepository;

    public static EventEntityRepository getInstance() {
        if (eventEntityRepository == null) {
            eventEntityRepository = new EventEntityRepositoryImpl();
        }
        return eventEntityRepository;
    }

    @Override
    public void save(Event event) {
        Realm mRealm = Realm.getDefaultInstance();
        EventEntity eventEntity = EntityConverter.toEventEntity(event);
        mRealm.beginTransaction();
        EventEntity e = mRealm.createObject(EventEntity.class);

        e.setName(eventEntity.getName());
        e.setDescription(eventEntity.getDescription());
        e.setDate(eventEntity.getDate());
        e.setTimeSlotBeg(eventEntity.getTimeSlotBeg());
        e.setTimeSlotEnd(eventEntity.getTimeSlotEnd());
        e.setLocalizationLatitude(eventEntity.getLocalizationLatitude());
        e.setLocalizationLongitude(eventEntity.getLocalizationLongitude());

        mRealm.commitTransaction();
        mRealm.close();
    }

    @Override
    public List<Event> getAllEvents() {
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<EventEntity> eventEntities = mRealm.where(EventEntity.class).findAll();
        List<Event> events = new ArrayList<>();

        for (EventEntity e : eventEntities) {
            events.add(EntityConverter.fromEventEntity(e));
        }
        mRealm.close();
        return events;
    }
}
