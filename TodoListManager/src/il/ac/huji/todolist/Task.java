package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
	
	public String taskName;
	public Date dueDate;
	
	public Task(String name, Date d/*TODO - complete!*/) {
		this.taskName = name;
		this.dueDate = d;
		
	}
	
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return taskName + "  " + sdf.format(dueDate);	
	}

}
