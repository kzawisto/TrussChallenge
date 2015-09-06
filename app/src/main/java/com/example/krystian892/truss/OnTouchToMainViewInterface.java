package com.example.krystian892.truss;

import com.example.krystian892.truss.calculations.PointD;

/**
 * Created by krystian892 on 4/24/15.
 */
public interface OnTouchToMainViewInterface {
    void readDialog();
    void saveDialog();
    void triggerAnimation();
    void simulation();
    void updateIndicator(double x,double y,double x_anchor, double y_anchor,int state);
    void stopAnimation();
}
