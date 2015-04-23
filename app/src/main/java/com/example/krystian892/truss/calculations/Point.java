package com.example.krystian892.truss.calculations;


public class Point {
	public int x,  y;
	public Point(int _x, int _y)	{
		x = _x;
		y = _y;
	}
	public void set(int _x, int _y)	{
		x = _x;
		y = _y;
	}
	public void set(Point p)	{
		set(p.x,p.y);
	}
	public static double dist(Point p1, Point p2)	{
		return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
	}
	public PointD PointD()	{
		return new PointD(x,y);
	}
}
