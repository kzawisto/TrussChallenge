package com.example.krystian892.truss;

public abstract class Action {
	abstract void  undoAction(Construction constr);
	abstract void  redoAction(Construction constr);
	 public class AddRod extends Action {
		 Rod r;
		 AddRod(Rod r) {
			 this.r = r;
		 }
		 void undoAction(Construction constr){
			 constr.rods.remove(r);
		 }
		void redoAction(Construction constr) {
			constr.rods.add(r);
		}
	 }
	 public class EraseRod extends Action{
		 Rod r;
		 EraseRod(Rod r) {
			 this.r = r;
		 }
		void undoAction(Construction constr) {
			constr.rods.add(r);
			
		}

		@Override
		void redoAction(Construction constr) {
			constr.rods.remove(r);
			
		}
		 
	 }
	 //public class 
}
