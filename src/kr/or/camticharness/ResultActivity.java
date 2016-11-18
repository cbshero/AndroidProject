package kr.or.camticharness;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

import kr.or.camticharness.util.DBHelper;
import kr.or.camticharness.util.Prefer;
import kr.or.camticharness.vo.Statics;
import kr.or.camticharness.vo.Users;

public class ResultActivity extends Activity {
    private Vibrator m_vibe;
    private Context m_context;

    private Prefer m_prefer;
    private int m_nUserSid;

    private TextView m_tvUserName;
    private TextView m_tvUserAge;
    private TextView m_tvUserSex;
    private TextView m_tvUserDisease;
    private TextView m_tvWeight;
    private TextView m_tvWeightRate;

    private ImageView m_ivPointer;
    LinearLayout.LayoutParams m_layoutParams;
    double dbLeftRightRate = (int) (120/(Config.LEFT_RIGHT_MAX - Config.LEFT_RIGHT_MIN));
    double dbForwBackRate = (int) (130/(Config.FORW_BACK_MAX - Config.FORW_BACK_MIN));

    private Statics m_statics;
    Handler handler = new Handler();
    // For chart
    protected BarChart mChart1;
    protected BarChart mChart2;
    protected BarChart mChart3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //진동 선언
        m_vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        m_context = this;

        m_prefer = new Prefer(m_context);
        m_nUserSid = m_prefer.getCurrUserSid();

        findView();

