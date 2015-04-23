package com.example.krystian892.truss;

import com.example.krystian892.truss.calculations.PointD;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by krystian892 on 3/1/15.
 */
public class Joint extends PointD implements Serializable {
    transient ArrayList<Integer>  neighbours;
    transient ArrayList<Joint> pointerNeighbours;
    transient int supports=0;
    public Joint(double _x, double _y) {
        super(_x, _y);
    }
    public Joint(PointD prototype){
        super(prototype.x, prototype.y);
        neighbours = new ArrayList<Integer>();
        pointerNeighbours = new ArrayList<Joint>();
    }

    public void addNeighbour(int j) {
        neighbours.add(j);
    }
    //void setSupport(){
     //   support = true;
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