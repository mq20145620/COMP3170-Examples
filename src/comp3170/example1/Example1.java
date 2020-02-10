package comp3170.example1;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
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
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);		

	
	
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		final float aspect = (float) width / (float) height;
		
	}

	public static void main(String[] args) {
		new Example1();
	}

}
