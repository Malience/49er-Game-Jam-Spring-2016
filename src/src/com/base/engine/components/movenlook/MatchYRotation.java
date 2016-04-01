package com.base.engine.components.movenlook;

import com.base.engine.components.GameComponent;
import com.base.engine.components.attachments.Updatable;
import com.base.engine.core.GameObject;
import com.base.engine.core.math.Vector3f;

public class MatchYRotation extends GameComponent implements Updatable
{
	GameObject match;
	
	public MatchYRotation(GameObject match)
	{
		this.match = match;
	}
	
	public int update(float delta)
	{
		if(match.getTransform().hasChanged())
		{
			Vector3f matchDir = match.getTransform().getRot().getRight().normal();
			Vector3f thisDir = this.getTransform().getRot().getRight().normal();
			float rot = (float)Math.acos(matchDir.dot(thisDir));
			match.getTransform().rotate(new Vector3f(0,1,0), (float)Math.toRadians(rot));
		}
		return 1;
	}
}
