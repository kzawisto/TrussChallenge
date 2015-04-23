package com.example.krystian892.truss;

import com.example.krystian892.truss.calculations.PointD;

import java.io.Serializable;

public class ForceVector extends Vector implements Serializable {
	static final double scale = 10;
	transient int jointId;
	ForceVector(PointD anchor, PointD end){
		super(anchor, end);
	}
}
