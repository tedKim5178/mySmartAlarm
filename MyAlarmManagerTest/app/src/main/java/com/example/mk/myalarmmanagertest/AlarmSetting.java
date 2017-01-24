package com.example.mk.myalarmmanagertest;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.mk.myalarmmanagertest.Data.AlarmContract;
import com.example.mk.myalarmmanagertest.Data.AlarmDbHelper;


/**
 * Created by mk on 2017-01-24.
 */

public class AlarmSetting extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    EditText updateMemo;
    AlarmDbHelper alarmDbHelper;
    private SQLiteDatabase mDb;
    TimePicker timePicker;
    int intentTag;
    AlarmAdapter alarmAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm);
        updateMemo = (EditText)findViewById(R.id.updateMemo);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        timePicker.setIs24HourView(false);


        // 이제 테그값 즉 primary Key 값이 있으므로 디비를 호출하자 수정도 할 것이니까 writable로 한다.
        Intent intent = getIntent();
        intentTag = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
        updateMemo.setText(String.valueOf(intentTag));

        // 데이터베이스 매니져
        alarmDbHelper = new AlarmDbHelper(this);
        mDb = alarmDbHelper.getWritableDatabase();
        // Initialize the adapter and attach it to the RecyclerView

        Cursor cursor = getOneAlarm(intentTag);
        Log.d(TAG, "왜@@@@@@@@@" + cursor.getCount());
        Log.d(TAG, "왜@@@@@@@@@" + cursor.getColumnCount());

        alarmAdapter = new AlarmAdapter(this, cursor);

        cursor.moveToLast();

        if(cursor != null){
            updateMemo.setText(cursor.getString(3));
            timePicker.setCurrentHour(cursor.getInt(1));
            timePicker.setCurrentMinute(cursor.getInt(2));
        }

    }

    public Cursor getOneAlarm(int tag){
        // 여기서 하나의 row를 가져옴 query문 작성해야된다..!
        return mDb.rawQuery("SELECT * FROM " + AlarmContract.AlarmlistEntry.TABLE_NAME
        + " where _id = " + tag + ";", null);

    }

    // timepicker를 수정을 하고 이 버튼이 눌리면 timepicker의 시, 분 , 메모를 가져와서 갱신시켜준다..! update 써야겠지..!
    public void updateButtonClicked(View view){
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        String memo = updateMemo.getText().toString();

        // 이 3개를 바탕으로 update 시켜주자
        updateDB(hour, minute, memo, intentTag);
        finish();
    }

    public void updateDB(int hour, int minute, String memo, int tag){
        ContentValues values = new ContentValues();
        values.put(AlarmContract.AlarmlistEntry.COLUMN_HOUR, hour);
        values.put(AlarmContract.AlarmlistEntry.COLUMN_MINUTE, minute);
        values.put(AlarmContract.AlarmlistEntry.COLUMN_MEMO, memo);
        mDb.update(AlarmContract.AlarmlistEntry.TABLE_NAME, values, "_id=" + tag, new String[]{});
//        Cursor cursor2 = getAllAlarm();
//        alarmAdapter.swapCursor(cursor2);
//        mDb.execSQL("UPDATE " + AlarmContract.AlarmlistEntry.TABLE_NAME
//        + " SET " + AlarmContract.AlarmlistEntry.COLUMN_HOUR + "=" + hour
//        + ", " + AlarmContract.AlarmlistEntry.COLUMN_MINUTE + "=" + minute
//        + ", " + AlarmContract.AlarmlistEntry.COLUMN_MEMO + "=" + memo
//        + " WHERE " + AlarmContract.AlarmlistEntry._ID + "=" + tag +";");
//        mDb.close();
    }

}
