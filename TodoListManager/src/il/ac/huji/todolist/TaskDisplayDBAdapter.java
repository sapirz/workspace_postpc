package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

//import android.content.Context;
import android.widget.SimpleCursorAdapter;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class TaskDisplayDBAdapter extends SimpleCursorAdapter {

	public TaskDisplayDBAdapter(TodoListManagerActivity activity, int layout, Cursor cursor, String[] from, int[] to) {
		super(activity,layout,cursor,from,to);
	}


	private View changeView(Cursor c, View v){
		String taskName = c.getString(1);
		
		int index = c.getColumnIndexOrThrow("due");
		Long d = null;
		if (!c.isNull(index))
			d = c.getLong(index);
		
		Date date = (d != null)?new Date(d):null;	         

		TextView task = 	(TextView) v.findViewById(R.id.txtTodoTitle);
		task.setText(taskName);

		TextView dueDate = (TextView) v.findViewById(R.id.txtTodoDueDate);

		if (date != null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dueDate.setText(sdf.format(date));
			//if date passed, set color to RED
			Date today = new Date();
			today.setHours(0);
			today.setMinutes(0);
			today.setSeconds(0);

			if (date.compareTo(today)<0){
				dueDate.setTextColor(Color.RED);
				task.setTextColor(Color.RED);	
			}else{
				dueDate.setTextColor(Color.BLACK);
				task.setTextColor(Color.BLACK);
			}
		}
		else{
			dueDate.setText("No due date");
			dueDate.setTextColor(Color.BLACK);
			task.setTextColor(Color.BLACK);
		}

		return v;

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		Cursor c = getCursor();

		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.row, null);

		return changeView(c,v);
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		changeView(c,v);
	}
}