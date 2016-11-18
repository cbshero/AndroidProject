package kr.or.camticharness.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
	public DBHelper(Context context, int version) {
		super(context, "kr.or.camtic.harness.db", null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    StringBuilder strbUsersCreate = new StringBuilder(1024);
	    strbUsersCreate.append("CREATE TABLE IF NOT EXISTS users(");
	    strbUsersCreate.append("sid INTEGER PRIMARY KEY AUTOINCREMENT, ");
	    strbUsersCreate.append("name TEXT, ");
	    strbUsersCreate.append("age INTEGER, ");
	    strbUsersCreate.append("sex INTEGER, ");
		strbUsersCreate.append("weight INTEGER, ");
	    strbUsersCreate.append("disease TEXT, ");
		strbUsersCreate.append("regdate DATETIME DEFAULT (datetime('now','localtime'))");
	    strbUsersCreate.append(");");
	    db.execSQL(strbUsersCreate.toString());
	    
	    StringBuilder strbStaticsCreate = new StringBuilder(1024);
	    strbStaticsCreate.append("CREATE TABLE IF NOT EXISTS statics(");
	    strbStaticsCreate.append("sid INTEGER PRIMARY KEY AUTOINCREMENT, ");
		strbStaticsCreate.append("user_sid INTEGER, ");
	    strbStaticsCreate.append("practice_type INTEGER, ");
	    strbStaticsCreate.append("forward_backward REAL, ");
		strbStaticsCreate.append("left_right REAL, ");
	    strbStaticsCreate.append("speed INTEGER, ");
		strbStaticsCreate.append("weight_rate REAL, ");
		strbStaticsCreate.append("practice_time INTEGER, ");
		strbStaticsCreate.append("regdate DATETIME DEFAULT (datetime('now','localtime'))");
	    strbStaticsCreate.append(");");
	    db.execSQL(strbStaticsCreate.toString());
	    
/*	    
	    ContentValues contentValues3 = new ContentValues();
	    contentValues3.put("sid", Config.ALARM_UNIQUE_CODE3);
	    contentValues3.put("alarmTime", "17:00");
	    contentValues3.put("sound", "");
	    contentValues3.put("onOff", Integer.valueOf(1));
	    db.insert("AlarmTime", null, contentValues3);
*/	    
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS users");
		db.execSQL("DROP TABLE IF EXISTS statics");
		onCreate(db);
	}
}
