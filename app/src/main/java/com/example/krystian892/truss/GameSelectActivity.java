package com.example.krystian892.truss;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.truss2.R;

import java.util.StringTokenizer;

/**
 * Created by krystian892 on 4/30/15.
 */
public class GameSelectActivity extends Activity implements View.OnClickListener{
    TableLayout tl;
    TableRow tr[];
    Button bt[][];
    static int score =0;
    int size_y=2, size_x=5;
    static int red = 0xFFCC0000,green = 0xFF00FF00, gray = 0xFF656565, white = 0xFFFFFFFF;
    static final int silver =0xFFBCC6CC, platinum= 0xFFEEF8FE, gold=0xFFFFCC33, bronze=0xFFCD7F32;
    static int colors [] = {bronze, silver,gold, platinum};
    static int leveliDs;
    //int buttonIds []= new int[]{R.raw.level10,R.raw.level1};
    int resIds [][]= {{R.raw.level10,R.raw.level11,R.raw.level12,R.raw.level13,R.raw.level14},{-1,-1,-1,-1,-1}};
    Pair<Integer,Integer> getColorsForLevel(String lvlname, AchievementsDbAccess ada){
        int res = -1;
        if(ada.existAchievement("platinum_medal_resilience",lvlname)) res = 3;
        else if(ada.existAchievement("gold_medal_resilience",lvlname)) res = 2;
        else if(ada.existAchievement("silver_medal_resilience",lvlname)) res = 1;
        else if(ada.existAchievement("bronze_medal_resilience",lvlname)) res = 0;
        int cost = -1;
        if(ada.existAchievement("platinum_medal_cost",lvlname)) cost = 3;
        else if(ada.existAchievement("gold_medal_cost",lvlname)) cost = 2;
        else if(ada.existAchievement("silver_medal_cost",lvlname)) cost = 1;
        else if(ada.existAchievement("bronze_medal_cost",lvlname)) cost = 0;
        score+= cost+1;
        score += res+1;
        return new Pair<>(res,cost);
    }
    void setColoredButton(GradientDrawable.Orientation o, int colors[],int i, int j){
        GradientDrawable gd = new GradientDrawable(o,colors);
        gd.setStroke(10,0xFFFFFFFF);
        bt[i][j].measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        bt[i][j].layout(0, 0,
                bt[i][j].getMeasuredWidth(),
                bt[i][j].getMeasuredHeight());
        bt[i][j].setBackgroundDrawable(gd);
        gd.setCornerRadius(bt[i][j].getHeight()/2);
    }
    void fixColors(int i, int j, AchievementsDbAccess ada){
       if(resIds[i][j] == -1){
            setColoredButton(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{gray,red,gray},i,j);
       }
       else {
           Pair<Integer, Integer> pair = getColorsForLevel(getLevelString(i,j),ada);
           if(pair.first == -1 || pair.second ==-1)
               setColoredButton(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{white,green,white},i,j);
            else
               setColoredButton(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{colors[pair.first],colors[pair.second],colors[pair.first]},i,j);
       }
    }

    String getLevelString(int i,int j){
        return "level_"+(i+1)+"_"+j;
    }
    Pair<Integer,String> findButton(View v){
        int id= -1;
        String levelTitle="";
        for(int i =0;i<2;++i)
            for(int j=0;j<5;++j)
                if(v == bt[i][j]) {
                    levelTitle = getLevelString(i, j);
                    id = resIds[i][j];
                }

        return new Pair<>(id,levelTitle);
    }
    @Override
    public void onClick(View v) {

       // Log.e("Button Height2",Float.toString(bt[1][0].getHeight()));
        Pair<Integer,String> pair= findButton(v);
        int id= pair.first;
        String levelTitle=pair.second;

        if(id!= -1) {
            Intent i2 = new Intent(this, MainActivity2.class);
            i2.putExtra("RAWRES",id);
            i2.putExtra("LEVELID",levelTitle);
           // i2.putExtra("LOADED_ALREADY",id);
            startActivity(i2);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AchievementsDbAccess.test(this);
        //  TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        Log.wtf("BOOO", "");



          tl= (TableLayout)findViewById(R.id.tableLayout1);
        tl = new TableLayout(this);
        tr=new TableRow[5];
        bt=new Button[5][5];
        tl.setGravity(Gravity.CENTER);
        tl.setStretchAllColumns(true);
        AchievementsDbAccess ada = new AchievementsDbAccess(getApplicationContext());
        ada.open();
        for(int i =0; i <2;++i) {
            tr[i] = new TableRow(this);
            for (int j = 0; j < 5; j++) {
                bt[i][j]=new Button(this);
                bt[i][j].setText(""+(i+1)+"."+(j+1));
                bt[i][j].setOnClickListener(this);
                tr[i].addView(bt[i][j]);
                fixColors(i,j,ada);
//                if(i>2)
//                    bt[i][j].setActivated(true);

                bt[i][j].setVisibility(View.VISIBLE);
//
//                if(i==0)
//                bt[i][j].setBackgroundResource(R.drawable.button_green);
//                else if( j < 4){
//                    GradientDrawable gd = new GradientDrawable(
//                            GradientDrawable.Orientation.LEFT_RIGHT,
//                            new int[] {colors[i-1],colors[j],colors[i-1]});
//                    gd.setStroke(10,0xFFFFFFFF);
//
//                    bt[i][j].measure(
//                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//
//                    bt[i][j].layout(0, 0,
//                            bt[i][j].getMeasuredWidth(),
//                            bt[i][j].getMeasuredHeight());
//                    Log.e("Button Height1",Float.toString(bt[i][j].getHeight()));
//                   // this.get
//                    // gd.setSize();
//
//                    bt[i][j].setBackgroundDrawable(gd);
//                    gd.setCornerRadius(bt[i][j].getHeight()/2);
//
//                }
//                else
//                 bt[i][j].setBackgroundResource(R.drawable.button2);


            }
            tl.addView(tr[i]);
        }
        setContentView(tl);
        ada.close();
        Log.e("Button Height2",Float.toString(bt[1][0].getHeight()));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}

