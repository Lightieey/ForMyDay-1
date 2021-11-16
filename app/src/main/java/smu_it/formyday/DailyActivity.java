package smu_it.formyday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ServiceCompat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DailyActivity extends AppCompatActivity {

    TextView dateText;
    ListView checkList;
    CheckListAdapter adapter;
    ArrayList<String> listItem;
    TextView stopWatch;
    Button btnStart, btnRecord;
    final static int Init = 0;
    final static int Run = 1;
    final static int Pause = 2;

    int cur_Status = Init;
    long myBaseTime, myPauseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        dateText = findViewById(R.id.tv_date);
        dateText.setText(getTime());

        listItem = new ArrayList<String>();
        adapter = new CheckListAdapter();
        checkList = (ListView) findViewById(R.id.lv_check);
        checkList.setAdapter(adapter);
        adapter.addItem("일정 1");

        stopWatch = (TextView) findViewById(R.id.tv_stopWatch);
        btnStart = (Button) findViewById(R.id.bt_start);
        btnRecord = (Button) findViewById(R.id.bt_record);



    }

    // 현재 날짜 가져오는 함수
    private String getTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일");
        String getTime = dateFormat.format(date);

        return getTime;
    }

    // 스톱워치 기능 구현 함수
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void myOnClick(View view){
        switch(view.getId()){
            case R.id.bt_start:
                switch(cur_Status){
                    case Init:
                        myBaseTime = SystemClock.elapsedRealtime();
                        System.out.println(myBaseTime);
                        myTimer.sendEmptyMessage(0);
                        btnStart.setText("STOP");
                        btnRecord.setEnabled(true);
                        cur_Status = Run;
                        break;
                    case Run:
                        myTimer.removeMessages(0);
                        myPauseTime = SystemClock.elapsedRealtime();
                        btnStart.setText("START");
                        btnRecord.setText("RECORD");
                        cur_Status = Pause;
                        break;
                    case Pause:
                        long now = SystemClock.elapsedRealtime();
                        myTimer.sendEmptyMessage(0);
                        myBaseTime += (now-myPauseTime);
                        btnStart.setText("STOP");
                        btnRecord.setText("RECORD");
                        cur_Status = Run;
                        break;
                }
                break;
        }
    }

    Handler myTimer = new Handler(){
        public void handleMessage(Message msg){
            stopWatch.setText(getTimeOut());
            myTimer.sendEmptyMessage(0);
        }
    };

    String getTimeOut() {
        long now = SystemClock.elapsedRealtime();
        long outTime = now - myBaseTime;
        String easy_outTime = String.format("%02d:%02d:%02d",
                (outTime / 1000) / 60 / 60, (outTime / 1000) / 60, (outTime / 1000) % 60);
        return easy_outTime;
    }
}