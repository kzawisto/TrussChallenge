package com.example.krystian892.truss;

import android.util.Log;

import com.example.krystian892.truss.calculations.PointD;

import java.io.Serializable;

/**
 * Created by krystian892 on 5/1/15.
 */
public class Obstacle implements Serializable {
    final PointD p1, p2,p1b,p2b;
    double x1, y1;
    public Obstacle(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
      p1 = new PointD(Math.min(x1, x2),Math.min(y1,y2));
       p1b = new PointD(Math.min(x1,x2),Math.max(y1, y2));
      p2 = new PointD(Math.max(x1, x2),Math.max(y1, y2));
        p2b = new PointD(Math.max(x1,x2),Math.min(y1, y2));
    }

    void setEnd(double x, double y){

    }
    public boolean isPointInside(PointD p){
        if(p.x >= p1.x && p.y >= p1.y && p.x <= p2.x && p.y <= p2.y) return true;
        else return false;
    }
    boolean isVectorIntersecting(Vector v){
        if(isPointInside(v.start) || isPointInside(v.end)) {
           // Log.e("Point inside", "in");
            return true;

        }

        Vector v1 = new Vector(p1, p1b),v2 = new Vector(p1b,p2),v3 = new Vector(p2,p2b),v4 = new Vector(p2b,p1);
        if(v1.testIntersection(v)) {

            return true;
        }
        if(v2.testIntersection(v)) return true;
        if(v3.testIntersection(v)) return true;
        if(v4.testIntersection(v)) return true;

        return false;
    }
}
