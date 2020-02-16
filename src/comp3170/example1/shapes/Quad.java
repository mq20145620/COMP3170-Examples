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

	private final static int SIZEOF_FLOAT = 4;
	
	public float[] vertices = {
			// front
		  	 1.0f,  1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
             1.0f, -1.0f, 0.0f,

            -1.0f, -1.0f, 0.0f,
             1.0f,  1.0f, 0.0f,
            -1.0f,  1.0f, 0.0f,
	};


	private int vertexBuffer;
	private int barycentricBuffer;

	public Quad() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		int[] buffers = new int[2];
		gl.glGenBuffers(buffers.length, buffers, 0);
		
        this.vertexBuffer = buffers[0];
        this.barycentricBuffer = buffers[1];
        
        FloatBuffer buffer;
        
        buffer = Buffers.newDirectFloatBuffer(this.vertices);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.vertexBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, this.vertices.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);

        float[] barycentric = makeBarycentric(vertices);
		buffer = Buffers.newDirectFloatBuffer(barycentric);
        gl.glBindBuffer(GL_ARRAY_BUFFER, barycentricBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, barycentric.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);

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
