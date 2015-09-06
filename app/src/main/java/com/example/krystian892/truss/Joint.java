package com.example.krystian892.truss;

import android.util.Log;

import com.example.krystian892.truss.calculations.PointD;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by krystian892 on 3/1/15.
 */
public class Joint extends PointD implements Serializable {
    transient ArrayList<Integer>  neighbours;
    transient ArrayList<PointD> copies;
    transient int supports=0;
    transient PointD displacement = new PointD(0,0);
    public Joint(double _x, double _y) {
        super(_x, _y);
    }
    public Joint(PointD prototype){
        super(prototype.x, prototype.y);
        neighbours = new ArrayList<Integer>();
        copies = new ArrayList<PointD>();
    }

    public void addNeighbour(int j) {
        neighbours.add(j);
    }
    public void addCopy(PointD j) { copies.add(j);}
    public  void displaceCopies(double d){

        for(PointD p: copies){
            //Log.wtf("Old was", ":" + p.x + "  " + p.y);
            p.set(p.x +displacement.x * d,p.y+displacement.y*d);

          //  Log.wtf("New is", ":" + p.x + "  " + p.y);
        }
    }
    //void setSupprt = true;
    //}
}
class GraphJoint extends Joint{
    boolean support = false;
    GraphJoint(double _x, double _y) {
        super(_x, _y);
    }

    GraphJoint(PointD prototype, boolean support) {
        super(prototype);
        this.support = support;
    }
    void setSupport(){
        support = true;
    }
}