        init();
    }

    private void findView() {
        m_tvUserName = (TextView) findViewById(R.id.user_name);
        m_tvUserAge = (TextView) findViewById(R.id.user_age);
        m_tvUserSex = (TextView) findViewById(R.id.user_sex);
        m_tvUserDisease = (TextView) findViewById(R.id.user_disease);
        m_tvWeight = (TextView) findViewById(R.id.weight);
        m_tvWeightRate = (TextView) findViewById(R.id.weight_rate);

        m_ivPointer = (ImageView) findViewById(R.id.position_point);
        m_layoutParams = (LinearLayout.LayoutParams) m_ivPointer.getLayoutParams();
        //for chart
        mChart1 = (BarChart) findViewById(R.id.chart1);
        mChart2 = (BarChart) findViewById(R.id.chart2);
        mChart3 = (BarChart) findViewById(R.id.chart3);
    }

    private void init() {
        Users users = getUser();
        m_tvUserName.setText(users.getName());
        m_tvUserAge.setText(String.valueOf(users.getAge()));
        if(users.getSex()==0)
            m_tvUserSex.setText("남자");
        else
            m_tvUserSex.setText("여자");
        m_tvUserDisease.setText(users.getDisease());
        m_tvWeight.setText(users.getWeight()+" kg");

        ArrayList<Statics> arrayList = getStatics();
        if(arrayList.size()>0) {
            Statics sttLastest = arrayList.get(arrayList.size() - 1);
            if(sttLastest != null)
                m_tvWeightRate.setText(sttLastest.getWeight_rate() + " kg");

            float fFowardBackward = Float.parseFloat(String.valueOf((sttLastest.getForward_backward()-Config.FORW_BACK_MIN)/((Config.FORW_BACK_MAX-Config.FORW_BACK_MIN)/100)));
            float fLeftRight = Float.parseFloat(String.valueOf((sttLastest.getLeft_right()-Config.LEFT_RIGHT_MIN)/((Config.LEFT_RIGHT_MAX-Config.LEFT_RIGHT_MIN)/100)));

            m_layoutParams.topMargin = (int) (fFowardBackward * 1.6) + 60;
            m_layoutParams.leftMargin = (int) (fLeftRight * 1.6) + 60;

            Log.e("m_layoutParams", m_layoutParams.topMargin+":"+m_layoutParams.leftMargin);

            m_ivPointer.setLayoutParams(m_layoutParams);
            //균형 차트
            float[] chart1_data = {fLeftRight, fFowardBackward};
            BarData data = new BarData(getXAxisValues4Chart1(), getDataSet4Chart1(chart1_data));
            mChart1.setData(data);
            mChart1.setDescription("");
            mChart1.animateXY(2000, 2000);
            mChart1.getAxisLeft().setAxisMaxValue(100f);
            mChart1.getAxisRight().setAxisMaxValue(100f);
            mChart1.invalidate();

            //일별 그래프 Data Setting
            ArrayList<float[]> arrFloatData = new ArrayList<>();
            ArrayList<BarEntry> arrData4Chart3 = new ArrayList<>();
            ArrayList<String> arrDate = new ArrayList<>();
            int i = 0;
//            Random random = new Random();
            for(Statics statics:arrayList){
                float[] fData = new float[2];
//                fData[0] = random.nextFloat()*90;
//                fData[1] = random.nextFloat()*90;
                fData[0] = Float.parseFloat(String.valueOf((statics.getForward_backward()-Config.FORW_BACK_MIN)/((Config.FORW_BACK_MAX-Config.FORW_BACK_MIN)/100)));
                fData[1] = Float.parseFloat(String.valueOf((statics.getLeft_right()-Config.LEFT_RIGHT_MIN)/((Config.LEFT_RIGHT_MAX-Config.LEFT_RIGHT_MIN)/100)));
                arrFloatData.add(fData);

                arrData4Chart3.add(new BarEntry(statics.getPractice_time(), i++));
                arrDate.add(statics.getRegdate().substring(5,16));
            }

            // 자세균형 결과
            BarData data2 = new BarData(arrDate, getDataSet4Chart2(arrFloatData));
            mChart2.setData(data2);
            mChart2.setDescription("");
            mChart2.animateXY(2000, 2000);
            mChart2.invalidate();

            // 날짜별 훈련시간
            BarDataSet barDataSet3 = new BarDataSet(arrData4Chart3, "sec(초)");
            barDataSet3.setColors(ColorTemplate.COLORFUL_COLORS);

            BarData data3 = new BarData(arrDate, barDataSet3);
            mChart3.setData(data3);
            mChart3.setDescription("");
            mChart3.animateXY(2000, 2000);
            mChart3.invalidate();

        }
    }

    private Users getUser() {
        Users users = new Users();
        DBHelper dbHelper = new DBHelper(m_context, Config.VERSION_NUM);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] strParams =  new String[]{String.valueOf(m_nUserSid)};
        Cursor cursor1 = db.rawQuery("select sid, name, age, sex, weight, disease, regdate from users where sid=? order by regdate desc", strParams);
        if (cursor1.moveToFirst()) {
            users.setSid(cursor1.getInt(0));
            users.setName(cursor1.getString(1));
            users.setAge(cursor1.getInt(2));
            users.setSex(cursor1.getInt(3));
            users.setWeight(cursor1.getInt(4));
            users.setDisease(cursor1.getString(5));
            users.setRegdate(cursor1.getString(6));
        }
        if (db.isOpen())
            db.close();
        dbHelper.close();

        return users;
    }

    private ArrayList<Statics> getStatics() {
        ArrayList<Statics> arrayList = new ArrayList<Statics>();
        DBHelper dbHelper = new DBHelper(m_context, Config.VERSION_NUM);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] strParams =  new String[]{String.valueOf(m_nUserSid)};
        Cursor cursor1 = db.rawQuery("select sid, user_sid, practice_type, forward_backward, left_right, speed, weight_rate, practice_time, regdate from statics where user_sid=? order by regdate asc", strParams);
        if (cursor1.moveToFirst()) {
            do {
                Statics statics = new Statics();
                statics.setSid(cursor1.getInt(0));
                statics.setUser_sid(cursor1.getInt(1));
                statics.setPractice_type(cursor1.getInt(2));
                statics.setForward_backward(cursor1.getDouble(3));
                statics.setLeft_right(cursor1.getDouble(4));
                statics.setSpeed(cursor1.getInt(5));
                statics.setWeight_rate(cursor1.getDouble(6));
                statics.setPractice_time(cursor1.getInt(7));
                statics.setRegdate(cursor1.getString(8));

                arrayList.add(statics);
            } while (cursor1.moveToNext());
        }
        if (db.isOpen())
            db.close();
        dbHelper.close();

        return arrayList;
    }

    private ArrayList<BarDataSet> getDataSet4Chart1(float[] data) {
        ArrayList<BarDataSet> dataSets = new ArrayList<>();

        ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
        valueSet1.add(new BarEntry(data[0], 0));// 좌
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "좌우");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        dataSets.add(barDataSet1);

        ArrayList<BarEntry> valueSet2 = new ArrayList<BarEntry>();
        valueSet2.add(new BarEntry(data[1], 0));// 우
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "앞뒤");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSets.add(barDataSet2);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues4Chart1() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("좌우/앞뒤 균형(%)");
        return xAxis;
    }


    private ArrayList<BarDataSet> getDataSet4Chart2(ArrayList<float[]> data) {
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        int i = 0;
        ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
        for(float[] fData: data) {
            valueSet1.add(new BarEntry(fData[0], i++));
        }
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "좌우");
        barDataSet1.setColor(ColorTemplate.PASTEL_COLORS[0]);
        dataSets.add(barDataSet1);


        int j = 0;
        ArrayList<BarEntry> valueSet2 = new ArrayList<BarEntry>();
        for(float[] fData: data) {
            valueSet2.add(new BarEntry(fData[1], j++));
        }
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "앞뒤");
        barDataSet2.setColor(ColorTemplate.PASTEL_COLORS[1]);
        dataSets.add(barDataSet2);
        return dataSets;
    }

}