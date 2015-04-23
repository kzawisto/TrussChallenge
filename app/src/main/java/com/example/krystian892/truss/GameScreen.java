package com.example.krystian892.truss;

import com.example.krystian892.truss.calculations.PointD;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class GameScreen  extends Activity implements OnClickListener{
	PointD zero = new PointD(0,0);

	GameView gameView;
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameView = new GameView(this);
		setContentView(gameView);
		gameView.requestFocus();
	}

}
