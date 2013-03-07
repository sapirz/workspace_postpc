package il.ac.huji.tipcalculator;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class TipCalculatorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calculator);

		final Button btn = (Button)findViewById(R.id.btnCalculate);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				double tip =0;
				
				//get Bill amount of dollars in bill amount
				EditText billAmount = (EditText) findViewById(R.id.edtBillAmount);
				if (billAmount.getText().length() != 0){//bill amount is not empty
					double amount = Double.parseDouble(billAmount.getText().toString());
					//calculate the tip and update text.
					tip = 0.12*amount;
				}
				
				//round if check box is checked, and update tip field accordinglly 
				CheckBox checkBox = (CheckBox) findViewById(R.id.chkRound);
				TextView tipView = (TextView) findViewById(R.id.txtTipResult);
				if (checkBox.isChecked()) {
					//round!
					tipView.setText("Tip: $"+Math.round(tip));
				}
				else{
					DecimalFormat df=new DecimalFormat("0.00");
					//tipView.setText("Tip: $"+Double.toString(Math.round( tip * 100.0 ) / 100.0)); TODO - delete!
					tipView.setText("Tip: $"+df.format(tip));//TODO - should the this field contain only the number?
				}

			}
		});
	}
}
