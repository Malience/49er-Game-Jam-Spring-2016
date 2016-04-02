package com.base.game.LevelGeneration;

import java.util.Random;

public class IslandGeneration
{
	private class Island
	{
		public float angle;
		public Island island;
	}
	
	private Island[] islands;
	private Random rand;
	
	
	public IslandGeneration(int size, int maxDepth, float minBridgeLength, float maxBridgeLength)
	{
		//long seed;
		long seed = new Random().nextLong();
		rand = new Random(seed);
		
		islands = new Island[size];
		
		
		Graph<Island> graph = new Graph<>();
		
		for(int i = 0; i < size; i++)
		{
			Island island = new Island();
			
			float min = 0.0f;
			float max = 2 * (float)(Math.PI);
			island.angle = this.randomFloat(min, max);
			
			islands[i] = island;
			
			if(rand.nextBoolean())
			{
				final float f = 0.1f; 
				Island next = new Island();
				next.angle = this.randomFloat(min - f, max + f);
				islands[i].island = next;
			}
			
		}
		
		float[] bridgeLengths = new float[size - 1];
		
		for(int i = 0; i < bridgeLengths.length; i++)
		{
			bridgeLengths[i] = this.randomFloat(minBridgeLength, maxBridgeLength);
		}
		
	}
	
	public float randomFloat(float min, float max)
	{
		//Java's nextFloat returns a number from 0.0 to 1.0.  There is no
		//version that takes a range, so we have to do here.
		return rand.nextFloat() * (max - min) + min;
	}
}
