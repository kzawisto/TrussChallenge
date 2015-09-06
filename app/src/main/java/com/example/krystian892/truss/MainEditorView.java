package com.example.krystian892.truss;

import com.example.krystian892.truss.calculations.Point;
import com.example.krystian892.truss.calculations.PointD;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainEditorView extends View implements OnTouchListener, ViewRefreshing, OnTouchToMainViewInterface {
	//private final GameScreen game;
	PointD   zero = new PointD(7,12);
	PointD cursor = new PointD(0,0), indicator=new PointD(0,0);
	Rod tempRod = new Rod(new PointD(0,0), new PointD(0,0));
	Construction constr = new Construction();
    String text;
    //boolean prohibited = false;
    boolean lockTouch = false;
    int indicatorDrawMode=0;
	OnScreenMenu menu = new OnScreenMenu(20,100);
    boolean draw_available=true;
    boolean stopAnim=false;

	Scene scene = new Scene();
	OnTouch onTouch = new OnTouch(zero,menu, scene ,constr, this);
	int mode = 2; 
	public MainEditorView(Context context) {
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
        scene.updateSize(w,h);
		int size = Math.min(scene.size.x, scene.size.y);
		scene.transDarkGrayText.setTextSize(size /26);
		scene.transDarkGrayText.setTextScaleX((float) 1.3);
		menu.setSize(size / 11, (int) (size /4.3));
		menu.setCorner((int) (scene.size.x - 1.1 * menu.buttonWidth), (int)( 0.2 * menu.buttonHeight));
		super.onSizeChanged(w, h, oldw, oldh);
	}

	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		return false;		
	}
	public boolean onTouchEvent(MotionEvent event)	{

            int action = event.getActionMasked();
            if (event.getPointerCount() == 2) onTouch.ScalingAndMoving(event, action);
            else if (event.getPointerCount() == 1) {
                if (onTouch.OnScreenMenu(event, action))
                    onTouch.RodsDrawing(event, action);
            }
            cursor.set((double) event.getX(), (double) event.getY());
            invalidate();

		return true;
		
	}
	protected void onDraw(Canvas canvas) {
        if(draw_available) {
            scene.drawGrid(canvas);
            scene.drawCursorPosition(canvas);
            if (onTouch.drawingRod) scene.drawRod(onTouch.tempRod, canvas);
            if (onTouch.drawingForce) scene.drawForce(onTouch.tempForce, canvas);
            if(onTouch.drawingObstacle) scene.drawProhibitedRegion(onTouch.tempObstacle,canvas);
            for (Rod e : constr.rods) scene.drawRod(e, canvas);
            for (ForceVector e : constr.forces) scene.drawForce(e, canvas);
            for (PointD e : constr.supports.tab) scene.drawSupport(e, canvas);
            for(Obstacle o:constr.obstacles) scene.drawProhibitedRegion(o,canvas);
            scene.drawMenu(canvas);
            //scene.drawProhibitedRegion(scene.ph,canvas);
           // scene.drawSupport(scene.ph.p1,canvas);
            //scene.drawSupport(scene.ph.p2,canvas);

            //canvas.drawText(indicator.toStringInt(),sce
           // scene.drawSupport(scene.ph.p1,canvas);
            //scene.drawSupport(scene.ph.p2,canvas);

            //canvas.drawText(indicator.toStringInt(),scene.size.x *0.05f,scene.size.x *0.05f,scene.black);
            drawIndicator(canvas);
        }

	}
    void drawIndicator(Canvas canvas){
        String str=indicator.toStringInt();//+ " " + new Boolean(prohibited).toString();
       /* if(prohibited){

        }*/
        if(indicatorDrawMode==0)
        canvas.drawText(str,scene.size.x *0.05f,scene.size.y *0.05f,scene.black);
        if(indicatorDrawMode==1)
            canvas.drawText(indicator.toStringInt(),scene.size.x *0.05f,scene.size.y *0.05f,scene.gray);
        if(indicatorDrawMode==2)
            canvas.drawText(indicator.toStringInt(),scene.size.x *0.05f,scene.size.y *0.05f,scene.supportBlue);
    }
    void drawProgressCounter(Canvas canvas){
        if(constr.calculationsStarted) {
            canvas.drawText(indicator.toStringInt(), scene.size.x * 0.05f, scene.size.x * 0.05f, scene.supportBlue);
        }
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
		//int size_x =( canvas.getWidth()/6);
		//int size_y = canvas.getHeight();
		//Bitmap b = Bitmap.createBitmap(size_x, size_y,Bitmap.Config.ARGB_8888);
	}
	public void simulation(){

        constr.simulation();
    }
    public void saveDialog(){
        Log.wtf("EXPORT",constr.exportString());
        AlertDialog.Builder builder = new AlertDialog.Builder((MainActivity2)ActivitySingleton.getActivity());
        builder.setTitle("Set filename");

// Set up the input
        final EditText input = new EditText((MainActivity2)ActivitySingleton.getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                text = input.getText().toString();
               // constr.saveToFile(text);
                saveConstrToFile(text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void triggerAnimation() {
        final Handler handler = new Handler();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(!sp.getBoolean("deflection_enabled",false)) return;
        final double scale = (double)sp.getInt("deflection",10)/10.0;

        lockTouch=true;
        for(Rod r:constr.rods) r.copyForce();
        final int animationLength= (int)(sp.getInt("animation_time",150)/2);

       //Log.wtf("Reading ", "value " + animationLength);
        final double singleDisplacement = 0.0005 * 100.0/ animationLength;
                Runnable runnable = new Runnable() {
             int i =0;
            @Override
            public void run() {

                invalidate();
                i++;
                constr.setDisplacement(singleDisplacement*scale);
                constr.setPartialForce(((double)i/animationLength));
                if(i<animationLength && !stopAnim)
                    handler.postDelayed(this, 30);
                else{
                    stopAnim = false;
                    constr.setDisplacement(-singleDisplacement*i*scale);
                    constr.setPartialForce(1.0);
                    lockTouch=false;
                }

            }
        };
        handler.postDelayed(runnable, 0);

    }

    @Override
    public void stopAnimation() {
        stopAnim=true;
    }

    @Override
    public void updateIndicator(double x, double y,double x_anchor, double y_anchor,int state) {
        //int y = scene.size.y*0.95,x = scene.size.x*0.05;

        indicatorDrawMode=state;
        indicator.set(x,y);
        //prohibited =
      //  prohibited = scene.ph.isVectorIntersecting(new Vector(new PointD(x_anchor,y_anchor),new PointD(x,y)));
    }

    public void readDialog(){
        draw_available = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySingleton.getActivity());
        builder.setTitle("Set filename");

// Set up the input
        final EditText input = new EditText(ActivitySingleton.getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                text = input.getText().toString();

                setConstr(readConstrFromFile(text));//ConstructionLoader.readFromFile(text));
                   for(Rod r:constr.rods) {
                       r.paint = null;
                       r.force =0;
                   }
                draw_available = true;
                invalidate();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                draw_available= true;
                dialog.cancel();

            }
        });


        builder.show();
    }
    void setConstr(Construction constr){
        this.constr=constr;
        onTouch.constr = constr;
    }
    void saveConstrToFile(String path){
        constr.saveToFile(path);
    }
    Construction readConstrFromFile(String path){
        return ConstructionLoader.readFromFile(path);
    }
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



