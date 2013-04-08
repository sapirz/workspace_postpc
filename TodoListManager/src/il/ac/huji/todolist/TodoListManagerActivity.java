package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.R.string;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleCursorAdapter;



public class TodoListManagerActivity extends Activity {


	public final static int ADD_REQ = 75269;
	
	
	private SimpleCursorAdapter dbAdapter;
	private ListView listTasks;
	private SQLiteDatabase db;
	private Cursor cursor;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		listTasks = (ListView)findViewById(R.id.lstTodoItems); 

		registerForContextMenu(listTasks);
		
		DBHelper helper = new DBHelper(this);
		db = helper.getWritableDatabase();

		cursor = db.query("todo", new String[] { "_id", "title", "due" },
				null, null, null, null, null);
		String[] from = { "title", "due" };
		int[] to = { R.id.txtTodoTitle, R.id.txtTodoDueDate };
		dbAdapter = new TaskDisplayDBAdapter(this, R.layout.row, cursor, from, to);
		listTasks.setAdapter(dbAdapter);
		
		
		Parse.initialize(this, /*R.string.parseApplication*/"7oGrGTLAmr3XO48HD7WRzNtDaypu1xtSqRSJIZLs",  
				/*R.string.clientKey*/"zTmXla9Ba045weVI48FmUhGVYPKcekiSBFKbhWOq");//TODO - make it work!
		//TODO - the data will be retrieved from the local db.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {		
		super.onCreateContextMenu(menu, v, menuInfo);

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		Cursor c = (Cursor) dbAdapter.getItem(info.position);
		String taskName = c.getString(1);
		
		menu.setHeaderTitle(taskName);
		getMenuInflater().inflate(R.menu.contextmenu, menu);
		if(!taskName.startsWith("Call ")){
			menu.removeItem(R.id.menuItemCall);
		}
		else{
			//if "Call ---" set text to taskName.	
			MenuItem call = menu.findItem(R.id.menuItemCall);
			call.setTitle(taskName);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int selectedItemIndex = info.position;
		Cursor c = (Cursor) dbAdapter.getItem(selectedItemIndex);
		if (c!=null && c.getString(1) != null) {
			String taskName = c.getString(1);
			switch (item.getItemId()){	
			case R.id.menuItemDelete:
				//delete from local DB
				db.delete("todo", "title= ?", new String[]{taskName});
				cursor.requery();
			
				//delete from parse
				ParseQuery query = new ParseQuery("todo");
				query.whereEqualTo("title", taskName);
				query.findInBackground(new FindCallback() {
					@Override
					public void done(List<ParseObject> objects, ParseException exc) {
						if (exc != null) {
							//TODO - hnadle this
							exc.printStackTrace();
						} else {
							for (ParseObject object : objects) {
								try {
									object.delete();
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				});
				break;
			case R.id.menuItemCall:
				//call!!
				String num = taskName.substring(("Call ").length());
				Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+num));
				startActivity(dial);
				break;
			}
		}
		return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemAdd:
			Intent intent = new Intent(this, AddNewTodoItemActivity.class);
			startActivityForResult(intent, ADD_REQ);
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == ADD_REQ && resultCode == RESULT_OK) {
    		
    		String name = data.getStringExtra("title");//will return OK only is the string is not empty!
    		Date d = (Date)data.getSerializableExtra("dueDate");//may be null
    		
    		ParseObject parseObject = new ParseObject("todo");
    		
    		ContentValues values = new ContentValues();
			
    		values.put("title", name);
    		parseObject.put("title", name);
			
			if(d != null){
				values.put("due", d.getTime());//TODO - change Task's Date to string - don't forget the null option...
				parseObject.put("due", d.getTime());
			}
			else{
				values.put("due", -1);//what TODO???
				parseObject.put("due", -1);
			}
			db.insert("todo", null, values);
			cursor.requery();
			parseObject.saveInBackground();
    	}
    }
	

}
