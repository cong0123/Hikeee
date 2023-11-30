package com.example.hikeee;

import android.provider.BaseColumns;

public final class ObservationContract {
    private ObservationContract() {
    }

    public static final class ObservationEntry implements BaseColumns {
        public static final String TABLE_NAME = "observations";
        public static final String COLUMN_HIKE_ID = "hike_id";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_WEATHER = "weather";
        public static final String COLUMN_TRAIL_CONDITION = "trail_condition";
    }
}
