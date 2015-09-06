package com.example.krystian892.truss;

import com.example.krystian892.truss.calculations.Point;
import com.example.krystian892.truss.calculations.PointD;

import android.util.Log;
import android.view.MotionEvent;

public class OnTouch	{
	Point cursor = new Point(0,0);
	PointD   zero = new PointD(7,12);
	Point pinchDrag = new Point(0,0), pinchDragOld = new Point(0,0);
	Rod tempRod =new Rod(new PointD(0,0), new PointD(0,0));
	ForceVector tempForce = new ForceVector(new PointD(0,0), new PointD(0,0));
    Obstacle tempObstacle = new Obstacle(0,0, 0,0);
	double scale = 30, pinchScaling, pinchScalingOld = 0, SCALE_MAX =90, SCALE_MIN = 20;
	boolean drawingRod = false, drawingForce = false,drawingObstacle=false;
   Construction constr;
    OnTouchToMainViewInterface onTouchToMainViewInterface;
	SceneSizes scene;
	Menu_OnTouch menu;
	CommandQueue commandQueue = new CommandQueue();
	Point[] pointer = new Point[2], pointer_old = new Point[2];
	EditMode eMode = new EditMode();
	OnTouch(PointD zero, Menu_OnTouch menu, Scene scene, Construction constr, OnTouchToMainViewInterface onTouchToMainViewInterface)	{
		pointer[0] = new Point(0,0);
		pointer[1] = new Point(0,0);
		pointer_old[0] = new Point(0,0);
		pointer_old[1] = new Point(0,0);
		this.zero =zero;
		this.menu = menu;
		this.scene = scene;
		this.constr=constr;
        this.onTouchToMainViewInterface = onTouchToMainViewInterface;
	}
	class EditMode {
		final int RODS =1, SUPPORTS =2, ERASE=6,FORCES=3,OBSTACLE=4, CALCULATING=5;
		State[] array = new State[10];
		State state;
		boolean erasingMode = false;
		EditMode()	{
			array[RODS] = new RodsState();
			array[SUPPORTS] = new SupportsState();
			array[FORCES] = new ForcesState();
            array[OBSTACLE]= new ObstaclesState();

            array[CALCULATING]= new CalculationsState();
			state = array[RODS];
		}
		void setState(int r){
            Log.wtf("State is", " "+r);
			if(r == ERASE) {
                erasingMode = !erasingMode;

            }

			else if(r >= 1 && r <= 5) {
                state.onStateChanged(r);
                state = array[r];

            }
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
            void onStateChanged(int newState){
                if(newState == CALCULATING && this != array[CALCULATING]) {
                    onTouchToMainViewInterface.simulation();
                    onTouchToMainViewInterface.triggerAnimation();
                }

                  //  Log.wtf("BOO","BOO");
            }
		}
		class RodsState extends State{
			void onMouseMove(MotionEvent event, int action) {
				cursor.set((int)event.getX(), (int)event.getY());
                scene.setCursorPosition(scene.transformFromCanvasX(cursor.x), scene.transformFromCanvasY(cursor.y));
				if(!erasingMode)
				tempRod.end.set( Math.round(scene.transformFromCanvasX(cursor.x)),  Math.round(scene.transformFromCanvasY(cursor.y)));
                int state=0;
                float x=Math.round(scene.transformFromCanvasX(cursor.x));
                float y= Math.round(scene.transformFromCanvasY(cursor.y));
                PointD p=new PointD(x,y);
                for( Rod r:constr.rods) if(r.start.dist(p) < 1e-2 || r.end.dist(p) <1e-2 ){state=1;break;}
                for(PointD p1:constr.supports.tab)if(p1.dist(p)<1e-2) {state=2;break;}

                onTouchToMainViewInterface.updateIndicator(x,y,tempRod.start.x,tempRod.start.y,state);
            }
			@Override
			void onMouseDown(MotionEvent event, int action) {
				cursor.set((int)event.getX(), (int)event.getY());
				tempRod = new Rod(scene.transformFromCanvas(cursor),scene.transformFromCanvas(cursor));
				if(erasingMode) for(Rod e: constr.rods) {
					if(e.distFromRod( new PointD(scene.transformFromCanvasX(cursor.x),scene.transformFromCanvasY(cursor.y))) < 0.5){
							commandQueue.issueCommand(new RodEraseCommand(constr, e));
						break;
					}
				}
				else {
					tempRod.start.set( Math.round(scene.transformFromCanvasX(cursor.x)),  Math.round(scene.transformFromCanvasY(cursor.y)));
				    drawingRod = true;
				}
			}
			@Override
			void onMouseUp(MotionEvent event, int action) {
				if(!drawingRod) return;
				drawingRod = false;
				if(tempRod.start.dist(tempRod.end) <= 1) return;
                if(constr.isRodAllowed(tempRod))
				 commandQueue.issueCommand(new RodDrawCommand(constr,tempRod));
				
			}
		}
		class SupportsState extends State{
			void onMouseUp(MotionEvent event, int action){
				cursor.set((int)event.getX(), (int)event.getY());
				PointD p =new PointD(Math.round(scene.transformFromCanvasX(cursor.x)),  
						 Math.round(scene.transformFromCanvasY(cursor.y)));
				if(!erasingMode) addSupportPoint(p);
				else removeSupportPoint(p);
				
			}
			void addSupportPoint(PointD p){
                if(constr.supports.findPointLike(p) ==-1)
				commandQueue.issueCommand(new SupportPlaceCommand(constr.supports, p));
			}
			void removeSupportPoint(PointD p){
               if( constr.supports.findPointLike(p) > -1)
				commandQueue.issueCommand(new SupportRemoveCommand(constr.supports, p));
			}
			
		}
		class ForcesState extends State{
			void onMouseMove(MotionEvent event, int action) {
				cursor.set((int)event.getX(), (int)event.getY());
				if(!erasingMode)tempForce.end.set( Math.round(scene.transformFromCanvasX(cursor.x)),  Math.round(scene.transformFromCanvasY(cursor.y)));
			}
			@Override
			void onMouseDown(MotionEvent event, int action) {
				cursor.set((int)event.getX(), (int)event.getY());

				tempForce = new ForceVector(scene.transformFromCanvas(cursor),scene.transformFromCanvas(cursor));
				if(erasingMode) for(ForceVector e: constr.forces) {
					if(e.distFromRod( new PointD(scene.transformFromCanvasX(cursor.x),scene.transformFromCanvasY(cursor.y))) < 0.5){
							commandQueue.issueCommand(new ForceVectorEraseCommand(constr.forces, e));
						break;
					}
				}
				else {
					drawingForce = true;
					tempForce.start.set( Math.round(scene.transformFromCanvasX(cursor.x)),  Math.round(scene.transformFromCanvasY(cursor.y)));
				}
			}
			@Override
			void onMouseUp(MotionEvent event, int action) {
				if(!drawingForce) return;
				drawingForce = false;
				if(tempForce.start.dist(tempForce.end) <= 1) return;
				//constr.forces.add(tempForce);
				commandQueue.issueCommand(new ForceVectorDrawCommand(constr.forces, tempForce));
			
			}
		}
        class ObstaclesState extends State{
            @Override
            void onMouseMove(MotionEvent event, int action) {
                cursor.set((int)event.getX(), (int)event.getY());
                scene.setCursorPosition(scene.transformFromCanvasX(cursor.x), scene.transformFromCanvasY(cursor.y));
                if(!erasingMode){
                    PointD cursorPosition=new PointD(scene.transformFromCanvasX(cursor.x), scene.transformFromCanvasY(cursor.y));
                    tempObstacle = new Obstacle(tempObstacle.x1,tempObstacle.y1,cursorPosition.x,cursorPosition.y);

                }

            }

