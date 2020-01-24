package comp3170;

import javax.swing.JFrame;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;


public class Example1 extends JFrame implements GLEventListener {

	private GLCanvas myCanvas;
	
	public Example1() {
		super("Example 1");
		
		setSize(600,400);
		myCanvas = new GLCanvas();
		myCanvas.addGLEventListener(this);
		
		this.add(myCanvas);
		this.setVisible(true);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		new Example1();
	}

}
