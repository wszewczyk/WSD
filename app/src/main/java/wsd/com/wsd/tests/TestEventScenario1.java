package wsd.com.wsd.tests;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import wsd.com.wsd.models.*;
import wsd.com.wsd.models.types.*;

/**
 * Created by Mikolaj on 12/13/16.
 */

public class TestEventScenario1 {
    public static void main(String args[]) {

        // Test scenario 1 - ten sam dzień, ta sama lokalizacja, wspólne "okienko" od 12 do 14
        testCase1(); // for device 1
        testCase2(); // for device 2
        testCase3(); // for device 3

    }

    public static void testCase1() {

        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            //testEvent11
            String testDateString1 = "12/13/2016";
            Date testDate1 = df.parse(testDateString1);
            TimeSlot testTimeSlot1 = new TimeSlot(Interwal._8, Interwal._10);
            Localization testLocalization1 = new Localization(52.232222, 21.008333);
            Event testEvent11 = new Event("testEvent11", "testEvent11 for testing purposes", testDate1, testTimeSlot1, testLocalization1);

            //testEvent12
            String testDateString2 = "12/13/2016";
            Date testDate2 = df.parse(testDateString2);
            TimeSlot testTimeSlot2 = new TimeSlot(Interwal._14, Interwal._16);
            Localization testLocalization2 = new Localization(52.232222, 21.008333);
            Event testEvent12 = new Event("testEvent12", "testEvent12 for testing purposes", testDate2, testTimeSlot2, testLocalization2);

            //testEvent13
            String testDateString3 = "12/13/2016";
            Date testDate3 = df.parse(testDateString3);
            TimeSlot testTimeSlot3 = new TimeSlot(Interwal._16, Interwal._18);
            Localization testLocalization3 = new Localization(52.232222, 21.008333);
            Event testEvent13 = new Event("testEvent13", "testEvent13 for testing purposes", testDate3, testTimeSlot3, testLocalization3);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void testCase2() {

        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            //testEvent21
            String testDateString1 = "12/13/2016";
            Date testDate1 = df.parse(testDateString1);
            TimeSlot testTimeSlot1 = new TimeSlot(Interwal._10, Interwal._12);
            Localization testLocalization1 = new Localization(52.232222, 21.008333);
            Event testEvent21 = new Event("testEvent21", "testEvent21 for testing purposes", testDate1, testTimeSlot1, testLocalization1);

            //testEvent22
            String testDateString2 = "12/13/2016";
            Date testDate2 = df.parse(testDateString2);
            TimeSlot testTimeSlot2 = new TimeSlot(Interwal._16, Interwal._18);
            Localization testLocalization2 = new Localization(52.232222, 21.008333);
            Event testEvent22 = new Event("testEvent22", "testEvent22 for testing purposes", testDate2, testTimeSlot2, testLocalization2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void testCase3() {

        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            //testEvent31
            String testDateString1 = "12/13/2016";
            Date testDate1 = df.parse(testDateString1);
            TimeSlot testTimeSlot1 = new TimeSlot(Interwal._8, Interwal._10);
            Localization testLocalization1 = new Localization(52.232222, 21.008333);
            Event testEvent31 = new Event("testEvent31", "testEvent31 for testing purposes", testDate1, testTimeSlot1, testLocalization1);

            //testEvent32
            String testDateString2 = "12/13/2016";
            Date testDate2 = df.parse(testDateString2);
            TimeSlot testTimeSlot2 = new TimeSlot(Interwal._14, Interwal._16);
            Localization testLocalization2 = new Localization(52.232222, 21.008333);
            Event testEvent32 = new Event("testEvent32", "testEvent32 for testing purposes", testDate2, testTimeSlot2, testLocalization2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
