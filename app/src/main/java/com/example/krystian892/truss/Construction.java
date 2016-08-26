package com.example.krystian892.truss;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.krystian892.truss.calculations.Matrix;
import com.example.krystian892.truss.calculations.MatrixInverse;
import com.example.krystian892.truss.calculations.PointD;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Construction implements Serializable {
    ArrayList<Rod> rods = new ArrayList<Rod>(500);
    ArrayListOfJoints supports = new ArrayListOfJoints();
    ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    ArrayListOfJoints joints = new ArrayListOfJoints();
    ArrayList<ForceVector> forces = new ArrayList<ForceVector>();
    double maxCost =0,currentCost =0;
    double displ=0;
    double maxforce =0;
    boolean rawres =false;
    String levelname;
   //transient double displacement;
    public boolean calculationsStarted = false;

    void addRod(Rod r) {
        rods.add(r);
        currentCost+= r.length()+1;
    }


    Matrix transitionsMatrix() {
        Matrix m1 = new Matrix(rods.size(), joints.size() * 2);

        m1.fillWithZeros();
        for (int j = 0; j < rods.size(); ++j) {
            //	Log.e("ids",""+ rods.get(j).id1+ "  " + rods.get(j).id2);
            if (rods.get(j).id1 > -1) {
                m1.array[j][rods.get(j).id1 * 2] = rods.get(j).cosPhi();
                m1.array[j][rods.get(j).id1 * 2 + 1] = rods.get(j).sinPhi();
            }
            if (rods.get(j).id2 > -1) {
                m1.array[j][rods.get(j).id2 * 2] = -rods.get(j).cosPhi();
                m1.array[j][rods.get(j).id2 * 2 + 1] = -rods.get(j).sinPhi();
            }
        }
        Log.wtf("Transition matrix",m1.display());
        return m1;
    }

    Matrix calculateLoadMatrix() {
        Matrix m1 = new Matrix(1, joints.size() * 2);
        m1.fillWithZeros();
        for (ForceVector f : forces) {
            f.jointId = joints.findPointLike(f.start);
            joints.get(f.jointId).addCopy(f.start);
            joints.get(f.jointId).addCopy(f.end);
            m1.array[0][f.jointId * 2] += f.end.x - f.start.x;
            m1.array[0][f.jointId * 2 + 1] += f.end.y - f.start.y;
        }
        //Log.wtf("LOADS","LOADS");
        Log.wtf("Load matrix",m1.display());
        return m1;
    }

    boolean doCalculations() {
        Matrix m1 = transitionsMatrix(), m2 = m1.transposition(), m3, m4, tensions, transitions;
        //	m2.display();
        //Matrix.cauchyProduct(m2,m1).display();
        for (int a = 0; a < rods.size(); ++a)
            for (int b = 0; b < joints.size() * 2; ++b)
                m2.array[b][a] /= (rods.get(a).length() / 100.0);

        m3 = (new MatrixInverse(Matrix.cauchyProduct(m1, m2))).result();
       // Log.wtf("m3 inverse", m3.display());
        //m4 = Matrix.cauchyProduct(m2, m3);
        transitions = Matrix.cauchyProduct(m3, calculateLoadMatrix());
        for(int a =0;a<joints.size();++a){
            if(Double.isNaN(transitions.array[0][2*a]) || Double.isNaN(transitions.array[0][2*a+1])) {


                Toast.makeText(ActivitySingleton.getActivity(), "Calculation error.",Toast.LENGTH_SHORT).show();
                return false;
            }
            joints.get(a).displacement.set(transitions.array[0][2*a], transitions.array[0][2*a+1]);
        }
        tensions = Matrix.cauchyProduct(m2, transitions);

        for (int a = 0; a < rods.size(); ++a) {
            if(Double.isNaN(tensions.array[0][a])) {
                Toast.makeText(ActivitySingleton.getActivity(), "Calculation error.",Toast.LENGTH_SHORT).show();
                return false;
            }
            rods.get(a).setForce(tensions.array[0][a]);
        }
        Rod.maxforce=0;
        for(Rod r:rods) if(Math.abs(r.force) > Rod.maxforce) Rod.maxforce = Math.abs(r.force);
        return true;
    }
    void resetDisplacement(){
        setDisplacement(displ);
        displ=0;
    }
    void computeJoints() {
        joints = new ArrayListOfJoints();
        int sup1, sup2;

        for (int i = 0; i < rods.size(); ++i) {
            Joint joint1 = null, joint2 = null;
            if ((sup1 = supports.findPointLike(rods.get(i).start)) == -1) {
                int id = joints.findPointLike(rods.get(i).start);
                if (id == -1) {
                    joint1 = new Joint(rods.get(i).start);
                    joints.add(joint1);
                    id = joints.size() - 1;
                } else joint1 = joints.get(id);
                rods.get(i).id1 = id;
                joints.get(id).addCopy(rods.get(i).start);

            } else rods.get(i).id1 = -1 - sup1;
            if ((sup2 = supports.findPointLike(rods.get(i).end)) == -1) {
                int id = joints.findPointLike(rods.get(i).end);
                if (id == -1) {
                    joint2 = new Joint(rods.get(i).end);
                    joints.add(joint2);
                    id = joints.size() - 1;
                } else joint2 = joints.get(id);
                rods.get(i).id2 = id;
                joints.get(id).addCopy(rods.get(i).end);
            } else rods.get(i).id2 = -1 - sup2;
            if (joint1 != null) joint1.addNeighbour(rods.get(i).id2);
            if (joint2 != null) joint2.addNeighbour(rods.get(i).id1);
        }
        //for(int i =0;i< joints.size();++i) Log.d(" joint "+i," "+ joints.get(i).x+" "+ joints.get(i).y);
    }

    public void removeRod(Rod r) {
        rods.remove(r);
        currentCost -= r.length()+1;
    }


    class ArrayListOfJoints implements Serializable {
        ArrayList<Joint> tab = new ArrayList<Joint>(100);

        void add(Joint a) {
            tab.add(a);
        }

        Joint get(int i) {
            return tab.get(i);
        }

        int size() {
            return tab.size();
        }

        int findPointLike(PointD p) {
            int id = -1;
            for (int i = 0; i < tab.size(); ++i)
                if (DoubleComp.isEqual(tab.get(i).x, p.x)
                        && DoubleComp.isEqual(tab.get(i).y, p.y)) return id = i;
            return id;
        }

        void removePoint(PointD p) {
            int i = findPointLike(p);
            if (i >= 0 && i <= tab.size())
                tab.remove(i);
        }

    }

    public boolean jointTest() {
        for (Joint j : joints.tab) {
            if (j.neighbours.size() < 2) {
                Toast.makeText(ActivitySingleton.getActivity(), "Truss is not static. There are loose rods.", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (j.neighbours.size() == 2) {

            }
        }

        return true;
    }

    public boolean simulation() {
        // TODO Auto-generated method stub
        calculationsStarted = true;

        computeJoints();
       if(jointTest())
       if( doCalculations()) return true;

        //setDisplacement(0.1);

       Toast.makeText(ActivitySingleton.getActivity(), "Calculation Error.", Toast.LENGTH_SHORT).show();
        return false;
    }

    class RigidityTester {
        ArrayListOfJoints joints;
        ArrayList<ExRod> rods;

        RigidityTester(ArrayList<Rod> rods1, ArrayListOfJoints joints) {
            this.rods = new ArrayList<ExRod>();
            for (Rod r : rods1)
                rods.add(new ExRod(r));
            this.joints = joints;
        }

        int last_key = 0;

        class ExRod {

            Rod r;
            int key;

            ExRod(Rod r) {
                this.r = r;
                key = last_key++;
            }
        }

        void iterateOverRods() {

        }

    }

    void saveToFile(String fileName) {
       saveToFile(fileName,"user");
    }
    void saveToFile(String fileName,String prefix){
        Context ctx = ActivitySingleton.getActivity().getApplicationContext();
        try {
            //fileName = "constructions/" + fileName;
            String root = Environment.getExternalStorageDirectory().toString();
            File fdir =new File(root + "/TrussSimulator/"+prefix);
            fdir.mkdirs();
            File f = new File(root + "/TrussSimulator/"+prefix+"/"+fileName);

            FileOutputStream fos = new FileOutputStream(f);
            //FileOutputStream fos = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
            BufferedOutputStream bos =new BufferedOutputStream(fos);
            ObjectOutputStream os = new ObjectOutputStream(bos);
            os.writeObject(this);
            os.close();
            bos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    String exportString(){
        String s = ""+ currentCost+"\n4.0\n"+supports.tab.size()+"\n";

        for(Joint j: supports.tab){
            s+= ""+j.x+" "+j.y+"\n";
        }
        s+= forces.size()+"\n";
        for(ForceVector j: forces)
            s+= ""+j.start.x+" "+j.start.y+" "+j.end.x+" "+j.end.y+ "\n";
        s+= obstacles.size()+"\n";
        for(Obstacle j: obstacles)
            s+= ""+j.p1.x+" "+j.p1.y+" "+j.p2.x+" "+j.p2.y+ "\n";
        return s;
    }

    public void setDisplacement(double displacement){
        displ +=displacement;
        for(Joint j : joints.tab) j.displaceCopies(displacement);
    }
    public void setPartialForce(double partial){
        for(Rod r:rods){
            r.force = r.force_copy * partial;
            r.resetPaintEx();
        }
    }

    boolean isRodAllowed(Rod r){
        for(Obstacle o:obstacles)if( o.isVectorIntersecting(r)) {
            //Log.wtf("Touched obstacle","cant add rod");
            return false;
        }
        for(Rod r1:rods)if(r1.start.dist(r.start) <1e-3 && r1.end.dist(r.end) <1e-3 ) {
            //Log.wtf("Another such rod exists","cant add rod");
            return false;
        }

        return true;
    }
}
class ConstructionLoader{
    public static Construction readFromFile(String fileName) {
        return readFromFile(fileName,"user");
      /*  //Construction construction = null;
        Construction construction=null;
        //   Toast.makeText(ActivitySingleton.getActivity(), "Reading "+fileName, Toast.LENGTH_SHORT).show();
        String root = Environment.getExternalStorageDirectory().toString();
        File f = new File(root + "/TrussSimulator/user/"+fileName);
        try{construction = readFromStream(new FileInputStream(f));}
        catch (Exception e){ Log.wtf("File Exception",e.toString());e.printStackTrace();}
        return construction;*/
    }
    public static Construction readFromFile(String fileName, String prefix){
        Construction construction=null;
        //   Toast.makeText(ActivitySingleton.getActivity(), "Reading "+fileName, Toast.LENGTH_SHORT).show();
        String root = Environment.getExternalStorageDirectory().toString();
        File f = new File(root + "/TrussSimulator/" +prefix+"/"+fileName);
        try{construction = readFromStream(new FileInputStream(f));}
        catch (Exception e){ Log.wtf("File Exception",e.toString());e.printStackTrace();}
        return construction;
    }
    public static Construction readFromAssets(String s) {
        Context ctx = ActivitySingleton.getActivity().getApplicationContext();
        AssetManager asm = ctx.getAssets();
        FileInputStream fis=null;
        Construction constr=null;
        try{constr=readFromStream(asm.open(s));}
        catch (Exception e){ Log.wtf("File Exception",e.toString());e.printStackTrace();}
        return constr;
    }
    public static Construction readPlainTextFromAssets(String s){
        return  null;
    }
    static private Construction readFromStream (InputStream fis){
        BufferedInputStream bis= null;
        ObjectInputStream is = null;
        Construction cons=null;
        try {

            bis = new BufferedInputStream(fis);
            assert (fis.available() > 0);
            is = new ObjectInputStream(bis);
            cons= (Construction) is.readObject();
            is.close();
            bis.close();
            fis.close();
            ;
        }
        catch (IOException e){
            Log.wtf("Input", "Error: "+ e.toString());

            e.printStackTrace();
        }
        catch (Exception e) {
            Log.wtf("Some other error",e.toString() );
            e.printStackTrace();
        }
        return cons;

    }
}
class GameConstruction extends Construction{

}
class TextResourceLoader {
    static Construction load(Context context,int id){
        Construction c =null;
        InputStream ins = context.getResources().openRawResource(id);
        try {
            c=readInputStream(ins);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.wtf("Error parsing raw/res", "IO Exception");
        }
        return c;
    }
    static public Construction readInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        String s ="";
        Construction constr = new Construction();
        int cost = Integer.parseInt(bufferedReader.readLine());;
        constr.maxCost =cost;
        constr.maxforce = Double.parseDouble(bufferedReader.readLine());
        int supports = Integer.parseInt(bufferedReader.readLine());
        for (int i = 0; i < supports; i++) {
            double[] tab = sliceStringIntoNumbers(bufferedReader.readLine());
            constr.supports.add(new Joint(tab[0],tab[1]));
        }
        int forces=Integer.parseInt(bufferedReader.readLine());
        for (int i = 0; i < forces; i++) {
            double[] tab = sliceStringIntoNumbers(bufferedReader.readLine());
            constr.forces.add(new ForceVector(new PointD(tab[0],tab[1]),new PointD(tab[2],tab[3])));
        }
        int obstacles = Integer.parseInt(bufferedReader.readLine());
        for (int i = 0; i < obstacles; i++) {
            double[] tab = sliceStringIntoNumbers(bufferedReader.readLine());
            constr.obstacles.add(new Obstacle(tab[0],tab[1],tab[2],tab[3]));
        }
        int rods = Integer.parseInt(bufferedReader.readLine());
        for (int i = 0; i < rods; i++) {{

        }
            double[] tab = sliceStringIntoNumbers(bufferedReader.readLine());
            constr.rods.add(new Rod(new PointD(tab[0], tab[1]), new PointD(tab[2], tab[3])));
        }
        return constr;
    }
    static public double[] sliceStringIntoNumbers(String s){
        String str[]=s.split(" ");
        double [] tab = new double[str.length];
        for (int i = 0; i < str.length; i++) tab[i]= Double.parseDouble(str[i]);
        return tab;
    }


}
