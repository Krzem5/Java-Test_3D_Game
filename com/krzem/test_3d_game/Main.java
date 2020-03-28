package com.krzem.test_3d_game;



import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.lang.Math;
import javax.swing.JFrame;



public class Main{
	public static void main(String[] args) {
		new Main.Main_(args);
	}



	public static class Main_ implements GLEventListener{
		public static final int DISPLAY_ID=0;
		public static final GraphicsDevice SCREEN=GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[DISPLAY_ID];
		public static final Rectangle WINDOW_SIZE=SCREEN.getDefaultConfiguration().getBounds();
		public int MOUSE=0;
		public int MOUSE_COUNT=0;
		public int MOUSE_BUTTON=0;
		public Vector MOUSE_POS=new Vector(0,0);
		public int SCROLL_D=0;
		public Keyboard KEYBOARD;
		public Camera cam;
		public JFrame frame;
		public GLCanvas canvas;
		private int _mouse;
		private int _mouseC;
		private int _mouseB;
		private MouseEvent _mouseM;
		private int _sc;
		private boolean _break=false;



		public Main_(String[] args){
			this.init();
			this.frame_init();
			this.run();
		}



		public void init(){
			this.cam=new Camera(this);
			this.KEYBOARD=new Keyboard(this);
		}



		public void frame_init(){
			Main.Main_ cls=this;
			this.frame=new JFrame("GL - Test");
			this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.frame.setUndecorated(true);
			this.frame.setResizable(false);
			this.frame.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					cls.quit();
				}
			});
			SCREEN.setFullScreenWindow(this.frame);
			this.canvas=new GLCanvas(new GLCapabilities(GLProfile.get(GLProfile.GL2)));
			this.canvas.addGLEventListener(this);
			this.canvas.setSize(WINDOW_SIZE.width,WINDOW_SIZE.height);
			this.canvas.setPreferredSize(new Dimension(WINDOW_SIZE.width,WINDOW_SIZE.height));
			this.canvas.addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent e){
					cls._mouse=1;
					cls._mouseC=e.getClickCount();
					cls._mouseB=e.getButton();
				}
				public void mouseReleased(MouseEvent e){
					cls._mouse=2;
					cls._mouseC=e.getClickCount();
					cls._mouseB=e.getButton();
				}
				public void mouseClicked(MouseEvent e){
					cls._mouse=3;
					cls._mouseC=e.getClickCount();
					cls._mouseB=e.getButton();
				}
			});
			this.canvas.addMouseMotionListener(new MouseMotionAdapter(){
				public void mouseMoved(MouseEvent e){
					cls._mouseM=e;
				}
				public void mouseDragged(MouseEvent e){
					cls._mouseM=e;
				}
			});
			this.canvas.addMouseWheelListener(new MouseWheelListener(){
				public void mouseWheelMoved(MouseWheelEvent e){
					if (e.getWheelRotation()<0){
						cls._sc=1;
					}
					else{
						cls._sc=-1;
					}
				}
			});
			this.canvas.addKeyListener(new KeyListener(){
				public void keyPressed(KeyEvent e){
					if (cls.KEYBOARD==null){
						return;
					}
					cls.KEYBOARD.down(e);
				}
				public void keyReleased(KeyEvent e){
					if (cls.KEYBOARD==null){
						return;
					}
					cls.KEYBOARD.up(e);
				}
				public void keyTyped(KeyEvent e){
					if (cls.KEYBOARD==null){
						return;
					}
					cls.KEYBOARD.press(e);
				}
			});
			this.canvas.setCursor(this.canvas.getToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB),new Point(),null));
			this.frame.add(this.canvas);
			this.frame.setVisible(true);
			this.canvas.requestFocus();
		}



		public void run(){
			new FPSAnimator(this.canvas,1000,true).start();
		}



		public void update(){
			this.cam.update();
			if (this.KEYBOARD.pressed(27)){
				this.quit();
			}
			this.KEYBOARD.update();
		}



		public void draw(GL2 gl){
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);
			gl.glEnable(GL2.GL_DEPTH_TEST);
			gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT,GL2.GL_NICEST);
			this.cam.draw(gl);



			float SHINE_ALL_DIRECTIONS = 1;
			float[] lightPos = {-30, 0, 0, SHINE_ALL_DIRECTIONS};
			float[] lightColorAmbient = {0.2f, 0.2f, 0.2f, 1f};
			float[] lightColorSpecular = {0.8f, 0.8f, 0.8f, 1f};
			gl.glShadeModel(GL2.GL_FLAT);
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);
			gl.glEnable(GL2.GL_LIGHT1);
			gl.glEnable(GL2.GL_LIGHTING);
			float[] rgba={0.3f, 0.5f, 1f};
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, rgba, 0);
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);
			gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 0.5f);



			gl.glTranslatef(0.0f,0.0f,-5.0f);
			this._cylinder(gl,1,5,20,true);
			gl.glFlush();
		}



		@Override
		public void display(GLAutoDrawable drawable){
			this._update_events();
			this.update();
			this.draw(drawable.getGL().getGL2());
		}



		@Override
		public void dispose(GLAutoDrawable drawable){

		}



		@Override
		public void init(GLAutoDrawable drawable){
			this.cam.setup(drawable.getGL().getGL2());
		}



		@Override
		public void reshape(GLAutoDrawable drawable,int x,int y,int w,int h){
			this.cam.setup(drawable.getGL().getGL2());
		}



		private void _update_events(){
			this.MOUSE=this._mouse+0;
			this.MOUSE_COUNT=this._mouseC+0;
			this.MOUSE_BUTTON=this._mouseB+0;
			if (this._mouse!=1){
				this._mouse=0;
				this._mouseC=0;
				this._mouseB=0;
			}
			if (this._mouseM!=null){
				this.MOUSE_POS=new Vector(this._mouseM.getPoint().x,this._mouseM.getPoint().y);
				this._mouseM=null;
			}
			this.SCROLL_D=this._sc+0;
			this._sc=0;
		}



		private void quit(){
			if (this._break==true){
				return;
			}
			this._break=true;
			this.frame.dispose();
			this.frame.dispatchEvent(new WindowEvent(this.frame,WindowEvent.WINDOW_CLOSING));
		}



		private void _cylinder(GL2 gl,float w,float h,int d,boolean cap){
			gl.glBegin(GL2.GL_QUADS);
			gl.glColor3f(1f,0f,0f);
			for (int i=0;i<d;i++){
				double ax=w*Math.cos((double)i/d*Math.PI*2);
				double az=w*Math.sin((double)i/d*Math.PI*2);
				double bx=w*Math.cos((double)(i+1)/d*Math.PI*2);
				double bz=w*Math.sin((double)(i+1)/d*Math.PI*2);
				gl.glVertex3f((float)ax,-h/2,(float)az);
				gl.glNormal3f((float)ax,-h/2,(float)az);
				gl.glVertex3f((float)ax,h/2,(float)az);
				gl.glNormal3f((float)ax,h/2,(float)az);
				gl.glVertex3f((float)bx,h/2,(float)bz);
				gl.glNormal3f((float)bx,h/2,(float)bz);
				gl.glVertex3f((float)bx,-h/2,(float)bz);
				gl.glNormal3f((float)bx,-h/2,(float)bz);
			}
			gl.glEnd();
			if (cap==true){
				gl.glBegin(GL2.GL_TRIANGLES);
				gl.glColor3f(0f,0f,1f);
				for (int i=0;i<d;i++){
					double ax=w*Math.cos((double)i/d*Math.PI*2);
					double az=w*Math.sin((double)i/d*Math.PI*2);
					double bx=w*Math.cos((double)(i+1)/d*Math.PI*2);
					double bz=w*Math.sin((double)(i+1)/d*Math.PI*2);
					gl.glVertex3f((float)ax,h/2+1e-5f,(float)az);
					gl.glNormal3f((float)ax,h/2+1e-5f,(float)az);
					gl.glVertex3f((float)bx,h/2+1e-5f,(float)bz);
					gl.glNormal3f((float)bx,h/2+1e-5f,(float)bz);
					gl.glVertex3f(0.0f,h/2+1e-5f,0.0f);
					gl.glNormal3f(0.0f,h/2+1e-5f,0.0f);
					gl.glVertex3f((float)ax,-h/2-1e-5f,(float)az);
					gl.glNormal3f((float)ax,-h/2-1e-5f,(float)az);
					gl.glVertex3f((float)bx,-h/2-1e-5f,(float)bz);
					gl.glNormal3f((float)bx,-h/2-1e-5f,(float)bz);
					gl.glVertex3f(0.0f,-h/2-1e-5f,0.0f);
					gl.glNormal3f(0.0f,-h/2-1e-5f,0.0f);
				}
				gl.glEnd();
			}
		}
	}
}