package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//import android.content.Context;
import android.widget.ArrayAdapter;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class TaskDisplayAdapter extends ArrayAdapter<Task> {

	public TaskDisplayAdapter(TodoListManagerActivity activity, List<Task> tasks) {
		super(activity, android.R.layout.simple_list_item_1, android.R.layout.simple_list_item_1, tasks);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Task t = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row, null);
		TextView task = 	(TextView)view.findViewById(R.id.txtTodoTitle);
		task.setText(t.taskName);

		TextView date = (TextView) view.findViewById(R.id.txtTodoDueDate);

		if (t.dueDate != null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			date.setText(sdf.format(t.dueDate));
			//if date passed, set color to RED
			Date today = new Date();
			today.setHours(0);
			today.setMinutes(0);
			today.setSeconds(0);
			
			if (t.dueDate.compareTo(today)<0){
				date.setTextColor(Color.RED);
				task.setTextColor(Color.RED);	
			}
		}
		else{
			date.setText("No due date");
		}
		return view;
	}

}
