package fr.medoc.main.render;
/*
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
*/
public class ShadowMapping {
	public void render()
	{

	}
	
	/*public boolean bake()
	{
		 // The framebuffer, which regroups 0, 1, or more textures, and 0 or 1 depth buffer.
		 GLuint FramebufferName = 0;
		 glGenFramebuffers(1, &FramebufferName);
		 glBindFramebuffer(GL_FRAMEBUFFER, FramebufferName);

		 // Depth texture. Slower than a depth buffer, but you can sample it later in your shader
		 GLuint depthTexture;
		 glGenTextures(1, &depthTexture);
		 glBindTexture(GL_TEXTURE_2D, depthTexture);
		 glTexImage2D(GL_TEXTURE_2D, 0,GL_DEPTH_COMPONENT16, 1024, 1024, 0,GL_DEPTH_COMPONENT, GL_FLOAT, 0);
		 glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		 glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		 glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		 glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		 glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthTexture, 0);

		 glDrawBuffer(GL_NONE); // No color buffer is drawn to.

		 // Always check that our framebuffer is ok
		 if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
			 return false;
		 return true;
	}*/
}
