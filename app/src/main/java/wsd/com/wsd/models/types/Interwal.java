package wsd.com.wsd.models.types;


import android.support.v4.app.INotificationSideChannel;
import android.util.Log;

public enum Interwal {
    _8(8), _10(10), _12(12), _14(14), _16(16), _18(18), _20(20);


    private int interval;

    Interwal(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }

    public static Interwal getNextInterval(Interwal beg, int period) {
        int endInterval = beg.getInterval() + period;
        return getByNymber(endInterval);
    }

    public static Interwal getNextInterwal(int hour, int minutes)
    {
        if(minutes == 0)
        {
            if((hour % 2) == 0)
            {
                return getByNymber(hour);
            }
            else
            {
                return getByNymber(hour + 1);
            }
        }
        else
        {
            if((hour % 2) == 0)
            {
                return getByNymber(hour + 2);
            }
            else
            {
                return getByNymber(hour + 1);
            }
        }

    }

    public static Interwal getPreviousInterwal(int hour, int minutes)
    {
        if((hour % 2) == 0)
        {
            return getByNymber(hour);
        }
        else
        {
            return getByNymber(hour - 1);
        }
    }

    public static Interwal getByNymber(int number) {
        switch (number) {
            case 8:
                return Interwal._8;
            case 10:
                return Interwal._10;
            case 12:
                return Interwal._12;
            case 14:
                return Interwal._14;
            case 16:
                return Interwal._16;
            case 18:
                return Interwal._18;
            case 20:
                return Interwal._20;
            default:
                throw new IllegalArgumentException("Interval does not exist!!! Number value: " + number);
        }
    }
}
