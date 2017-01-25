package com.example.mk.myalarmmanagertest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.example.mk.myalarmmanagertest.Data.AlarmContract;
import com.example.mk.myalarmmanagertest.Data.AlarmDbHelper;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements AlarmAdapter.AlarmAdapterOnClickHandler{
    private static final String TAG = MainActivity.class.getSimpleName();

    private SQLiteDatabase mDb;
    AlarmDbHelper alarmDbHelper;

    private static final int ALARM_LOADER_ID = 0;
    // Member variables for the adapter and RecyclerView
    private AlarmAdapter mAdapter;
    RecyclerView mRecyclerView;

    // variables for the alarm
    AlarmManager alarmManager;
    PendingIntent pIntent;
    Calendar calendar;

//    // View들
//    EditText hourText;
//    EditText minuteText;
//    EditText memoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);

//        hourText = (EditText)findViewById(R.id.hourText);
//        minuteText = (EditText)findViewById(R.id.minuteText);
//        memoText = (EditText)findViewById(R.id.memo);

        // Set the RecyclerView to its corresponding view
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewTasks);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 데이터베이스 먼저!
        alarmDbHelper = new AlarmDbHelper(this);
        mDb = alarmDbHelper.getWritableDatabase();
        // Initialize the adapter and attach it to the RecyclerView
        Cursor cursor = getAllAlarm();
        for(int i=0; i<cursor.getCount();i++){
            cursor.moveToNext();
            Log.d(TAG, "_id 값은 뭐지? :" + cursor.getInt(0));
        }
        mAdapter = new AlarmAdapter(this, cursor, this);
        mRecyclerView.setAdapter(mAdapter);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent addTaskIntent = new Intent(MainActivity.this, AlarmNewSetting.class);
                startActivity(addTaskIntent);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            // COMPLETED (4) Override onMove and simply return false inside

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@##");
                return false;
            }

            // COMPLETED (5) Override onSwiped
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // COMPLETED (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
                //get the id of the item being swiped
                int id = (int)viewHolder.itemView.getTag();
                // COMPLETED (9) call removeGuest and pass through that id
                //remove from DB
                removeAlarm(id);
                // COMPLETED (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
                //update the list
                mAdapter.swapCursor(getAllAlarm());

                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent cancelIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.cancel(cancelIntent);
            }

            //COMPLETED (11) attach the ItemTouchHelper to the waitlistRecyclerView
        }).attachToRecyclerView(mRecyclerView);


        // 알람 두개를 넣어둘것임.
        // 하나는 9시 35분 다른 하나는 9시 40분.
//        addNewAlarm(22, 40, "test1");
//        addNewAlarm(22, 50, "test2");
//        cursor.moveToNext();
//        cursor.moveToNext();
//        cursor.moveToNext();
//        cursor.moveToNext();
//        cursor.moveToNext();
//        cursor.moveToNext();
        // 이제 확실히 들어간건 확인 했음 .
//        Log.d(TAG, "@@@@@@@@@@@@@@@@@@@"+cursor.getCount());
//        Log.d(TAG, "@@@@@@@@@@@@@@@@@@@"+cursor.getColumnCount());
        // 알람을 만들때 데이터베이스에 있는 것들을 이용해서 알람을 만들것임.
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        Calendar calendar = Calendar.getInstance();
        // 지금은 현재 시간의 3초후에 시작을 했는데.. 그게 아니고.. set을 이용해서..
//        calendar.set(Calendar.HOUR_OF_DAY, 21);
//        calendar.set(Calendar.MINUTE, 41);
//        Log.d(TAG, "@@@@@" + calendar.get(Calendar.SECOND));
//        Log.d(TAG, "@@@@@" + calendar.get(Calendar.MINUTE));
//        Log.d(TAG, "@@@@@" + calendar.get(Calendar.HOUR));
//        Log.d(TAG, "@@@@@" + calendar.get(Calendar.HOUR_OF_DAY));
//        Log.d(TAG, "@@@@@" + calendar.get(Calendar.DAY_OF_WEEK));

        // 데이터베이스에서 빼놨으니까 long 타입의 triggerTime만들자
//        calendar.set(Calendar.HOUR_OF_DAY, cursor.getInt(1));
//        calendar.set(Calendar.MINUTE, cursor.getInt(2));
//        long triggerTime = calendar.getTimeInMillis();
//        Intent intent = new Intent(this, AlarmActivity.class);
//        pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, 1000*60, pIntent);


    }

    @Override
    protected void onResume() {
        super.onResume();
        // 정보들 바뀐거를 표현해줘야지 resume될때..!
        //시간이 있다면 SharedPreferencelistener를 사용해보자.. 우선은 swapCursor이용해보고..!
        mAdapter.swapCursor(getAllAlarm());
    }

    public Cursor getAllAlarm(){
        return mDb.query(
                AlarmContract.AlarmlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                AlarmContract.AlarmlistEntry.COLUMN_HOUR
        );
    }

