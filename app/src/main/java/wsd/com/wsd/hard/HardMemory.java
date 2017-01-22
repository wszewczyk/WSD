package wsd.com.wsd.hard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import wsd.com.wsd.models.Event;
import wsd.com.wsd.models.Localization;
import wsd.com.wsd.models.TimeSlot;
import wsd.com.wsd.models.types.Interwal;

/**
 * Created by tobia on 21.01.2017.
 */

public class HardMemory {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public static List<Event> getMemoryByDeviceId(int deviceId){
        if(deviceId==1){
            return memoryForMaster();
        } else if (deviceId == 2) {
            return memoryForFirstDevice();
        } else {
            return memoryForSecondDevice();
        }
    }

    private static List<Event> memoryForMaster() {
        Date date1 = null;
        Date date2 = null;
        Date date3 = null;
        Date date4 = null;
        try {
            date1 = sdf.parse("31-08-2017");
            date2 = sdf.parse("15-08-2017");
            date3 = sdf.parse("17-08-2017");
            date4 = sdf.parse("21-08-2017");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Arrays.asList(
                new Event(0, "a", "a", date1, new TimeSlot(Interwal._8, Interwal._16), new Localization(11111, 222222)),
                new Event(1, "b", "b", date2, new TimeSlot(Interwal._8, Interwal._10), new Localization(11111, 222222)),
                new Event(2, "c", "c", date3, new TimeSlot(Interwal._8, Interwal._12), new Localization(11111, 222222)),
                new Event(3, "d", "d", date4, new TimeSlot(Interwal._8, Interwal._16), new Localization(11111, 222222))
        );
    }

    private static List<Event> memoryForFirstDevice() {
        Date date1 = null;
        Date date2 = null;
        Date date3 = null;
        Date date4 = null;
        try {
            date1 = sdf.parse("31-08-2017");
            date2 = sdf.parse("15-08-2017");
            date3 = sdf.parse("17-08-2017");
            date4 = sdf.parse("21-08-2017");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Arrays.asList(
                new Event(0, "e", "e", date1, new TimeSlot(Interwal._16, Interwal._20), new Localization(11111, 222222)),
                new Event(1, "f", "f", date2, new TimeSlot(Interwal._8, Interwal._16), new Localization(11111, 222222)),
                new Event(2, "g", "g", date3, new TimeSlot(Interwal._8, Interwal._10), new Localization(11111, 222222)),
                new Event(3, "h", "h", date4, new TimeSlot(Interwal._8, Interwal._16), new Localization(11111, 222222))
        );
    }

    private static List<Event> memoryForSecondDevice() {
        Date date1 = null;
        Date date2 = null;
        Date date3 = null;
        Date date4 = null;
        try {
            date1 = sdf.parse("31-08-2017");
            date2 = sdf.parse("15-08-2017");
            date3 = sdf.parse("17-08-2017");
            date4 = sdf.parse("21-08-2017");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Arrays.asList(
                new Event(0, "i", "i", date1, new TimeSlot(Interwal._8, Interwal._16), new Localization(11111, 222222)),
                new Event(1, "j", "j", date2, new TimeSlot(Interwal._12, Interwal._16), new Localization(11111, 222222)),
                new Event(2, "k", "k", date3, new TimeSlot(Interwal._8, Interwal._16), new Localization(11111, 222222)),
                new Event(3, "l", "l", date4, new TimeSlot(Interwal._8, Interwal._16), new Localization(11111, 222222))
        );
    }
}
