package com.example.krystian892.truss;

import com.example.krystian892.truss.calculations.PointD;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class GameScreen  extends Activity implements OnClickListener{
	PointD zero = new PointD(0,0);

	MainEditorView mainEditorView;
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainEditorView = new MainEditorView(this);
        Bundle bundle = ActivitySingleton.getActivity().getIntent().getExtras();
        if(bundle!=null) {
            String s= bundle.getString("CONSTRUCTION");
            if (s!= null) {
                Log.wtf("Reading", s);
                mainEditorView.constr = ConstructionLoader.readFromFile(s);
            }
            else
                Log.wtf("Not","Found");
        }
        else Log.wtf("Not","Found bundle");
		setContentView(mainEditorView);
		mainEditorView.requestFocus();
	}

}
