package com.base.game.LevelGeneration;

import com.base.engine.core.math.Vector3f;


public class Island
{
	public int depth;
	public Island parent;
	public int quadrant;
	public Vector3f location;
	
	public Island(Vector3f vec)
	{
		this.location = vec;
		this.depth = 0;
	}
	
	public Island(Island parent, Vector3f vec)
	{
		this.parent = parent;
		this.location = parent == null ? vec : parent.location.add(vec);
		this.depth = parent.depth + 1;
		
		if(parent != null)
		{
			float x = parent.location.x;
			float y = parent.location.y;
			
			if(x > 0.0f && y > 0.0f)
			{
				parent.quadrant = 1;
			}
			else if(x < 0.0f && y > 0.0f)
			{
				parent.quadrant = 2;
			}
			else if(x < 0.0f && y < 0.0f)
			{
				parent.quadrant = 3;
			}
			else
			{
				parent.quadrant = 4;
			}
		}
		
	}
	
	public boolean equals(Island other)
	{
		return this.location.equals(other);
	}
	
	public String toString()
	{
		return "(" + location.x + ", " + location.y + ", " + location.z + ")";
	}
}