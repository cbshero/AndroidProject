package kr.or.camticharness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

public class SelectMenuActivity extends Activity implements View.OnClickListener {
    private Vibrator m_vibe;
    private ImageButton m_ibTraining;
    private ImageButton m_ibTrainingResult;
    private Context m_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_menu);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        m_context = this;
        //진동 선언
        m_vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        findView();

        init();
    }

    private void findView(){
        m_ibTraining = (ImageButton)findViewById(R.id.bt_training);
        m_ibTraining.setOnClickListener(this);
        m_ibTrainingResult = (ImageButton)findViewById(R.id.bt_training_result);
        m_ibTrainingResult.setOnClickListener(this);
    }

    private void init(){

    }

    @Override
    public void onClick(View view) {
        m_vibe.vibrate(50);
        switch (view.getId()){
            case R.id.bt_training:
                Intent intent = new Intent(SelectMenuActivity.this, SelectActionActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_training_result:
                Intent intentResult = new Intent(SelectMenuActivity.this, ResultActivity.class);
                startActivity(intentResult);
                break;
        }
    }
}
