package kr.or.camticharness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class SelectActionActivity extends Activity implements View.OnClickListener{
    private Vibrator m_vibe;
    private ImageButton m_ibAuto;
    private ImageButton m_ibManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //진동 선언
        m_vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        findView();

        init();
    }

    private void findView(){
        m_ibAuto = (ImageButton)findViewById(R.id.bt_auto);
        m_ibAuto.setOnClickListener(this);
        m_ibManual = (ImageButton)findViewById(R.id.bt_manual);
        m_ibManual.setOnClickListener(this);
    }

    private void init(){

    }

    @Override
    public void onClick(View view) {
        m_vibe.vibrate(300);
        switch (view.getId()){
            case R.id.bt_auto:
                Intent intent1 = new Intent(SelectActionActivity.this, AutoModeActivity.class);
                startActivity(intent1);
                break;
            case R.id.bt_manual:
                Intent intent2 = new Intent(SelectActionActivity.this, ManualModeActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
