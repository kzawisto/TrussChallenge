package com.example.krystian892.truss;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import com.example.krystian892.truss.Construction.ArrayListOfJoints;
import com.example.krystian892.truss.calculations.PointD;
public class CommandQueue {
	LinkedList<Command> list;
	ListIterator<Command> it;
	static int maxSize = 1000;
	CommandQueue(){
		list = new LinkedList<Command>();
		it = list.listIterator();
	}
	void issueCommand(Command c){
		c.execute();
		it.add(c);
		if(list.size() > maxSize)
			if(it.hasNext()) list.removeLast();
			else list.removeFirst();
	}
	void undo(){
		if(it.hasPrevious()){
			it.previous().undo();
		}
	}
	void redo(){
		if(it.hasNext()){
			it.next().execute();
		}
	}
	
}
abstract class Command{
	abstract void undo();
	abstract void execute();
}

class RodDrawCommand extends Command{
	ArrayList<Rod> rods;
	Rod r;
	RodDrawCommand(ArrayList<Rod> rods, Rod r){
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
class RodEraseCommand extends Command{
	ArrayList<Rod> rods;
	Rod r;
	RodEraseCommand(ArrayList<Rod> rods, Rod r){
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

class SupportPlaceCommand extends Command{
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
class SupportRemoveCommand extends Command{
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
class ForceVectorDrawCommand extends Command{
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
class ForceVectorEraseCommand extends Command{
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
