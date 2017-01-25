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
    boolean repeat = false ;

    @Override
    public void onReceive(Context context, Intent intent) {
          // 리시버를 한단계 거쳐서 엑티비티를 실행시키자. 만약 요일이 다르다면 엑티비티를 호출하지 말아야 할 것이다.

        Log.d(TAG, "[요일반복확인] 리시버 받는다.");
        this.intent = intent;
        long triggerTime = intent.getLongExtra("triggerTime", 0);
        int id = intent.getIntExtra("id", 0);
//        int day_of_week = intent.getIntExtra("day_of_week", 0);
        int[] repeatDay = intent.getIntArrayExtra("repeatDay");
        Log.d(TAG, "[요일반복확인] 인텐트에서 정보들 안전하게 받았다.");

        if(repeatDay[0] == 1){
            repeat = true;
        }else{
            repeat = false;
        }

        Calendar calendar = Calendar.getInstance();

        // 만약 반복이 있으면
        if(repeat){
            // 지금 요일과 비교해야됨... 우선 오늘이 무슨 요일인지 가지고 온다..!
            Log.d(TAG, "[요일반복확인] 반복이 있는거 확인 됬다.");
            if(repeatDay[(int) calendar.get(Calendar.DAY_OF_WEEK)] == 1){
                // 오늘 알람이 울리는 날이면
                Log.d(TAG, "[요일반복확인] 반복이 있고 오늘이다.");
                Intent newActivity = new Intent(context, AlarmActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, newActivity, PendingIntent.FLAG_UPDATE_CURRENT);
                try {
                    pIntent.send();
                }catch(PendingIntent.CanceledException exception){
                    exception.printStackTrace();
                }
            }else{
                Log.d(TAG, "[요일반복확인] 반복은 있는데 오늘은 아니다.");
                return;
            }
        }else{
            Log.d(TAG, "[요일반복확인] 반복이 없다.");
            // 반복이 없다면 해당 시간에만 딱 한번만 울리게 하고 종료시켜야 할 것이다.
            Intent newActivity = new Intent(context, AlarmActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, newActivity, PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                pIntent.send();
            }catch(PendingIntent.CanceledException exception){
                exception.printStackTrace();
            }
            return;
        }

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
