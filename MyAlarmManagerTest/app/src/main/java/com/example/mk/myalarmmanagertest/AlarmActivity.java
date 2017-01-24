package com.example.mk.myalarmmanagertest;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by mk on 2017-01-23.
 */

public class AlarmActivity extends AppCompatActivity {
    AlarmManager alarmManager;
    Button button;
    private static final String TAG = MainActivity.class.getSimpleName();
    Vibrator vibrator;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Intent intent = getIntent();
        //만약에 알람이 월요일 반복이었는데 오늘이 화요일이라면...
        // 알람 intent에는 월요일 반복이라는 정보가 들어있을테지..
        // 그렇다면 가장 먼저 해야 할것은 오늘의 요일을 알아내야겠지.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_receiver);
        Log.d(TAG, "AlarmActivity까지 와서 진동까지 잘됨...! 지우는게 이제 문제지..!");
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {3000, 1000};
        vibrator.vibrate(pattern, 0);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        button = (Button)findViewById(R.id.button);

    }

    public void alarmCancelClicked(View view){

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + id + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//        Intent intent = getIntent();
//        PendingIntent cancelIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        alarmManager.cancel(cancelIntent);
        vibrator.cancel();
        finish();
    }
}
