package com.example.krystian892.truss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import junit.framework.Assert;

import java.sql.Array;
import java.util.ArrayList;

/**
 * Created by krystian892 on 9/6/15.
 */
public abstract class DbAccess{
    static String createTable(String tableName, DbColumn [] array){
        String s =   "CREATE TABLE " + tableName+ "( ";
        for(DbColumn col : array)
            s+= col.getName() + " " + col.getOptions()+ ", ";
        s=s.substring(0,s.length()-2);
        s+=");";
        return s;
    }
    static String dropTable(String tableName){
       return  "DROP TABLE IF EXISTS " + tableName;
    }

}
class Achievement{
    int id;
    String name;
    String level;
    int counter;
}
class AchievementsDbAccess extends  DbAccess {
    private static final String DEBUG_TAG = "AchievementsDbAccess";

    private static final int VERSION = 1;
    private static final String DB_NAME = "database.db";

    private static final String TABLE_NAME = "achievements";
    public static final DbColumn ID = new DbColumn("_id", "INTEGER PRIMARY KEY AUTOINCREMENT",0);
    public static final DbColumn COUNTER = new DbColumn("_counter", "INTEGER",1);
    public static final DbColumn NAME = new DbColumn("_name", "TEXT NOT NULL",2);
    public static final DbColumn FOR_LEVEL = new DbColumn("_for_level", "TEXT NOT NULL",2);
    public static DbColumn array[] = {ID,COUNTER,NAME,FOR_LEVEL};
    private SQLiteDatabase db;

    private DatabaseHelper openHelper;
    Context context;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createTable(TABLE_NAME,array));

          //  Log.d(DEBUG_TAG, "Database creating...");
           // Log.d(DEBUG_TAG, "Table " + TABLE_NAME + " ver." + VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
    public AchievementsDbAccess(Context context){
        this.context = context;
    }
    public AchievementsDbAccess open(){
        openHelper = new DatabaseHelper(context, DB_NAME, null,VERSION);
        try {
            db = openHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e(DEBUG_TAG, "Opening for writing failed, trying opening for reading...");
            db = openHelper.getReadableDatabase();
        }
        return this;
    }
    public void close() {
        openHelper.close();
    }
    public long insertAchievement(String name, String for_level) {
        ContentValues val = new ContentValues();
        val.put(NAME.getName(), name);
        val.put(FOR_LEVEL.getName(), for_level);
        val.put(COUNTER.getName(),1);

        return db.insert(TABLE_NAME,null,val);
    }
    public boolean existAchievement(String name, String for_level){
        String columns [] = {NAME.getName(), FOR_LEVEL.getName()};
        String where_args []= {name,for_level};
        String filterString = NAME.getName()  +"=? and " + FOR_LEVEL.getName()+"=?";
        Cursor c=db.query(TABLE_NAME, columns,filterString,where_args, null, null, null);

        Log.d(DEBUG_TAG,"Count: "+ c.getCount());
        return c.getCount() >0;
    }
    static void test(Context context){
        AchievementsDbAccess ada = new AchievementsDbAccess(context);
        ada.open();
        ada.existAchievement("testAchievement","testLevel");
        ada.insertAchievement("testAchievement","testLevel");
        Assert.assertTrue( ada.existAchievement("testAchievement","testLevel"));
        ada.close();
        //Assert.assertFalse();
    }
}



class DbColumn{
    String name;
    String options;
    int columnid;

    DbColumn(String name, String options, int columnid) {
        this.name = name;
        this.options = options;
        this.columnid = columnid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public int getColumnid() {
        return columnid;
    }

    public void setColumnid(int columnid) {
        this.columnid = columnid;
    }
}