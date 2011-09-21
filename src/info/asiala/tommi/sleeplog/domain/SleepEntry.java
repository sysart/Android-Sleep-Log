package info.asiala.tommi.sleeplog.domain;

import java.util.Date;

public class SleepEntry {
    private final Date date;
    private final SleepLength sleepLength;

    public SleepEntry(Date date, SleepLength sleepLength) {
        this.sleepLength = sleepLength;
        this.date = (Date) date.clone();
    }

    public Date getDate() {
        return date;
    }

    public SleepLength getSleepLength() {
        return sleepLength;
    }
}
