package com.base.engine.rendering.UI;

import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.core.math.Vector2f;
import com.base.engine.rendering.RenderingEngine;

public class UIBarCircle extends UIElement implements UIRenderable
{
	float radius;
	float current; //Must be between 0 and 100
	float start;
	UIMesh mesh = new UIMesh("vOxygenGauge001.obj");
	
	public UIBarCircle(String texturePath, float radius){this(texturePath, radius, 0);}
	public UIBarCircle(String texturePath, float radius, float start)
	{
		super(texturePath);
		this.radius = radius;
		current = 100;
		this.start = start;
	}
	
	//EQUATION OF A CIRCLE AT POSITION (h,k) WITH RADIUS R
	// y = -(sqrt(r^2 - x^2 - 2hx - h^2) - k)
	private Vector2f getVertex(float x, boolean negative)
	{
		if(negative) return new Vector2f(-((float)Math.sqrt(radius*radius - x*x )), x);
		return new Vector2f(((float)Math.sqrt(radius*radius - x*x)), x);
	}
	
	private Vector2f getVertex(float h, float k, float x, boolean negative)
	{
		if(negative) return new Vector2f(-((float)Math.sqrt(radius*radius - x*x + 2*h*x - h*h) - k), x);
		return new Vector2f(((float)Math.sqrt(radius*radius - x*x + 2*h*x - h*h) - k), x);
	}
	
	@Override
	public void generate()
	{
		//More iterations for a more finely smoothed circle
		int iterations = 20; //minimum 3
		
		//Iterations are a quarter of the circle so double it and then the 4 cardinal points + the center point
		Vector2f[] vertices = new Vector2f[iterations*4 + 1];
		Vector2f[] texCoords = new Vector2f[vertices.length];
		int[] indices = new int[iterations*12];
		
		Vector2f position = this.getTransform().getPos().getXY();
		
		//The center of the circle
		float h = position.x;
		float k = position.y;
		
		vertices[0] = new Vector2f(h, k);
		texCoords[0] = new Vector2f(.5f,.5f); //Because the center's texture coord should always be the middle
		
		float x = -radius;
		float dx = radius/iterations;
		
		//Top Left
		for(int i = 1; i <= iterations; i++)
		{
			vertices[i] = getVertex(x, false);
			x += dx;
		}
		
		//Top Right
		for(int i = 1; i <= iterations; i++)
		{
			vertices[i+iterations] = new Vector2f(vertices[iterations+1-i]);
			vertices[i+iterations].x = -vertices[i+iterations].x;
		}
		
		//Bottom Right
		for(int i = 1; i <= iterations; i++)
		{
			vertices[i+iterations*2] = vertices[iterations+1-i].mul(-1);
		}
		
		//Top Right
		for(int i = 1; i <= iterations; i++)
		{
			vertices[i+iterations*3] = new Vector2f(vertices[iterations+1-i]);
			vertices[i+iterations*3].y = -vertices[i+iterations].y;
		}
		
		//TexCoords
		for(int i = 1; i <= iterations*4; i++)
		{
			texCoords[i] = vertices[i].add(radius).div(2*radius);
		}
		
		//Indices
		for(int i = 1; i <= iterations*4 - 1; i++)
		{
			indices[0 + 3*(i-1)] = 0;
			indices[1 + 3*(i-1)] = i + 1;
			indices[2 + 3*(i-1)] = i;
		}
		
		indices[iterations*11 + 0] = 0;
		indices[iterations*11 + 1] = 1;
		indices[iterations*11 + 2] = iterations*4;
		
		for(int i = 1; i <= iterations*4; i++)
		{
			vertices[i] = vertices[i].add(position);
		}
		
		this.vertices = vertices;
		this.texCoords = texCoords;
		this.indices = indices;
		//EVERY SINGLE FRAME SUCKERZ!!!!!!!!!!
	}
	
	@Override
	public int render(RenderingEngine renderingEngine)
	{
		super.render(renderingEngine);
		
		return 1;
	}
	
	public void set(float amount)
	{
		current = amount;
	}
	
	public void sub(float amount)
	{
		current -= amount;
	}
}
