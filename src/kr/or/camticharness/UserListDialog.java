package kr.or.camticharness;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AbsListView.OnScrollListener;

import java.util.ArrayList;

import kr.or.camticharness.util.DBHelper;
import kr.or.camticharness.util.Prefer;
import kr.or.camticharness.vo.Users;

public class UserListDialog extends Dialog {

    public UserListDialog(Context context) {super(context);}
    public UserListDialog(Context context, int theme) {
        super(context, theme);
    }
    protected UserListDialog(Context context, boolean cancelable,OnCancelListener cancelListener) {super(context, cancelable, cancelListener);}

    public static class Builder {
        Vibrator m_vibe;
        private View m_layout;
        private ListView lv_user_list;
        private Context m_context;
        private ArrayList<Users> m_user_list;
        private MyCustomAdapter dataAdapter;
        private int m_user_sid;

        public Builder(Context context) {
            //진동 선언
            m_vibe = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

            m_context = context;

            m_user_list = new ArrayList<Users>();
        }

        public Builder setView() {
            LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            m_layout = inflater.inflate(R.layout.dialog_user_list, null);
            return this;
        }

        public UserListDialog create() {
            final UserListDialog dialog = new UserListDialog(m_context, R.style.Dialog);

            lv_user_list = (ListView) m_layout.findViewById(R.id.user_list);
//            lv_user_list.setOnScrollListener(this);
//            lv_user_list.setSelection(0);

            addItems();

            dialog.setContentView(m_layout);
            return dialog;
        }

        public void addItems() {
            ArrayList<Users> arr = getUserList();
            if (arr != null)
                m_user_list.addAll(arr);

            dataAdapter = new MyCustomAdapter(m_context, R.layout.view_user_list, m_user_list);
            if (arr.size() > 0) {
                // 스크롤 리스너를 등록합니다. onScroll에 추가구현을 해줍니다.
                lv_user_list.setAdapter(dataAdapter);
                dataAdapter.notifyDataSetChanged();
            } else {
                dataAdapter.notifyDataSetChanged();
            }
        }

        private ArrayList<Users> getUserList() {
            ArrayList<Users> arr = new ArrayList<Users>();
            DBHelper dbHelper = new DBHelper(m_context, Config.VERSION_NUM);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor1 = db.rawQuery("select sid, name, age, sex, weight, disease, regdate from users order by regdate desc", null);
            if (cursor1.moveToFirst()) {
                do {
                    Users users = new Users();
                    users.setSid(cursor1.getInt(0));
                    users.setName(cursor1.getString(1));
                    users.setAge(cursor1.getInt(2));
                    users.setSex(cursor1.getInt(3));
                    users.setWeight(cursor1.getInt(4));
                    users.setDisease(cursor1.getString(5));
                    users.setRegdate(cursor1.getString(6));

                    arr.add(users);
                    Log.e("cursor", users.getRegdate());
                } while (cursor1.moveToNext());
            }
            if (db.isOpen())
                db.close();
            dbHelper.close();

            return arr;
        }

        private class MyCustomAdapter extends ArrayAdapter<Users> {
            private ArrayList<Users> m_arrList;

            public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Users> arrList) {
                super(context, textViewResourceId, arrList);
                m_arrList = arrList;
            }

            private class ViewHolder {
                TextView tvName;
                TextView tvAge;
                TextView tvSex;
                TextView tvWeight;
                TextView tvDisease;
                TextView tvRegdate;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View rowView = convertView;

                //			  if(m_arrList.size()>0){
                if (rowView == null) {
                    LayoutInflater vi = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    rowView = vi.inflate(R.layout.view_user_list, null);
                }

                Users users = m_arrList.get(position);
                if (users != null) {
                    ViewHolder viewHolder = new ViewHolder();
                    viewHolder.tvName = (TextView) rowView.findViewById(R.id.name);
                    viewHolder.tvAge = (TextView) rowView.findViewById(R.id.age);
                    viewHolder.tvSex = (TextView) rowView.findViewById(R.id.sex);
                    viewHolder.tvWeight = (TextView) rowView.findViewById(R.id.weight);
                    viewHolder.tvDisease = (TextView) rowView.findViewById(R.id.disease);
                    viewHolder.tvRegdate = (TextView) rowView.findViewById(R.id.regdate);

                    viewHolder.tvName.setText(users.getName());
                    viewHolder.tvAge.setText(String.valueOf(users.getAge()));
                    viewHolder.tvSex.setText(Config.SEX[users.getSex()]);
                    viewHolder.tvWeight.setText(String.valueOf(users.getWeight()));
                    viewHolder.tvDisease.setText(String.valueOf(users.getDisease()));
                    viewHolder.tvRegdate.setText(users.getRegdate());

                    rowView.setTag(viewHolder);

                    LinearLayout ll_user = (LinearLayout) rowView.findViewById(R.id.user_layout);
                    ll_user.setTag(users);
                    ll_user.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v_tmp) {
                            m_vibe.vibrate(50);
                            Users users = (Users) v_tmp.getTag();
                            m_user_sid = users.getSid();

                            Prefer prefer = new Prefer(m_context);
                            prefer.setCurrUserSid(m_user_sid);

                            Intent intent = new Intent(m_context, SelectMenuActivity.class);
                            m_context.startActivity(intent);
                        }
                    });
                }
                //			  }
                return (View) rowView;
            }
        }
    }
}
