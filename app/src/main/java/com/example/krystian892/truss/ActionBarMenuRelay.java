package com.example.krystian892.truss;

/**
 * Created by krystian892 on 3/1/15.
 */

import com.example.truss2.R;
public class ActionBarMenuRelay {
    static public enum message {CLICK, DOWN, UP};
    static MainEditorView gm =null;
    boolean erasing = false;

    static void setGm(MainEditorView gm1) {
        gm = gm1;
    }
    static void sendMessage(int controlId) {
        if(gm == null) return;

        if(controlId == R.id.action_eraser) {
                gm.menu.switchMode(gm.menu.ERASE);
                gm.onTouch.eMode.setState(gm.onTouch.eMode.ERASE);
        }
        if(controlId == R.id.action_rod){
                gm.menu.switchMode(gm.menu.RODS);
                gm.onTouch.eMode.setState(gm.onTouch.eMode.RODS);
        }

        if(controlId == R.id.action_force){
            gm.menu.switchMode(gm.menu.FORCES);;
            gm.onTouch.eMode.setState(gm.onTouch.eMode.FORCES);
        }
        if(controlId == R.id.action_support){
            gm.menu.switchMode(gm.menu.SUPPORT);;
            gm.onTouch.eMode.setState(gm.onTouch.eMode.SUPPORTS);
        }
        if(controlId == R.id.action_simulate){
            gm.simulation();
        }
        if(controlId == R.id.action_undo){
            gm.onTouch.commandQueue.undo();;
        }
        if(controlId == R.id.action_redo){

            gm.onTouch.commandQueue.redo();;
        }
        gm.invalidate();

    }
}
