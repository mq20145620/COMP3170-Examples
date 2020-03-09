#version 410

uniform vec4 u_colour;

layout(location = 0) out vec4 color;

void main() {
    gl_FragColor = u_colour;
}

