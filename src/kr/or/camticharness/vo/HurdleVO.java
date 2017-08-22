package kr.or.camticharness.vo;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * Created by cbshero on 2017-08-22.
 */

public class HurdleVO implements Comparable<HurdleVO> {
    private ImageView hurdle;
    private int xline_num;
    private int yline_num;



    public int getXline_num() {
        return xline_num;
    }

    public void setXline_num(int xline_num) {
        this.xline_num = xline_num;
    }

    public ImageView getHurdle() {
        return hurdle;
    }

    public void setHurdle(ImageView hurdle) {
        this.hurdle = hurdle;
    }

    public int getYline_num() {
        return yline_num;
    }

    public void setYline_num(int yline_num) {
        this.yline_num = yline_num;
    }

    @Override
    public int compareTo(@NonNull HurdleVO hurdleVO) {
        int yline_num = ((HurdleVO)hurdleVO).getYline_num();
        /* For Ascending order*/
        return this.yline_num - yline_num;
    }
}
