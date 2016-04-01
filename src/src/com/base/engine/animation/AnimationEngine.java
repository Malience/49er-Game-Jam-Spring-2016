package com.base.engine.animation;

import java.util.ArrayList;

import com.base.engine.components.attachments.Animate;
import com.base.engine.core.World;

public class AnimationEngine 
{
	ArrayList<Animate> animations;
	
	public void gather()
	{
		animations = World.world.getAnimate();
	}
	
	public void playAnimations(float delta)
	{
		for(Animate animation : animations)
		{
			if(animation.isActive()) animation.playAnimation(delta);
		}
	}
}
