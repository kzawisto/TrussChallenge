package com.example.krystian892.truss;

import com.example.krystian892.truss.calculations.PointD;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
public class GameView extends View implements OnTouchListener, ViewRefreshing{
	//private final GameScreen game;
	PointD   zero = new PointD(7,12);
	PointD cursor = new PointD(0,0);
	Rod tempRod = new Rod(new PointD(0,0), new PointD(0,0));
	Construction constr = new Construction();
	OnScreenMenu menu = new OnScreenMenu(20,100);

	Scene scene = new Scene();
	OnTouch onTouch = new OnTouch(zero,menu, scene ,constr);
	int mode = 2; 
	public GameView(Context context) {
		super(context);
		//game = (GameScreen) context;
		//Scene scene = new Scene();
		scene.setOnTouch(onTouch);
		scene.setColors();
		scene.setMenu(menu);
		setOnTouchListener(this);
		ActionBarMenuRelay.setGm(this);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		scene.size.x = w; scene.size.y = h;
		int size = Math.min(scene.size.x, scene.size.y);
		scene.transDarkGrayText.setTextSize(size /26);
		scene.transDarkGrayText.setTextScaleX((float) 1.3);
		menu.setSize(size / 10, (int) (size /4.3));
		menu.setCorner((int) (scene.size.x - 1.1 * menu.buttonWidth), (int)( 0.2 * menu.buttonHeight));
		super.onSizeChanged(w, h, oldw, oldh);
	}

	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		return false;		
	}
	public boolean onTouchEvent(MotionEvent event)	{
		int action = event.getActionMasked();
		if(event.getPointerCount() == 2)	onTouch.ScalingAndMoving(event, action);
		else if(event.getPointerCount() == 1)	{
			if(onTouch.OnScreenMenu(event, action))
			onTouch.RodsDrawing(event, action);
		}
		cursor.set((double)event.getX(), (double)event.getY());
		invalidate();
		return true;
		
	}
	protected void onDraw(Canvas canvas) {

		scene.drawGrid(canvas);
		if(onTouch.drawingRod) scene.drawRod(onTouch.tempRod, canvas);
		if(onTouch.drawingForce) scene.drawForce(onTouch.tempForce, canvas);
		for(Rod e: constr.rods)scene.drawRod(e, canvas);
		for(ForceVector e: constr.forces)scene.drawForce(e, canvas);
		for(PointD e: constr.supports.tab) scene.drawSupport(e, canvas);
		scene.drawMenu(canvas);

	}	
	public static String actionToString(int action) {
	    switch (action) {
	             
	        case MotionEvent.ACTION_DOWN: return "Down";
	        case MotionEvent.ACTION_MOVE: return "Move";
	        case MotionEvent.ACTION_POINTER_DOWN: return "Pointer Down";
	        case MotionEvent.ACTION_UP: return "Up";
	        case MotionEvent.ACTION_POINTER_UP: return "Pointer Up";
	        case MotionEvent.ACTION_OUTSIDE: return "Outside";
	        case MotionEvent.ACTION_CANCEL: return "Cancel";
	    }
	    return "";
	}
	void drawZoom(Canvas canvas, int x, int y){
		int size_x =( canvas.getWidth()/6);
		int size_y = canvas.getHeight();
		Bitmap b = Bitmap.createBitmap(size_x, size_y,Bitmap.Config.ARGB_8888);
	}
	void simulation(){

        constr.simulation();
    }
	/*private class OnTouch	{
		Point pinchDrag = new Point(0,0), pinchDragOld = new Point(0,0);
		double scale = 30, pinchScaling, pinchScalingOld = 0, SCALE_MAX =90, SCALE_MIN = 20;
		boolean drawingRod = false;

		Point[] pointer = new Point[2], pointer_old = new Point[2];
		EditMode dMode = new EditMode();
		OnTouch()	{
			pointer[0] = new Point(0,0);
			pointer[1] = new Point(0,0);
			pointer_old[0] = new Point(0,0);
			pointer_old[1] = new Point(0,0);
		}
		class EditMode {
			//TreeMap<String, State> map = new TreeMap<String, State>();
			final State RODS, SUPPORTS, ERASE;
			State state;
			EditMode()	{
				//map.put("rods",new Rods());
				//map.put("supports",new Supports());
				//map.put("erase",new Supports());
				RODS = new Rods();
				SUPPORTS = new Supports();
				ERASE = new Erase();
				state = RODS;
			}
			void processMouseEvent(MotionEvent e, int action)	{
				if(MotionEvent.ACTION_DOWN == action) state.onMouseDown(e, action);
				else if(MotionEvent.ACTION_MOVE == action) state.onMouseMove(e, action);
				else if(MotionEvent.ACTION_UP == action) state.onMouseUp(e, action);
			}
			abstract class State{
				void onMouseMove(MotionEvent event, int action){}
				void onMouseDown(MotionEvent event, int action){}
				void onMouseUp(MotionEvent event, int action){}
			}
			class Rods extends State{
				void onMouseMove(MotionEvent event, int action) {
					// TODO Auto-generated method stub
					
				}

				@Override
				void onMouseDown(MotionEvent event, int action) {
					// TODO Auto-generated method stub
					
				}

				@Override
				void onMouseUp(MotionEvent event, int action) {}
				
			}
			class Supports extends State{
			}
			class Erase extends State{}
		}
		void RodsDrawing(MotionEvent event, int action)	{
			if(MotionEvent.ACTION_DOWN == action)	{
				drawingRod = true;
				cursor.set((int)event.getX(), (int)event.getY());
				tempRod.start.set( Math.round(scene.transformFromCanvasX(cursor.x)),  Math.round(scene.transformFromCanvasY(cursor.y)));
			}
			if(MotionEvent.ACTION_MOVE == action)	{
				cursor.set((int)event.getX(), (int)event.getY());
				tempRod.end.set( Math.round(scene.transformFromCanvasX(cursor.x)),  Math.round(scene.transformFromCanvasY(cursor.y)));
				invalidate();
			}
			if(MotionEvent.ACTION_UP == action && drawingRod)	{ 
				constr.rods.add(tempRod);
				tempRod = new Rod(new PointD(0,0), new PointD(0,0));
				drawingRod = false;
			}
			
		}
		void ScalingAndMoving(MotionEvent event, int action)	{
			if(action == MotionEvent.ACTION_MOVE)	{
				drawingRod = false;
				updateGesturePointers(event);
				if(pinchScalingOld > 0.01) {
					scaleScene();
					moveScene();
					invalidate();
				}
				moveNewGesturePointersToOld();
			}
			if(action == MotionEvent.ACTION_POINTER_UP)
				pinchScalingOld = 0;	
		}
		boolean OnScreenMenu(MotionEvent event, int action) 	{
			if(action == MotionEvent.ACTION_DOWN)	{
				PointD p = new PointD(event.getX(), event.getY());
				
				int r = menu.pointInside(p);
				if(r==0) return true;
				if(r == 1) {
					menu.shown = !menu.shown;
					invalidate();
				}
				return false;
			}
			return true;canvas set bitmap
		}
		private void scaleScene()	{
			if(scale * pinchScaling / pinchScalingOld < SCALE_MAX && scale * pinchScaling / pinchScalingOld > SCALE_MIN)	{
				scale *= pinchScaling / pinchScalingOld;
				zero.add(-(double)(pinchDrag.x - scene.size.x / 2) / (int)scale *( 1 - (pinchScalingOld / pinchScaling)), 
					+(double)(pinchDrag.y - scene.size.y / 2) / (int)scale * (1 - (pinchScalingOld / pinchScaling)));
			}
		}
		private void moveScene()	{
			zero.add(-(double)(-pinchDrag.x + pinchDragOld.x) / (int) scale,(double)(-pinchDrag.y + pinchDragOld.y) / (int) scale);
		}
		private void updateGesturePointers(MotionEvent event)	{
			pointer[0].set((int)event.getX(0),(int)event.getY(0));
			pointer[1].set((int)event.getX(1),(int)event.getY(1));
			pinchScaling = Point.dist(pointer[0], pointer[1]);
			pinchDrag.set((pointer[0].x + pointer[1].x) / 2,(pointer[0].y + pointer[1].y) / 2 );
		}

		private void moveNewGesturePointersToOld()	{ 
			pointer_old[0].set(pointer[0]);
			pointer_old[1].set(pointer[1]);
			pinchScalingOld = pinchScaling;
			pinchDragOld.set(pinchDrag);
		}
		
	};*/
}
class DrawingMode {
	abstract class State{
		abstract void processMouseEvent(MotionEvent e, int action);
	}
	class Rods extends State{

		void processMouseEvent(MotionEvent e, int action) {
			// TODO Auto-generated method stub
			
		}
		
	}
}

