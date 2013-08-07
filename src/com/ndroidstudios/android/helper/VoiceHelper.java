package com.ndroidstudios.android.helper;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;

public class VoiceHelper {
	
	private Context context;
	
	public VoiceHelper(Context context) {
		this.context = context;
	}
	
	public boolean isVoiceCapable() {
    	boolean voiceCapability = false;
    	
    	PackageManager pm = this.context.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
        	voiceCapability = true;
        }
        //return voiceCapability;
        
        //FOR TESTING PURPOSES ONLY
        return true;
    }
}
