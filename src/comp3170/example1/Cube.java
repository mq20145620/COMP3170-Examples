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
			1,  1, 1,
            -1, -1, 1,
            1, -1, 1,

            -1, -1, 1,
            1,  1, 1,
            -1,  1, 1,

            // back 
            -1, -1, -1,
            1,  1, -1,
            1, -1, -1,

            1,  1, -1,
            -1, -1, -1,
            -1,  1, -1,

            // left
            -1, -1, -1, 
            -1,  1,  1,
            -1,  1, -1,

            -1,  1,  1,
            -1, -1, -1, 
            -1,  -1, 1,

            // right
             1,  1,  1,
             1, -1, -1, 
             1,  1, -1,

             1, -1, -1, 
             1,  1,  1,
             1,  -1, 1,

            // top
            1, 1,  1,
            -1, 1, -1,
            -1, 1,  1,

            -1, 1, -1,
            1, 1,  1,
            1, 1,  -1,

            // bottom
            -1, -1, -1,
            1, -1,  1,
            -1, -1,  1,

            1, -1,  1,
            -1, -1, -1,
            1, -1,  -1,
	};

	public float[] uvs = {           // front
			// front
			1, 1,
            0, 0,
            1, 0,

            0, 0,
            1, 1,
            0, 1,

            // back 
            0, 0,
            1, 1,
            1, 0,

            1, 1,
            0, 0,
            0, 1,

            // left
            0, 0, 
            1, 1,
            1, 0,

            1, 1,
            0, 0, 
            0, 1,

            // right
            1, 1,
            0, 0, 
            1, 0,

            0, 0, 
            1, 1,
            0, 1,

            // top
            1, 1,
            0, 0,
            0, 1,

            0, 0,
            1, 1,
            1, 0,

            // bottom
            0, 0,
            1, 1,
            0, 1,

            1, 1,
            0, 0,
            1, 0,
	};

	
	public float[] normals = {
            // front
            0,  0, 1,
            0,  0, 1,
            0,  0, 1,

            0,  0, 1,
            0,  0, 1,
            0,  0, 1,

            // back 
            0,  0, -1,
            0,  0, -1,
            0,  0, -1,

            0,  0, -1,
            0,  0, -1,
            0,  0, -1,

            // left
            -1,  0, 0,
            -1,  0, 0,
            -1,  0, 0,

            -1,  0, 0,
            -1,  0, 0,
            -1,  0, 0,

            // right
            1,  0, 0,
            1,  0, 0,
            1,  0, 0,

            1,  0, 0,
            1,  0, 0,
            1,  0, 0,

            // top
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,

            0, 1, 0,
            0, 1, 0,
            0, 1, 0,

            // bottom
            0, -1, 0,
            0, -1, 0,
            0, -1, 0,

            0, -1, 0,
            0, -1, 0,
            0, -1, 0,
	};

	int vertexBuffer;
	int uvBuffer;
	int normalBuffer;

	public Cube() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		int[] buffers = new int[3];
		gl.glGenBuffers(buffers.length, buffers, 0);
		
        this.vertexBuffer = buffers[0];
        this.uvBuffer = buffers[1];
        this.normalBuffer = buffers[2];

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
		
	}
	
}
