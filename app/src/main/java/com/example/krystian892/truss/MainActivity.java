package com.example.krystian892.truss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.truss2.R;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		View continueButton = findViewById(R.id.continue_button);
		continueButton.setOnClickListener((OnClickListener) this);
		View newButton = findViewById(R.id.newgame_button);
		newButton.setOnClickListener((OnClickListener) this);
		View aboutButton = findViewById(R.id.about_button);
		aboutButton.setOnClickListener((OnClickListener) this);
		View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener((OnClickListener) this);
		View prefsButton = findViewById(R.id.prefs_button);
		prefsButton.setOnClickListener((OnClickListener) this);
        View gameButton = findViewById(R.id.game_button);
        gameButton.setOnClickListener((OnClickListener) this);
        View loadGameButton = findViewById(R.id.loadgame_button);
        loadGameButton.setOnClickListener((OnClickListener) this);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v) {
        Intent i;
        // TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.about_button:
		i = new Intent(this, About.class);
		startActivity(i);
		break;
		case R.id.prefs_button:
			Intent i1 = new Intent(this, Prefs.class);
			startActivity(i1);
			break;
		case R.id.newgame_button:
			//Intent i2 = new Intent(this, GameScreen.class);
            Intent i2 = new Intent(this, MainActivity2.class);
			startActivity(i2);
			break;
            case R.id.continue_button:
                Intent i3 = new Intent(this, FileLoadActivity.class);
                startActivity(i3);
            break;
            case R.id.game_button:
             //   Log.wtf("BOO","");
                Intent i4 = new Intent(this,GameSelectActivity.class);
                overridePendingTransition(0, 0);
                startActivity(i4);
                break;
            case R.id.loadgame_button:
                Intent i5 = new Intent(this,LoadGameActivity.class);
                overridePendingTransition(0, 0);
                startActivity(i5);
                break;
            // More buttons go here (if any) ...
		}


	}


}
