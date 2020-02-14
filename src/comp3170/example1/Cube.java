package comp3170.example1;	

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.GLContext;

public class Cube {

	private final static int SIZEOF_FLOAT = 4;

	public float[] vertices = {           // front
			// front
			0.8f,  0.8f, 0.8f,
            -0.8f, -0.8f, 0.8f,
            0.8f, -0.8f, 0.8f,

            -0.8f, -0.8f, 0.8f,
            0.8f,  0.8f, 0.8f,
            -0.8f,  0.8f, 0.8f,

            // back 
            -0.8f, -0.8f, -0.8f,
            0.8f,  0.8f, -0.8f,
            0.8f, -0.8f, -0.8f,

            0.8f,  0.8f, -0.8f,
            -0.8f, -0.8f, -0.8f,
            -0.8f,  0.8f, -0.8f,

            // left
            -0.8f, -0.8f, -0.8f, 
            -0.8f,  0.8f,  0.8f,
            -0.8f,  0.8f, -0.8f,

            -0.8f,  0.8f,  0.8f,
            -0.8f, -0.8f, -0.8f, 
            -0.8f,  -0.8f, 0.8f,

            // right
             0.8f,  0.8f,  0.8f,
             0.8f, -0.8f, -0.8f, 
             0.8f,  0.8f, -0.8f,

             0.8f, -0.8f, -0.8f, 
             0.8f,  0.8f,  0.8f,
             0.8f,  -0.8f, 0.8f,

            // top
            0.8f, 0.8f,  0.8f,
            -0.8f, 0.8f, -0.8f,
            -0.8f, 0.8f,  0.8f,

            -0.8f, 0.8f, -0.8f,
            0.8f, 0.8f,  0.8f,
            0.8f, 0.8f,  -0.8f,

            // bottom
            -0.8f, -0.8f, -0.8f,
            0.8f, -0.8f,  0.8f,
            -0.8f, -0.8f,  0.8f,

            0.8f, -0.8f,  0.8f,
            -0.8f, -0.8f, -0.8f,
            0.8f, -0.8f,  -0.8f,
	};

	public float[] uvs = {           // front
			// front
			1.0f, 1.0f,
            0, 0,
            1.0f, 0,

            0, 0,
            1.0f, 1.0f,
            0, 1.0f,

            // back 
            0, 0,
            1.0f, 1.0f,
            1.0f, 0,

            1.0f, 1.0f,
            0, 0,
            0, 1.0f,

            // left
            0, 0, 
            1.0f, 1.0f,
            1.0f, 0,

            1.0f, 1.0f,
            0, 0, 
            0, 1.0f,

            // right
            1.0f, 1.0f,
            0, 0, 
            1.0f, 0,

            0, 0, 
            1.0f, 1.0f,
            0, 1.0f,

            // top
            1.0f, 1.0f,
            0, 0,
            0, 1.0f,

            0, 0,
            1.0f, 1.0f,
            1.0f, 0,

            // bottom
            0, 0,
            1.0f, 1.0f,
            0, 1.0f,

            1.0f, 1.0f,
            0, 0,
            1.0f, 0,
	};

	
	public float[] normals = {
            // front
            0,  0, 1.0f,
            0,  0, 1.0f,
            0,  0, 1.0f,

            0,  0, 1.0f,
            0,  0, 1.0f,
            0,  0, 1.0f,

            // back 
            0,  0, -1.0f,
            0,  0, -1.0f,
            0,  0, -1.0f,

            0,  0, -1.0f,
            0,  0, -1.0f,
            0,  0, -1.0f,

            // left
            -1.0f,  0, 0,
            -1.0f,  0, 0,
            -1.0f,  0, 0,

            -1.0f,  0, 0,
            -1.0f,  0, 0,
            -1.0f,  0, 0,

            // right
            1.0f,  0, 0,
            1.0f,  0, 0,
            1.0f,  0, 0,

            1.0f,  0, 0,
            1.0f,  0, 0,
            1.0f,  0, 0,

            // top
            0, 1.0f, 0,
            0, 1.0f, 0,
            0, 1.0f, 0,

            0, 1.0f, 0,
            0, 1.0f, 0,
            0, 1.0f, 0,

            // bottom
            0, -1.0f, 0,
            0, -1.0f, 0,
            0, -1.0f, 0,

            0, -1.0f, 0,
            0, -1.0f, 0,
            0, -1.0f, 0,
	};
	
	public float[] barycentric;

	int vertexBuffer;
	int uvBuffer;
	int normalBuffer;
	int barycentricBuffer;

	public Cube() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

	    int[] buffers = new int[4];
		gl.glGenBuffers(buffers.length, buffers, 0);
		
        this.vertexBuffer = buffers[0];
        this.uvBuffer = buffers[1];
        this.normalBuffer = buffers[2];
        this.barycentricBuffer = buffers[3];

        FloatBuffer buffer;
        
        buffer = Buffers.newDirectFloatBuffer(this.vertices);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.vertexBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, this.vertices.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);

		buffer = Buffers.newDirectFloatBuffer(this.uvs);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.uvBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, this.uvs.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);

		buffer = Buffers.newDirectFloatBuffer(this.normals);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.normalBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, this.normals.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);

        this.barycentric = makeBarycentric();
		buffer = Buffers.newDirectFloatBuffer(this.barycentric);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.barycentricBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, this.barycentric.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);

	}
	
	private float[] makeBarycentric() {
		float[] barycentric = new float[vertices.length];

		int i = 0;
		while (i < vertices.length) {
			barycentric[i++] = 1;
			barycentric[i++] = 0;
			barycentric[i++] = 0;

			barycentric[i++] = 0;
			barycentric[i++] = 1;
			barycentric[i++] = 0;
			
			barycentric[i++] = 0;
			barycentric[i++] = 0;
			barycentric[i++] = 1;			
		}
		
		return barycentric;
	}
	
}
