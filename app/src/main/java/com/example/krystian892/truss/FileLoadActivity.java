package com.example.krystian892.truss;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
public class FileLoadActivity extends ActionBarActivity{
    void onCreateInherited(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_fileselect);

        String assets[]= listOfAssets("predef");
        Log.d("Asset",assets[0]);
      //  File f = //getApplicationContext().getExternalFilesDir("files");
        String root = Environment.getExternalStorageDirectory().toString();
        Log.d("", root);
        File f = new File(root + "/TrussSimulator/user/");
        f.mkdirs();
        File[] files = f.listFiles();

        int index=(f.toString()).length();
        ArrayList<String> ss = new ArrayList<String>();
        for(String s:assets)    ss.add("#"+s);
        for(File f1: files)         ss.add( f1.toString().substring(index+1));

        setTitle("Load file");
        ListView view = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_fileselect, R.id.label, (String[])ss.toArray(new String[ss.size()]));
        view.setAdapter(adapter);
        view.setOnItemClickListener(new ListListenerLoad(this));
        setContentView(view);

    }
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//       final String item = (String) getListAdapter().getItem(position);
//        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Do you want to load \""+item+"\"?");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent i = new Intent(FileLoadActivity.this, MainActivity2.class);
//                //Construction constr = Construction.readFromFile(item);
//                i.putExtra("CONSTRUCTION",item);
//
//                startActivity(i);
//                finish();
//                // invalidate();
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                dialog.cancel();
//            }
//        });
//        builder.show();
//    }
    String[] listOfAssets(String root){
        AssetManager asm = getAssets();
        String list[] = null;
        try{
            list= asm.list(root);
        }
        catch (IOException e) { e.printStackTrace();}
        return list;
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
        inflater.inflate(R.menu.loadfile, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==R.id.action_delete){
            Intent i1 = new Intent(this, FileDeleteActivity.class);
            startActivity(i1);
        }
        return super.onOptionsItemSelected(item);
    }

}
class ConstrFileIO{
    static Construction loadFromFile(File f){

        return null;
    }
    static void WriteFile(String filename, Construction c, Context ctx){
        String string = "Hello world!";
        FileOutputStream outputStream;

        try {
            outputStream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void ReadFile(String filename,  Context ctx){
        FileInputStream inputStream;
        File f = new File(filename);
        ByteArrayOutputStream bos=null;
        try {
            InputStream is = ctx.openFileInput(filename);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int bytesRead;
            while (( bytesRead = is.read(b))!=-1){
                bos.write(b, 0, bytesRead);
            }
        }
        catch(Exception e){e.printStackTrace();}
        byte[] bytes = bos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

    }

}

class ListListenerLoad implements AdapterView.OnItemClickListener{

    Activity activity;

    ListListenerLoad(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String s =(String) parent.getItemAtPosition(position);
        Intent i = new Intent(activity, MainActivity2.class);
        //Construction constr = Construction.readFromFile(item);
        i.putExtra("CONSTRUCTION",s);
        activity.startActivity(i);
        activity.finish();
    }
}

