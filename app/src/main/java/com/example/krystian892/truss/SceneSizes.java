package com.example.krystian892.truss;



import com.example.krystian892.truss.calculations.Point;
import com.example.krystian892.truss.calculations.PointD;

public interface SceneSizes {
	double transformFromCanvasY(int y);
	double transformFromCanvasX(int x);

	PointD transformFromCanvas(Point p);
	double getSizeX();
	double getSizeY(); 
}
