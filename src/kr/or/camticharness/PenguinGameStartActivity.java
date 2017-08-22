package kr.or.camticharness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.net.ssl.SSLContextSpi;

import kr.or.camticharness.view.PenguinGameView;

public class PenguinGameStartActivity extends Activity {
    private Vibrator m_vibe;
    private int m_nLevel;
    private RelativeLayout m_rlPenguin;
    private PenguinGameView m_penguinGameView;
    private Context mContext;
    AnimationDrawable m_drawable;
    private ImageView m_ivPenguin;
    private TextView m_tvScore;

    private ImageView m_ivEnemy1;
    private ImageView m_ivEnemy2;
    private ImageView m_ivEnemy3;
    private ImageView m_ivEnemy4;
    private ImageView m_ivEnemy5;
    private ImageView m_ivEnemy6;
    private ImageView m_ivHurdle1;
    private ImageView m_ivHurdle2;
    private ImageView m_ivHurdle3;
    private ImageView m_ivHurdle4;

    private ImageView m_ivThree;
    private ImageView m_ivTwo;
    private ImageView m_ivOne;


    private ArrayList<ImageView> m_arrHuddles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penguin_game_start);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mContext = this;
        //진동 선언
        m_vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        Intent intent = getIntent();
        m_nLevel = intent.getIntExtra("level", 1);

        m_arrHuddles = new ArrayList<ImageView>();

        findView();
    }

    private void findView(){
        m_rlPenguin = (RelativeLayout) findViewById(R.id.rl_penguin);

        m_ivPenguin = (ImageView)findViewById(R.id.iv_penguin);
        m_tvScore = (TextView)findViewById(R.id.tv_score);

        m_ivEnemy1 = (ImageView)findViewById(R.id.iv_enemy1);
        m_ivEnemy2 = (ImageView)findViewById(R.id.iv_enemy2);
        m_ivEnemy3 = (ImageView)findViewById(R.id.iv_enemy3);
        m_ivEnemy4 = (ImageView)findViewById(R.id.iv_enemy4);
        m_ivEnemy5 = (ImageView)findViewById(R.id.iv_enemy5);
        m_ivEnemy6 = (ImageView)findViewById(R.id.iv_enemy6);
        m_ivHurdle1 = (ImageView)findViewById(R.id.iv_hurdle1);
        m_ivHurdle2 = (ImageView)findViewById(R.id.iv_hurdle2);
        m_ivHurdle3 = (ImageView)findViewById(R.id.iv_hurdle3);
        m_ivHurdle4 = (ImageView)findViewById(R.id.iv_hurdle4);
        m_ivThree = (ImageView)findViewById(R.id.iv_three);
        m_ivTwo = (ImageView)findViewById(R.id.iv_two);
        m_ivOne = (ImageView)findViewById(R.id.iv_one);

        m_arrHuddles.add(m_ivEnemy1);
        m_arrHuddles.add(m_ivEnemy2);
        m_arrHuddles.add(m_ivEnemy3);
        m_arrHuddles.add(m_ivEnemy4);
        m_arrHuddles.add(m_ivEnemy5);
        m_arrHuddles.add(m_ivEnemy6);
        m_arrHuddles.add(m_ivHurdle1);
        m_arrHuddles.add(m_ivHurdle2);
        m_arrHuddles.add(m_ivHurdle3);
        m_arrHuddles.add(m_ivHurdle4);

        m_penguinGameView = new PenguinGameView(mContext, m_nLevel, m_ivPenguin, m_tvScore, m_arrHuddles, mHandler);
        m_rlPenguin.addView(m_penguinGameView);

        m_drawable = (AnimationDrawable) m_rlPenguin.getBackground();

        mHandlerCount.sendEmptyMessage(3);

    }

    private Handler mHandlerCount = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(msg.what==3){
                m_ivThree.setVisibility(View.VISIBLE);
                m_ivTwo.setVisibility(View.GONE);
                m_ivOne.setVisibility(View.GONE);
                mHandlerCount.sendEmptyMessageDelayed(2, 1000);
            }else if(msg.what==2){
                m_ivThree.setVisibility(View.GONE);
                m_ivTwo.setVisibility(View.VISIBLE);
                m_ivOne.setVisibility(View.GONE);
                mHandlerCount.sendEmptyMessageDelayed(1, 1000);
            }else if(msg.what==1){
                m_ivThree.setVisibility(View.GONE);
                m_ivTwo.setVisibility(View.GONE);
                m_ivOne.setVisibility(View.VISIBLE);
                mHandlerCount.sendEmptyMessageDelayed(0, 1000);
            }else if(msg.what==0){
                m_ivThree.setVisibility(View.GONE);
                m_ivTwo.setVisibility(View.GONE);
                m_ivOne.setVisibility(View.GONE);
                m_penguinGameView.startGame();

                init();
            }
        }
    };

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //result handler 게임 완료 후
        }
    };

    private void init(){
        m_drawable.start();
    }

}