class GameView extends MainEditorView{
    String levelname;
    CostIndicator cost;

    final Handler myHandler = new Handler();
    final Runnable myRunnable = new Runnable() {
        public void run() {
            invalidate();
        }
    };
    void updateGUI(){
        myHandler.post(myRunnable);
    }
    public GameView(Context context,String levelname) {
        super(context);
        menu = new GameOnScreenMenu(20,100);

        scene.setMenu(menu);
        this.levelname=levelname;
        onTouch.menu=menu;

    }

    @Override
    void saveConstrToFile(String path){
        constr.saveToFile(path,this.levelname);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cost.resize(Math.max(w,h)/50, w*3/50);
    }

    @Override
    Construction readConstrFromFile(String path){
        Log.e("Gameview", "read");
        return ConstructionLoader.readFromFile(path,this.levelname);
    }
    @Override
    void setConstr(Construction constr){
        super.setConstr(constr);
        int size =(scene.size.x / 50);

        cost = new CostIndicator(constr);
    }
    @Override
    public void simulation() {
        super.simulation();
        Log.wtf("MAX FORCE", Double.toString(Rod.maxforce));

        doNotificationAnimations();

    }
    void doNotificationAnimations(){
        final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.schedule(new Runnable(){
            @Override
            public void run(){
                cost.setNotification(Rod.maxforce);
                updateGUI();
            }
        }, 1, TimeUnit.SECONDS);
        exec.schedule(new Runnable(){
            @Override
            public void run(){
                cost.displayStrength=true;
                updateGUI();
            }
        }, 2, TimeUnit.SECONDS);
        exec.schedule(new Runnable(){
            @Override
            public void run(){
                cost.displayCostResult=true;
                updateGUI();
            }
        }, 3, TimeUnit.SECONDS);
        exec.schedule(new Runnable(){
            @Override
            public void run(){
                cost.displayNotification=false;
                cost.displayStrength=false;
                cost.displayCostResult=false;
                updateGUI();
            }
        }, 11, TimeUnit.SECONDS);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       // PointD p = new PointD(scene.size.x*0.05f, scene.size.y*0.07f);
                cost.draw(canvas, scene.size);
        //canvas.drawText();
    }
}

