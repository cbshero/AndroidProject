package kr.or.camticharness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class PenguinGameStartActivity extends AppCompatActivity {
    private int m_nDifficulty;
    private ImageView m_ivBG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penguin_game_start);

        Intent intent = getIntent();
        m_nDifficulty = intent.getIntExtra("diffciulty", 1);

        findView();

        init();

    }

    private void findView(){
        m_ivBG = (ImageView)findViewById(R.id.bt_middle);

    }

    private void init(){

    }

}
