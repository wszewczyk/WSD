package wsd.com.wsd.models.types;


public enum Interwal {
    _8(8), _10(10), _12(12), _14(14), _16(16), _18(18), _20(20);


    private int interval;

    Interwal(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }
}
