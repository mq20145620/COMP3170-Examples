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

	private final static int SIZEOF_FLOAT = 4;

	public float[] vertices = {           // front
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

	public int vertexBuffer;
	public int uvBuffer;
	public int normalBuffer;
	public int barycentricBuffer;

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

        this.barycentric = makeBarycentric(vertices);
		buffer = Buffers.newDirectFloatBuffer(this.barycentric);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.barycentricBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, this.barycentric.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);

	}
	
	@Override
	public void draw(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.vertexBuffer);
        gl.glVertexAttribPointer(shader.getAttribute("a_position"), 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(shader.getAttribute("a_position"));

        gl.glBindBuffer(GL_ARRAY_BUFFER, this.barycentricBuffer);
        gl.glVertexAttribPointer(shader.getAttribute("a_barycentric"), 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(shader.getAttribute("a_barycentric"));
        
        gl.glUniform4f(shader.getUniform("u_colour"), 1, 1, 0, 1);
        gl.glUniform1f(shader.getUniform("u_width"), 2f);
        
        gl.glDrawArrays(GL_TRIANGLES, 0, this.vertices.length / 3);           	
		
	}
	
}
