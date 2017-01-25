package com.example.mk.myalarmmanagertest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

public class AlarmSetting extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    EditText updateMemo;
    AlarmDbHelper alarmDbHelper;
    private SQLiteDatabase mDb;
    TimePicker timePicker;
    int intentTag;
    AlarmAdapter alarmAdapter;
    AlarmManager alarmManager;
    int[] repeatDay={0,0,0,0,0,0,0,0};
    boolean repeat = false;

    // 버터나이프 사용
    @Bind(R.id.update_toggle_sunday) ToggleButton sunday;
    @Bind(R.id.update_toggle_monday) ToggleButton monday;
    @Bind(R.id.update_toggle_tuesday) ToggleButton tuesday;
    @Bind(R.id.update_toggle_wednesday) ToggleButton wednesday;
    @Bind(R.id.update_toggle_thursday) ToggleButton thursday;
    @Bind(R.id.update_toggle_friday) ToggleButton friday;
    @Bind(R.id.update_toggle_saturday) ToggleButton saturday;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm);

        ButterKnife.bind(this);

        updateMemo = (EditText)findViewById(R.id.updateMemo);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        timePicker.setIs24HourView(false);
        // 이제 테그값 즉 primary Key 값이 있으므로 디비를 호출하자 수정도 할 것이니까 writable로 한다.
        Intent intent = getIntent();
        intentTag = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
        updateMemo.setText(String.valueOf(intentTag));

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        // 데이터베이스 매니져
        alarmDbHelper = new AlarmDbHelper(this);
        mDb = alarmDbHelper.getWritableDatabase();
        // Initialize the adapter and attach it to the RecyclerView

        Cursor cursor = getOneAlarm(intentTag);
        cursor.moveToFirst();
        int hourIndex = cursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_HOUR);
        int minuteIndex = cursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_MINUTE);
        int memoIndex = cursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_MEMO);
        int repeatIndex = cursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_REPEAT);
        Log.d(TAG, "[데이터베이스] 총 row 갯수 확인(예상 1) : " + cursor.getCount());
        Log.d(TAG, "[데이터베이스] 총 column 갯수 확인 : " + cursor.getColumnCount());
        Log.d(TAG, "[데이터베이스] 가져온 것의 시간정보확인 : " + cursor.getInt(hourIndex));
        Log.d(TAG, "[데이터베이스] 가져온 것의 분정보확인 : " + cursor.getInt(minuteIndex));
        Log.d(TAG, "[데이터베이스] 가져온 것의 메모정보확인 : " + cursor.getString(memoIndex));
        Log.d(TAG, "[데이터베이스] 가져온 것의 반복정보확인" + cursor.getString(repeatIndex));

        alarmAdapter = new AlarmAdapter(this, cursor);

        if(cursor != null){
            // 데이터정보를 바탕으로 데이터 세팅
            updateMemo.setText(cursor.getString(3));
            timePicker.setCurrentHour(cursor.getInt(1));
            timePicker.setCurrentMinute(cursor.getInt(2));
            // 여기서 repeat정보 이용할까..? 이용하자!
            String valueOfRepeat = cursor.getString(repeatIndex);
            Log.d(TAG,"[업데이트반복정보확인] : " + valueOfRepeat);
            for(int i=0; i< valueOfRepeat.length(); i++){
                // 맨앞에가 char(0)이 될것이다.
                Log.d(TAG,"[업데이트반복정보확인] : " + i);
                repeatDay[i] = Integer.parseInt(String.valueOf(valueOfRepeat.charAt(i)));
            }
            Log.d(TAG,"[업데이트반복정보확인] :" + repeatDay[0] + "," + repeatDay[1] + "," + repeatDay[2]+ "," + repeatDay[3]+ "," + repeatDay[4]
                    + "," + repeatDay[5]+ "," + repeatDay[6]+ "," + repeatDay[7]);

            // repeatDay 정보가 다 가져와졌으면 이제 togglebutton들 세팅해주자!
            if(repeatDay[1] == 1){
                sunday.setChecked(true);
                sunday.setTextColor(Color.GREEN);
            }
            if(repeatDay[2] == 1){
                monday.setChecked(true);
                monday.setTextColor(Color.GREEN);
            }
            if(repeatDay[3] == 1){
                tuesday.setChecked(true);
                tuesday.setTextColor(Color.GREEN);
            }
            if(repeatDay[4] == 1){
                wednesday.setChecked(true);
                wednesday.setTextColor(Color.GREEN);
            }
            if(repeatDay[5] == 1){
                thursday.setChecked(true);
                thursday.setTextColor(Color.GREEN);
            }
            if(repeatDay[6] == 1){
                friday.setChecked(true);
                friday.setTextColor(Color.GREEN);
            }
            if(repeatDay[7] == 1){
                saturday.setChecked(true);
                saturday.setTextColor(Color.GREEN);
            }

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
        // repeatDay를 이용해서 repeatString 정보를 만들자 데이터베이스에는 String형식으로 들어가야 되니까..!
        String repeatString = "";
        // repeat정보 바꿔줘야한다.
        repeatDay[0] = 0;
        for(int i=1; i<repeatDay.length; i++){
            if(repeatDay[i] == 1){
                repeatDay[0] = 1;
            }
        }
        for(int i=0; i<repeatDay.length;i++){
            repeatString = repeatString + repeatDay[i];
        }
        Log.d(TAG, "[업데이트 버튼 눌렸을때 데이터베이스 들어갈 repeat정보 확인 ] : " + repeatString);
        updateDB(hour, minute, memo, repeatString, intentTag);
        finish();
    }

    public void updateDB(int hour, int minute, String memo, String repeatString, int tag){
        ContentValues values = new ContentValues();
        values.put(AlarmContract.AlarmlistEntry.COLUMN_HOUR, hour);
        values.put(AlarmContract.AlarmlistEntry.COLUMN_MINUTE, minute);
        values.put(AlarmContract.AlarmlistEntry.COLUMN_MEMO, memo);
        values.put(AlarmContract.AlarmlistEntry.COLUMN_REPEAT, repeatString);
        mDb.update(AlarmContract.AlarmlistEntry.TABLE_NAME, values, "_id=" + tag, new String[]{});

        // 이렇게 업데이트 시킨 뒤에 해당 pendingintent를 무효화 시키고 새로운 pendingintent를 만들어야한다.
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent cancelIntent = PendingIntent.getBroadcast(getApplicationContext(), tag, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(cancelIntent);

        // 새롭게 만들어주자.
        Intent newIntent = new Intent(getApplicationContext(), AlarmReceiver.class);

        // 업데이트된 시간으로 새로 설정해주는거다.
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        // 우선은 30초로 설정하자. !@#
        long intervalTime = 1000*15;

        long currentTime = System.currentTimeMillis();
        if(currentTime > calendar.getTimeInMillis()){
            // 시간이 이미 지난경우에는 설정한거보다 10분 후로 예를들어서 4시 12분인데 내가 4시 10분으로 하면 4시 20분에 울리게..!
            Log.d(TAG, "여기 들어오면 안된다..!" + currentTime);
            Log.d(TAG, "여기 들어오면 안된다..!" + calendar.getTimeInMillis());
            Log.d(TAG, "여기 들어오면 안된다..!");    //!@#
            calendar.setTimeInMillis(calendar.getTimeInMillis() + 1000*60*60*24);
        }

        long triggerTime = calendar.getTimeInMillis();
        newIntent.putExtra("triggerTime", triggerTime);
        newIntent.putExtra("id", tag);
        newIntent.putExtra("repeatDay", repeatDay);
//        newIntent.putExtra("day_of_week", 2);
        Log.d(TAG, "[업데이트/ PendingIntent Tag 정보] : " + tag);       //!@#
        PendingIntent newPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), tag, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(repeatDay[0] == 1){
            Log.d(TAG, "[업데이트/ 반복이있는 PendingIntent 생성]" + tag); //!@#
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, intervalTime, newPendingIntent);
        }else if(repeatDay[0] == 0){
            Log.d(TAG, "[업데이트/ 반복이없는 PendingIntent 생성]" + tag);  //!@#
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, newPendingIntent);
        }

    }

    // 이제 버튼 클릭 이벤트를 넣어주자.
    @OnClick({R.id.update_toggle_sunday, R.id.update_toggle_monday, R.id.update_toggle_tuesday, R.id.update_toggle_wednesday
            , R.id.update_toggle_thursday, R.id.update_toggle_friday, R.id.update_toggle_saturday})
    void buttonClicked(View view) {
        switch (view.getId()) {
            case R.id.update_toggle_sunday: {
                if (sunday.isChecked()) {
                    sunday.setTextColor(Color.GREEN);
                    repeatDay[1] = 1;
                } else {
                    sunday.setTextColor(Color.BLACK);
                    repeatDay[1] = 0;
                }
                break;
            }
            case R.id.update_toggle_monday: {
                if (monday.isChecked()) {
                    monday.setTextColor(Color.GREEN);
                    repeatDay[2] = 1;
                } else {
                    monday.setTextColor(Color.BLACK);
                    repeatDay[2] = 0;
                }
                break;
            }
            case R.id.update_toggle_tuesday: {
                if (tuesday.isChecked()) {
                    tuesday.setTextColor(Color.GREEN);
                    repeatDay[3] = 1;
                } else {
                    tuesday.setTextColor(Color.BLACK);
                    repeatDay[3] = 0;
                }
                break;
            }
            case R.id.update_toggle_wednesday: {
                if (wednesday.isChecked()) {
                    wednesday.setTextColor(Color.GREEN);
                    repeatDay[4] = 1;
                } else {
                    wednesday.setTextColor(Color.BLACK);
                    repeatDay[4] = 0;
                }
                break;
            }
            case R.id.update_toggle_thursday: {
                if (thursday.isChecked()) {
                    thursday.setTextColor(Color.GREEN);
                    repeatDay[5] = 1;
                } else {
                    thursday.setTextColor(Color.BLACK);
                    repeatDay[5] = 0;
                }
                break;
            }
            case R.id.update_toggle_friday: {
                if (friday.isChecked()) {
                    friday.setTextColor(Color.GREEN);
                    repeatDay[6] = 1;
                } else {
                    friday.setTextColor(Color.BLACK);
                    repeatDay[6] = 0;
                }
                break;
            }
            case R.id.update_toggle_saturday: {
                if (saturday.isChecked()) {
                    saturday.setTextColor(Color.GREEN);
                    repeatDay[7] = 1;
                } else {
                    saturday.setTextColor(Color.BLACK);
                    repeatDay[7] = 0;
                }
                break;
            }
        }// swtich 문 끝
        for(int i=0; i<repeatDay.length;i++){
            if(repeatDay[i] == 1){
                repeat = true;
                repeatDay[0] = 1;
            }
        }
        Log.d(TAG,"[업데이트반복정보확인] :" + repeatDay[0] + "," + repeatDay[1] + "," + repeatDay[2]+ "," + repeatDay[3]+ "," + repeatDay[4]     //!@#
                + "," + repeatDay[5]+ "," + repeatDay[6]+ "," + repeatDay[7]);      //!@#

        // 이제 repeatDay를 통해서 데이터베이스 repeat정보에 update 시켜줘야됨.
    }
}
