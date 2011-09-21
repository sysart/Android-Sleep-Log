package info.asiala.tommi.sleeplog.ui;

import android.app.Activity;
import android.widget.TextView;
import info.asiala.tommi.sleeplog.R;
import info.asiala.tommi.sleeplog.database.SleepyDao;
import info.asiala.tommi.sleeplog.domain.SleepEntry;
import info.asiala.tommi.sleeplog.domain.SleepLength;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Statistics extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.statistics);

        TextView statsWeekly = (TextView) findViewById(R.id.stats_weekly);
        List<SleepEntry> entries = new SleepyDao(getApplicationContext()).getAll();

        HashMap<Integer, Double> weekdaySleepLengths = new HashMap<Integer, Double>();
        HashMap<Integer, Integer> weekdayEntryCount = new HashMap<Integer, Integer>();

        Calendar calendar = Calendar.getInstance();
        for(SleepEntry entry : entries) {
            calendar.setTime(entry.getDate());
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            int hours = entry.getSleepLength().getHours();
            int minutes = entry.getSleepLength().getMinutes();
            double total = hours + (minutes / 60.0);

            if(weekdaySleepLengths.containsKey(weekDay))
                weekdaySleepLengths.put(weekDay, weekdaySleepLengths.get(weekDay) + total);
            else
                weekdaySleepLengths.put(weekDay, total);

            if(weekdayEntryCount.containsKey(weekDay))
                weekdayEntryCount.put(weekDay, weekdayEntryCount.get(weekDay) + 1);
            else
                weekdayEntryCount.put(weekDay, 1);
        }

        StringBuffer weekdayText = new StringBuffer()
                .append("Monday: ")
                .append(convertTotalLengthToString(weekdaySleepLengths, weekdayEntryCount, Calendar.MONDAY))
                .append("\n")
                .append("Tuesday: ")
                .append(convertTotalLengthToString(weekdaySleepLengths, weekdayEntryCount, Calendar.TUESDAY))
                .append("\n")
                .append("Wednesday: ")
                .append(convertTotalLengthToString(weekdaySleepLengths, weekdayEntryCount, Calendar.WEDNESDAY))
                .append("\n")
                .append("Thursday: ")
                .append(convertTotalLengthToString(weekdaySleepLengths, weekdayEntryCount, Calendar.THURSDAY))
                .append("\n")
                .append("Friday: ")
                .append(convertTotalLengthToString(weekdaySleepLengths, weekdayEntryCount, Calendar.FRIDAY))
                .append("\n")
                .append("Saturday: ")
                .append(convertTotalLengthToString(weekdaySleepLengths, weekdayEntryCount, Calendar.FRIDAY))
                .append("\n")
                .append("Sunday: ")
                .append(convertTotalLengthToString(weekdaySleepLengths, weekdayEntryCount, Calendar.SUNDAY))
                .append("\n");

        statsWeekly.setText(weekdayText.toString());
    }

    private String convertTotalLengthToString(HashMap<Integer, Double> weekdaySleepLengths, HashMap<Integer, Integer> weekdayEntryCount, int weekday) {
        Integer entryCount = weekdayEntryCount.get(weekday);
        if(entryCount == null) return "";

        Double weekDayTotal = weekdaySleepLengths.get(weekday);
        Double average = weekDayTotal / entryCount;
        int hours = average.intValue();
        Double minutes = (average - hours) * 60.0;

        return new SleepLength(hours, minutes.intValue()).toString();

    }
}
