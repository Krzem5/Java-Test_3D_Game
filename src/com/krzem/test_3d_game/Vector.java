package com.krzem.test_3d_game;



public class Vector{
	public int x;
	public int y;



	public Vector(int x,int y){
		this.x=x;
		this.y=y;
	}



	public Vector clone(){
		return new Vector(this.x,this.y);
	}



	public Vector add(int x,int y){
		return new Vector(this.x+x,this.y+y);
	}



	@Override
	public String toString(){
		return String.format("Vector(x=%d,y=%d)",this.x,this.y);
	}
}