package kr.or.camticharness;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.LayoutInflater;

import kr.or.camticharness.util.DBHelper;

public class UserRegDialog extends Dialog{

    public UserRegDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public UserRegDialog(Context context, int theme) {
        super(context, theme);
    }

    protected UserRegDialog(Context context, boolean cancelable,OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        Vibrator m_vibe;
        private Context m_context;
        private View m_layout;

        private Button bt_user_reg;
        private EditText et_user_name;
        private EditText et_user_age;
        private EditText et_user_disease;
        private EditText et_user_weight;
        private Spinner spn_user_sex;

        public Builder(Context context) {
            //진동 선언
            m_vibe = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

            m_context = context;
        }

        public Builder setView() {
            LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            m_layout = inflater.inflate(R.layout.dialog_user_reg, null);
            return this;
        }

        public UserRegDialog create() {
            final UserRegDialog dialog = new UserRegDialog(m_context, R.style.Dialog);

            bt_user_reg = (Button)m_layout.findViewById(R.id.user_reg);
            et_user_name = (EditText)m_layout.findViewById(R.id.user_name);
            et_user_name.requestFocus();
            et_user_age = (EditText)m_layout.findViewById(R.id.user_age);
            et_user_disease = (EditText)m_layout.findViewById(R.id.user_disease);
            et_user_weight = (EditText)m_layout.findViewById(R.id.user_weight);

            spn_user_sex = (Spinner)m_layout.findViewById(R.id.user_sex);
//            spn_user_sex.setPrompt(m_context.getResources().getString(R.string.sex_title));
            ArrayAdapter adapter = ArrayAdapter.createFromResource(m_context, R.array.sex_type, R.layout.textview_spinner);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_user_sex.setAdapter(adapter);

            bt_user_reg.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v_tmp) {
                    m_vibe.vibrate(50);

                    String strSex = spn_user_sex.getSelectedItem().toString();
                    String strName = et_user_name.getText().toString();
                    String strAge = et_user_age.getText().toString();
                    String strDisease = et_user_disease.getText().toString();
                    String strWeight = et_user_weight.getText().toString();

                    boolean bl = true;
                    if(strName==null || strName.equals("")){
                        Toast.makeText(m_context, "이름을 입력해주세요", Toast.LENGTH_LONG).show();
                        bl = false;
                    }else if(strAge==null || strAge.equals("")){
                        Toast.makeText(m_context, "나이를 입력해주세요", Toast.LENGTH_LONG).show();
                        bl = false;
                    }else if(strWeight==null || strWeight.equals("")){
                        Toast.makeText(m_context, "몸무게를 입력해주세요", Toast.LENGTH_LONG).show();
                        bl = false;
                    }

                    if(bl){
                        int nSex = 0;
                        if(strSex.contains("남")){
                            nSex = 0;
                        }else{
                            nSex = 1;
                        }
                        boolean bl_result = regUserInfo(strName, (int)Double.parseDouble(strAge), nSex, strDisease, Integer.parseInt(strWeight));
                        if(bl_result)
                            Toast.makeText(m_context, "사용자가 등록되었습니다.", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(m_context, "등록되지 못했습니다.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(m_context, MainActivity.class);
                        m_context.startActivity(intent);
                        dialog.cancel();
                    }
                }
            });

            dialog.setContentView(m_layout);
            return dialog;
        }

        private boolean regUserInfo(String strName, int nAge, int nSex, String strDisease, int nWeight){
            boolean bl = false;
            DBHelper dbHelper = new DBHelper(m_context, Config.VERSION_NUM);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("name", strName);
            contentValues.put("age", nAge);
            contentValues.put("sex", nSex);
            contentValues.put("weight", nWeight);
            contentValues.put("disease", strDisease);

            db.insert("users", null, contentValues);

            bl = true;

            if(db.isOpen())
                db.close();
            dbHelper.close();

            return bl;
        }
    }
}
