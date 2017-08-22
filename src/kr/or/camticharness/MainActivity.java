package kr.or.camticharness;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends Activity implements View.OnClickListener {
	Vibrator m_vibe;
	private Context m_context;

	private ImageButton ib_user_reg;
	private ImageButton ib_user_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//진동 선언
		m_vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

		m_context = this;

		findView();

		init();
	}

	private void findView(){
		ib_user_reg = (ImageButton)findViewById(R.id.user_reg);
		ib_user_reg.setOnClickListener(this);
		ib_user_list = (ImageButton)findViewById(R.id.user_list);
		ib_user_list.setOnClickListener(this);
	}

	private void init() {
	}

	@Override
	public void onClick(View view) {
		m_vibe.vibrate(50);
		switch(view.getId()) {
			case R.id.user_list:
				UserListDialog.Builder userListDialogBuilder = new UserListDialog.Builder(m_context);
				userListDialogBuilder.setView();
				UserListDialog userListDialog = userListDialogBuilder.create();
				userListDialog.setCanceledOnTouchOutside(false);
				userListDialog.show();

				break;
			case R.id.user_reg:
				UserRegDialog.Builder builder = new UserRegDialog.Builder(m_context);
				builder.setView();
				UserRegDialog userRegDialog = builder.create();
				userRegDialog.setCanceledOnTouchOutside(false);
				userRegDialog.show();

				break;
		}
	}
}
