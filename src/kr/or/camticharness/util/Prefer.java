package kr.or.camticharness.util;

import android.content.Context;
import android.content.SharedPreferences;

import kr.or.camticharness.vo.Users;

public class Prefer {
    private Context mContext;

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    public Prefer(Context context) {
        mContext = context;
        pref = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    //Setter
    public void set_bt_address(String bt_address) {
        editor.putString("bt_address", bt_address);
        editor.commit();
    }

    public void setCurrUserSid(int user_sid) {
        editor.putInt("curr_user_sid", user_sid);
        editor.commit();
    }

    //Getter
    public int getCurrUserSid() {
        return pref.getInt("curr_user_sid", 0);
    }

    public String get_bt_address() {
        return pref.getString("bt_address", "");
    }

}
