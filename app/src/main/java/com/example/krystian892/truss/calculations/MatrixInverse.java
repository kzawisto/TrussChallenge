package com.example.krystian892.truss.calculations;


public class MatrixInverse extends Matrix {
	Matrix result, cpy;
	PositionTable pos;
	public MatrixInverse(Matrix m){
		super(m);
		result = new Matrix(n,n);
		pos = new PositionTable(n);
		for(int a =0;a<n;++a) result.array[a][a] = 1;
		makeTriangular();
		triangularToDiagonal();
		diagonalToUnit();
		fixPositions();
		//display();
	}
	Matrix inverse()	{
		Matrix result = new Matrix(n,n);
		PositionTable pos = new PositionTable(n);
		
		return result;
	}
	public Matrix result(){
		return result;
	}
	public String display(){
		String s1="";
		for(int a =0;a <n;++a){
		String s = "";
		for(int b =0;b<n;++b)s+=String.format("%.3f", result.array[b][pos.tab[a] ])+" ";
		//Log.wtf("",s);
			s1+=s+"\n";
		}
		return  s1;
	}
	void makeTriangular()	{
		for(int a=0; a<n;++a) {
			int c=a;
			for(int b =0; b<n;++b) if(Math.abs(array[pos.tab[b]][a]) >= Math.abs(array[pos.tab[c]][a])) c =b;
			pos.swap(c, a);
			for(int b = a+1; b<n;++b){
			  double ratio = -array[pos.tab[a]][b] /array[pos.tab[a]][a];
			
			 // array[pos.tab[b]][a] = 0;
			  for(int i = 0;i < n;++i){
				  array[pos.tab[i]][b] += ratio * array[pos.tab[i]][a];
				  result.array[pos.tab[i]][b] += ratio * result.array[pos.tab[i]][a];
			  }
			}	
		}
	}
	void triangularToDiagonal() {
		 for(int a = n-1;a>=0;--a) {
			 for(int b =a-1; b>= 0;--b) {
				 double ratio = -array[pos.tab[a]][b] / array[pos.tab[a]][a];
			  		for(int i = 0;i < n;++i){
			  			array[pos.tab[i]][b] += ratio * array[pos.tab[i]][a];
			  			result.array[pos.tab[i]][b] += ratio * result.array[pos.tab[i]][a];
					 
			  		}
			 }
		 }
	}
	void diagonalToUnit()	{
		for(int a =0;a<n;++a)
			for(int c =0; c<n;++c)
				result.array[c][a] /=array[pos.tab[a]][a];
	}
	void fixPositions(){
		Matrix m1 = new Matrix(result.m, result.n);
		for(int a =0;a < m1.n;++a)
			for(int b =0;b < m1.m;++b)
				m1.array[a][pos.tab[b]] = result.array[a][b];
		result = m1;
	}
	private class PositionTable
	{
		
		int tab[];
		PositionTable(int n){
			tab = new int[n];
			for(int b=0;b <n;++b)
				tab[b]= b;
		
		}
		void swap(int a, int b) {
			int temp =tab[a];
			tab[a]= tab[b];
			tab[b] = temp;
		}
		
		
	}
}
