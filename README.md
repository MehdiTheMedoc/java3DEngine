# Java3DEngine
An attempt to make the use of LWJGL 3D easier

# What's new
the diffuse light seems to work now, but only if the entire scene uses only one shader. Can't figure out why.
the specular light works too.

added a "effect" toggle parameter in order to virtually use several shaders in one, and avoid the issue.

Boundbox collision detection ! Seems to work properly, just need some testing to see if it's bug free.

ok, it wasn't bug free. It's better now that I've a better comprehension of the 3D transformations

added the normal mapping ! :)
it needs some testing on the glsl side through.

added a way to load gameobjects presets and instantiate them in a scene.

# Features

GRAPHICS
- textures
- glsl shader handling
- sun light diffuse
- sun light specular
- texture filter
- skybox
- normal mapping

CORE
- frames and tick gestion
- transformations on gameobjects

PROGRAMER SIDE
- heightmap loading and rendering
- OBJ mesh loading and rendering
- gameobject preset loading

GAMEPLAY
- scenes gestion
- basic boundbox collision detection

# Next things to do
- add a component system with an "update" function, like we can see in unity, so that we could make a gameobject preset and then add it some code without affecting the preset
- less basic collision detection
- collision handler
- reorganize the code to make it cleaner (for example with the class FullGameObject. I'm not satisfied with it. Plus we need more clever packages layout)
- make some graphs to make the engine layout and logics more understandable
