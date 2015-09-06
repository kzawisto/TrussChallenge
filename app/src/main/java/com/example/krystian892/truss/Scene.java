package com.example.krystian892.truss;

import com.example.krystian892.truss.calculations.Point;
import com.example.krystian892.truss.calculations.PointD;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Scene	implements SceneSizes{
	Point size = new Point(0,0);
	PointD zero;
	OnTouch onTouch;

	OnScreenMenu menu;
    Obstacle ph = new Obstacle(-3,-3,3,3);

    PointD cursorPosition = new PointD(0,0);
	Paint black = new Paint(), green = new Paint(), paleBlue = new Paint(), gray = new Paint(),
			transLightGray = new Paint(), transDarkGray = new Paint(), transDarkGrayText = new Paint(), red = new Paint(),
			blackMenu = new Paint(), blackMenuText = new Paint(), supportBlue = new Paint(),bigText= new Paint();

	void setOnTouch(OnTouch onTouch){
		this.onTouch = onTouch;
		zero = onTouch.zero;
	}
	void setMenu(OnScreenMenu menu){
		this.menu = menu;
	}
	void setColors()	{
		green.setColor(Color.rgb(0,200,0));
        black.setTextSize(10);
		paleBlue.setColor(Color.argb(120, 80, 80, 225));
        paleBlue.setStrokeWidth(3);
		gray.setColor(Color.argb(120, 80, 80,130));
        gray.setStrokeWidth(3);
		red.setColor(Color.rgb(233,0,0));
		red.setStyle(Paint.Style.FILL);
		blackMenu.setStyle(Paint.Style.STROKE);
		blackMenu.setStrokeWidth(4);
		blackMenuText.setTextAlign(Paint.Align.CENTER);
		transDarkGray.setColor(Color.argb(150, 100,100, 100));
		transDarkGrayText.setColor(Color.argb(150, 100,100, 100));
		transDarkGray.setStrokeWidth(4);
		transDarkGray.setStyle(Paint.Style.STROKE);
		supportBlue.setColor(Color.argb(200,0,0,130));
		supportBlue.setStyle(Paint.Style.FILL);
		transLightGray.setColor(Color.argb(60, 100,100, 100));
		transDarkGrayText.setTextAlign(Paint.Align.CENTER);
        bigText.setTextSize(34);

	}
	int transformToCanvasX(double x)	{
		return (int) ((x + zero.x) * (int)onTouch.scale) + size.x/2;
	}
	int transformToCanvasY(double y)	{
		return (int) ((-y - zero.y) * (int)onTouch.scale) + size.y/2;
	}
	Point transformToCanvas(PointD d)	{
		return new Point(transformToCanvasX(d.x),transformToCanvasY(d.y));
	}
	public double transformFromCanvasX(int x)	{
		return (double)(x - size.x/2) / (int)(onTouch.scale) - zero.x;
	}
	public double transformFromCanvasY(int y)	{
		return -(double)(y - size.y/2) / (int)(onTouch.scale) - zero.y;
	}
	public PointD transformFromCanvas(Point p)	{
		return new PointD(transformFromCanvasX(p.x), transformFromCanvasY(p.y));
	}
	void drawRod(Rod r, Canvas canvas)	{
		Point start =	transformToCanvas(r.start), end = transformToCanvas(r.end);;
        if(r.paint==null || Math.abs(r.force) <0.001){///Math.abs(r.force) <0.001) {
            canvas.drawLine(start.x, start.y, end.x, end.y, gray);

           // canvas.drawText(String.format("%.4g%n", r.force), (start.x + end.x) / 2, (start.y + end.y) / 2, gray);
        }
        else{
            canvas.drawLine(start.x, start.y, end.x, end.y, r.paint);
            canvas.drawText(String.format("%.4g%n", r.force), (start.x + end.x) / 2, (start.y + end.y) / 2, r.paint);
        }
       canvas.drawCircle(start.x,start.y,4,gray);
        canvas.drawCircle(end.x,end.y,4,gray);




    }
	void drawForce(ForceVector f, Canvas canvas){
		Point start =	transformToCanvas(f.start), end = transformToCanvas(f.end);
		canvas.drawLine(start.x, start.y, end.x, end.y, red);
		canvas.drawCircle(end.x, end.y, 5, red);
		
	}
	void drawZoom(Canvas canvas, Point p){
		//Bitmap b = new Bitmap();
		
	}
	void drawGrid(Canvas canvas){
		Point zero1 = transformToCanvas(new PointD(0,0));
		Point start =new Point((transformToCanvasX(zero1.x) % (int)onTouch.scale), 
				(transformToCanvasY(zero1.y) % (int)onTouch.scale));
		for(int x = start.x;x < size.x; x += (int)onTouch.scale)
			for(int y = start.y;y < size.y; y += (int)onTouch.scale)
				canvas.drawRect(x, y, x+2, y+2,black);
		canvas.drawRect(zero1.x-2, zero1.y-2, zero1.x+2, zero1.y+2, green);
		canvas.drawLine(zero1.x, 0, zero1.x, size.y, black);
		canvas.drawLine(0, zero1.y, size.x, zero1.y, black);
        for(int a =-5;a<=5;++a){
            float x= (float)transformToCanvasX((5*a)),y = (float)transformToCanvasY((5*a));
            canvas.drawLine(x, 0, x, size.y, transLightGray);
            canvas.drawLine(0, y, size.x, y, transLightGray);
        }
	}
	public void drawSupport(PointD support, Canvas canvas) {
		Point s = transformToCanvas(support);
		//canvas.drawCircle(s.x, s.y,6, supportBlue);
        int sizeSupport=6;
        canvas.drawRect(s.x-sizeSupport,s.y-sizeSupport,s.x+sizeSupport,s.y+sizeSupport,supportBlue);
	}
    public void drawProgressCounter(Canvas canvas){

    }
	public void drawMenu(Canvas canvas){
		RectF r = new RectF((float)menu.corner.x,(float) menu.corner.y, (float) menu.corner.x + menu.buttonWidth, (float)menu.corner.y + menu.buttonHeight);
		if(menu.isVisible())canvas.drawRoundRect(r, 10, 10, blackMenu);
		else canvas.drawRoundRect(r, 10, 10, transDarkGray);
		canvas.drawText("MENU", menu.corner.x + menu.buttonWidth / 2, (float) (menu.corner.y + menu.buttonHeight * (0.65)), transDarkGrayText);
		int  y = menu.corner.y;
		if(menu.shown)
		for(int i =0; i < menu.items.size();++i){
			y += menu.buttonHeight;
			 r = new RectF((float)menu.corner.x,(float)y, (float) menu.corner.x + menu.buttonWidth, (float)y + menu.buttonHeight);
			 if(i + 1 == menu.mode || (i+1 == menu.eraseID() && menu.erasingMode)) canvas.drawRoundRect(r, 10, 10, blackMenu);
			 else canvas.drawRoundRect(r, 10, 10, transDarkGray);
			 canvas.drawText(menu.items.get(i).text, menu.corner.x + menu.buttonWidth / 2, (float) (y + menu.buttonHeight * (0.65)), transDarkGrayText);
		}
	}
    public void drawCursorPosition(Canvas canvas){
;
    }
    public void drawProhibitedRegion(Obstacle _ph,Canvas canvas){
        Point start =	transformToCanvas(_ph.p1), end = transformToCanvas(_ph.p2);
        canvas.drawRect(start.x,end.y,end.x,start.y,transLightGray);

      // Log.e("s ",start.x+" "+start.y+ " " +end.x+ " "+ end.y);
    }
    @Override
    public void setCursorPosition(double x,double y){
        cursorPosition.set(x,y);
    }
	@Override
	public double getSizeX() {
		return size.x;
	}
	@Override
	public double getSizeY() {
		return size.y;
	}
	public  void    updateSize(int x, int y){
        size.x=x;
        size.y=y;
        int newSize=Math.max(size.x,size.y)/50;
        black.setTextSize(newSize);
        gray.setTextSize(newSize);
        supportBlue.setTextSize(newSize);
    }
}
