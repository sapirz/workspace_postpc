package il.ac.huji.todolist;

import java.util.List;

//import android.content.Context;
import android.widget.ArrayAdapter;
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
		TextView rowView = (position%2 == 1)?
				(TextView)view.findViewById(R.id.evenRow):
				(TextView)view.findViewById(R.id.odRow);
		rowView.setText(t.taskName);
		return view;
	}

}
