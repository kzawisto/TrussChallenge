package com.example.krystian892.truss;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.krystian892.truss.calculations.PointD;

import dalvik.annotation.TestTargetClass;

/**
 * Created by krystian892 on 7/17/15.
 */
public class VectorTest1 extends AndroidTestCase{
    Vector v1;
    Vector v2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        v1=new Vector(new PointD(1,3),new PointD(0,6));
        v2=new Vector(new PointD(3,4),new PointD(-3,5));

       // v2=new Vector(new PointD(3,4),new PointD(-3,5));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testIntersections(){
        assertTrue( testIntersection(v1,v2));
    }
    boolean testIntersection(Vector v,Vector v1){
        PointD r1 = v1.end.minus(v1.start);
        PointD s1 = v.end.minus(v.start);
        double c1 = r1.cross(v.end.minus(v1.start)), c2 = r1.cross(v.start.minus(v1.start));
        if(c1*c2 <=0)
        {
            double c3 = s1.cross(v1.end.minus(v.start)), c4 = s1.cross(v1.start.minus(v.start));
            if(c3*c4 <= 0) return true;
        }
        return false;

    }
}
