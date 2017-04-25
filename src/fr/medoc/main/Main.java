package fr.medoc.main;


import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.opengl.Display;

import fr.medoc.main.game.Game;
import fr.medoc.main.render.DisplayManager;

public class Main {

	// is the game running
	boolean running = false;

	public static final double FRAME_CAP = 60;
	public static final double TICK_CAP = 60;
	Game game;

	public Main() {
		DisplayManager.create(640, 480, "Bubune 3D");
		game = new Game();
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}

	/**
	 * Starts the game loop
	 */
	public void start() {
		running = true;
		loop();
	}

	/**
	 * Stops the game loop
	 */
	public void stop() {
		running = false;
	}

	/**
	 * Closes the game
	 */
	public void exit() {
		DisplayManager.dispose();
		System.exit(0);
	}

	/**
	 * This is the game loop. It is relevant only if running = true
	 * 
	 * executes the update and render functions
	 * handles the frame_cap and tick_cap correctly to
	 * optimize the game
	 * 
	 * use Start() to start it
	 */
	public void loop() {
		
		long lastTickTime = System.nanoTime();
		long lastRenderTime = System.nanoTime();
		
		double tickTime = 1000000000.0 / TICK_CAP;
		double renderTime = 1000000000.0 / FRAME_CAP;
		
		int ticks = 0;
		int frames = 0;
		
		long timer = System.currentTimeMillis();
		
		while (running) {
			
			if (DisplayManager.isClosed()) stop();
			@SuppressWarnings("unused")
			boolean rendered = false;
			
			if (System.nanoTime() - lastTickTime > tickTime)
			{
				update();
				ticks += 1;
				lastTickTime += tickTime;
			} else if (System.nanoTime() - lastRenderTime > renderTime) {
				render();
				DisplayManager.update();
				frames += 1;
				rendered = true;
				lastRenderTime += renderTime;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				System.out.println(ticks + " ticks, " + frames + " fps");
				ticks = 0;
				frames = 0;
			}
		}
		exit();
	}

	
	/**
	 * render :
	 * everything that will be drawn on screen
	 */
	public void render() {
		if(Display.wasResized())
		{
			glViewport(0,0, Display.getWidth(), Display.getHeight());
		}
		DisplayManager.clearBuffers();
		
		game.render();
	}
	
	/**
	 * update:
	 * everything that will be calculated or handled, except render
	 */
	public void update()
	{
		game.update();
	}
}
