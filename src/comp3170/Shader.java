package comp3170;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

public class Shader {

	public int program;
	private Map<String, Integer> attributes;
	private Map<String, Integer> attributeTypes;
	private Map<String, Integer> uniforms;
	private Map<String, Integer> uniformTypes;

	/**
	 * Compile and link a vertex and fragment shader
	 * 
	 * @param vertexShaderFile
	 * @param fragmentShaderFile
	 * @throws IOException
	 * @throws GLException
	 */

	public Shader(File vertexShaderFile, File fragmentShaderFile) throws IOException, GLException {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// compile the shaders

		int vertexShader = compileShader(GL4.GL_VERTEX_SHADER, vertexShaderFile);
		int fragmentShader = compileShader(GL4.GL_FRAGMENT_SHADER, fragmentShaderFile);

		// link the shaders

		this.program = gl.glCreateProgram();
		gl.glAttachShader(this.program, vertexShader);
		gl.glAttachShader(this.program, fragmentShader);
		gl.glLinkProgram(program);
		GLException.checkGLErrors();

		// check for linker errors

		int[] linked = new int[1];
		gl.glGetProgramiv(this.program, GL4.GL_LINK_STATUS, linked, 0);
		if (linked[0] != 1) {
			int[] maxlen = new int[1];
			int[] len = new int[1];
			byte[] log = null;
			String logString = "";

			// determine length of the program compilation log
			gl.glGetProgramiv(this.program, GL4.GL_INFO_LOG_LENGTH, maxlen, 0);

			if (maxlen[0] > 0) {
				log = new byte[maxlen[0]];

				gl.glGetProgramInfoLog(this.program, maxlen[0], len, 0, log, 0);
				logString = new String(log);
			}

			String message = String.format("Link failed:\n", logString);
			throw new GLException(message);
		}

		// record attribute and uniforms

		recordAttributes();
		recordUniforms();
	}

	public int getAttribute(String name) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int a = gl.glGetAttribLocation(this.program, name);
		
		if (a < 0) {
			throw new IllegalArgumentException(String.format("Unknown attribute: '%s'", name));
		}
		
