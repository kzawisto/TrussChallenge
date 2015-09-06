package com.example.krystian892.truss;

import java.util.ArrayList;

import com.example.krystian892.truss.calculations.Point;
import com.example.krystian892.truss.calculations.PointD;

import android.util.Log;
import android.view.MotionEvent;

public class OnScreenMenu implements Menu_OnTouch{
	int mode = 1, buttonHeight, buttonWidth,textsize = 16;;
	boolean shown = true, erasingMode = false;
	double buttonHeightSpace = 1.2;
	Point corner = new Point(0,0);
	int RODS = 1, SUPPORT = 2,FORCES = 3, CALCULATING=5,ERASE = 6 ,NON_CLICK_ACTION = -2;
    OnScreenMenu(int _buttonHeight,int _buttonWidth,String s){
        buttonHeight =_buttonHeight;
        buttonWidth = _buttonWidth;
    }
	OnScreenMenu(int _buttonHeight, int _buttonWidth){
		buttonHeight =_buttonHeight;
		buttonWidth = _buttonWidth;
		addItem("RODS");
		addItem("SUPPORT");
		addItem("FORCES");
        addItem("OBSTACLE");
		addItem("COMPUTE");
        addItem("ERASE");
		addItem("UNDO");
		addItem("REDO");
        addItem("SAVE");
        addItem("LOAD");
	}
    public int eraseID(){return 6;}
	public boolean isVisible()	{
		return shown;
	}
	void setSize(int _buttonHeight, int _buttonWidth){
		buttonHeight =_buttonHeight;
		buttonWidth = _buttonWidth;
	}
	void setCorner(int x, int y)	{
		corner.set(x, y);
		for(int i =0; i < items.size();++i)	{
			items.get(i).pos.set(corner.x + items.size() * buttonHeight, corner.y);
		}
	}
	public ArrayList<Record> items = new ArrayList(10);
	void addItem( String _text){
		
		items.add(new Record( new Point(corner.x + items.size() * buttonHeight, corner.y),   _text));
	}
	 class Record	{
		Point pos;
		String text;
		Record(Point _pos,  String _text)	{
			pos = _pos; text = _text; 
		}
	}
	public int pointInside(PointD p)	{
		if(corner.x < p.x && p.x < corner.x + buttonWidth && corner.y < p.y)
			if (p.y < corner.y + buttonHeight * (items.size() + 1) && shown)
				return (int) (p.y - corner.y) / buttonHeight + 1;
			else if(p.y <corner.y + buttonHeight) return 1;
		return 0;
	}
	public void cycleMenuVisibility() {
		shown = !shown;
	}
	@Override
	public int onTouchOnMenu(MotionEvent event, int action) {
	//	if(action == MotionEvent.ACTION_DOWN)	{
	
			PointD p = new PointD(event.getX(), event.getY());
			int r = pointInside(p);
			if(action == MotionEvent.ACTION_DOWN) {
               // Log.wtf("R", "" + r);
				if(r == 1) cycleMenuVisibility();
				else {
					if(r-1 == ERASE)  switchErasing();
					else if(r != 0 && r < 7) mode = r - 1;
				}
                return r;
			}
            else if(r == 0) return 0;
            else return NON_CLICK_ACTION;


			//Log.d("r = ",""+r);

		
		
		//return 0;
	}
    void switchMode(int r){
        mode= r;
    }
	@Override
	public boolean isErasingOn() {
		return erasingMode;
	}
	public void switchErasing(){
        erasingMode = !erasingMode;
    }
}

class GameOnScreenMenu extends OnScreenMenu{
    int ERASE;
    GameOnScreenMenu(int bwidth, int bheight){
        super(bwidth,bheight,"");
        ERASE=3;
        addItem("RODS");
        addItem("TEST");
        addItem("ERASE");
        addItem("UNDO");
        addItem("REDO");
        addItem("SAVE");
        addItem("LOAD");
    }
    public int onTouchOnMenu(MotionEvent event, int action) {
        PointD p = new PointD(event.getX(), event.getY());
        int r = pointInside(p);
        if(action == MotionEvent.ACTION_DOWN) {
            if(r == 1) {
                cycleMenuVisibility();
                return r;
            }
            if(r==2){  mode =1;return 2;}
            if(r==3){mode = 2;return 6; }
            if(r==4) {
                switchErasing();
                return 7;
            }
            if(r >=5)return r+3;
        }
        if(r == 0) return 0;
        return NON_CLICK_ACTION;

    }

    public int eraseID(){return 3;}
}