class  CostIndicator{
    Construction construction;
    int sizeSmall =30,sizeBig=80;
    Paint gold=new Paint(),plat=new Paint(),silver=new Paint(), bronze=new Paint(),black=new Paint(),red=new Paint();
    boolean displayNotification=false, displayStrength=false, displayCostResult = false;
    Paint goldBig=new Paint(),platBig=new Paint(),silverBig=new Paint(), bronzeBig=new Paint(),blackBig=new Paint(),redBig=new Paint(),
    greenBig =new Paint();
    Paint currentPaint = black;
    Paint testPaint, resiliencePaint, costPaint;
    String currentString, result1, result2, result3;
    void selectPaint(Paint paint, String str){
      //  currentString += str;
        currentPaint = paint;
    }
    CostIndicator(Construction construction){

        gold.setColor(Color.argb(255, 255, 222, 0));

        gold.setTextSize(sizeSmall);
        plat.setColor(Color.argb(255, 190, 190, 230));
        plat.setTextSize(sizeSmall);
        silver.setColor(Color.argb(255, 162, 162, 162));
        silver.setTextSize(sizeSmall);
        bronze.setColor(Color.argb(255, 188, 140, 0));
        bronze.setTextSize(sizeSmall);
        red.setColor(Color.argb(255,255, 0, 0));
        red.setTextSize(sizeSmall);
        goldBig.setColor(Color.argb(255, 255, 222, 0));
        goldBig.setTextSize(sizeBig);
        platBig.setColor(Color.argb(255, 190, 190, 230));
        platBig.setTextSize(sizeBig);
        silverBig.setColor(Color.argb(255, 162, 162, 162));
        silverBig.setTextSize(sizeBig);
        bronzeBig.setColor(Color.argb(255, 188, 140, 0));
        bronzeBig.setTextSize(sizeBig);
        redBig.setColor(Color.argb(255,255, 0, 0));
        redBig.setTextSize(sizeBig);
        greenBig.setColor(Color.argb(255,0, 255,0));
        greenBig.setTextSize(sizeBig);
        this.construction = construction;
    }
   void draw(Canvas canvas, Point position){
      // int w=canvas.getWidth(), h = canvas.getHeight();
       double cc = construction.currentCost, mc = construction.maxCost;

       currentPaint = black;
       currentString = "Cost:"+Double.toString(Math.floor(construction.currentCost)) + " / " + Double.toString(construction.maxCost)+" ";
       if(cc <= 0.7*mc) selectPaint(plat,"Perfect");
       else if(cc <= 0.8*mc) selectPaint(gold,"Excellent");
       else if(cc <= 0.9*mc) selectPaint(silver,"Good");
       else if(cc < mc+1) selectPaint(bronze,"Fair");
       else  selectPaint(red,"Exceeded");
       canvas.drawText(currentString, (float)position.x*0.05f,(float)position.y*0.05f+ sizeSmall,currentPaint);
        if(displayNotification)
            canvas.drawText(result1, (float)position.x*0.05f,(float)position.y*0.75f,testPaint);
       if(displayStrength)
            canvas.drawText(result2, (float)position.x*0.05f,(float)position.y*0.75f+1.5f*sizeBig,resiliencePaint);
       if(displayCostResult)
            canvas.drawText(result3, (float)position.x*0.05f,(float)position.y*0.75f+3*sizeBig,costPaint);


   }
   void setNotification(double maxForce){
       result1 ="Test failed"; testPaint = redBig;
       double cc = construction.currentCost, mc = construction.maxCost;
       if(cc <= mc && maxForce < 4.0){
           result1 = "Test passed"; testPaint = greenBig;
       }
       setResultForResilience(maxForce,4.0);
       setResultForCost(cc,mc);
       final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        displayNotification = true;



       //if(construction.currentCost < )

   }
   void setResultForResilience(double maxForce, double forceLimit){
       double score =forceLimit/maxForce;
       result2 = "Strength:" +Double.toString( Math.round(100*score))+"%, ";
       if(score < 1.0) { result2  += "Failure :("; resiliencePaint = redBig;}
       if(score >= 1.0 && score < 1.15) { result2  += "Fair."; resiliencePaint = bronzeBig;}
       if(score >= 1.15 && score < 1.3) { result2  += "Good!"; resiliencePaint = silverBig;}
       if(score >= 1.3 && score < 1.5) { result2  += "Excellent!"; resiliencePaint = goldBig;}
       if(score >= 1.5 ) { result2  += "Outstanding!!!"; resiliencePaint = platBig;}

   }
    void setResultForCost(double cost, double costLimit){
        double score =cost/costLimit;
        result3 = "Cost:" +Double.toString( Math.round(100*score))+"%, ";
        if(score > 1.0) { result3  += "Failure :("; costPaint = redBig;}
        if(score <= 1.0 && score > 0.9) { result3  += "Fair."; costPaint = bronzeBig;}
        if(score <= 0.9 && score > 0.75) { result3  += "Good!"; costPaint = silverBig;}
        if(score <= 0.75 && score > 0.6) { result3  += "Excellent!"; costPaint = goldBig;}
        if(score <=0.6 ) { result3  += "Outstanding!!!"; costPaint = platBig;}

    }
    void resize(int size1, int size2){
        this.sizeBig = size2;
        this.sizeSmall = size1;
        gold.setTextSize(sizeSmall);
        plat.setTextSize(sizeSmall);
        silver.setTextSize(sizeSmall);
        bronze.setTextSize(sizeSmall);
        red.setTextSize(sizeSmall);
        goldBig.setTextSize(sizeBig);
        platBig.setTextSize(sizeBig);
        silverBig.setTextSize(sizeBig);
        bronzeBig.setTextSize(sizeBig);
        redBig.setTextSize(sizeBig);
        greenBig.setTextSize(sizeBig);
    }
}

