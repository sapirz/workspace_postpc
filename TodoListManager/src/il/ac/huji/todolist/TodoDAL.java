package il.ac.huji.todolist;

import java.util.Date;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class TodoDAL {

	private SQLiteDatabase db;
	private Cursor cursor;


	public TodoDAL(Context context) {
		DBHelper helper = new DBHelper(context);
		db = helper.getWritableDatabase();

		cursor = db.query("todo", new String[] { "_id", "title", "due" },
				null, null, null, null, null);

		Parse.initialize(context, context.getResources().getString(R.string.parseApplication),  
				context.getResources().getString(R.string.clientKey));

	}

	public boolean insert(ITodoItem todoItem) {

		boolean res = true;
		String name = todoItem.getTitle();
		Date d = todoItem.getDueDate();

		ParseObject parseObject = new ParseObject("todo");

		ContentValues values = new ContentValues();

		values.put("title", name);
		parseObject.put("title", name);

		if(d != null){
			values.put("due", d.getTime());
			parseObject.put("due", d.getTime());
		}
		else{
			values.put("due", -1/*null*/);//what TODO - should be null...
			parseObject.put("due", null);
		}
		if (db.insert("todo", null, values) < 0){
			res = false;
		}		
		cursor.requery();
		parseObject.saveInBackground();//TODO - change to save?
		
		return res;
	}

	public boolean update(ITodoItem todoItem) { 
		//TODO
		return false;
	}

	public boolean delete(ITodoItem todoItem) { 
		String taskName = todoItem.getTitle();
		//delete from local DB
		if( db.delete("todo", "title= ?", new String[]{taskName})<=0){
			return false;
		}
		cursor.requery();

		//delete from parse
		ParseQuery query = new ParseQuery("todo");
		query.whereEqualTo("title", taskName);
		try {
			List<ParseObject> objects = query.find();
			for (ParseObject object : objects) {
				object.delete();
			}
		} catch (ParseException e1) {
			return false;
		}
		return true;
	}

	public List<ITodoItem> all() { 
		//TODO
		return null;
	}
	
	public Cursor getDBCursor() {
		return cursor;
		
	}
}


