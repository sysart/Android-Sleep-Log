package info.asiala.tommi.sleeplog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;

    static final String TABLE_SLEEP_ENTRIES = "sleep_entry";
    static final String KEY_DATE = "date";
    static final String KEY_LENGTH = "length";
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_SLEEP_ENTRIES + " ( " + KEY_DATE + " TEXT PRIMARY KEY, " + KEY_LENGTH + " TEXT );";

    DatabaseOpenHelper(Context context) {
        super(context, "sleep", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(DatabaseOpenHelper.class.getName(),
                "Database upgrade from " + oldVersion + " to " + newVersion);
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SLEEP_ENTRIES);
		onCreate(sqLiteDatabase);
    }
}
