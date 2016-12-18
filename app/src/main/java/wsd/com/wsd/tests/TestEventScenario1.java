package wsd.com.wsd.tests;

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import wsd.com.wsd.models.*;
import wsd.com.wsd.models.types.*;

/**
 * Created by Mikolaj on 12/13/16.
 *
 * Test scenario 1
 * - ten sam dzień
 * - ta sama lokalizacja
 * - jedyne wspólne "okienko" od 12:00 do 14:00
 *
 */

public class TestEventScenario1 {
    public static void main(String args[]) {

        testCase1(); // for device 1 (coordinator)
        testCase2(); // for device 2
        testCase3(); // for device 3

    }

    private static Date getDateByVariables(int y, int m, int d) {
        GregorianCalendar gc = new GregorianCalendar(y, m, d);
        return gc.getTime();
    }

    private static List<Event> testCase1() {
        return Arrays.asList(
                new Event("testEvent11", "testEvent11 for testing purposes", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._8, Interwal._10), new Localization(52.232222, 21.008333)),
                new Event("testEvent12", "testEvent12 for testing purposes", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._14, Interwal._16), new Localization(52.232222, 21.008333)),
                new Event("testEvent13", "testEvent13 for testing purposes", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._16, Interwal._18), new Localization(52.232222, 21.008333))
        );
    }

    private static List<Event> testCase2() {
        return Arrays.asList(
                new Event("testEvent21", "testEvent21 for testing purposes", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._10, Interwal._12), new Localization(52.232222, 21.008333)),
                new Event("testEvent22", "testEvent22 for testing purposes", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._16, Interwal._18), new Localization(52.232222, 21.008333))
        );
    }

    private static List<Event> testCase3() {
        return Arrays.asList(
                new Event("testEvent31", "testEvent31 for testing purposes", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._8, Interwal._10), new Localization(52.232222, 21.008333)),
                new Event("testEvent32", "testEvent32 for testing purposes", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._14, Interwal._16), new Localization(52.232222, 21.008333)),
                new Event("testEvent33", "testEvent33 for testing purposes", getDateByVariables(2016, 12, 15), new TimeSlot(Interwal._18, Interwal._20), new Localization(52.232222, 21.008333))
        );
    }
}
