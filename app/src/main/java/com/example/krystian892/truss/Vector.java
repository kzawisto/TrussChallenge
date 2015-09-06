package com.example.krystian892.truss;

import android.util.Log;

import com.example.krystian892.truss.calculations.*;

import java.io.Serializable;

public class Vector implements Serializable {
	PointD start, end;
	
	Vector(PointD _start, PointD _end)	{
		start = _start;
		end = _end;
		}
	double distFromRod(PointD point)	{
		PointD dir = end.minus(start), dir1 = point.minus(start);
		if(dir.dot(dir1) > 0 && dir.dot(dir1) < dir.dot(dir)) {
			if(start.x == end.x) return Math.abs(point.x - end.x);
			if(start.y == end.y) return Math.abs(point.y - end.y);
			double B =1, A= -(start.y - end.y) / (start.x - end.x),C = - start.x * A - start.y;
			return Math.abs(A * point.x + B * point.y + C) / Math.sqrt(A * A + B * B);
		}
		return Math.min(start.dist(point), end.dist(point));
		
	}
	double cosPhi(){
		return (end.x -start.x) / length(); 
	}
	double sinPhi(){
		return (end.y -start.y) / length(); 
	}
	double length(){
		return Math.sqrt((end.x - start.x) * (end.x - start.x) + (end.y - start.y) * (end.y - start.y));
	}
    boolean testIntersection(Vector v){

            PointD r1 = end.minus(start);
            PointD s1 = v.end.minus(v.start);
            double c1 = r1.cross(v.end.minus(start)), c2 = r1.cross(v.start.minus(start));
            if(c1*c2 <=0)
            {
                double c3 = s1.cross(end.minus(v.start)), c4 = s1.cross(start.minus(v.start));
                if(c3*c4 <= 0) return true;
            }
            return false;


    }
}
