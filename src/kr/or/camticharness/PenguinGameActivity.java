package kr.or.camticharness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class PenguinGameActivity extends Activity implements View.OnClickListener {
    private Vibrator m_vibe;
    private ImageButton m_ibHigh;
    private ImageButton m_ibMiddle;
    private ImageButton m_ibLow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //진동 선언
        m_vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        findView();

        init();
    }

    private void findView(){
        m_ibHigh = (ImageButton)findViewById(R.id.bt_high);
        m_ibHigh.setOnClickListener(this);
        m_ibMiddle = (ImageButton)findViewById(R.id.bt_middle);
        m_ibMiddle.setOnClickListener(this);
        m_ibLow = (ImageButton)findViewById(R.id.bt_low);
        m_ibLow.setOnClickListener(this);
    }

    private void init(){

    }

    @Override
    public void onClick(View view) {
        m_vibe.vibrate(50);
        Intent intent;
        switch (view.getId()){
            case R.id.bt_high:
                intent = new Intent(this, PenguinGameStartActivity.class);
                intent.putExtra("difficulty", 3);
                startActivity(intent);
                break;
            case R.id.bt_middle:
                intent = new Intent(this, PenguinGameStartActivity.class);
                intent.putExtra("difficulty", 2);
                startActivity(intent);
                break;
            case R.id.bt_low:
                intent = new Intent(this, PenguinGameStartActivity.class);
                intent.putExtra("difficulty", 1);
                startActivity(intent);
                break;
        }
    }

}
