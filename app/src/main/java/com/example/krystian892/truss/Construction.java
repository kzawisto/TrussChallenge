package com.example.krystian892.truss;

import android.widget.Toast;

import java.util.ArrayList;

import com.example.krystian892.truss.calculations.*;

public class Construction {
	ArrayList<Rod> rods = new ArrayList<Rod>(500);
	ArrayListOfJoints supports = new ArrayListOfJoints();;
	ArrayListOfJoints joints = new ArrayListOfJoints();
	ArrayList<ForceVector> forces = new ArrayList<ForceVector>();

	public boolean calculationsStarted = false;
	void addRod(Rod r)	{
		rods.add(r);
	}
	
	Matrix transitionsMatrix(){
		Matrix m1 = new Matrix(rods.size(), joints.size()*2);
		m1.fillWithZeros();
		for(int j = 0;j < rods.size();++j){
		//	Log.e("ids",""+ rods.get(j).id1+ "  " + rods.get(j).id2);
			if(rods.get(j).id1 > -1) {
				m1.array[j][rods.get(j).id1*2] = rods.get(j).cosPhi();
				m1.array[j][rods.get(j).id1*2 + 1] = rods.get(j).sinPhi();
			}
			if(rods.get(j).id2 > -1) {
			m1.array[j][rods.get(j).id2*2] = -rods.get(j).cosPhi();
			m1.array[j][rods.get(j).id2*2 + 1] = -rods.get(j).sinPhi();
			}
		}
		
		return m1;
	}
	Matrix calculateLoadMatrix(){
		Matrix m1 = new Matrix(1, joints.size()*2);
		m1.fillWithZeros();
		for(ForceVector f : forces){
			f.jointId = joints.findPointLike(f.start);
			m1.array[0][f.jointId*2] = f.end.x-f.start.x;
			m1.array[0][f.jointId*2+1] = f.end.y-f.start.y;
		}
		//Log.wtf("LOADS","LOADS");
		//sm1.display();
		return m1;
	}
	void doCalculations(){
		Matrix m1 = transitionsMatrix(), m2 = m1.transposition(),m3,m4,tensions, transitions;
	//	m2.display();
		//Matrix.cauchyProduct(m2,m1).display();
		for(int a =0; a< rods.size();++a)
			for(int b = 0; b< joints.size() * 2;++b)
				m2.array[b][a] /= (rods.get(a).length()/100.0);
	
		Matrix.cauchyProduct(m1,m2).displayMaxima();
	
		m3 = (new MatrixInverse(Matrix.cauchyProduct(m1,m2))).result();
		
		//m4 = Matrix.cauchyProduct(m2, m3);
		transitions = Matrix.cauchyProduct(m3,calculateLoadMatrix());
		transitions.display();
		
		tensions = Matrix.cauchyProduct(m2, transitions);

		for(int a =0 ;a< rods.size();++a)
			rods.get(a).setForce( tensions.array[0][a]);
	}
	void computeJoints()	{
		joints = new ArrayListOfJoints();
        int sup1, sup2;

		for(int i = 0; i < rods.size();++i)	{
            Joint joint1 = null, joint2 = null;
			if((sup1 = supports.findPointLike(rods.get(i).start)) == -1) {
				int id = joints.findPointLike(rods.get(i).start);
				if(id == -1) {
                    joint1=new Joint(rods.get(i).start);
					joints.add(joint1);
					id = joints.size()-1;
				}
                else joint1= joints.get(id);
				rods.get(i).id1 = id;

			}
			else rods.get(i).id1=-1- sup1;
			if((sup2=supports.findPointLike(rods.get(i).end)) == -1){
				int id =  joints.findPointLike(rods.get(i).end);
				if(id == -1)	{
                    joint2 = new Joint(rods.get(i).end);
					joints.add(joint2);
					id = joints.size()-1;
				}
                else joint2= joints.get(id);
				rods.get(i).id2 = id;
			}
			else rods.get(i).id2=-1-sup2;
            if(joint1 != null) joint1.addNeighbour(rods.get(i).id2);
            if(joint2 != null) joint2.addNeighbour(rods.get(i).id1);
		}
		//for(int i =0;i< joints.size();++i) Log.d(" joint "+i," "+ joints.get(i).x+" "+ joints.get(i).y);
	}
	class ArrayListOfJoints
	{
		ArrayList<Joint> tab = new ArrayList<Joint>(100);
		void add(Joint a) {tab.add(a);}
		Joint get(int i) {return tab.get(i);}
		int size() {return tab.size();}
		int findPointLike(PointD p)	{
			int id = -1;
			for(int i = 0;i < tab.size();++i) if(DoubleComp.isEqual(tab.get(i).x, p.x) 
					&& DoubleComp.isEqual(tab.get(i).y, p.y))return id = i;
			return id;
		}
		void removePoint(PointD p){
			int i = findPointLike(p);
			if(i >= 0 && i <= tab.size())
			tab.remove(i);
		}

	}
    public boolean jointTest(){
        for(Joint j : joints.tab) {
            if(j.neighbours.size() <2) {
                Toast.makeText(ActivitySingleton.getActivity(), "Truss is not static. There are loose rods.", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(j.neighbours.size() == 2){

            }
        }

        return true;
    }
	public void simulation() {
		// TODO Auto-generated method stub
		calculationsStarted = true;

		computeJoints();
      //  if(jointTest())
        Toast.makeText(ActivitySingleton.getActivity(), "Calculating.", Toast.LENGTH_SHORT).show();
        doCalculations();

        Toast.makeText(ActivitySingleton.getActivity(), "Done .", Toast.LENGTH_SHORT).show();
		
	}

    class RigidityTester{
        ArrayListOfJoints joints;
        ArrayList<ExRod> rods;

        RigidityTester(ArrayList<Rod> rods1, ArrayListOfJoints joints) {
            this.rods = new ArrayList<ExRod>();
            for(Rod r: rods1)
                rods.add(new ExRod(r));
            this.joints = joints;
        }
        int last_key=0;
        class ExRod{

            Rod r;
            int key;
            ExRod(Rod r) {
                this.r = r;
                key=last_key++;
            }
        }
        void iterateOverRods(){

        }

    }

}