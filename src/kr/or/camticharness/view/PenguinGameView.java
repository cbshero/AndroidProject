package kr.or.camticharness.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import kr.or.camticharness.Config;
import kr.or.camticharness.PenguinGameStartActivity;
import kr.or.camticharness.R;
import kr.or.camticharness.vo.HurdleVO;

/**
 * Created by cbshero on 2017-08-21.
 */

public class PenguinGameView extends View {
    private Context m_context;
    private Vibrator m_vibe;
    private Handler m_hanlder;
//    private Bitmap m_bmPenguin;
    private ImageView m_ivPenguin;
    AnimationDrawable m_adPenguin;
    ArrayList<ImageView> m_arrHurdles;
    ArrayList<HurdleVO> m_arrCurrHurdles;
    private TextView m_tvScore;
    private int m_nTimer;
    private int m_nLevel;
    private Random m_random;

    public PenguinGameView(Context context, int nLevel, ImageView ivPenguin, TextView tvScore, ArrayList<ImageView> arrHurdles, Handler handler) {
        super(context);
        m_context = (PenguinGameStartActivity) context;

        //인스턴스변수로 세팅
        m_nLevel = nLevel;
        m_ivPenguin = ivPenguin;
        m_tvScore = tvScore;
        m_arrHurdles = arrHurdles;
        m_hanlder = handler;

        //진동 선언
        m_vibe = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

        m_tvScore.setText("0");

        m_random = new Random();
        m_arrCurrHurdles = new ArrayList<HurdleVO>();

//        m_bmPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.m_3);

    }

    public void startGame() {
        handler1.sendEmptyMessage(0);
    }

    //주인공 표출 및 기타 옵션 세팅 handler
    public Handler handler1 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            //주인공 세팅
            m_adPenguin = (AnimationDrawable)m_ivPenguin.getBackground();
            m_adPenguin.start();
            m_ivPenguin.setX(790);
            m_ivPenguin.setY(800);
            m_ivPenguin.setVisibility(View.VISIBLE);

            m_nTimer = 0;
            handler2.sendEmptyMessageDelayed(0, 1000);
        }
    };

    //장애물 표출 handler
    public Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if(msg.what==0){
                if(m_nTimer<Config.GAME_TOTAL_REPEAT) {
                    //난위도에 따라 Random값 설정
                    int nHurdleRandom = 0;
                    if(m_nLevel==1){
                        nHurdleRandom = m_random.nextInt(3);
                    }else if(m_nLevel==2) {
                        nHurdleRandom = m_random.nextInt(2);
                    }else if(m_nLevel==3) {
                        nHurdleRandom = m_random.nextInt(1);
                    }
                    //난위도에 따라 장애물 출력
                    if(nHurdleRandom==0){
                        HurdleVO hurdleVO = new HurdleVO();
                        int nHurdleType = m_random.nextInt(Config.PENUGUIN_TOTAL_HURDLE);
                        ImageView ivTmp = m_arrHurdles.get(nHurdleType);
                        if(nHurdleType < Config.PENUGUIN_HURDLE_COUNT) {//장애물이 펭귄일 경우만 재생
                            AnimationDrawable ad = (AnimationDrawable) ivTmp.getBackground();
                            ad.start();
                        }
                        hurdleVO.setHurdle(ivTmp);//장애물 10개중에 하나
                        hurdleVO.setXline_num(m_random.nextInt(Config.PENUGUIN_HURDLE_LINE));
                        hurdleVO.setYline_num(0);

                        m_arrCurrHurdles.add(hurdleVO);
                    }
                    //현재 장애물 리스트
                    Log.e("nHurdleRandom", "====>"+m_arrCurrHurdles.size());
                    for(int i=0; i<m_arrCurrHurdles.size(); i++){
                        HurdleVO obj = m_arrCurrHurdles.get(i);
                        if(obj!=null) {
                            ImageView ivTmp = obj.getHurdle();
                            if (obj.getYline_num() >= 4) {//주인공을 넘어가면 삭제
                                ivTmp.setVisibility(View.GONE);
                                m_arrCurrHurdles.remove(i);
                                Log.e("remove", i+"====>"+m_arrCurrHurdles.size());
                            } else {
                                //[Line Position][Depth Position][x,y,width,height]
                                ivTmp.setX(Config.PENGUIN_HURDLE_POSTION[obj.getXline_num()][obj.getYline_num()][0]);
                                ivTmp.setY(Config.PENGUIN_HURDLE_POSTION[obj.getXline_num()][obj.getYline_num()][1]);
                                ivTmp.getLayoutParams().width = Config.PENGUIN_HURDLE_POSTION[obj.getXline_num()][obj.getYline_num()][2];
                                ivTmp.getLayoutParams().height = Config.PENGUIN_HURDLE_POSTION[obj.getXline_num()][obj.getYline_num()][3];
                                ivTmp.setVisibility(View.VISIBLE);
                                ivTmp.requestLayout();
                                obj.setYline_num(obj.getYline_num() + 1);
                            }
                        }else{
                            m_arrCurrHurdles.remove(i);
                            Log.e("remove", i+"====>"+m_arrCurrHurdles.size());
                        }
                        Collections.sort(m_arrCurrHurdles);
                    }
                    m_nTimer++;
                    handler2.sendEmptyMessageDelayed(0, 1000);
                    invalidate();
                }
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        int width = getWidth();
//        int height = getHeight();
//        Log.e("bmPenguin", m_bmPenguin+":"+width+":"+height+":"+canvas);
//        if(canvas!=null)
//            canvas.drawBitmap(m_bmPenguin, width/2, height-500, new Paint());
    }

}