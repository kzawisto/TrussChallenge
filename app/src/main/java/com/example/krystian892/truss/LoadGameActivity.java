package com.example.krystian892.truss;

import android.content.Intent;
import android.util.Pair;
import android.view.View;

import com.example.truss2.R;

public class LoadGameActivity extends GameSelectActivity{
    @Override
    public void onClick(View v) {
        Pair<Integer,String> pair= findButton(v);
        int id= pair.first;
        String levelTitle=pair.second;
        if(id!= -1) {

            Intent i2 = new Intent(this,SelectSavedGameActivity.class);
            //i2.putExtra("RAWRES",id);
            i2.putExtra("LEVELID",levelTitle);
            // i2.putExtra("LOADED_ALREADY",id);
            startActivity(i2);
        }
    }

}
