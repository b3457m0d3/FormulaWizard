package com.ndroidstudios.android.formulawizard;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ndroidstudios.android.helper.CustomCalculatorHelper;
import com.ndroidstudios.android.helper.FontHelper;
import com.ndroidstudios.android.helper.UIHelper;

public class CustomCalculator extends SherlockActivity {

	private static int EDITFORMULA_REQ = 1;
	private TextView mNameText;
	private TextView mFormulaText;
	private TextView mInfoText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_calculator);	
		
		mNameText = (TextView)findViewById(R.id.formula_name_heading);
		mFormulaText = (TextView)findViewById(R.id.formula_on_chalkboard);
		mInfoText = (TextView)findViewById(R.id.display_result);
		
		overrideFonts();
		populateViews(this.getIntent());
		createVariableContainers();
		registerButtonListener();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_share, menu);
		inflater.inflate(R.menu.menu_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_edit: 
			Intent intent = new Intent(this, CustomFormulaEdit.class);
			putIntentExtras(intent);
			startActivityForResult(intent, EDITFORMULA_REQ);
			return true;
		case R.id.menu_share:
			return true;
		default: 	
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);	
		if (requestCode == EDITFORMULA_REQ) {
			if (resultCode == MainActivity.RESULT_OK) {	
				populateViews(data);
				createVariableContainers();
			}
		}
	}

	private String getStringIntentExtras(String name) {
		Intent intent = getIntent();
		String value = intent.getStringExtra(name);
		return value;
	}
	
	private long getLongIntentExtras(String name) {
		Intent intent = getIntent();
		long value = intent.getLongExtra("rowID", 0);
		return value;
	}
	
	private String getNameString() {
		return mNameText.getText().toString();
	}
	
	private String getFormulaString() {
		return mFormulaText.getText().toString();
	}
	
	private void putIntentExtras(Intent intent) {
		intent.putExtra("name", getNameString());
		intent.putExtra("formula", getFormulaString());
		intent.putExtra("category", getStringIntentExtras("category"));
		intent.putExtra("rowID", getLongIntentExtras("rowID"));
	}
	
	private void overrideFonts() {
		FontHelper.overrideFonts(this, findViewById(android.R.id.content));
	}
	
	private void populateViews(Intent i) {
		// Override fonts again, for specific content
		Typeface chalkdust = Typeface.createFromAsset(this.getAssets(), "fonts/chalkduster.ttf");
		FontHelper.overrideFonts(this, mFormulaText, chalkdust);
		
		mNameText.setText(i.getStringExtra("name"));
		mFormulaText.setText(i.getStringExtra("formula"));
		
		// Set the default prompt text
		TextView mInfoText = (TextView)findViewById(R.id.display_result);
		UIHelper.setDefaultText(mInfoText);
	}
	
	private void createVariableContainers() {
		ArrayList<String> variableNames = CustomCalculatorHelper.getVariableArray(getFormulaString()); // Holds all variable names
		LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(CustomCalculator.LAYOUT_INFLATER_SERVICE); // Setup the inflater
		LinearLayout variableContainer = (LinearLayout)this.findViewById(R.id.variable_container);	// Major item container
		
		variableContainer.removeAllViews(); // Make sure our container is clean before inflating it
		for (String variable : variableNames) {
			
			// Create a new container item that holds a TextView and an EditText. Then add it to main container
			LinearLayout variableContainerItem = (LinearLayout) layoutInflater.inflate(R.layout.variable_container_item, null);
			variableContainer.addView(variableContainerItem);
			
			// Set text for item container
			TextView itemText = (TextView)variableContainerItem.findViewById(R.id.variable_text);
			EditText itemEdit = (EditText)variableContainerItem.findViewById(R.id.variable_edit);
			itemEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER); // Restrict input type to numbers only
			FontHelper.overrideFonts(this, itemText);
			FontHelper.overrideFonts(this, itemEdit);
			itemText.setText(variable + ":");
		}
	}
	
	private void registerButtonListener() {
		Button calculateButton = (Button)this.findViewById(R.id.calculate_button);
    	calculateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleInput();
			}
		});
    }
	
	private void handleInput() {
		if (UIHelper.isEmpty(CustomCalculatorHelper.getEditTextList(this))) {
			UIHelper.setErrorText(mInfoText);
		} else {
			double result = CustomCalculatorHelper.calculateResult(getFormulaString());
			mInfoText.setText("Result = " + result);
		}
	}
}


