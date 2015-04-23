package com.example.krystian892.truss;

import com.example.truss2.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Prefs extends PreferenceActivity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		extracted();
	}

	
	private void extracted() {
		addPreferencesFromResource(R.xml.settings1);
	}
}
