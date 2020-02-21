package comp3170.example1.shapes;	

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_TRIANGLES;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

public class Cube extends Mesh {

	// The cube as a collection of siz faces, each made from two triangles
	
	public float[] vertices = {
			// front
			1.0f,  1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,

            -1.0f, -1.0f, 1.0f,
            1.0f,  1.0f, 1.0f,
            -1.0f,  1.0f, 1.0f,

            // back 
            -1.0f, -1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            1.0f,  1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,

            // left
            -1.0f, -1.0f, -1.0f, 
            -1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f, -1.0f,

            -1.0f,  1.0f,  1.0f,
            -1.0f, -1.0f, -1.0f, 
            -1.0f,  -1.0f, 1.0f,

            // right
             1.0f,  1.0f,  1.0f,
             1.0f, -1.0f, -1.0f, 
             1.0f,  1.0f, -1.0f,

             1.0f, -1.0f, -1.0f, 
             1.0f,  1.0f,  1.0f,
             1.0f,  -1.0f, 1.0f,

            // top
            1.0f, 1.0f,  1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f,  1.0f,

            -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f,  1.0f,
            1.0f, 1.0f,  -1.0f,

            // bottom
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,

            1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f,  -1.0f,
	};


	public Cube() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		FloatBuffer buffer;
        
        buffer = Buffers.newDirectFloatBuffer(this.vertices);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.vertexBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, this.vertices.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);
		this.size = vertices.length;

        float[] barycentric = makeBarycentric(vertices);
		buffer = Buffers.newDirectFloatBuffer(barycentric);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.barycentricBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, barycentric.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);

	}
	
	
}
