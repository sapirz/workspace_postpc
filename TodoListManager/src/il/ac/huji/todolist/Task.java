package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements ITodoItem{
	
	public String taskName;
	public Date dueDate;
	
	public Task(String name, Date d) {
		this.taskName = name;
		this.dueDate = d;
		
	}

	@Override
	public String getTitle() {
		return taskName;
	}

	@Override
	public Date getDueDate() {
		return dueDate;
	}

}
