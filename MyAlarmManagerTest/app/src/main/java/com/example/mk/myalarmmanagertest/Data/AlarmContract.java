package com.example.mk.myalarmmanagertest.Data;

/**
 * Created by mk on 2017-01-23.
 */

import android.provider.BaseColumns;

/**
 * Created by mk on 2017-01-23.
 */

public class AlarmContract {
    public static final class AlarmlistEntry implements BaseColumns {
        public static final String TABLE_NAME = "alarmlist";
        public static final String COLUMN_HOUR = "hour";
        public static final String COLUMN_MINUTE = "minute";
        public static final String COLUMN_MEMO = "memo";
    }
}
