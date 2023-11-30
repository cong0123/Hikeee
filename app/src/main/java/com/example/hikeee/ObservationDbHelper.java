package com.example.hikeee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.hikeee.ObservationContract.ObservationEntry;

import java.util.ArrayList;
import java.util.List;

public class ObservationDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "observations.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_OBSERVATION =
            "CREATE TABLE " + ObservationEntry.TABLE_NAME + " (" +
                    ObservationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ObservationEntry.COLUMN_HIKE_ID + " INTEGER, " +
                    ObservationEntry.COLUMN_CONTENT + " TEXT, " +
                    ObservationEntry.COLUMN_TIME + " TEXT, " +
                    ObservationEntry.COLUMN_WEATHER + " TEXT, " +
                    ObservationEntry.COLUMN_TRAIL_CONDITION + " TEXT);";

    public ObservationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_OBSERVATION);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long addObservation(long hikeId, String content, String time, String weather, String trailCondition) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ObservationEntry.COLUMN_HIKE_ID, hikeId);
        values.put(ObservationEntry.COLUMN_CONTENT, content);
        values.put(ObservationEntry.COLUMN_TIME, time);
        values.put(ObservationEntry.COLUMN_WEATHER, weather);
        values.put(ObservationEntry.COLUMN_TRAIL_CONDITION, trailCondition);

        // Thực hiện việc chèn dữ liệu và trả về ID mới được tạo
        return db.insert(ObservationEntry.TABLE_NAME, null, values);
    }
    // Phương thức lấy danh sách quan sát cho một chuyến đi
    public List<Observation> getObservationsForHike(long hikeId) {
        List<Observation> observationList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Câu truy vấn để lấy tất cả quan sát của một chuyến đi
        String query = "SELECT * FROM " + ObservationEntry.TABLE_NAME +
                " WHERE " + ObservationEntry.COLUMN_HIKE_ID + " = " + hikeId;

        Cursor cursor = db.rawQuery(query, null);

// Kiểm tra xem cột có tồn tại hay không
        int idColumnIndex = cursor.getColumnIndex(ObservationEntry._ID);
        int contentColumnIndex = cursor.getColumnIndex(ObservationEntry.COLUMN_CONTENT);
        int timeColumnIndex = cursor.getColumnIndex(ObservationEntry.COLUMN_TIME);
        int weatherColumnIndex = cursor.getColumnIndex(ObservationEntry.COLUMN_WEATHER);
        int trailConditionColumnIndex = cursor.getColumnIndex(ObservationEntry.COLUMN_TRAIL_CONDITION);

        if (idColumnIndex == -1 || contentColumnIndex == -1 || timeColumnIndex == -1 || weatherColumnIndex == -1 || trailConditionColumnIndex == -1) {

        } else {
            // Đọc dữ liệu từ Cursor và thêm vào danh sách quan sát
            if (cursor.moveToFirst()) {
                do {
                    long observationId = cursor.getLong(idColumnIndex);
                    String content = cursor.getString(contentColumnIndex);
                    String time = cursor.getString(timeColumnIndex);
                    String weather = cursor.getString(weatherColumnIndex);
                    String trailCondition = cursor.getString(trailConditionColumnIndex);

                    Observation observation = new Observation(observationId, hikeId, content, time, weather, trailCondition);
                    observationList.add(observation);
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        return observationList;
    }
    public void updateObservation(Observation observation) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ObservationContract.ObservationEntry.COLUMN_CONTENT, observation.getContent());
        values.put(ObservationContract.ObservationEntry.COLUMN_TIME, observation.getTime());
        values.put(ObservationContract.ObservationEntry.COLUMN_WEATHER, observation.getWeather());
        values.put(ObservationContract.ObservationEntry.COLUMN_TRAIL_CONDITION, observation.getTrailCondition());

        String selection = ObservationContract.ObservationEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(observation.getId())};

        db.update(
                ObservationContract.ObservationEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        db.close();
    }
    public void deleteObservation(long observationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ObservationContract.ObservationEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(observationId) };
        db.delete(ObservationContract.ObservationEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }


}
