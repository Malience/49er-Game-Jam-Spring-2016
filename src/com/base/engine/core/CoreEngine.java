package com.base.engine.core;

import static com.base.engine.rendering.Window.createMainWindow;
import static com.base.engine.rendering.Window.dispose;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

import org.lwjgl.opengl.GL;

import com.base.engine.animation.AnimationEngine;
import com.base.engine.atmospherics.AtmosphericsEngine;
import com.base.engine.audio.AudioEngine;
import com.base.engine.physics.PhysicsEngine;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Window;

public class CoreEngine {
	private static CoreEngine engine;
	private boolean isRunning;
	private long MainWindow;
	private World world;
	private RenderingEngine renderingEngine;
	private PhysicsEngine physicsEngine;
	private AnimationEngine animationEngine;
	private AtmosphericsEngine atmosphericsEngine;
	private AudioEngine audioEngine;
	private int width;
	private int height;
	private String name;
	private double frameTime;
	// TODO Edit mode
	@SuppressWarnings("unused")
	private boolean editMode;

	public CoreEngine(int width, int height, String name, double framerate) {
		this(width, height, name, framerate, false);
	}

	public CoreEngine(int width, int height, String name, double framerate, boolean editMode) {
		this.world = World.world;
		this.width = width;
		this.height = height;
		this.name = name;
		this.frameTime = 1.0 / framerate;
		this.editMode = editMode;

		engine = this;

		start();
	}

	private void start() {
		glfwInit();

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

		MainWindow = createMainWindow(width, height, name);

		glfwMakeContextCurrent(MainWindow);
		glfwSwapInterval(1);

		glfwShowWindow(MainWindow);

		run();
	}

	private void stop() {
		isRunning = false;
	}

	private void run() {
		isRunning = true;

		GL.createCapabilities();
		System.out.println(glGetString(GL_VERSION));

		Input.init(MainWindow);

		renderingEngine = new RenderingEngine();
		physicsEngine = new PhysicsEngine();
		animationEngine = new AnimationEngine();
		atmosphericsEngine = new AtmosphericsEngine();
		audioEngine = new AudioEngine();
		world.init();

		Input.lockMouse();

		int frames = 0;
		double frameCounter = 0;

		double lastTime = Time.getTime();
		double unprocessedTime = 0;

		while (isRunning && glfwWindowShouldClose(MainWindow) == GL_FALSE) {
			boolean render = false;

			double startTime = Time.getTime();
			double passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessedTime += passedTime;
			frameCounter += passedTime;

			world.remove();
			world.refreshActives();
			//world.updateObjects();
			physicsEngine.startFrame();
			// Gather Resources
			Input.gather();
			renderingEngine.gather();
			physicsEngine.gather();
			animationEngine.gather();
			atmosphericsEngine.gather();
			audioEngine.gather();
			world.gather();

			while (unprocessedTime > frameTime) {
				render = true;

				unprocessedTime -= frameTime;

				if (glfwWindowShouldClose(MainWindow) == GL_TRUE)
					stop();

				glfwPollEvents();

				Input.update();
				Input.input((float) frameTime);
				Input.drag((float) frameTime);
				Interaction.gather();
				Interaction.interact();

				if (frameCounter >= 1.0) {
					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}

			if (render) {
				// Physics Engine
				physicsEngine.integrate((float) frameTime);
				physicsEngine.simulate((float) frameTime);
				physicsEngine.generateContacts((float) frameTime);
				physicsEngine.handleCollisions((float) frameTime);

				// Animation
				animationEngine.playAnimations((float) frameTime);
				// Atmospherics
				atmosphericsEngine.setupEnviroments();
				// atmosphericsEngine.simulate((float)frameTime);
				// Rendering
				renderingEngine.render();
				// Audio
				audioEngine.play();
				Window.render(MainWindow);
				
				world.update((float) frameTime);
				frames++;
				// System.out.println(Input.getMousePosition());
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

		cleanUp();
	}

	private void cleanUp() {
		dispose(MainWindow);
		audioEngine.cleanUp();
	}

	public RenderingEngine getRenderingEngine() {
		return renderingEngine;
	}

	public static void debugBreak() {
		if (Input.getBindingPressed("Break")) {
			int i = 0;
			i = i + 1;
		}
	}

	public static void exit(int i) {
		engine.isRunning = false;
		if (i == 0) {
			new Exception().printStackTrace();
		}
	}
}
