package com.example.mk.myalarmmanagertest;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mk.myalarmmanagertest.Data.AlarmContract;

/**
 * Created by mk on 2017-01-23.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>{

    private static final String TAG = MainActivity.class.getSimpleName();

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;

    final private AlarmAdapterOnClickHandler mClickHandler;


    public AlarmAdapter(Context mContext, Cursor cursor, AlarmAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        this.mCursor = cursor;
        this.mClickHandler = clickHandler;
    }
    public AlarmAdapter(Context mContext, Cursor cursor){
        this.mContext = mContext;
        this.mCursor = cursor;
        mClickHandler = null;
    }

    public interface AlarmAdapterOnClickHandler{
        // 아이디, 시간, 분, 메모 넘겨줄거임..
        void onClick(int adapterPosition);
    }

    @Override
    public AlarmAdapter.AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.alarm_item_list, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmAdapter.AlarmViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position))
            return;
        // Indices for the _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(AlarmContract.AlarmlistEntry._ID);
        int hourIndex = mCursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_HOUR);
        int minuteIndex = mCursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_MINUTE);
        int repeatDayIndex = mCursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_REPEAT);
        int memoIndex = mCursor.getColumnIndex(AlarmContract.AlarmlistEntry.COLUMN_MEMO);
        Log.d(TAG, "언제호출되는지보자@@");
        mCursor.moveToPosition(position);
        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        int hour = mCursor.getInt(hourIndex);
        int minute = mCursor.getInt(minuteIndex);
        String repeatDay = mCursor.getString(repeatDayIndex);
        String memo = mCursor.getString(memoIndex);

        // 지울경우 position 값과 id값은 차이가 점점 벌어진다. 따라서 우리는 화면에서는 표시되지 않지만 안에 들어가 있는 tag값을 이용한다.
        // 테그 값에는 id가 들어가있다. Id는 기본키값이고 앞의 id들이 지워져도 계속 쭉 증가만 한다.

        holder.itemView.setTag(id);
        String day_or_night;
        String repeatDayShown;


        if(Integer.parseInt(String.valueOf(repeatDay.charAt(0))) == 0){
            // 반복이 없다는 뜻이므로
            repeatDayShown = "No repeat";
        }else{
            // 반복이 있다는 뜻이므로
            repeatDayShown = "(";
            if(Integer.parseInt(String.valueOf(repeatDay.charAt(1))) == 1){
                // 만약 일요일 반복이 있으면
                repeatDayShown = repeatDayShown + "일";
            }
            if(Integer.parseInt(String.valueOf(repeatDay.charAt(2))) == 1){
                // 만약 일요일 반복이 있으면
                repeatDayShown = repeatDayShown + "월";
            }
            if(Integer.parseInt(String.valueOf(repeatDay.charAt(3))) == 1){
                // 만약 일요일 반복이 있으면
                repeatDayShown = repeatDayShown + "화";
            }
            if(Integer.parseInt(String.valueOf(repeatDay.charAt(4))) == 1){
                // 만약 일요일 반복이 있으면
                repeatDayShown = repeatDayShown + "수";
            }
            if(Integer.parseInt(String.valueOf(repeatDay.charAt(5))) == 1){
                // 만약 일요일 반복이 있으면
                repeatDayShown = repeatDayShown + "목";
            }
            if(Integer.parseInt(String.valueOf(repeatDay.charAt(6))) == 1){
                // 만약 일요일 반복이 있으면
                repeatDayShown = repeatDayShown + "금";
            }
            if(Integer.parseInt(String.valueOf(repeatDay.charAt(7))) == 1){
                // 만약 일요일 반복이 있으면
                repeatDayShown = repeatDayShown + "토";
            }
            repeatDayShown = repeatDayShown + ")";
        }

        // 만약 불러온 시간이 12 보다 컸으면
        if(hour > 12){
            hour = hour-12;
            day_or_night = "(p.m)";
        }else if(hour == 12){
            day_or_night = "(p.m)";
        }else{
            day_or_night = "(a,m)";
        }
        holder.hourView.setText(String.valueOf(hour));
        holder.minuteView.setText(String.valueOf(minute));
        holder.day_or_night_View.setText(day_or_night);
        holder.repeat_day_View.setText(repeatDayShown);

        // 나중에는 여기 id로 바꾸자..
        holder.idView.setText(memo);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        // Always close the previous mCursor first
        if(mCursor != null) mCursor.close();
        mCursor = newCursor;
        if(newCursor != null){
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView hourView;
        TextView minuteView;
        TextView day_or_night_View;
        TextView repeat_day_View;

        TextView idView;
        public AlarmViewHolder(View itemView){
            super(itemView);
            hourView = (TextView) itemView.findViewById(R.id.hourView);
            minuteView = (TextView) itemView.findViewById(R.id.minuteView);
            day_or_night_View = (TextView) itemView.findViewById(R.id.day_or_night_View);
            repeat_day_View = (TextView) itemView.findViewById(R.id.repeat_day_View);
            idView = (TextView) itemView.findViewById(R.id.idView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // 아이템이 클릭되면 여기로옴
            int adapterPosition = getAdapterPosition();
            // 이런식으로하면 해당 텍스트들을 받아올 수 있다. 이것을 다음 엑티비티로 넘길 순 있지만 굳이 그러지 말자..
            // 아이디만 넘긴뒤에 해당 엑티비티에서 디비를 이용해서 하자..
//            String test = hourView.getText().toString();
//            Toast.makeText(mContext, test, Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "po: " + adapterPosition + "id: " + , Toast.LENGTH_SHORT);

            // 해당 포지션을 넘겨주지말고 해당 포지션의 ID값을 가져오자.
            int tag = (int)view.getTag();
            mClickHandler.onClick(tag);
            Log.d(TAG, "@@@@@@@@@@@@onClick in ViewHolder" + adapterPosition);
        }
    }
}
