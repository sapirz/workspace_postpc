package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class TodoDAL {

	private SQLiteDatabase db;
	
	public TodoDAL(Context context) {
		DBHelper helper = new DBHelper(context);
		db = helper.getWritableDatabase();

		Parse.initialize(context, context.getResources().getString(R.string.parseApplication),  
				context.getResources().getString(R.string.clientKey));
		ParseUser.enableAutomaticUser();

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
			values.putNull("due");
		}
		if (db.insert("todo", null, values) < 0){
			res = false;
		}		
		try {
			parseObject.save();
		} catch (ParseException e) {
			res = false;
		}
		return res;
	}

	public boolean update(ITodoItem todoItem) { 
		boolean res = true;
		String name = todoItem.getTitle();
		Date d = todoItem.getDueDate();

		//update parse
		ParseQuery query = new ParseQuery("todo");
		query.whereEqualTo("title", name);
		try {
			List<ParseObject> objects = query.find();
			for (ParseObject object : objects) {
				if(d!=null){
					object.put("due", d.getTime());
				}
				else{
					object.remove("due");
				}
				object.save();
			}
		} catch (ParseException e1) {
			return false;
		}

		ContentValues values = new ContentValues();
		values.put("title", name);
		if(d != null){
			values.put("due", d.getTime());
		}
		else{
			values.putNull("due");
		}
		if (db.update("todo", values, "title= ?", new String[]{name}) < 1){
			res = false;
		}		
		return res;
	}

	public boolean delete(ITodoItem todoItem) { 
		String taskName = todoItem.getTitle();
		//delete from local DB
		if( db.delete("todo", "title= ?", new String[]{taskName})<=0){
			return false;
		}
		
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
		ParseQuery query = new ParseQuery("todo");
		List<ITodoItem> items = new ArrayList<ITodoItem>();
		try {
			List<ParseObject> objects = query.find();
			for (ParseObject object : objects) {
				items.add(new Task(object.getString("title"), new Date(object.getLong("due"))));
			}
		} catch (ParseException e1) {
			return null;
		}
		return items;
	}
	
	public SQLiteDatabase getDB() {
		return db;
		
	}
}


