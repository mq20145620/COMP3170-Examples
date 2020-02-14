package comp3170.example1;	

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.GLContext;

public class Quad {

	private final static int SIZEOF_FLOAT = 4;
	
	public float[] vertices = {
			// front
		  	 1.0f,  1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
             1.0f, -1.0f, 1.0f,

            -1.0f, -1.0f, 1.0f,
             1.0f,  1.0f, 1.0f,
            -1.0f,  1.0f, 1.0f,
	};


	int vertexBuffer;

	public Quad() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		int[] buffers = new int[1];
		gl.glGenBuffers(buffers.length, buffers, 0);
		
        this.vertexBuffer = buffers[0];
   
        FloatBuffer buffer;
        
        buffer = Buffers.newDirectFloatBuffer(this.vertices);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.vertexBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, this.vertices.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);

	}
	
}
