package il.ac.huji.todolist;

import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
	private Cursor cursor;

	private TodoDAL td;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		listTasks = (ListView)findViewById(R.id.lstTodoItems); 

		registerForContextMenu(listTasks);
		
		td = new TodoDAL(this);
		
		cursor  = td.getDBCursor();
		
		String[] from = { "title", "due" };
		int[] to = {R.id.txtTodoTitle, R.id.txtTodoDueDate };
		dbAdapter = new TaskDisplayDBAdapter(this, R.layout.row, cursor, from, to);
		
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
			Date d = null;
			if(c.getLong(2) >= 0)
				d = new Date(c.getLong(2));
			ITodoItem task = new Task(taskName,d);
			
			switch (item.getItemId()){	
			case R.id.menuItemDelete:
				td.delete(task);
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
    		ITodoItem todoItem = new Task(name, d);
    		td.insert(todoItem);
    	}
    }
	

}
