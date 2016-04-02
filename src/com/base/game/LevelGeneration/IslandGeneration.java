package com.base.game.LevelGeneration;

import java.util.Random;

import com.base.engine.core.math.Vector3f;

public class IslandGeneration
{
	private class Island
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
		
		public String toString()
		{
			return "(" + location.x + ", " + location.y + ", " + location.z + ")";
		}
	}
	
	private Random rand;
	private Graph<Island> graph;
	private final float discountFactor = .5f;
	
	
	public IslandGeneration(int size, int maxDepth, float minBridgeLength, float maxBridgeLength)
	{
		long seed = 1337;
		//long seed = new Random().nextLong();
		rand = new Random(seed);		
		
		graph = new Graph<>();
		
		Island main = null;
		Island prev = null;
		
		for(int i = 0; i < size; i++)
		{
			Island island;
			
			if(prev == null)
			{
				island = new Island(new Vector3f(0, 0, 0));
				graph.add(island);
				
				main = island;
				
			}
			else
			{
				
				if(getChance(prev))
				{	
					int quadrant = getNewQuadrant(prev);
					
					island = new Island(prev, getNewVector(quadrant));
					
					graph.addEdge(island.parent, island, 0.0f);
				}
				else
				{
					int quadrant = getNewQuadrant(main);
					
					island = new Island(main, getNewVector(quadrant));
										
					graph.addEdge(island.parent, island, 0.0f);
					
				}
			}
			
			prev = island;
			
		}
		
		
	}
	
	public boolean getChance(Island parent)
	{
		float prob = (float)Math.pow(discountFactor, parent.depth);		
		return parent.depth == 0 ? true : prob >= rand.nextFloat();
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
