package game;

import com.base.engine.components.GameComponent;
import com.base.engine.components.attachments.Updatable;
import com.base.engine.components.lighting.DirectionalLight;
import com.base.engine.core.GameObject;
import com.base.engine.core.math.Vector3f;

public class Star extends GameObject
{
	DirectionalLight light;
	float delta;
	
	public Star(float color, float delta)
	{
		this.delta = delta;
		light = new DirectionalLight(new Vector3f(color,color,color), .8f);
		this.addComponent(light);
		this.addComponent(new Update(this));
	}
	
	private class Update extends GameComponent implements Updatable
	{
		private static final float amount = 1;
//		private static final Vector3f axis = new Vector3f(0,0,1);
		
		private Star star;
		public Update(Star star)
		{
			this.star = star;
		}
		
		@Override
		public int update(float delta)
		{
			star.getTransform().rotate(new Vector3f(1,0,0), star.delta * amount);
			return 1;
		}
	}
}
