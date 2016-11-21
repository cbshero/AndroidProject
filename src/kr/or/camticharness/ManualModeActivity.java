package kr.or.camticharness;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import kr.or.camticharness.util.AutoModeListener;
import kr.or.camticharness.util.CommandManager;
import kr.or.camticharness.util.DBHelper;
import kr.or.camticharness.util.FileManager;
import kr.or.camticharness.util.Prefer;
import kr.or.camticharness.util.ProtocolControl;
import kr.or.camticharness.vo.DeviceData;
import kr.or.camticharness.vo.Statics;
import kr.or.camticharness.vo.Users;

public class ManualModeActivity extends Activity implements View.OnClickListener, AutoModeListener {
    Vibrator m_vibe;
    private Context m_context;

    private Prefer m_prefer;
    private int m_nUserSid;

    private ImageButton m_ibStart;
    private ImageButton m_ibStop;
    private Spinner m_spSpeed;
    private TextView m_tvLoadCell;
    private TextView m_tvTime;

    private int m_nWorkTime = 0;
    private DeviceData m_data;
    private boolean m_blStopTime = false;
    private boolean m_blStart = false;
    private ProtocolControl m_pc;

    double dbLeftRightRate = (int) (400/(Config.LEFT_RIGHT_MAX - Config.LEFT_RIGHT_MIN));
    double dbForwBackRate = (int) (400/(Config.FORW_BACK_MAX - Config.FORW_BACK_MIN));
    LinearLayout.LayoutParams m_layoutParams;

    private ImageButton m_ivMoveUp;
    private ImageButton m_ivMoveDown;
    private ImageButton m_ivMoveBackward;
    private ImageButton m_ivMoveForward;

