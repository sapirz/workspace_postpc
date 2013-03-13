package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {


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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemAdd:
			EditText newTask = (EditText) findViewById(R.id.edtNewItem);
			if (newTask.getText().length() != 0){
				adapter.add(new Task(newTask.getText().toString()));//get task name from edtNewItem	
			}
			break;
		case R.id.menuItemDelete:
			//get selected item and remove it from the list
			Task t = (Task) listTasks.getSelectedItem();
			if (t!=null) {
				adapter.remove(t);
			}
			break;
		}
		return true;
	}

}
