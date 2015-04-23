package com.example.krystian892.truss.calculations;


import static java.lang.Math.*;

public class PointD {
	public double x,y;
    static final double eps = 1e-2;
	public PointD(double _x, double _y) {
		x = _x;
		y = _y;
	}
	public void add(double _x, double _y) {
		x += _x;
		y += _y;
	}
	public void set(double _x, double _y) {
		x = _x;
		y = _y;
	}
	public PointD minus(PointD p)	{
		return new PointD(x - p.x, y - p.y);
	}
	public double dot(PointD p)	{
		return x*p.x + y*p.y;
	}
	public double dist(PointD p)	{
		return sqrt(pow(x - p.x,2) + pow(y - p.y,2));
	}
    public boolean comp(PointD p) {
        if(dist(p) < eps) return true;
        else return false;
    }
}