		return a;
	}

	public int getUniform(String name) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int u = gl.glGetUniformLocation(this.program, name);
		
		if (u < 0) {
			throw new IllegalArgumentException(String.format("Unknown uniform: '%s'", name));
		}
		
		return u;
	}
	
	
	public void enable() {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		gl.glUseProgram(this.program);
	}
	
	/**
	 * Establish the mapping from attribute names to IDs
	 */

	private void recordAttributes() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		this.attributes = new HashMap<String, Integer>();
		this.attributeTypes = new HashMap<String, Integer>();

		int[] iBuff = new int[1];
		gl.glGetProgramiv(this.program, GL4.GL_ACTIVE_ATTRIBUTES, iBuff, 0);
		int activeAttributes = iBuff[0];

		gl.glGetProgramiv(this.program, GL4.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH, iBuff, 0);
		int maxNameSize = iBuff[0];

		byte[] nameBuffer = new byte[maxNameSize];

		int[] sizeBuffer = new int[1];
		int[] typeBuffer = new int[1];
		int[] nameLenBuffer = new int[1];
		for (int i = 0; i < activeAttributes; ++i) {
			gl.glGetActiveAttrib(this.program, i, maxNameSize, nameLenBuffer, 0, sizeBuffer, 0, typeBuffer, 0,
					nameBuffer, 0);
			String name = new String(nameBuffer);
			this.attributes.put(name, i);
			this.attributeTypes.put(name, typeBuffer[0]);
		}
	}

	/**
	 * Establish the mapping from uniform names to IDs
	 */

	private void recordUniforms() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		this.uniforms = new HashMap<String, Integer>();
		this.uniformTypes = new HashMap<String, Integer>();
		
		int[] iBuff = new int[1];
		gl.glGetProgramiv(this.program, GL4.GL_ACTIVE_UNIFORMS, iBuff, 0);
		int activeUniforms = iBuff[0];

		gl.glGetProgramiv(this.program, GL4.GL_ACTIVE_UNIFORM_MAX_LENGTH, iBuff, 0);
		int maxNameSize = iBuff[0];

		byte[] nameBuffer = new byte[maxNameSize];

		int[] sizeBuffer = new int[1];
		int[] typeBuffer = new int[1];
		int[] nameLenBuffer = new int[1];
		for (int i = 0; i < activeUniforms; ++i) {
			gl.glGetActiveUniform(this.program, i, maxNameSize, nameLenBuffer, 0, sizeBuffer, 0, typeBuffer, 0,
					nameBuffer, 0);
			String name = new String(nameBuffer);
			this.uniforms.put(name, i);
			this.uniformTypes.put(name, typeBuffer[0]);
		}
	}

	public static Class typeToClass(int type) {

		switch (type) {

		case GL4.GL_BOOL:
			return Boolean.class;
		
		case GL4.GL_INT:
			return Integer.class;
		
		case GL4.GL_FLOAT:
			return Float.class;

		case GL4.GL_FLOAT_VEC2:
			return Vector2f.class;

		case GL4.GL_FLOAT_VEC3:
			return Vector3f.class;

		case GL4.GL_FLOAT_VEC4:
			return Vector4f.class;

		case GL4.GL_FLOAT_MAT2:
			return Matrix2f.class;

		case GL4.GL_FLOAT_MAT3:
			return Matrix3f.class;

		case GL4.GL_FLOAT_MAT4:
			return Matrix4f.class;
			
		}

		throw new UnsupportedOperationException("Unsupported GLSL type: " + type);
	}

	/**
	 * Read source code from a shader file.
	 * 
	 * @param shaderFile
	 * @return
	 * @throws IOException
	 */
	public static String[] readSource(File shaderFile) throws IOException {
		ArrayList<String> source = new ArrayList<String>();
		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(shaderFile));

			for (String line = in.readLine(); line != null; line = in.readLine()) {
				source.add(line);
			}

		} catch (IOException e) {
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
		}

		String[] lines = new String[source.size()];
		return source.toArray(lines);
	}

	/**
	 * Compile a shader
	 * 
	 * @param type
	 * @param sourceFile
	 * @return
	 * @throws GLException
	 * @throws IOException
	 */

	public static int compileShader(int type, File sourceFile) throws GLException, IOException {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		String[] source = readSource(sourceFile);

		int shader = gl.glCreateShader(type);
		gl.glShaderSource(shader, source.length, source, null, 0);
		gl.glCompileShader(shader);
		GLException.checkGLErrors();

		// check compilation

		int[] compiled = new int[1];
		gl.glGetShaderiv(shader, GL4.GL_COMPILE_STATUS, compiled, 0);
		String logString = "";

		if (compiled[0] != 1) {

			int[] maxlen = new int[1];
			gl.glGetShaderiv(shader, GL4.GL_INFO_LOG_LENGTH, maxlen, 0);

			if (maxlen[0] > 0) {
				int[] len = new int[1];
				byte[] log = null;

				log = new byte[maxlen[0]];
				gl.glGetShaderInfoLog(shader, maxlen[0], len, 0, log, 0);
				logString = new String(log);
			}

			String message = String.format("%s: compilation error\n%s", sourceFile.getName(), logString);
			throw new GLException(message);
		}

		return shader;
	}

	/**
	 * Turn a shader type constant into a descriptive string.
	 * 
	 * @param type
	 * @return
	 */
	public static String shaderType(int type) {
		switch (type) {
		case GL4.GL_VERTEX_SHADER:
			return "Vertex shader";
		case GL4.GL_FRAGMENT_SHADER:
			return "Fragment shader";
		}
		return "Unknown shader";
	}
	
	
	

}
