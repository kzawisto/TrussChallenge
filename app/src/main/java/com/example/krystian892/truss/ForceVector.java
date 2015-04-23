package com.example.krystian892.truss;

import com.example.krystian892.truss.calculations.PointD;

public class ForceVector extends Vector{
	static final double scale = 10;
	int jointId;
	ForceVector(PointD anchor, PointD end){
		super(anchor, end);
	}
}
