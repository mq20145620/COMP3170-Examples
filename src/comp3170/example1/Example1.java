package comp3170.example1;

import static com.jogamp.opengl.GL.GL_BACK;
import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_CULL_FACE;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_LEQUAL;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

import org.joml.Matrix4f;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import comp3170.GLException;
import comp3170.Shader;
import comp3170.example1.shapes.Cube;
import comp3170.example1.shapes.Mesh;
import comp3170.example1.shapes.Quad;
import comp3170.example1.shapes.Sphere;
import comp3170.example1.shapes.Torus;

public class Example1 extends JFrame implements GLEventListener, KeyListener {

	private final float TAU = (float) (Math.PI * 2);
	
	private GLCanvas canvas;
	private Shader shader;
	
	final private String VERTEX_SHADER = "src/comp3170/example1/wireframe vertex.glsl";
	final private String FRAGMENT_SHADER = "src/comp3170/example1/wireframe fragment.glsl";
	
	private Cube cube; 
	private Quad quad; 
	private Sphere sphere;
	private Torus torus;
	private Mesh currentMesh;
	
	final private Matrix4f modelMatrix = new Matrix4f();
	final private Matrix4f cameraMatrix = new Matrix4f();
	final private Matrix4f projectionMatrix = new Matrix4f();
	final private Matrix4f mvpMatrix = new Matrix4f(); 
	
	private float cameraDistance = 5.0f;
	private float cameraPitch = 0;
	private float cameraYaw = 0;
	private float cameraFOVY = TAU / 6;	// 90 degrees
	final private float cameraNear = 0.1f;
	final private float cameraFar = 20f;
	
	final private float cameraTurnSpeed = TAU / 3;	// radians per second
	final private float cameraDollySpeed = 1.0f; // m per second
	
	private Animator animator;
	private long oldTime;
	
	private Set<Integer> keysDown;


	public Example1() {
		super("Example 1");
		
		// create an OpenGL canvas and add this as a listener
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);		 
		GLCapabilities capabilities = new GLCapabilities(profile);
		this.canvas = new GLCanvas(capabilities);
		this.canvas.addGLEventListener(this);
		this.add(canvas);
		
		// set up the JFrame
		
		this.setSize(800,600);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});	
		
		// Listen for key events
		
		keysDown = new HashSet<Integer>();
		this.addKeyListener(this);
		canvas.addKeyListener(this);

		// Add an animator to regularly redraw the screen
		
		this.animator = new Animator(canvas);
		oldTime = System.currentTimeMillis();
		this.animator.start();
		
	}

	@Override
	/**
	 * Initialise the GLCanvas
	 */
	public void init(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		System.err.println(gl.glGetString(GL.GL_VERSION));
	
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);	
		gl.glEnable(GL_CULL_FACE);
		gl.glCullFace(GL_BACK);

		// Compile the shader
		try {
			this.shader = new Shader(new File(VERTEX_SHADER), new File(FRAGMENT_SHADER));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GLException e) {
			e.printStackTrace();
		}
		
		// Create the matrices
		
		this.modelMatrix.identity();
		this.cameraMatrix.identity();
		this.projectionMatrix.identity();
		
		// Create the meshes
		
		this.cube = new Cube();
		this.quad = new Quad();
		this.sphere = new Sphere();
		this.torus = new Torus();
		currentMesh = quad;

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	/**
	 * Update the scene
	 */
	private void update() {
		// calculate how much time has passed since the last update
		long time = System.currentTimeMillis();			// ms
		float deltaTime = (time - oldTime) / 1000f;		// seconds
		oldTime = time;	
		
		// move the camera using the arrow keys
		
		if (keysDown.contains(KeyEvent.VK_LEFT)) {
			this.cameraYaw -= cameraTurnSpeed * deltaTime; 
		}
		if (keysDown.contains(KeyEvent.VK_RIGHT)) {
			this.cameraYaw += cameraTurnSpeed * deltaTime; 
		}
		if (keysDown.contains(KeyEvent.VK_UP)) {
			this.cameraPitch -= cameraTurnSpeed * deltaTime;
		}
		if (keysDown.contains(KeyEvent.VK_DOWN)) {
			this.cameraPitch += cameraTurnSpeed * deltaTime;
		}
		if (keysDown.contains(KeyEvent.VK_PAGE_DOWN)) {
			this.cameraDistance += cameraDollySpeed * deltaTime;
		}
		if (keysDown.contains(KeyEvent.VK_PAGE_UP)) {
			this.cameraDistance -= cameraDollySpeed * deltaTime;
		}
		
		// change the mesh
		if (keysDown.contains(KeyEvent.VK_1) || keysDown.contains(KeyEvent.VK_NUMPAD1)) {
			this.currentMesh = quad;
		}
		if (keysDown.contains(KeyEvent.VK_2) || keysDown.contains(KeyEvent.VK_NUMPAD2)) {
			this.currentMesh = cube;
		}
		if (keysDown.contains(KeyEvent.VK_3) || keysDown.contains(KeyEvent.VK_NUMPAD3)) {
			this.currentMesh = sphere;
		}
		if (keysDown.contains(KeyEvent.VK_4) || keysDown.contains(KeyEvent.VK_NUMPAD4)) {
			this.currentMesh = torus;
		}
		
		
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// update the scene
		
		update();
		
        // clear the colour and depth buffers
        
		gl.glClear(GL_COLOR_BUFFER_BIT);		
		gl.glClear(GL_DEPTH_BUFFER_BIT);		

		// set up the model, view and perspective 

		this.cameraMatrix.identity();
		this.cameraMatrix.rotateAffineXYZ(0, cameraYaw, 0);
		this.cameraMatrix.rotateAffineXYZ(cameraPitch, 0, 0);
		this.cameraMatrix.translate(0, 0, cameraDistance);
	
		this.mvpMatrix.set(this.projectionMatrix);
		this.mvpMatrix.mul(this.cameraMatrix.invertAffine());
		this.mvpMatrix.mul(this.modelMatrix);

		// send the matrix to the shader
		
		this.shader.enable();

		FloatBuffer fb = Buffers.newDirectFloatBuffer(16);
		gl.glUniformMatrix4fv(shader.getUniform("u_mvpMatrix"), 1, false, this.mvpMatrix.get(fb));
		
		// draw a cube
		currentMesh.draw(shader);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// resize the viewport
        gl.glViewport(0, 0, width, height);
        
        // resize the camera 
		final float aspect = (float) width / (float) height;
		this.projectionMatrix.setPerspective(this.cameraFOVY, aspect, this.cameraNear, this.cameraFar);		
//		this.projectionMatrix.setOrtho(-2 * aspect, 2 * aspect, -2, 2, this.cameraNear, this.cameraFar);
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keysDown.add(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keysDown.remove(e.getKeyCode());		
	}
	
	public static void main(String[] args) throws IOException, GLException {
		new Example1();
	}


}
