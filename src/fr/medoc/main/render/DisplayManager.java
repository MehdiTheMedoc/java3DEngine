package fr.medoc.main.render;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;


public class DisplayManager {
	
	/**
	 * creates the window
	 * @param width : window width
	 * @param height : window height
	 * @param title : window title
	 */
	public static void create(int width, int height, String title) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle(title);
			
			/*ByteBuffer[] icon_array = new ByteBuffer[50];
			for(int i=0; i< icon_array.length; i++)
				icon_array[i] = ImageUtil.convertImageData(ImageUtil.loadImage("/textures/icons/bube.png"));
			
	        Display.setIcon(icon_array);*/
			
			Display.setResizable(true);
			Display.create(new PixelFormat(/*Alpha Bits*/8, /*Depth bits*/ 8, /*Stencil bits*/ 0, /*samples*/2));
			glEnable(GL_DEPTH_TEST);
			glEnable(GL_CULL_FACE);
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_SMOOTH);
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * updates the display
	 */
	public static void update()
	{
		Display.update();
	}
	
	/**
	 * Clear the screen
	 */
	public static void clearBuffers() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * Returns true if the red cross button is pressed
	 */
	public static boolean isClosed() {
		return Display.isCloseRequested();
	}
	/**
	 * closes the window
	 */
	public static void dispose()
	{
		Display.destroy();
	}
	
}
