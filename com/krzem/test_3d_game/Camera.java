package com.krzem.test_3d_game;



import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import java.awt.Robot;
import java.lang.Exception;
import java.lang.Math;



public class Camera{
	public static final double MOVE_SPEED=0.5d;
	public static final double ROT_SPEED=0.1d;
	public static final double ZOOM_SPEED=0.5d;
	public Main.Main_ cls;
	public double fov;
	public double x;
	public double y;
	public double z;
	public double rx;
	public double ry;
	public double rz;
	public double zm;
	private Robot rb;
	private GLU glu=new GLU();



	public Camera(Main.Main_ cls){
		this.cls=cls;
		try{
			this.rb=new Robot(this.cls.SCREEN);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		this.fov=70d;
		this.x=0d;
		this.y=0d;
		this.z=0d;
		this.rx=0d;
		this.ry=0d;
		this.rz=0d;
		this.zm=1d;
		this.rb.mouseMove(this.cls.WINDOW_SIZE.width/2,this.cls.WINDOW_SIZE.height/2);
	}



	public void setup(GL2 gl){
		gl.glViewport(0,0,this.cls.WINDOW_SIZE.width,this.cls.WINDOW_SIZE.height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		this.glu.gluPerspective((float)this.fov,(float)this.cls.WINDOW_SIZE.width/(float)this.cls.WINDOW_SIZE.height,0.05,1000);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}



	public void update(){
		if (this.cls.KEYBOARD.pressed(16)){
			this.y+=MOVE_SPEED;
		}
		if (this.cls.KEYBOARD.pressed(32)){
			this.y-=MOVE_SPEED;
		}
		this.rx+=(this.cls.WINDOW_SIZE.height/2-this.cls.MOUSE_POS.y)*ROT_SPEED;
		this.ry+=(this.cls.WINDOW_SIZE.width/2-this.cls.MOUSE_POS.x)*ROT_SPEED;
		this.rx=Math.max(Math.min(this.rx,90),-90);
		this.ry=(this.ry+360)%360;
		double dx=Math.sin(-this.ry/180d*Math.PI)*MOVE_SPEED;
		double dz=Math.cos(-this.ry/180d*Math.PI)*MOVE_SPEED;
		if (this.cls.KEYBOARD.pressed(87)){
			this.x-=dx;
			this.z+=dz;
		}
		if (this.cls.KEYBOARD.pressed(83)){
			this.x+=dx;
			this.z-=dz;
		}
		if (this.cls.KEYBOARD.pressed(65)){
			this.x+=dz;
			this.z+=dx;
		}
		if (this.cls.KEYBOARD.pressed(68)){
			this.x-=dz;
			this.z-=dx;
		}
		this.zm+=-this.cls.SCROLL_D*ZOOM_SPEED;
		this.zm=Math.max(Math.min(10,this.zm),1);
		this.rb.mouseMove(this.cls.WINDOW_SIZE.width/2,this.cls.WINDOW_SIZE.height/2);
	}



	public void draw(GL2 gl){
		gl.glLoadIdentity();
		gl.glRotatef((float)-this.rx,1.0f,0.0f,0.0f);
		gl.glRotatef((float)-this.ry,0.0f,1.0f,0.0f);
		gl.glRotatef((float)-this.rz,0.0f,0.0f,1.0f);
		gl.glTranslatef((float)this.x,(float)this.y,(float)this.z);
		gl.glScalef((float)(1/this.zm),(float)(1/this.zm),(float)(1/this.zm));
	}
}