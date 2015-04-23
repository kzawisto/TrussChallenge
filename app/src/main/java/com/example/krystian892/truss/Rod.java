package com.example.krystian892.truss;

import android.graphics.Paint;

import com.example.krystian892.truss.calculations.PointD;

import java.io.Serializable;


public class Rod extends Vector implements Serializable{
    static double maxforce = 0.002;
	transient int id1,id2;
    transient double force = 0;
    Joint jstart, jend;
    transient Paint paint = null;
	Rod(PointD _start, PointD _end)	{
        super(_start, _end);
        jstart = new Joint(start);
        jend = new Joint(end);

	}
    void setForce(double force){
        this.force=force;
        if(Math.abs(force) >maxforce)
            maxforce= Math.abs(force);
        resetPaint();
    }
    void resetPaint(){
        if(paint == null) paint=new Paint();
        if(force <0) paint.setARGB(225,60,60,60-(int)(190.0*force/maxforce));
        else paint.setARGB(225,60+ (int)(190.0*force/maxforce),60,60);
    }


}