            @Override
            void onMouseDown(MotionEvent event, int action) {
                cursor.set((int)event.getX(), (int)event.getY());
                PointD cursorPosition=new PointD(scene.transformFromCanvasX(cursor.x), scene.transformFromCanvasY(cursor.y));
                scene.setCursorPosition(scene.transformFromCanvasX(cursor.x), scene.transformFromCanvasY(cursor.y));
                if(erasingMode) {
                    for (Obstacle o1 : constr.obstacles) {
                        if (o1.isPointInside(cursorPosition))
                            commandQueue.issueCommand(new ObstacleEraseCommand(constr.obstacles, o1));
                        break;
                    }
                }
                else {
                    tempObstacle = new Obstacle(cursorPosition.x, cursorPosition.y,cursorPosition.x,cursorPosition.y);
                    drawingObstacle=true;
                }

            }

            @Override
            void onMouseUp(MotionEvent event, int action) {
                if(drawingObstacle){
                     commandQueue.issueCommand(new ObstacleDrawCommand(constr.obstacles,tempObstacle));
                    drawingObstacle=false;
                }
            }
        }
        class CalculationsState extends State{
            @Override
            void onMouseDown(MotionEvent event, int action){
                onTouchToMainViewInterface.stopAnimation();
                Log.wtf("Boo","Boo");
            }

            @Override
            void onStateChanged(int newState) {
                //constr.
            }
        }
	}
	void RodsDrawing(MotionEvent event, int action)	{
		eMode.processMouseEvent(event, action);
	}
	void ScalingAndMoving(MotionEvent event, int action)	{
		if(action == MotionEvent.ACTION_MOVE)	{
			drawingRod = false;
			drawingForce = false;
			updateGesturePointers(event);
			if(pinchScalingOld > 0.01) {
				scaleScene();
				moveScene();
			}
			moveNewGesturePointersToOld();
		}
		if(action == MotionEvent.ACTION_POINTER_UP)
			pinchScalingOld = 0;	
	}
	boolean OnScreenMenu(MotionEvent event, int action) 	{
		int code = menu.onTouchOnMenu(event, action);

		if(code == 0) return true;

		if(action == MotionEvent.ACTION_DOWN)	{
			if(code ==6 ) {

			}
		 if(code == 8) commandQueue.undo();
		 if(code == 9) commandQueue.redo();
            if(code == 10) onTouchToMainViewInterface.saveDialog();;
            if(code==11) onTouchToMainViewInterface.readDialog();;

		}
		if(code < 8 && code > 0)
		    eMode.setState(code-1);
		return false;
		
	}
	private void scaleScene()	{
		if(scale * pinchScaling / pinchScalingOld < SCALE_MAX && scale * pinchScaling / pinchScalingOld > SCALE_MIN)	{
			scale *= pinchScaling / pinchScalingOld;
			zero.add(-(double)(pinchDrag.x - scene.getSizeX() / 2) / (int)scale *( 1 - (pinchScalingOld / pinchScaling)), 
				+(double)(pinchDrag.y - scene.getSizeY()/ 2) / (int)scale * (1 - (pinchScalingOld / pinchScaling)));
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
    void parseSignalFromDrawer(int signal){

    }
    void setCommandQueue(CommandQueue cq) {
        commandQueue = cq;
        cq.restoreIterator();;
    }
}

