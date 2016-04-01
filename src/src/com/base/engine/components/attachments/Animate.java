package com.base.engine.components.attachments;

public interface Animate extends ComponentAttachment
{
	public void playAnimation(float delta);
	public default void playAnimation(int animNum){playAnimation(animNum,0,0);}
	public void playAnimation(int animNum, float startTime, float transitionTime);
}
