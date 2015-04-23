package com.example.krystian892.truss.calculations;

public class DoubleComp {
	static double eps = 1e-7;
	public static boolean isEqual(double p1, double p2)	{
		if(Math.abs(p1 - p2) < eps) return true; else return false;
	}
}