    private ImageButton m_ibPower;
    private boolean m_blCurrPower;

    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_mode);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //진동 선언
        m_vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        m_context = this;

        m_prefer = new Prefer(m_context);
        m_nUserSid = m_prefer.getCurrUserSid();

        findView();

        init();

    }

    private void findView(){
        m_ibStart = (ImageButton)findViewById(R.id.ib_start);
        m_ibStart.setOnClickListener(this);
        m_ibStop = (ImageButton)findViewById(R.id.ib_stop);
        m_ibStop.setOnClickListener(this);

        m_spSpeed = (Spinner) findViewById(R.id.speed);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(m_context, R.array.speed_type, R.layout.textview_spinner2);
        m_spSpeed.setAdapter(adapter);

        m_tvLoadCell = (TextView) findViewById(R.id.loadcell);
        m_tvTime = (TextView) findViewById(R.id.time);

        m_ivMoveUp = (ImageButton)findViewById(R.id.btn_move_up);
        m_ivMoveUp.setOnClickListener(this);
        m_ivMoveDown = (ImageButton)findViewById(R.id.btn_move_down);
        m_ivMoveDown.setOnClickListener(this);
        m_ivMoveBackward = (ImageButton)findViewById(R.id.btn_move_left);
        m_ivMoveBackward.setOnClickListener(this);
        m_ivMoveForward = (ImageButton)findViewById(R.id.btn_move_right);
        m_ivMoveForward.setOnClickListener(this);

        m_ibPower = (ImageButton) findViewById(R.id.btn_power);
        m_ibPower.setOnClickListener(this);
    }

    private void init() {
        m_blCurrPower = false;

        m_pc = new ProtocolControl(this);
        //Mannual Mode Command Send
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(m_pc.m_blConnectServer) {
            byte[] command = CommandManager.makeCommand("mode", 0);
            if (m_pc != null)
                m_pc.sendCommand(command);
        }else{
            Toast.makeText(m_context, "서버에 연결하지 못하였습니다. 다시 시도해 주십시요.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        byte[] command;
        switch (view.getId()){
            case R.id.btn_power:
                String strTitle = null;
                if(m_blCurrPower) {
                    strTitle = "정말 전원을 종료하시겠습니까?";
                }else {
                    strTitle = "정말 전원을 켜시겠습니까?";
                }
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
                alert_confirm.setMessage(strTitle).setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                byte[] command;
                                if(m_blCurrPower) {
                                    m_blCurrPower = false;
                                    command = CommandManager.makeCommand("power", 0);
                                    handler.post(new Runnable() {
                                        public void run() {
                                            m_ibPower.setImageResource(R.drawable.btn_off);
                                        }
                                    });
                                }else{
                                    m_blCurrPower = true;
                                    command = CommandManager.makeCommand("power", 1);
                                    handler.post(new Runnable() {
                                        public void run() {
                                            m_ibPower.setImageResource(R.drawable.btn_on);
                                        }
                                    });
                                }
                                if (m_pc != null)
                                    m_pc.sendCommand(command);
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
                break;
            case R.id.ib_start:
                if(m_pc.m_blConnectServer) {
                    m_blStart = true;
                    //시작 버튼은 한번만 클릭되게 하기 위해서
                    m_ibStart.setSelected(!m_ibStart.isSelected());
                    m_ibStart.setOnClickListener(null);

                    // 주행 속도 데이터 전송
                    int nSpeedType = m_spSpeed.getSelectedItemPosition();
                    command = CommandManager.makeCommand("speed", nSpeedType + 1);
                    if (m_pc != null)
                        m_pc.sendCommand(command);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //몸무게 전송
/*
                    Users users = getUser();
                    command = CommandManager.makeCommand("weight", users.getWeight());
                    if (m_pc != null)
                        m_pc.sendCommand(command);
*/

                    // 수동 모드 훈련 시작
                    command = CommandManager.makeCommand("start", 0);
                    if (m_pc != null)
                        m_pc.sendCommand(command);

                    //현재 시간 출력
                    new Thread() {
                        public void run() {
                            while (!m_blStopTime) {
                                handler.post(new Runnable() {
                                    public void run() {
                                        m_tvTime.setText((m_nWorkTime++) + " sec");
                                    }
                                });
                                try {
                                    sleep(999);//최소 적용 시간
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();
                }else{
                    Toast.makeText(m_context, "장비측 서버와 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_stop:
                if(m_blStart) {
                    m_blStart = false;
                    m_blStopTime = true;
                    command = CommandManager.makeCommand("stop", 0);
                    if (m_pc != null)
                        m_pc.sendCommand(command);

                    if(m_pc.m_arrDeviceData.size()>0) {
                        FileManager fileManager = new FileManager(m_context);
                        String strFilePath = fileManager.writeStringAsFile(m_pc.m_arrDeviceData);
                        if (strFilePath != null)
                            Toast.makeText(m_context, strFilePath + "에 로그파일이 생성되었습니다.", Toast.LENGTH_LONG).show();

                        //훈련 결과 저장
                        double dbForwadBackWard = 0.0;
                        double dbLeftRight = 0.0;
                        for(DeviceData deviceData:m_pc.m_arrDeviceData){
                            dbForwadBackWard += deviceData.getForward_backward();
                            dbLeftRight += deviceData.getLeft_right();
                        }
                        int deviceCnt = m_pc.m_arrDeviceData.size();
                        Statics statics = new Statics();
                        statics.setForward_backward(dbForwadBackWard/deviceCnt);
                        statics.setLeft_right(dbLeftRight/deviceCnt);
                        statics.setPractice_type(0);
                        statics.setPractice_time(m_nWorkTime);
                        statics.setUser_sid(m_nUserSid);
                        if(m_tvLoadCell.getText()!=null) {
                            String strWeightRate = m_tvLoadCell.getText().toString().replaceAll(" ", "").replaceAll("kg", "");
                            statics.setWeight_rate(Double.parseDouble(strWeightRate));
                        }
                        int nSpeedType = m_spSpeed.getSelectedItemPosition();
                        statics.setSpeed(nSpeedType+1);
                        addStatics(statics);
                    }else{
                        Toast.makeText(m_context, "장비로부터 수신된 데이터가 없습니다.", Toast.LENGTH_LONG).show();
                    }
                    if (m_pc != null) {
                        try {
                            m_pc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        m_pc = null;
                    }
                    finish();
                }
                break;
            case R.id.btn_move_up:
                if(m_pc.m_blConnectServer) {
                    command = CommandManager.makeCommand("up", 0);
                    if (m_pc != null)
                        m_pc.sendCommand(command);
                }else{
                    Toast.makeText(m_context, "장비측 서버와 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_move_down:
                if(m_pc.m_blConnectServer) {
                    command = CommandManager.makeCommand("down", 0);
                    if (m_pc != null)
                        m_pc.sendCommand(command);
                }else{
                    Toast.makeText(m_context, "장비측 서버와 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_move_left:
                if(m_pc.m_blConnectServer) {
                    command = CommandManager.makeCommand("backward", 0);
                    if (m_pc != null)
                        m_pc.sendCommand(command);
                }else{
                    Toast.makeText(m_context, "장비측 서버와 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_move_right:
                if(m_pc.m_blConnectServer) {
                    command = CommandManager.makeCommand("forward", 0);
                    if (m_pc != null)
                        m_pc.sendCommand(command);
                }else{
                    Toast.makeText(m_context, "장비측 서버와 연결이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e("destroy", "....................................");
        super.onDestroy();
        try {
            if(m_pc!=null) {
                m_pc.close();
                m_pc = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void receiveData(DeviceData data) {
//        Log.e("camtic", "rcv data==>"+data.getForward_backward()+":"+data.getLeft_right());
        m_data = data;
        if(m_data.getPower()==1){
            m_blCurrPower = true;
            handler.post(new Runnable() {
                public void run() {
                    m_ibPower.setImageResource(R.drawable.btn_on);
                }
            });
        }else{
            m_blCurrPower = false;
            handler.post(new Runnable() {
                public void run() {
                    m_ibPower.setImageResource(R.drawable.btn_off);
                }
            });
        }
        handler.post(new Runnable() {
            public void run() {
                m_tvLoadCell.setText(m_data.getLoadcell()+" kg");
            }
        });
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

    private boolean addStatics(Statics statics){
        boolean bl = false;
        DBHelper dbHelper = new DBHelper(m_context, Config.VERSION_NUM);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("user_sid", statics.getUser_sid());
        contentValues.put("practice_type", statics.getPractice_type());
        contentValues.put("forward_backward", statics.getForward_backward());
        contentValues.put("left_right", statics.getLeft_right());
        contentValues.put("speed", statics.getSpeed());
        contentValues.put("weight_rate", statics.getWeight_rate());
        contentValues.put("practice_time", statics.getPractice_time());

        db.insert("statics", null, contentValues);

        bl = true;

        if(db.isOpen())
            db.close();
        dbHelper.close();

        return bl;
    }
}
