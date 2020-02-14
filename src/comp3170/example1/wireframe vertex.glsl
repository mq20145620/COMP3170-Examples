
attribute vec3 a_position;
attribute vec3 a_barycentric;

uniform mat4 u_mvpMatrix;

varying vec3 v_barycentric;

void main() {
    gl_Position = u_mvpMatrix * vec4(a_position,1);
    v_barycentric = a_barycentric;    
}

