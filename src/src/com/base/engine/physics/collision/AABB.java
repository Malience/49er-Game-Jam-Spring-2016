package com.base.engine.physics.collision;

import com.base.engine.components.attachments.Collidable;
import com.base.engine.core.math.Vector3f;

public class AABB extends Primitive implements Collidable
{
	Vector3f min;
	Vector3f max;
	public AABB(Vector3f min, Vector3f max)
	{
		this.min = min;
		this.max = max;
	}
	
	public Vector3f greatestMax(AABB other)
	{
		int compare = max.compareTo(other.max);
		if(compare > 0) return max;
		else if(compare < 0) return other.max;
		float x = max.x > other.max.x ? max.x : other.max.x;
		float y = max.y > other.max.y ? max.y : other.max.y;
		float z = max.z > other.max.z ? max.z : other.max.z;
		return new Vector3f(x,y,z);
	}
	
	public Vector3f leastMin(AABB other)
	{
		int compare = min.compareTo(other.min);
		if(compare < 0) return min;
		else if(compare > 0) return other.min;
		float x = min.x < other.min.x ? min.x : other.min.x;
		float y = min.y < other.min.y ? min.y : other.min.y;
		float z = min.z < other.min.z ? min.z : other.min.z;
		return new Vector3f(x,y,z);
	}
	
	public AABB combine(AABB other)
	{
		Vector3f max = this.greatestMax(other);
		Vector3f min = this.leastMin(other);
		return new AABB(min, max);
	}
}
