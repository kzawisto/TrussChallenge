package com.example.krystian892.truss;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by krystian892 on 4/23/15.
 */
public class FileLoadActivity extends ListActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_fileselect);

        String path = Environment.getExternalStorageDirectory().toString()+"/constructions/";
        File f = new File(path);
        File[] files = f.listFiles();
        ArrayList<String> ss = new ArrayList<String>();
        for(File f1: files){
            ss.add(f1.toString());
        }
        setTitle(path);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_fileselect, R.id.label, (String[])ss.toArray());
        setListAdapter(adapter);
    }
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
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