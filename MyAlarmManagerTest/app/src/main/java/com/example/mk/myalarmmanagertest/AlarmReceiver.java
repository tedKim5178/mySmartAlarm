package com.example.mk.myalarmmanagertest;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by mk on 2017-01-23.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = MainActivity.class.getSimpleName();
    Intent intent;
    @Override
    public void onReceive(Context context, Intent intent) {
          // 리시버를 한단계 거쳐서 엑티비티를 실행시키자. 만약 요일이 다르다면 엑티비티를 호출하지 말아야 할 것이다.


        this.intent = intent;
        long triggerTime = intent.getLongExtra("triggerTime", 0);
        int id = intent.getIntExtra("id", 0);
        int day_of_week = intent.getIntExtra("day_of_week", 0);

        Log.d(TAG, "오늘 무슨 요일? in BroadcastReceiver?  : " + day_of_week);


        Calendar calendar = Calendar.getInstance();
        boolean matchDay = false;
        switch(day_of_week){
            case 0:
            {
                // 오늘이 일요일이고 알람이 일요일 반복이라면
                if(Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK)){
                    // 요일이 일치한다 그러므로 알람을 진행한다
                    matchDay = true;
                }
                break;
            }
            case 1:
            {
                // 오늘이 일요일이고 알람이 일요일 반복이라면
                if(Calendar.MONDAY == calendar.get(Calendar.DAY_OF_WEEK)){
                    // 요일이 일치한다 그러므로 알람을 진행한다
                    matchDay = true;
                }
                break;
            }case 2:
            {
                // 오늘이 일요일이고 알람이 일요일 반복이라면
                if(Calendar.TUESDAY == calendar.get(Calendar.DAY_OF_WEEK)){
                    // 요일이 일치한다 그러므로 알람을 진행한다
                    matchDay = true;
                }
                break;
            }case 3:
            {
                // 오늘이 일요일이고 알람이 일요일 반복이라면
                if(Calendar.WEDNESDAY == calendar.get(Calendar.DAY_OF_WEEK)){
                    // 요일이 일치한다 그러므로 알람을 진행한다
                    matchDay = true;
                }
                break;
            }case 4:
            {
                // 오늘이 일요일이고 알람이 일요일 반복이라면
                if(Calendar.THURSDAY == calendar.get(Calendar.DAY_OF_WEEK)){
                    // 요일이 일치한다 그러므로 알람을 진행한다
                    matchDay = true;
                }
                break;
            }case 5:
            {
                // 오늘이 일요일이고 알람이 일요일 반복이라면
                if(Calendar.FRIDAY == calendar.get(Calendar.DAY_OF_WEEK)){
                    // 요일이 일치한다 그러므로 알람을 진행한다
                    matchDay = true;
                }
                break;
            }case 6:
            {
                // 오늘이 일요일이고 알람이 일요일 반복이라면
                if(Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK)){
                    // 요일이 일치한다 그러므로 알람을 진행한다
                    matchDay = true;
                }
                break;
            }
        }
        Log.d(TAG, "오늘 반복되는 요일 맞지? : " + matchDay);
        // 이제 엑티비티 호출 하는데 요일을 따진 다음에 호출 할지 말지 하자
        if(matchDay){
            // 펜딩인텐트를 통해서 activity 호출하자
            //        long triggerTime = calendar.getTimeInMillis();
//        Intent intent = new Intent(this, AlarmActivity.class);
//        intent.putExtra("id", cursor.getInt(0));
//        intent.putExtra("day_of_week", 2);
//        pIntent = PendingIntent.getActivity(this, cursor.getInt(0), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, 1000*20, pIntent);
            Intent newActivity = new Intent(context, AlarmActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, newActivity, PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                pIntent.send();
            }catch(PendingIntent.CanceledException exception){
                exception.printStackTrace();
            }
        }else{
            return;
        }

        return;
//        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, AlarmActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification.Builder builder = new Notification.Builder(context);
//        builder.setSmallIcon(R.drawable.on).setTicker("HETT").setWhen(System.currentTimeMillis())
//        .setDefaults(Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);
//        notificationManager.notify(1, builder.getNotification());
    }
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//    }
}
