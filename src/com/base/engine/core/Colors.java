package com.base.engine.core;

import java.util.Random;

import com.base.engine.rendering.Texture;

public class Colors 
{
	public static final Texture black = new Texture("black.png");
	public static final Texture blue = new Texture("blue.png");
	public static final Texture brown = new Texture("brown.png");
	public static final Texture cyan = new Texture("cyan.png");
	public static final Texture green = new Texture("green.png");
	public static final Texture orange = new Texture("orange.png");
	public static final Texture red = new Texture("red.png");
	public static final Texture white = new Texture("white.png");
	public static final int numColors = 8;
	
	public static Texture randomColor()
	{
		return getColor(new Random().nextInt(numColors));
	}
	
	public static Texture getColor(int i)
	{
		switch(i)
		{
		case 0: return black;
		case 1: return blue;
		case 2: return brown;
		case 3: return cyan;
		case 4: return green;
		case 5: return orange;
		case 6: return red;
		case 7: return white;
		}
		return null;
	}
}
