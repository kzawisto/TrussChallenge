package com.example.krystian892.truss;

/**
 * Created by krystian892 on 5/5/15.
 */

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.truss2.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.truss2.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by krystian892 on 4/23/15.
 */
//public class FileDeleteActivity extends ListActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // setContentView(R.layout.activity_fileselect);
//
//        //  File f = //getApplicationContext().getExternalFilesDir("files");
//        String root = Environment.getExternalStorageDirectory().toString();
//        Log.d("", root);
//        File f = new File(root + "/TrussSimulator/user/");
//        f.mkdirs();
//        File[] files = f.listFiles();
//
//        int index=(f.toString()).length();
//        ArrayList<String> ss = new ArrayList<String>();
//        for(File f1: files)         ss.add( f1.toString().substring(index+1));
//
//        setTitle(f.toString());
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                R.layout.activity_fileselect, R.id.label, (String[])ss.toArray(new String[ss.size()]));
//        setListAdapter(adapter);
//    }
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        final String item = (String) getListAdapter().getItem(position);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Do you really want to delete \""+item+"\"?");
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String root = Environment.getExternalStorageDirectory().toString();
//                File f = new File(root + "/TrussSimulator/user/"+item);
//                f.delete();
//            }
//        });
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                dialog.cancel();
//            }
//        });
//        builder.show();
//    }
//    String[] listOfAssets(String root){
//        AssetManager asm = getAssets();
//        String list[] = null;
//        try{
//            list= asm.list(root);
//        }
//        catch (IOException e) { e.printStackTrace();}
//        return list;
//    }
//    void updateAdapter(){
//
//    }
//    public void restoreActionBar() {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setTitle(getTitle());
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.deletefile, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() ==R.id.action_load){
//            Intent i1 = new Intent(this, FileLoadActivity.class);
//            startActivity(i1);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}

public class FileDeleteActivity extends ActionBarActivity {

    String root;
    ListView view;
    String folder = "user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root= Environment.getExternalStorageDirectory().toString();
        Log.d("", root);
        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey("LEVELID"));
            folder = bundle.getString("LEVELID");
        File f = new File(root + "/TrussSimulator/"+folder+"/");
        f.mkdirs();
        File[] files = f.listFiles();

        int index=(f.toString()).length();
        ArrayList<String> ss = new ArrayList<String>();
        for(File f1: files)         ss.add( f1.toString().substring(index+1));

        setTitle("Delete files");
        if(folder != "user"){
            setTitle("Delete saved games for " + folder);
        }
        view = new ListView(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_fileselect, R.id.label, (String[])ss.toArray(new String[ss.size()]));
        view.setAdapter(adapter);
        view.setOnItemClickListener(new ListListenerDelete(this));
        setContentView(view);

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getTitle());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.deletefile, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==R.id.action_load){
            Intent i1 = new Intent(this, FileLoadActivity.class);
            startActivity(i1);
        }
        return super.onOptionsItemSelected(item);
    }
    class ListListenerDelete implements AdapterView.OnItemClickListener{

        Activity activity;

        ListListenerDelete(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final String s =(String) parent.getItemAtPosition(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(FileDeleteActivity.this);
            builder.setTitle(getString(R.string.confirm_delete)+ "\"" + s +"\"?");
// Set up the buttons
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String root = Environment.getExternalStorageDirectory().toString();
                    File f = new File(root + "/TrussSimulator/"+folder+"/"+s);
                    f.delete();
                    resetList();
                }
            });
            builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                }
            });


            builder.show();
        }
    }
    void resetList(){
        File f = new File(root + "/TrussSimulator/"+folder+"/");
        File[] files = f.listFiles();
        int index=(f.toString()).length();
        ArrayList<String> ss = new ArrayList<String>();
        for(File f1: files)         ss.add( f1.toString().substring(index+1));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_fileselect, R.id.label, (String[])ss.toArray(new String[ss.size()]));
        view.setAdapter(adapter);
    }

}