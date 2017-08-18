package kr.or.camticharness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class GameMenuActivity extends Activity implements View.OnClickListener {
    private Vibrator m_vibe;
    private ImageButton m_ibCar;
    private ImageButton m_ibPenguin;

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
        m_ibCar = (ImageButton)findViewById(R.id.bt_car);
        m_ibCar.setOnClickListener(this);
        m_ibPenguin = (ImageButton)findViewById(R.id.bt_penguin);
        m_ibPenguin.setOnClickListener(this);
    }

    private void init(){

    }

    @Override
    public void onClick(View view) {
        m_vibe.vibrate(50);
        Intent intent;
        switch (view.getId()){
            case R.id.bt_car:
                intent = new Intent(this, CarGameActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_penguin:
                intent = new Intent(this, PenguinGameActivity.class);
                startActivity(intent);
                break;
        }
    }

}
