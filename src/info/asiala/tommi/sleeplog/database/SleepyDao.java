package info.asiala.tommi.sleeplog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import info.asiala.tommi.sleeplog.domain.SleepEntry;
import info.asiala.tommi.sleeplog.domain.SleepLength;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SleepyDao {
    private Context applicationContext;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
    private final String sleep_length_text_separator = ":";

    public SleepyDao(Context applicationContext) {

        this.applicationContext = applicationContext;
    }

    public void save(SleepEntry entry) {
        SQLiteDatabase database = new DatabaseOpenHelper(applicationContext).getWritableDatabase();
        database.replace(DatabaseOpenHelper.TABLE_SLEEP_ENTRIES, null, getItemAsContentValues(entry));
        database.close();
    }

    public List<SleepEntry> getEntriesForPastNDays(int dayCount) {
        Date searchStartDate = new Date(new Date().getTime() - (1000 * 60 * 60 * 24 * dayCount));
        return queryEntries("SELECT * FROM " + DatabaseOpenHelper.TABLE_SLEEP_ENTRIES + " WHERE DATE > " + dateFormat.format(searchStartDate));
    }

    public List<SleepEntry> getAll() {
        return queryEntries("SELECT * FROM " + DatabaseOpenHelper.TABLE_SLEEP_ENTRIES);
    }

    private List<SleepEntry> queryEntries(String sqlQuery) {
        SQLiteDatabase database = new DatabaseOpenHelper(applicationContext).getReadableDatabase();
        Cursor cursor = database.rawQuery(sqlQuery, null);
        return readEntriesFromCursor(cursor);
    }

    private ContentValues getItemAsContentValues(SleepEntry entry) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseOpenHelper.KEY_DATE, entryAsSleepDateText(entry));
        contentValues.put(DatabaseOpenHelper.KEY_LENGTH, entryAsSleepLengthText(entry));
        return contentValues;
    }

    private String entryAsSleepDateText(SleepEntry entry) {
        return dateFormat.format(entry.getDate());
    }

    private String entryAsSleepLengthText(SleepEntry entry) {
        SleepLength sleepLength = entry.getSleepLength();
        return sleepLength.getHours() + sleep_length_text_separator + sleepLength.getMinutes();
    }

    private List<SleepEntry> readEntriesFromCursor(Cursor cursor) {
        List<SleepEntry> sleepEntries = new ArrayList<SleepEntry>();
        while(cursor.moveToNext()) {

            String date = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.KEY_DATE));
            String length = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.KEY_LENGTH));

            SleepLength sleepLength = sleepLengthTextAsSleepLength(length);

            Date parsedDate;
            try {
                parsedDate = dateFormat.parse(date);
            } catch (ParseException e) {
                Log.e(SleepyDao.class.getName(), "Database corrupted");
                return Collections.emptyList();
            }

            sleepEntries.add(new SleepEntry(parsedDate, sleepLength));

        }
        return sleepEntries;
    }

    private SleepLength sleepLengthTextAsSleepLength(String length) {
        String[] items = length.split(sleep_length_text_separator);
        return new SleepLength(Integer.valueOf(items[0]), Integer.valueOf(items[1]));
    }
}
