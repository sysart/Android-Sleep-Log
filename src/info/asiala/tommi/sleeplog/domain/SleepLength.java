package info.asiala.tommi.sleeplog.domain;

public class SleepLength {

    private final int hours;
    private final int minutes;


    public SleepLength(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        String hoursText = hours == 1 ? "h" : "h";
        String minutesText = minutes == 1 ? "min" : "min";

        StringBuilder sb = new StringBuilder()
                .append(String.valueOf(hours))
                .append(hoursText)
                .append(" ")
                .append(String.valueOf(minutes))
                .append(minutesText);

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SleepLength that = (SleepLength) o;

        if (hours != that.hours) return false;
        if (minutes != that.minutes) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hours;
        result = 31 * result + minutes;
        return result;
    }
}
