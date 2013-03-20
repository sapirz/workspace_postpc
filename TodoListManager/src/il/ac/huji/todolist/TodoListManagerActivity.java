package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		tasks = new ArrayList<Task>();

		listTasks = (ListView)findViewById(R.id.lstTodoItems);

		adapter = new TaskDisplayAdapter(this, tasks);

		listTasks.setAdapter(adapter);

		registerForContextMenu(listTasks);

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
        
        menu.setHeaderTitle(adapter.getItem(info.position).toString());//TODO - complete
		getMenuInflater().inflate(R.menu.contextmenu, menu);//TODO????
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int selectedItemIndex = info.position;
		switch (item.getItemId()){	
		case R.id.menuItemDelete:
			//adapter.remove();
			Task t = adapter.getItem(selectedItemIndex);
			if (t!=null) {
				adapter.remove(t);
			}
			break;
		case R.id.menuItemCall:
			//TODO - call!!
			///
			///
			break;
		}

		return true;
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemAdd:
			Intent intent = new Intent(this, AddNewTodoItemActivity.class);
			startActivityForResult(intent, ADD_REQ);

			
			
			//EditText newTask = (EditText) findViewById(R.id.edtNewItem);
			String newTask = "temp";
			//if (newTask.getText().length() != 0){
			adapter.add(new Task(newTask/*.getText().toString()*/, new Date()));//get task name from edtNewItem	
			//}
			break;
		}
		return true;
	}


	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == ADD_REQ && resultCode == RESULT_OK) {
    		String name = data.getStringExtra("taskName");//will return OK only is the string is not empty!
    		Date d = (Date)data.getSerializableExtra("dueDate");
    		adapter.add(new Task(name, d));
    	}
    }
	

}
