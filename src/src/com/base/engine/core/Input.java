package com.base.engine.core;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

import com.base.engine.components.attachments.Controlable;
import com.base.engine.components.attachments.Draggable;
import com.base.engine.components.attachments.DropLocation;
import com.base.engine.core.math.Vector2f;
import com.base.engine.rendering.Window;

public class Input
{
	static World world = World.world;
	
	static ArrayList<Controlable> inputs;
	static ArrayList<DropLocation> dropLocations;
	static ArrayList<Draggable> drags;
	static Draggable dragging;
	
	final static int NUM_KEYS = 338;
	final static int NUM_MOUSE = 8;
	
	static boolean[] currentKeys = new boolean[NUM_KEYS];
	static boolean[] currentMouse = new boolean[NUM_MOUSE];
	
	static boolean[] prevKeys = new boolean[NUM_KEYS];
	static boolean[] prevMouse = new boolean[NUM_MOUSE];
	
	static long MainWindow;
	
	private static HashMap<String, Integer> keyBindings;
	private static HashMap<String, Integer> mouseBindings;
	
	//private static GLFWKeyCallback keyCallback;
	
	private static DoubleBuffer mx;
	private static DoubleBuffer my;
	
	private static Vector2f currentMousePos;
	private static Vector2f prevMousePos;
	
	public static void init(long window)
	{
		MainWindow = window;
		
		mx = BufferUtils.createDoubleBuffer(1);
		my = BufferUtils.createDoubleBuffer(1);
		/*
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
            }
        });
        */
		
		keyBindings = new HashMap<String, Integer>();
		mouseBindings = new HashMap<String, Integer>();
		
		setKeyBinding("Escape", GLFW_KEY_ESCAPE);
		setKeyBinding("Interact", GLFW_KEY_E);
		setKeyBinding("Break", GLFW_KEY_SPACE);
	}
	
	public static void gather()
	{
		inputs = world.getControlable();
		drags = world.getDraggable();
		dropLocations = world.getDropLocations();
	}
	
	public static void input(float delta)
	{
		for(Controlable input : inputs)
		{
			if(input.isActive())input.input(delta);
		}
	}
	
	public static void drag(float delta)
	{
		//CoreEngine.debugBreak();
		Vector2f mousePos = getCurrentMousePosition();
		if(dragging != null)
		{
			if(!dragging.isActive()){dragging = null; return;} //You shouldn't be dragging that
			if(getMouseReleased(0))
			{
				dragging.drop(delta, mousePos, dropLocations);
				dragging = null;
				return;
			}
			dragging.dragging(delta, mousePos);
			return;
		}
		else
		{
			if(getMousePressed(0))
			{
				for(Draggable drag : drags)
				{
					if(drag.isActive() && drag.drag(delta, mousePos)) 
					{
						dragging = drag;
						//dragging.dragging(delta, mousePos); //MAYBE
						return;
					}
				}
			}
		}
	}
	
	public static void update()
	{
		for(int i = 0; i < NUM_KEYS; i++)
		{
			prevKeys[i] = currentKeys[i];
			currentKeys[i] = glfwGetKey(MainWindow, i) == 1;
		}
		for(int i = 0; i < NUM_MOUSE; i++)
		{
			prevMouse[i] = currentMouse[i];
			currentMouse[i] = glfwGetMouseButton(MainWindow, i) == 1;
		}
		prevMousePos = currentMousePos;
		currentMousePos = getMousePosition();
	}
	
	public static boolean getKey(int key)
	{
		return currentKeys[key];
	}
	
	public static boolean getKeyPressed(int key)
	{
		return currentKeys[key] && !prevKeys[key];
	}
	
	public static boolean getKeyHold(int key)
	{
		return currentKeys[key] && prevKeys[key];
	}
	
	public static boolean getKeyReleased(int key)
	{
		return !currentKeys[key] && prevKeys[key];
	}
	
	public static boolean getMouse(int mouse)
	{
		return currentMouse[mouse];
	}
	
	public static boolean getMousePressed(int key)
	{
		return currentMouse[key] && !prevMouse[key];
	}
	
	public static boolean getMouseHold(int key)
	{
		return currentMouse[key] && prevMouse[key];
	}
	
	public static boolean getMouseReleased(int key)
	{
		return !currentMouse[key] && prevMouse[key];
	}
	
	private static Vector2f getMousePosition()
	{
		glfwGetCursorPos(MainWindow, mx, my);
		return new Vector2f((float)mx.get(0), Window.height - (float)my.get(0));
	}
	
	public static Vector2f getCurrentMousePosition()
	{
		return currentMousePos;
	}
	
	public static Vector2f getPrevMousePosition()
	{
		return prevMousePos;
	}
	
	public static void setMousePosition(Vector2f pos)
	{
		glfwSetCursorPos(MainWindow, (double)pos.getX(), Window.height - (double)pos.getY());
	}
	
	public static void setCursor(boolean enabled)
	{
		if(enabled)
			glfwSetInputMode(MainWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
		else
			glfwSetInputMode(MainWindow, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}
	
	private static Vector2f centerPosition = new Vector2f(Window.getWidth()/2, Window.getHeight()/2);
	private static boolean mouseLocked = false;
	
	public static void calibrate()
	{
		centerPosition = new Vector2f(Window.getWidth()/2, Window.getHeight()/2);
	}
	
	public static void lockMouse()
	{
		setMousePosition(centerPosition);
		setCursor(false);
		mouseLocked = true;
	}
	
	public static void unlockMouse()
	{
		setCursor(true);
		mouseLocked = false;
	}
	
	public static void toggleMouse()
	{
		if(mouseLocked) unlockMouse();
		else lockMouse();
	}
	
	public static void setMouse(boolean lock)
	{
		if(!lock) unlockMouse();
		else lockMouse();
	}
	
	public static boolean isMouseLocked(){return mouseLocked;}
	public static Vector2f getCenter(){return centerPosition;}
	
	public static void setKeyBinding(String action, int key)
	{
		keyBindings.put(action, key);
	}
	
	public static void setMouseBinding(String action, int key)
	{
		mouseBindings.put(action, key);
	}
	
	public static boolean getBinding(String action)
	{
		int k = keyBindings.get(action);
		if(k == 0)
		{
			k = mouseBindings.get(action);
			if(k == 0) return false;
			return getMouse(k);
		}
		return getKey(k);
	}
	
	public static boolean getBindingPressed(String action)
	{
		if(action == null) return false;
		int k = keyBindings.get(action);
		if(k == 0)
		{
			k = mouseBindings.get(action);
			if(k == 0) return false;
			return getMousePressed(k);
		}
		return getKeyPressed(k);
	}
	
	public static boolean getBindingHold(String action)
	{
		int k = keyBindings.get(action);
		if(k == 0)
		{
			k = mouseBindings.get(action);
			if(k == 0) return false;
			return getMouseHold(k);
		}
		return getKeyHold(k);
	}
	
	public static boolean getBindingReleased(String action)
	{
		int k = keyBindings.get(action);
		if(k == 0)
		{
			k = mouseBindings.get(action);
			if(k == 0) return false;
			return getMouseReleased(k);
		}
		return getKeyReleased(k);
	}
}
