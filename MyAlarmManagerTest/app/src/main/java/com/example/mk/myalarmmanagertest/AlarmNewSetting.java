package com.example.mk.myalarmmanagertest;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.mk.myalarmmanagertest.Data.AlarmDbHelper;

/**
 * Created by mk on 2017-01-24.
 */

public class AlarmNewSetting extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    EditText newMemo;
    AlarmDbHelper alarmDbHelper;
    private SQLiteDatabase mDb;
    TimePicker timePicker;
    int intentTag;
    AlarmAdapter alarmAdapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alarm);

        // 이제 여기서 시간설정하고 디비에 추가해주는 작업을 여기서 다 해줘야되는거다....
        // 우선 기본적인 layout은 있으니까 이걸로 한번 해보자.
        timePicker = (TimePicker)findViewById(R.id.newTimePicker);
        newMemo = (EditText)findViewById(R.id.newMemo);
        timePicker.setIs24HourView(false);

    }
    // 버튼 눌리면
    public void newButtonClicked(View view){
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        String meno = newMemo.getText().toString();

        // 이 3개 통해서 데이터베이스에 넣어줘야뎀 즉 insert..!
    }

//    private long addNewAlarm(int hour, int minute, String memo){
//        ContentValues cv = new ContentValues();
//        cv.put(AlarmContract.AlarmlistEntry.COLUMN_HOUR, hour);
//        cv.put(AlarmContract.AlarmlistEntry.COLUMN_MINUTE, minute);
//        cv.put(AlarmContract.AlarmlistEntry.COLUMN_MEMO, memo);
//        return mDb.insert(AlarmContract.AlarmlistEntry.TABLE_NAME, null, cv);
//    }
}
