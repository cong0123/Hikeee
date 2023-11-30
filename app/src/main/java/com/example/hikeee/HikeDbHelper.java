package com.example.hikeee;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.List;

public class HikeDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hikes.db";
    private static final int DATABASE_VERSION = 1;

    // Tên bảng và các cột
    public static final String TABLE_NAME = "hikes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_DIFFICULTY = "difficulty";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PARKING_AREA = "parkingArea";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_RESTING_SPOTS = "restingSpots";

    private MutableLiveData<List<Hike>> hikesLiveData;

    public HikeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        hikesLiveData = new MutableLiveData<>();
    }

    LiveData<List<Hike>> getAllHikesLiveData() {
        return hikesLiveData;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_LENGTH + " TEXT, " +
                COLUMN_DIFFICULTY + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PARKING_AREA + " TEXT, " +
                COLUMN_RATING + " TEXT, " +
                COLUMN_RESTING_SPOTS + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, hike.getName());
        values.put(COLUMN_LOCATION, hike.getLocation());
        values.put(COLUMN_DATE, hike.getDate());
        values.put(COLUMN_LENGTH, hike.getLength());
        values.put(COLUMN_DIFFICULTY, hike.getDifficulty());
        values.put(COLUMN_DESCRIPTION, hike.getDescription());
        values.put(COLUMN_PARKING_AREA, hike.getParkingArea());
        values.put(COLUMN_RATING, hike.getRating());
        values.put(COLUMN_RESTING_SPOTS, hike.getRestingSpots());
        db.insert(TABLE_NAME, null, values);

        db.close();
        return 0;
    }

    public List<Hike> getAllHikes() {
        List<Hike> hikes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_LOCATION,
                COLUMN_DATE,
                COLUMN_LENGTH,
                COLUMN_DIFFICULTY,
                COLUMN_DESCRIPTION,
                COLUMN_PARKING_AREA,
                COLUMN_RATING,
                COLUMN_RESTING_SPOTS
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
            String length = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LENGTH));
            String difficulty = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
            String parkingArea = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PARKING_AREA));
            String rating = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RATING));
            String restingSpots = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTING_SPOTS));

            Hike hike = new Hike(name, location, date, length, difficulty, description, parkingArea,rating,restingSpots);
            hike.setId(id);
            hikes.add(hike);
        }

        cursor.close();
        db.close();
        return hikes;
    }

    // Trong HikeDbHelper
    public void deleteHike(long hikeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(hikeId)});
        db.close();
    }

    public void updateHike(Hike hike) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", hike.getName());
        values.put("location", hike.getLocation());
        values.put("date", hike.getDate());
        values.put("length", hike.getLength());
        values.put("difficulty", hike.getDifficulty());
        values.put("description", hike.getDescription());
        values.put("parkingArea", hike.getParkingArea());
        values.put("rating", hike.getRating());
        values.put("restingSpots", hike.getRestingSpots());

        db.update("hikes", values, "id = ?", new String[]{String.valueOf(hike.getId())});
        db.close();
    }
    public List<Hike> searchHikes(String query) {
        List<Hike> searchResults = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_NAME + " LIKE ? OR "
                + COLUMN_LOCATION + " LIKE ? OR "
                + COLUMN_LENGTH + " LIKE ? OR "
                + COLUMN_DATE + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%", "%" + query + "%", "%" + query + "%", "%" + query + "%"};

        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {

        }

        cursor.close();
        db.close();
        return searchResults;
    }

}
