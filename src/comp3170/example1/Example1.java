package comp3170.example1;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;

import javax.swing.JFrame;

import org.joml.Matrix4f;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;

import comp3170.GLException;
import comp3170.Shader;

public class Example1 extends JFrame implements GLEventListener {

	private final float TAU = (float) (Math.PI * 2);
	
	private GLCanvas canvas;
	private Shader shader;
	
	final private String VERTEX_SHADER = "src/comp3170/example1/wireframe vertex.glsl";
	final private String FRAGMENT_SHADER = "src/comp3170/example1/wireframe fragment.glsl";
	
	private Cube cube; 
	private Quad quad; 
	
	final private Matrix4f modelMatrix = new Matrix4f();
	final private Matrix4f cameraMatrix = new Matrix4f();
	final private Matrix4f projectionMatrix = new Matrix4f();
	final private Matrix4f mvpMatrix = new Matrix4f(); 
	
	final private float cameraDistance = 5.0f;
	final private float cameraPitch = 0;
	final private float cameraYaw = 0;
	final private float cameraFOVY = TAU / 6;	// 60 degrees
	final private float cameraNear = 0.1f;
	final private float cameraFar = 20f;
	
	public Example1() {
		super("Example 1");
		
		setSize(600,400);
		this.canvas = new GLCanvas();
		this.canvas.addGLEventListener(this);
		
		this.add(canvas);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);	

		try {
			this.shader = new Shader(new File(VERTEX_SHADER), new File(FRAGMENT_SHADER));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GLException e) {
			e.printStackTrace();
		}
		
		this.modelMatrix.identity();
		this.cameraMatrix.identity();
		this.projectionMatrix.identity();
		
		this.cube = new Cube();
		this.quad = new Quad();
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// resize the viewport
		
        gl.glViewport(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        
        // clear the colour and depth buffers
        
		gl.glClear(GL_COLOR_BUFFER_BIT);		
		gl.glClear(GL_DEPTH_BUFFER_BIT);		

		// set up the mvp matrix

		this.cameraMatrix.identity();
		this.cameraMatrix.rotateAffineXYZ(0, cameraYaw, 0);
		this.cameraMatrix.rotateAffineXYZ(cameraPitch, 0, 0);
		this.cameraMatrix.translate(0, 0, cameraDistance);
	
		this.mvpMatrix.set(this.projectionMatrix);
		this.mvpMatrix.mul(this.cameraMatrix.invertAffine());
		this.mvpMatrix.mul(this.modelMatrix);

		this.shader.enable();

		FloatBuffer fb = Buffers.newDirectFloatBuffer(16);
		gl.glUniformMatrix4fv(shader.getUniform("u_mvpMatrix"), 1, false, this.mvpMatrix.get(fb));
		
		// draw a cube
		
        gl.glBindBuffer(GL_ARRAY_BUFFER, cube.vertexBuffer);
        gl.glVertexAttribPointer(shader.getAttribute("a_position"), 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(shader.getAttribute("a_position"));

        gl.glBindBuffer(GL_ARRAY_BUFFER, cube.barycentricBuffer);
        gl.glVertexAttribPointer(shader.getAttribute("a_barycentric"), 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(shader.getAttribute("a_barycentric"));
        
        gl.glUniform4f(shader.getUniform("u_colour"), 1, 0, 0, 1);
        gl.glUniform1f(shader.getUniform("u_width"), 2f);
        
        gl.glDrawArrays(GL_TRIANGLES, 0, cube.vertices.length / 3);           	
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		final float aspect = (float) width / (float) height;
		this.projectionMatrix.setPerspective(this.cameraFOVY, aspect, this.cameraNear, this.cameraFar);		
//		this.projectionMatrix.setOrtho(-2 * aspect, 2 * aspect, -2, 2, this.cameraNear, this.cameraFar);
		
	}

	public static void main(String[] args) throws IOException, GLException {
		new Example1();
	}

}
