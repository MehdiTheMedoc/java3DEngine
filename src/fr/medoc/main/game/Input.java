package fr.medoc.main.game;

import java.util.HashMap;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;


public class Input {
	
	private static HashMap<String,Integer> keyBinding = new HashMap<String,Integer>();
	
	/**
	 * returns true if the bindedinput key is pressed
	 */
	public static boolean getInput(String input)
	{
		if(keyBinding.containsKey(input))
			return getKey(keyBinding.get(input));
		else
			return false;
	}
	
	/**
	 * returns true if the key is pressed
	 */
	public static boolean getKey(int key)
	{
		return Keyboard.isKeyDown(key);
	}
	
	/**
	 * returns the delta move vector of the mouse cursor on the screen
	 */
	public static Vector2f getMouseDelta()
	{
		return new Vector2f(Mouse.getDX(),Mouse.getDY());
	}
}
