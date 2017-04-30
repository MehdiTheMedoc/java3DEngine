# Java3DEngine
An attempt to make the use of LWJGL 3D easier

# What's new
the diffuse light seems to work now, but only if the entire scene uses only one shader. Can't figure out why.
the specular light works too.

added a "effect" toggle parameter in order to virtually use several shaders in one, and avoid the issue.

Boundbox collision detection ! Seems to work properly, just need some testing to see if it's bug free.

# Features
- frames and tick gestion
- scenes gestion
- transformations on gameobjects
- heightmap loading and rendering
- OBJ mesh loading and rendering
- textures
- glsl shader handling
- sun light diffuse
- sun light specular
- texture filter
- skybox
- basic boundbox collision detection

# Next things to do
- normal mapping
- collision handler
- reorganize the code to make it cleaner
- make some graphs to make the engine layout and logics more understandable
