package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.List;

//import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoListManagerActivity extends Activity {

	
    private ArrayAdapter<Task> adapter;
    private List<Task> tasks;//TODO - should it be here?
    private ListView listTasks;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_todo_list_manager);
    	//TODO
    	tasks = new ArrayList<Task>();
    	
    	listTasks = (ListView)findViewById(R.id.lstTodoItems);
    	
    	adapter = new ArrayAdapter<Task>(
    			this,
    			android.R.layout.simple_list_item_1,//TODO - change format
    			tasks
    			);
    			//new CourseDisplayAdapter(this, courses);
    	listTasks.setAdapter(adapter);
    	////
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
    		//TODO - get selected item and remove it from the list
    		/*listTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {

    	        public void onItemClick(AdapterView<?> parent, View view,
    	                int position, long id) {
    	            //use POSITION to get item clicked
    	        	adapter.remove(tasks.get(position));
    	        }
    	    });*/
    		Task t = (Task) listTasks.getSelectedItem();
    		if (t!=null) {
    			adapter.remove(t);
			}
    		break;
    	}
    	return true;
    }

}