//    public void addToAlarmlist(View view){
//        String hour = hourText.getText().toString();
//        String minute = minuteText.getText().toString();
//        String memo = memoText.getText().toString();
//
//        int hourInt = Integer.parseInt(hour);
//        int minuteInt = Integer.parseInt(minute);
//
//        addNewAlarm(hourInt, minuteInt, memo);
////        calendar.set(Calendar.HOUR_OF_DAY, cursor.getInt(1));
////        calendar.set(Calendar.MINUTE, cursor.getInt(2));
//
//        Cursor cursor = getAllAlarm();
//        mAdapter.swapCursor(cursor);
//        cursor.moveToLast();
//
//
//        calendar = Calendar.getInstance();
//        // cursor 정보를 가지고 alarm을 만들어..!
//
//        int idColumn = cursor.getColumnIndex(AlarmContract.AlarmlistEntry._ID);
//        int hourColumn = cursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_HOUR);
//        int minuteColumn = cursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_MINUTE);
////        int memoColumn = cursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_MEMO);
//
//        calendar.set(Calendar.HOUR_OF_DAY, cursor.getInt(hourColumn));
//        calendar.set(Calendar.MINUTE, cursor.getInt(minuteColumn));
//        calendar.set(Calendar.SECOND, 0);
//        // Trigger 시간을 조절해줘야됨. 테스트 결과 현재시간보다 이전의 시간을 설정하면 바로 울리는 현상이 있음..
//        // 그러면 안되니까 inteval 이랑 Trigger 시간을 설정하자.
//        // 우선 테스트는 10분으로 한다.
//        long intevalTime = 1000*60*10;
//
//        // 이제 calendar에 setting된 값으로 triggerTime을 설정해줘야한다. 만약 지금 오후 4시인데 내가 오후 2시로 설정하면 trigger시간을 다음날 오후 2시로 바꿔줘야한다 오늘이 아니고..!
//        // 지금 시간이 만약 설정해둔 시간보다 큰 경우 설정해둔 시간에 24시간을 해줘야한다.
//        long currentTime = System.currentTimeMillis();
//        if(currentTime > calendar.getTimeInMillis()){
//            // 시간이 이미 지난경우에는 설정한거보다 10분 후로 예를들어서 4시 12분인데 내가 4시 10분으로 하면 4시 20분에 울리게..!
//            calendar.setTimeInMillis(calendar.getTimeInMillis() + 1000*60*10);
//        }
//
////        long triggerTime = calendar.getTimeInMillis();
////        Intent intent = new Intent(this, AlarmActivity.class);
////        intent.putExtra("id", cursor.getInt(0));
////        intent.putExtra("day_of_week", 2);
////        pIntent = PendingIntent.getActivity(this, cursor.getInt(0), intent, PendingIntent.FLAG_UPDATE_CURRENT);
////        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerTime, 1000*20, pIntent);
//
//        long triggerTime = calendar.getTimeInMillis();
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        intent.putExtra("triggerTime", triggerTime);
//        intent.putExtra("id", cursor.getInt(idColumn));
//        // 요일을 다르게 보내 본다.
//        intent.putExtra("day_of_week", 3);
//        pIntent  = PendingIntent.getBroadcast(this, cursor.getInt(idColumn), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, 1000*60*60*24, pIntent);
//        //        Intent intent = new Intent(this, AlarmReceiver.class);
////        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*100, pIntent);
//
//
//        hourText.getText().clear();
//        minuteText.getText().clear();
//        memoText.getText().clear();
//    }
//    private long addNewAlarm(int hour, int minute, String memo){
//        ContentValues cv = new ContentValues();
//        cv.put(AlarmContract.AlarmlistEntry.COLUMN_HOUR, hour);
//        cv.put(AlarmContract.AlarmlistEntry.COLUMN_MINUTE, minute);
//        cv.put(AlarmContract.AlarmlistEntry.COLUMN_MEMO, memo);
//        return mDb.insert(AlarmContract.AlarmlistEntry.TABLE_NAME, null, cv);
//    }

    private boolean removeAlarm(long id){
        return mDb.delete(AlarmContract.AlarmlistEntry.TABLE_NAME, AlarmContract.AlarmlistEntry._ID + "=" + id, null) > 0;
    }

    @Override
    public void onClick(int adapterPosition) {
        Log.d(TAG, "onClick in Mainactivity this is making intent");
        Context context = this;
        Class destinationClass = AlarmSetting.class;
        Intent intent = new Intent(context, destinationClass);

        // 이제는 넘겨줄때 시간정보들을 넘겨줄껀데.. 그럼 데이터베이스에서 정보를 꺼내와야되는건가..?
        // 굳이 데이터베이스 말고 해당 아이템의 텍스트 뷰에선 못꺼내 오나..?
        // 근데 텍스트뷰에는 memo가 없으니까 그냥 디비에서 꺼내오자..! 나중에 텍스트 뷰에서 꺼내오는거 해보자..!
        // 메인엑티비티는 복잡하니까 해당 엑티비티에서 띄워줄까? 아니면 그냥 여기서 할까..?

        intent.putExtra(Intent.EXTRA_TEXT, adapterPosition);
        startActivity(intent);
    }



}
