package wsd.com.wsd.models;


import android.test.suitebuilder.annotation.LargeTest;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import wsd.com.wsd.models.types.Interwal;

@EqualsAndHashCode
@ToString
public class TimeSlot {

    @Getter
    private Interwal begin;
    @Getter
    private Interwal end;

    public TimeSlot(Interwal begin, Interwal end) {
        assert begin.getInterval()<end.getInterval();
        this.begin = begin;
        this.end = end;
    }
}
