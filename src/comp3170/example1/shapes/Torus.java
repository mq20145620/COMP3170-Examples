package comp3170.example1.shapes;	

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;

import java.nio.FloatBuffer;

import org.joml.Matrix3f;
import org.joml.Vector3f;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

public class Torus extends Mesh {

	private final static float TAU = (float) (Math.PI * 2);

	public float[] vertices;

	private int NUM_DIVISIONS = 72;
	private float MAJOR_RADIUS = 1;
	private float MINOR_RADIUS = 0.5f;
	
	public Torus() {
		GL4 gl = (GL4) GLContext.getCurrentGL();


        FloatBuffer buffer;
        
        this.vertices = new float[18 * NUM_DIVISIONS * NUM_DIVISIONS];
        
        int v = 0;

    	Matrix3f ri0 = new Matrix3f();
    	Matrix3f ri1 = new Matrix3f();
    	Matrix3f rj0 = new Matrix3f();
    	Matrix3f rj1 = new Matrix3f();
    	
       	Vector3f v00 = new Vector3f(1,0,0);
       	Vector3f v01 = new Vector3f(1,0,0);
       	Vector3f v10 = new Vector3f(1,0,0);
       	Vector3f v11 = new Vector3f(1,0,0);

        for (int i = 0; i < NUM_DIVISIONS; i++) {
        	float psi0 = i * TAU / NUM_DIVISIONS; 
        	float psi1 = (i+1) * TAU / NUM_DIVISIONS;
        	
        	ri0.identity();
        	ri0.rotateZ(psi0);

        	ri1.identity();
        	ri1.rotateZ(psi1);

        	
            for (int j = 0; j < NUM_DIVISIONS; j++) {
            	float theta0 = j * TAU / NUM_DIVISIONS;
            	float theta1 = (j+1) * TAU / NUM_DIVISIONS;
            	
            	v00.set(MINOR_RADIUS,0,0);
            	v01.set(MINOR_RADIUS,0,0);
            	v10.set(MINOR_RADIUS,0,0);
            	v11.set(MINOR_RADIUS,0,0);            	
            	
            	rj0.identity();
            	rj0.rotateY(theta0);

            	rj1.identity();
            	rj1.rotateY(theta1);
            	
            	v00.mul(rj0);
            	v01.mul(rj0);
            	v10.mul(rj1);
            	v11.mul(rj1);
            	
            	v00.add(MAJOR_RADIUS, 0, 0);
            	v01.add(MAJOR_RADIUS, 0, 0);
            	v10.add(MAJOR_RADIUS, 0, 0);
            	v11.add(MAJOR_RADIUS, 0, 0);
            	
            	v00.mul(ri0);
            	v01.mul(ri1);
            	v10.mul(ri0);
            	v11.mul(ri1);
            	            	
            	vertices[v++] = v11.x;
            	vertices[v++] = v11.y;
            	vertices[v++] = v11.z;

            	vertices[v++] = v00.x;
            	vertices[v++] = v00.y;
            	vertices[v++] = v00.z;

            	vertices[v++] = v10.x;
            	vertices[v++] = v10.y;
            	vertices[v++] = v10.z;

            	vertices[v++] = v00.x;
            	vertices[v++] = v00.y;
            	vertices[v++] = v00.z;

            	vertices[v++] = v11.x;
            	vertices[v++] = v11.y;
            	vertices[v++] = v11.z;

            	vertices[v++] = v01.x;
            	vertices[v++] = v01.y;
            	vertices[v++] = v01.z;
            }
        	
        }
       
        buffer = Buffers.newDirectFloatBuffer(this.vertices);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.vertexBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, this.vertices.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);
        this.size = this.vertices.length;
        
        float[] barycentric = makeBarycentric(this.vertices);
		buffer = Buffers.newDirectFloatBuffer(barycentric);
        gl.glBindBuffer(GL_ARRAY_BUFFER, this.barycentricBuffer);
        gl.glBufferData(GL_ARRAY_BUFFER, barycentric.length * SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);

	}
	
	
}
