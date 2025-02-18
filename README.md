# Pixel

A Software-Renderer written entirely in Java.

This project is licensed under the GNU General Public License v3.0. See the LICENSE file for details.

## Getting started

Take a look inside the [Main.java] file to get the general idea on how to setup and render a scene.

Invoke the terminal using the F12 key. You can change variables by typing in <variable_name> <new_value>. Use the "help" command for more information.

## Supported features

- Loading of .obj files
- Rendering of lines
- Rendering of triangles
- Programmable vertex, geometry, and fragment shaders
    - An implementation of the Phong reflection model with support for:
        - Point lights
        - Directional lights
        - Spotlights
- Backface culling
- Z-Buffering
- Clipping
- View-frustum culling
- Textures
- Texture filtering
- Cube maps
- Blending
- Post-processing filters

## Further ideas to implement
- Deferred shading
    - Stencil buffer
    - Shadow mapping
- Advanced texture filtering:
    - Mipmaps
    - Anisotropic texture filtering
