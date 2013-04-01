package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TodoListManagerActivity extends Activity {


	public final static int ADD_REQ = 75269;
	
	private ArrayAdapter<Task> adapter;
	private List<Task> tasks;
	private ListView listTasks;
	private SQLiteDatabase db;
	private Cursor cursor;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		tasks = new ArrayList<Task>();

		listTasks = (ListView)findViewById(R.id.lstTodoItems);

		adapter = new TaskDisplayAdapter(this, tasks);

		//listTasks.setAdapter(adapter); TODO - delete?

		registerForContextMenu(listTasks);
		
		DBHelper helper = new DBHelper(this);
		db = helper.getWritableDatabase();

		cursor = db.query("todo", new String[] { "_id", "title", "due" },
				null, null, null, null, null);
		String[] from = { "title", "due" };
		int[] to = { R.id.txtTodoTitle, R.id.txtTodoDueDate };
		SimpleCursorAdapter dbAdapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);
		listTasks.setAdapter(dbAdapter);

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
        String taskName = adapter.getItem(info.position).taskName;
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
		Task t = adapter.getItem(selectedItemIndex);
		if (t!=null) {
			switch (item.getItemId()){	
			case R.id.menuItemDelete:

				adapter.remove(t);

				break;
			case R.id.menuItemCall:
				//call!!
				String num = t.taskName.substring(("Call ").length());
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
    		adapter.add(new Task(name, d));
    		
    		ContentValues values = new ContentValues();
			values.put("title", name);
			values.put("due", d.getTime());//TODO - change Task's Date to string - don't forget the null option...
			db.insert("todo", null, values);
			cursor.requery();
    	}
    }
	

}
