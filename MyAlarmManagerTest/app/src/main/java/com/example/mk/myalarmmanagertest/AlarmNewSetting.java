package com.example.mk.myalarmmanagertest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.example.mk.myalarmmanagertest.Data.AlarmContract;
import com.example.mk.myalarmmanagertest.Data.AlarmDbHelper;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mk on 2017-01-24.
 */

public class AlarmNewSetting extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    EditText newMemo;
    AlarmDbHelper alarmDbHelper;
    private SQLiteDatabase mDb;
    TimePicker timePicker;
    AlarmManager alarmManager;

    // 요일들 넣어줄 스트링 배열
    // intarray for day of week
    int[] repeatDay = {0,0,0,0,0,0,0,0};
    boolean repeat = false;

    // 버터나이프 사용
    @Bind(R.id.toggle_sunday) ToggleButton sunday;
    @Bind(R.id.toggle_monday) ToggleButton monday;
    @Bind(R.id.toggle_tuesday) ToggleButton tuesday;
    @Bind(R.id.toggle_wednesday) ToggleButton wednesday;
    @Bind(R.id.toggle_thursday) ToggleButton thursday;
    @Bind(R.id.toggle_friday) ToggleButton friday;
    @Bind(R.id.toggle_saturday) ToggleButton saturday;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_alarm);
        ButterKnife.bind(this);

        // 이제 여기서 시간설정하고 디비에 추가해주는 작업을 여기서 다 해줘야되는거다....
        // 우선 기본적인 layout은 있으니까 이걸로 한번 해보자.
        timePicker = (TimePicker)findViewById(R.id.newTimePicker);
        newMemo = (EditText)findViewById(R.id.newMemo);
        timePicker.setIs24HourView(false);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmDbHelper = new AlarmDbHelper(this);
        mDb = alarmDbHelper.getWritableDatabase();


    }

    // 리스너들
    @OnClick({R.id.toggle_sunday, R.id.toggle_monday, R.id.toggle_tuesday, R.id.toggle_wednesday
            , R.id.toggle_thursday, R.id.toggle_friday, R.id.toggle_saturday})
    void buttonClicked(View view){
        switch (view.getId()){
            case R.id.toggle_sunday:
            {
                if(sunday.isChecked()){
                    sunday.setTextColor(Color.GREEN);
                    repeatDay[1] = 1;
                }else{
                    sunday.setTextColor(Color.BLACK);
                    repeatDay[1] = 0;
                }
                break;
            }
            case R.id.toggle_monday:
            {
                if(monday.isChecked()){
                    monday.setTextColor(Color.GREEN);
                    repeatDay[2] = 1;
                }else{
                    monday.setTextColor(Color.BLACK);
                    repeatDay[2] = 0;
                }
                break;
            }
            case R.id.toggle_tuesday:
            {
                if(tuesday.isChecked()){
                    tuesday.setTextColor(Color.GREEN);
                    repeatDay[3] = 1;
                }else{
                    tuesday.setTextColor(Color.BLACK);
                    repeatDay[3] = 0;
                }
                break;
            }
            case R.id.toggle_wednesday:
            {
                if(wednesday.isChecked()){
                    wednesday.setTextColor(Color.GREEN);
                    repeatDay[4] = 1;
                }else{
                    wednesday.setTextColor(Color.BLACK);
                    repeatDay[4] = 0;
                }
                break;
            }
            case R.id.toggle_thursday:
            {
                if(thursday.isChecked()){
                    thursday.setTextColor(Color.GREEN);
                    repeatDay[5] = 1;
                }else{
                    thursday.setTextColor(Color.BLACK);
                    repeatDay[5] = 0;
                }
                break;
            }
            case R.id.toggle_friday:
            {
                if(friday.isChecked()){
                    friday.setTextColor(Color.GREEN);
                    repeatDay[6] = 1;
                }else{
                    friday.setTextColor(Color.BLACK);
                    repeatDay[6] = 0;
                }
                break;
            }
            case R.id.toggle_saturday:
            {
                if(saturday.isChecked()){
                    saturday.setTextColor(Color.GREEN);
                    repeatDay[7] = 1;
                }else{
                    saturday.setTextColor(Color.BLACK);
                    repeatDay[7] = 0;
                }
                break;
            }

        }


        // 다 끝나고 앞에 반복 없으면
        for(int i=1; i<repeatDay.length;i++){
            if(repeatDay[i] == 1){
                repeat = true;
                repeatDay[0] = 1;
            }
        }
    }

    private long addNewAlarm(int hour, int minute, String memo, String repeat){
        ContentValues cv = new ContentValues();
        cv.put(AlarmContract.AlarmlistEntry.COLUMN_HOUR, hour);
        cv.put(AlarmContract.AlarmlistEntry.COLUMN_MINUTE, minute);
        cv.put(AlarmContract.AlarmlistEntry.COLUMN_MEMO, memo);
        cv.put(AlarmContract.AlarmlistEntry.COLUMN_REPEAT, repeat);
        return mDb.insert(AlarmContract.AlarmlistEntry.TABLE_NAME, null, cv);
    }

    // 버튼 눌리면
    public void newButtonClicked(View view){
        // 타임피커에서 시, 분, 메모 가져온다.
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        String memo = newMemo.getText().toString();
        String repeatLocal ="";
        // repeat을 가져오자.
        for(int i=0; i<repeatDay.length; i++){
            if(repeatDay[i] == 0){
                repeatLocal = repeatLocal + "0";
            }else if (repeatDay[i] == 1){
                repeatLocal = repeatLocal + "1";
            }
        }

        long afterInsert = addNewAlarm(hour, minute, memo, repeatLocal);
        Log.d(TAG, "Insert 하면 반환값이 뭐지? " + afterInsert);        //!@#
//        calendar.set(Calendar.HOUR_OF_DAY, cursor.getInt(1));
//        calendar.set(Calendar.MINUTE, cursor.getInt(2));
        // 데이터베이스에 이제 정보들 다 넣어서 바꿧는데... 그러면 pendingIntent 만들어줘야겠지?
        Calendar calendar = Calendar.getInstance();

//        int idColumn = cursor.getColumnIndex(AlarmContract.AlarmlistEntry._ID);
//        int hourColumn = cursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_HOUR);
//        int minuteColumn = cursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_MINUTE);
//        int memoColumn = cursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_MEMO);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        long intervalTime = 1000*60*60*24;
        long currentTime = System.currentTimeMillis();
        if(currentTime > calendar.getTimeInMillis()){
            // 시간이 이미 지난경우에는 설정한거보다 10분 후로 예를들어서 4시 12분인데 내가 4시 10분으로 하면 4시 20분에 울리게..!
            calendar.setTimeInMillis(calendar.getTimeInMillis() + intervalTime);
        }
        long triggerTime = calendar.getTimeInMillis();
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("triggerTime", triggerTime);
        intent.putExtra("id", (int) afterInsert);
        intent.putExtra("repeatDay", repeatDay);
        PendingIntent pIntent  = PendingIntent.getBroadcast(this, (int) afterInsert, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(repeat){
            Log.d(TAG, "[알람반복확인(AlarmNewSetting), 반복있음O.");
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, intervalTime, pIntent);
        }else{
            Log.d(TAG, "[알람반복확인(AlarmNewSetting), 반복없음X.");
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pIntent);
        }
        finish();
    }


}
