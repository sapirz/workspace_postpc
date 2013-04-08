package il.ac.huji.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, /*"todo_db"*/"todo_db.db", null, 1);//TODO - change!! .db is for view only!
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table todo ( " +
				"  _id integer primary key autoincrement, " +
				"  title text, " +
				"   due integer);");/*can it be long? TODO*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//nothing to do
	}

}
