
package com.example.krystian892.truss.calculations;

import android.util.Log;

public class Matrix {
	int n,m;
	public double array[][];
	public Matrix(int n, int m){
		this.n = n;
		this.m = m;
		array = new double[n][m];
	}
	public void fillWithZeros(){
		for(int a =0;a < n;++a)
			for(int b=0;b < m;++b)
				array[a][b] = 0;
	}
	Matrix(Matrix m1){
		this(m1.n, m1.m);
		for(int a =0;a < n;++a)
			for(int b=0;b < m;++b)
				array[a][b] = m1.array[a][b];
	}
	public static Matrix cauchyProduct(Matrix m1, Matrix m2)	{
		if(m1.n != m2.m){
			Log.e("ERROR", "WRONG DIMMENSIONS ");
			return new Matrix(0,0);
			
		}
		String s = "";
		Matrix result = new Matrix(m2.n, m1.m);
		result.fillWithZeros();
		for(int a = 0;a<m2.n;++a )
			for(int b=0;b<m1.m;++b){
			//	s = "";
				for(int c =0;c<m1.n;++c){
					result.array[a][b]+=m1.array[c][b] * m2.array[a][c];
					//Log.e(""+m1.array[c][b],"" + m2.array[a][c]);
				//	s+="   "+String.format("%.3f",m1.array[c][b]) + "*" + String.format("%.3f",m2.array[a][c]);
				}
				//s+= " ="+ String.format("%.3f * %.3f",result.array[a][b]);
				//Log.e(""+b,s );
			}
		return result;
	}
	public static Matrix example(){
		Matrix m1 =new Matrix(4,4);
		m1.fillWithZeros();
		m1.array[1][0] = -1;
		m1.array[2][0] = 2;
		m1.array[3][0] = 3;
		m1.array[0][1] = 1;
		m1.array[1][1] = 4;
		m1.array[3][2] = 1;
		m1.array[2][3] = 1;
		m1.display();
		return m1;
	}
	public Matrix transposition(){
		Matrix m1 = new Matrix(m,n);
		for(int a =0;a< n;++a)
			for(int b = 0;b <m;++b)
				m1.array[b][a] = array[a][b];
		return m1;
	}
	public String display(){
		String s1="";
		for(int a =0;a <n;++a){
		String s = "";
		for(int b =0;b<m;++b)s+=String.format("%.3f",array[a][b])+" ";
		//Log.wtf("row " + a,s);
            s1+=s+"\n";
		}
		return s1;
		
	}
	public void displayMaxima(){
		String s = "matrix(";
		for(int a =0;a <n;++a){
		s+="[";
		for(int b =0;b<m;++b)s+=String.format("%.3f",array[a][b])+";";
		s+="];";
		}
		//Log.wtf("matrix",s);
		
	}
	
	
}
