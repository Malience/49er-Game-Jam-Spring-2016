package game;

import java.util.Stack;

import com.base.engine.core.GameObject;

public class Clip extends GameObject
{
	Stack<Bullet> bullets;
	public Clip(int size, float speed)
	{
		bullets = new Stack<Bullet>();
		
		for(int i = 0; i < size; i++)
		{
			bullets.add(new Bullet(this, speed));
		}
	}
}
