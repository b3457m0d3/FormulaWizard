package com.ndroidstudios.android.formulawizard;

import com.ndroidstudios.android.helper.Formulas;
import com.ndroidstudios.android.helper.FontHelper;
import com.ndroidstudios.android.helper.UIHelper;
import com.ndroidstudios.android.helper.Formulas.InvalidInputException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EllipsoidVolumeActivity extends Activity {

	// Private instance variables
	private EditText mVariableA;
	private EditText mVariableB;
	private EditText mVariableC;
	private Button mCalculateButton;
	private TextView mInfoText;
	private double result;
	
	// Helper instance variables
	private UIHelper uiHelper = new UIHelper();
	private Formulas calculator = new Formulas();
	
	/** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ellipsoid_volume);
                
        mVariableA = (EditText)findViewById(R.id.variable_radius1);
        mVariableB = (EditText)findViewById(R.id.variable_radius2);
        mVariableC = (EditText)findViewById(R.id.variable_radius3);
        mCalculateButton = (Button)findViewById(R.id.calculate_button);
        mInfoText = (TextView)findViewById(R.id.display_x1);
        
        registerButtonListener();
        uiHelper.setDefaultText(mInfoText);
        FontHelper.overrideFonts(this, findViewById(android.R.id.content));
    }

    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    private void registerButtonListener() {	
    	mCalculateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleInput();
			}
		});
    }
    
    private void handleInput() {
    	try {
    		if (uiHelper.editTextIsEmpty(mVariableA, mVariableB, mVariableC)) {
				uiHelper.setErrorText(mInfoText);
			} else {
				double radius1 = Double.parseDouble(mVariableA.getText().toString());
				double radius2 = Double.parseDouble(mVariableB.getText().toString());
				double radius3 = Double.parseDouble(mVariableC.getText().toString());
				result = calculator.ellipsoidVolume(radius1, radius2, radius3);
				mInfoText.setText("Volume = " + result);				  
			}
    	} catch (InvalidInputException e) {
    		mInfoText.setText("The radius can't be negative! Enter a positive value!");
    	}
    }
}


