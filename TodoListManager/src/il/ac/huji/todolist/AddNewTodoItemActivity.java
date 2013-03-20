package il.ac.huji.todolist;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_todo_item);
		
		findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText edtTaskName = (EditText)findViewById(R.id.edtNewItem);
				String taskName = edtTaskName.getText().toString();
				if (taskName == null || "".equals(taskName)) {
					setResult(RESULT_CANCELED);
					finish();
				} else {
					//TODO get date
					Date dueDate = new Date();
					Intent resultIntent = new Intent();
					resultIntent.putExtra("taskName", taskName);
					resultIntent.putExtra("dueDate", dueDate);
					setResult(RESULT_OK, resultIntent);
					finish();
				}
			}
		});
	}
}
