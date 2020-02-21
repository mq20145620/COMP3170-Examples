package comp3170.example1.shapes;	

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Quad extends Mesh {

	// the vertices of the quad as two triangles
	
	public float[] vertices = {
		  	 1.0f,  1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
             1.0f, -1.0f, 0.0f,

            -1.0f, -1.0f, 0.0f,
             1.0f,  1.0f, 0.0f,
            -1.0f,  1.0f, 0.0f,
	};

	public Quad() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

        FloatBuffer buffer;
        
        // load the data onto the GPU
        
        buffer = Buffers.newDirectFloatBuffer(this.vertices);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.vertexBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, this.vertices.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);
		this.size = vertices.length;

        float[] barycentric = makeBarycentric(vertices);
		buffer = Buffers.newDirectFloatBuffer(barycentric);
        gl.glBindBuffer(GL_ARRAY_BUFFER, barycentricBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, barycentric.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);
	}


	
}
