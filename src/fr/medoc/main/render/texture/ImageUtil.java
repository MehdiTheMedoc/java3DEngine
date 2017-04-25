package fr.medoc.main.render.texture;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import fr.medoc.main.math.ARGBColor;

public class ImageUtil {
	 public static void printPixelARGB(int pixel) {
		    int alpha = (pixel >> 24) & 0xff;
		    int red = (pixel >> 16) & 0xff;
		    int green = (pixel >> 8) & 0xff;
		    int blue = (pixel) & 0xff;
		    System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
		  }
	 
	 public static ARGBColor pixelToARGB(int pixel)
	 {
		 	float alpha = (pixel >> 24) & 0xff;
		 	float red = (pixel >> 16) & 0xff;
		 	float green = (pixel >> 8) & 0xff;
		 	float blue = (pixel) & 0xff;
		    
		    return new ARGBColor(alpha/255.0f, red/255.0f, green/255.0f, blue/255.0f);
	 }
	 
	 public static BufferedImage loadImage(String path)
	 {
		 BufferedImage map = null;
		 try {
			map = ImageIO.read(ImageUtil.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	 }
	 
	 public static ByteBuffer convertImageData(BufferedImage bi) {
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    try {
		        ImageIO.write(bi, "png", out);
		        return ByteBuffer.wrap(out.toByteArray());
		    } catch (IOException ex) {
		        //TODO
		    }
		    return null;
		}
}
