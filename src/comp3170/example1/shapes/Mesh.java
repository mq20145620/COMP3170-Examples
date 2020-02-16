package comp3170.example1.shapes;

import comp3170.Shader;

abstract public class Mesh {
	
	abstract void draw(Shader shader);

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
