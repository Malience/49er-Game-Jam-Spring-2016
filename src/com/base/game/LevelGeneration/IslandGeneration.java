package com.base.game.LevelGeneration;

import java.util.Random;

import com.base.engine.core.math.Vector3f;

public class IslandGeneration
{
	private class Island
	{
		public Island parent;
		public int quadrant;
		public Vector3f location;
		
		public Island(Vector3f vec)
		{
			this.location = vec;
		}
		
		public Island(Island parent, Vector3f vec)
		{
			this.parent = parent;
			this.location = parent == null ? vec : parent.location.add(vec);
			
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
		
		public String toString()
		{
			return "(" + location.x + ", " + location.y + ", " + location.z + ")";
		}
	}
	
	private Island[] islands;
	private Random rand;
	private Graph<Island> graph;
	
	
	public IslandGeneration(int size, int maxDepth, float minBridgeLength, float maxBridgeLength)
	{
		//long seed;
		long seed = new Random().nextLong();
		rand = new Random(seed);
		
		islands = new Island[size];
		
		
		graph = new Graph<>();
		
		Island prev = null;
		
		for(int i = 0; i < size; i++)
		{
			Island island;
			
			if(prev == null)
			{
				island = new Island(new Vector3f(0, 0, 0));
				graph.add(island);
				
			}
			else
			{
				int quadrant = getNewQuadrant(prev);
				island = new Island(prev, getNewVector(quadrant));
				graph.addEdge(prev, island, 0.0f);
			}		
			
			
			prev = island;
			
		}
		
		
	}
	
	public int getNewQuadrant(Island parent)
	{
		int quadrant = rand.nextInt(3) + 1;
		
		if(quadrant > 2)
		{			
			return quadrant - 2 == parent.quadrant ? getNewQuadrant(parent) : quadrant;
		}
		else
		{
			return quadrant + 2 == parent.quadrant ? getNewQuadrant(parent) : quadrant;
		}
	}
	
	public Vector3f getNewVector(int quadrant)
	{
		final float min = 50.0f, max = 100.0f;
		
		if(quadrant == 1)
		{
			return new Vector3f(randomFloat(min, max), randomFloat(min, max), 0);
		}
		else if(quadrant == 2)
		{
			return new Vector3f(randomFloat(-min, -max), randomFloat(min, max), 0);
		}
		else if(quadrant == 3)
		{
			return new Vector3f(randomFloat(-min, -max), randomFloat(-min, -max), 0);
		}
		else
		{
			return new Vector3f(randomFloat(min, max), randomFloat(-min, -max), 0);
		}
	}
	
	
	public float randomFloat(float min, float max)
	{
		//Java's nextFloat returns a number from 0.0 to 1.0.  There is no
		//version that takes a range, so we have to do here.
		return rand.nextFloat() * (max - min) + min;
	}
	
	public Graph<Island> getGraph()
	{
		return graph;
	}
}
