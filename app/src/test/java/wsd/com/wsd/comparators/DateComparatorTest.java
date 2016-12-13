package wsd.com.wsd.comparators;


import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateComparatorTest {
    private DateComperator dateComperator = new DateComperator();
    private SimpleDateFormat dateFormat = new SimpleDateFormat ("dd-MM-yyyy");

    @Test
    public void dateComparatorEq() throws InterruptedException {
        Date date = new Date();
        Thread.sleep(1000);
        Date date1 = new Date();
        int res = dateComperator.compare(date, date1);
        assertEquals(0, res);
    }

    @Test
    public void dateComparatorLs() throws ParseException {
        String string1 = "22-02-2010";
        String string2 = "23-02-2010";

        Date date = dateFormat.parse(string1);
        Date date1 = dateFormat.parse(string2);

        int res = dateComperator.compare(date, date1);
        assertEquals(-1, res);
    }

    @Test
    public void dateComparatorGd() throws ParseException {
        String string1 = "24-02-2010";
        String string2 = "23-02-2010";

        Date date = dateFormat.parse(string1);
        Date date1 = dateFormat.parse(string2);

        int res = dateComperator.compare(date, date1);
        assertEquals(1, res);
    }

}
