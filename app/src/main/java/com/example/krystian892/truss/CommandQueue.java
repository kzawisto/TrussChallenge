package com.example.krystian892.truss;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import com.example.krystian892.truss.Construction.ArrayListOfJoints;
import com.example.krystian892.truss.calculations.PointD;
public class CommandQueue implements Serializable{
	LinkedList<Command> list;
	transient ListIterator<Command> it;
    Command now = null;
	static int maxSize = 1000;
	CommandQueue(){
		list = new LinkedList<Command>();
		it = list.listIterator();
	}
	void issueCommand(Command c){

		c.execute();
       it.add(c);
        now = c;
		if(list.size() > maxSize) {

           // Log.wtf("Remove","Remove");
            if (it.hasNext()) list.removeLast();
            else list.removeFirst();
        }
	}
	void undo(){

		if(it.hasPrevious()){

            (now= it.previous()).undo();
		}
	}
	void redo(){
      //  Log.wtf("Boo", "boo");
		if(it.hasNext()){
            (now=it.next()).execute();
		}
	}
    void restoreIterator(){
        if(now !=null)
            it=list.listIterator(list.lastIndexOf(now));


    }


	
}
abstract class Command implements  Serializable{
	abstract void undo();
	abstract void execute();
}

class RodDrawCommand extends Command implements  Serializable{
	//ArrayList<Rod> rods;
    Construction constr;
	Rod r;
	RodDrawCommand(Construction constr, Rod r){
		this.constr= constr;
		this.r = r;
	}
	@Override
	void undo() {
		constr.removeRod(r);
	}
	@Override
	void execute() {
		constr.addRod(r);
	}
	
}
class RodEraseCommand extends RodDrawCommand implements  Serializable{
	ArrayList<Rod> rods;
	Rod r;
	RodEraseCommand(Construction constr, Rod r){
		super(constr,r);
	}
	@Override void execute() {super.undo();}
	@Override void undo() {
		super.execute();
	}
}

class SupportPlaceCommand extends Command implements  Serializable{
	ArrayListOfJoints support;
    Joint s;
	public SupportPlaceCommand(ArrayListOfJoints support, PointD s) {
		super();
		this.support = support;
		this.s = new Joint(s);
	}
	@Override
	void undo() {
		support.tab.remove(s);
		
	}
	@Override
	void execute() {
		support.add(s);
	}
}
class SupportRemoveCommand extends Command implements  Serializable{
	ArrayListOfJoints support;
    Joint s;
	public SupportRemoveCommand(ArrayListOfJoints support, PointD s) {
		super();
		this.support = support;
		this.s = new Joint(s);
	}
	@Override
	void undo() {
		support.add(s);
	}
	@Override
	void execute() {
		support.removePoint(s);
	}
}
class ForceVectorDrawCommand extends Command implements  Serializable{
	ArrayList<ForceVector> rods;
	ForceVector r;
	ForceVectorDrawCommand(ArrayList<ForceVector> rods, ForceVector r){
		this.rods = rods;
		this.r = r;
	}
	@Override
	void undo() {
		rods.remove(r);
		
	}
	@Override
	void execute() {
		rods.add(r);
	}
	
}
class ForceVectorEraseCommand extends Command implements  Serializable{
	ArrayList<ForceVector> rods;
	ForceVector r;
	ForceVectorEraseCommand(ArrayList<ForceVector> rods, ForceVector r){
		this.rods = rods;
		this.r = r;
	}
	@Override
	void execute() {
		rods.remove(r);
		
	}
	@Override
	void undo() {
		rods.add(r);
	}
}
class ObstacleDrawCommand extends Command implements Serializable{
    ArrayList<Obstacle> obstacles;
    Obstacle o;
    ObstacleDrawCommand(ArrayList<Obstacle> obstacles, Obstacle o){
        this.o=o;
        this.obstacles=obstacles;
    }

    @Override
    void undo() {
       obstacles.remove(o);

    }
    @Override
    void execute() {
        obstacles.add(o);
    }
}

class ObstacleEraseCommand extends ObstacleDrawCommand implements Serializable{
    ArrayList<Obstacle> obstacles;
    Obstacle o;
    ObstacleEraseCommand(ArrayList<Obstacle> obstacles, Obstacle o) {
        super(obstacles,o);
    }
        @Override
    void execute() {
        super.undo();

    }
    @Override
    void undo() {
        super.execute();
    }
}
