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

public class Sphere extends Mesh {

	private final static float TAU = (float) (Math.PI * 2);

	public float[] vertices;

	private int NUM_DIVISIONS = 72;

	public Sphere() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

        FloatBuffer buffer;
        
        this.vertices = new float[18 * NUM_DIVISIONS * NUM_DIVISIONS / 2];
        
        int v = 0;
        for (int i = 0; i < NUM_DIVISIONS / 2; i++) {
        	float psi0 = i * TAU / NUM_DIVISIONS - TAU / 4; 
        	float psi1 = (i+1) * TAU / NUM_DIVISIONS - TAU / 4; 

            for (int j = 0; j < NUM_DIVISIONS; j++) {
            	float theta0 = j * TAU / NUM_DIVISIONS;
            	float theta1 = (j+1) * TAU / NUM_DIVISIONS;
            	
            	float x00 = (float) (Math.cos(psi0) * Math.cos(theta0));
            	float y00 = (float) (Math.cos(psi0) * Math.sin(theta0));

            	float x01 = (float) (Math.cos(psi0) * Math.cos(theta1));
            	float y01 = (float) (Math.cos(psi0) * Math.sin(theta1));

            	float x10 = (float) (Math.cos(psi1) * Math.cos(theta0));
            	float y10 = (float) (Math.cos(psi1) * Math.sin(theta0));

            	float x11 = (float) (Math.cos(psi1) * Math.cos(theta1));
            	float y11 = (float) (Math.cos(psi1) * Math.sin(theta1));

            	float z0 = (float) (Math.sin(psi0));
            	float z1 = (float) (Math.sin(psi1));

            	
            	vertices[v++] = x11;
            	vertices[v++] = y11;
            	vertices[v++] = z1;

            	vertices[v++] = x00;
            	vertices[v++] = y00;
            	vertices[v++] = z0;

            	vertices[v++] = x01;
            	vertices[v++] = y01;
            	vertices[v++] = z0;

            	vertices[v++] = x00;
            	vertices[v++] = y00;
            	vertices[v++] = z0;

            	vertices[v++] = x11;
            	vertices[v++] = y11;
            	vertices[v++] = z1;

            	vertices[v++] = x10;
            	vertices[v++] = y10;
            	vertices[v++] = z1;
            }
        	
        }
       
        buffer = Buffers.newDirectFloatBuffer(this.vertices);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.vertexBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, this.vertices.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);
		this.size = vertices.length;

        float[] barycentric = makeBarycentric(this.vertices);
		buffer = Buffers.newDirectFloatBuffer(barycentric);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.barycentricBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, barycentric.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);

	}
	
}
