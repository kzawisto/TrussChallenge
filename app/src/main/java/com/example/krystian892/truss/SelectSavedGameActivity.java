package com.example.krystian892.truss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.truss2.R;

import java.io.File;
import java.util.ArrayList;

public class SelectSavedGameActivity extends FileLoadActivity {
    String dir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreateInherited(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        assert bundle.containsKey("LEVELID");
        dir =bundle.getString("LEVELID");
        String root = Environment.getExternalStorageDirectory().toString();
        Log.d("", root);
        File f = new File(root + "/TrussSimulator/"+dir+"/");
        f.mkdirs();
        File[] files = f.listFiles();
        int index=(f.toString()).length();
        ArrayList<String> ss = new ArrayList<String>();
        for(File f1: files)         ss.add( f1.toString().substring(index+1));

        setTitle("Load game for /"+ dir+"/");
        ListView view = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_fileselect, R.id.label, (String[])ss.toArray(new String[ss.size()]));
        view.setAdapter(adapter);
        view.setOnItemClickListener(new ListListenerLoadGame(this,dir));
        setContentView(view);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==R.id.action_delete){
            Intent i1 = new Intent(this, FileDeleteActivity.class);
            i1.putExtra("LEVELID",dir);

            startActivity(i1);
        }
        return super.onOptionsItemSelected(item);
    }
}

class ListListenerLoadGame implements AdapterView.OnItemClickListener{

    Activity activity;
    String dir;
    ListListenerLoadGame(Activity activity,String dir) {
        this.activity = activity;
        this.dir = dir;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String s =(String) parent.getItemAtPosition(position);
        Intent i = new Intent(activity, MainActivity2.class);
        //Construction constr = Construction.readFromFile(item);
        i.putExtra("CONSTRUCTION",s);
        i.putExtra("LEVELID", dir);
        i.putExtra("LOADEDGAME",true);
        activity.startActivity(i);
        activity.finish();
    }
}