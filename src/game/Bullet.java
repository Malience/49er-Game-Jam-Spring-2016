package game;

import com.base.engine.core.GameObject;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.RigidBody.RigidBox;

public class Bullet extends GameObject
{
	private Clip clip;
	private float speed;
	private float distance;
	public RigidBox body;
	
	public Bullet(Clip clip, float speed)
	{
		this.clip = clip;
		this.speed = speed;
		//body = new RigidBox();
	}
	
	public void shoot(Vector3f pos, Vector3f dir, float distance)
	{
		this.distance = distance;
	}
	
	
}
