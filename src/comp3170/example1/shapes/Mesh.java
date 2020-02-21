package comp3170.example1.shapes;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_TRIANGLES;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.Shader;

abstract public class Mesh {
	public final static int SIZEOF_FLOAT = 4;

	protected int vertexBuffer;
	protected int barycentricBuffer;
	protected int size;
	
	public Mesh() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// Create buffers to load data onto the GPU
		
	    int[] buffers = new int[2];
		gl.glGenBuffers(buffers.length, buffers, 0);
		
        this.vertexBuffer = buffers[0];
        this.barycentricBuffer = buffers[1];
        
	}
	
	/**
	 * Draw the mesh using the given shader
	 * @param shader
	 */
	public void draw(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		// connect the vertex buffer to the a_position attribute
		
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.vertexBuffer);
        gl.glVertexAttribPointer(shader.getAttribute("a_position"), 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(shader.getAttribute("a_position"));

        // connect the barycentric buffer to the a_barycentric attribute
        
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.barycentricBuffer);
        gl.glVertexAttribPointer(shader.getAttribute("a_barycentric"), 3, GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(shader.getAttribute("a_barycentric"));
        
        // set the colour and width uniforms
        
        gl.glUniform4f(shader.getUniform("u_colour"), 1, 1, 0, 1);
        gl.glUniform1f(shader.getUniform("u_width"), 2f);
        
        // draw the triangles
        
        gl.glDrawArrays(GL_TRIANGLES, 0, size / 3);           	
		
	}
	/**
	 * Construct the barycentric coordinates for the given list of triangles 
	 * @param vertices
	 * @return
	 */
	protected float[] makeBarycentric(float [] vertices) {
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
