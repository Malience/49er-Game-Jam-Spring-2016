package game;

import com.base.engine.components.MeshRenderer;
import com.base.engine.core.GameObject;
import com.base.engine.core.Shapes;
import com.base.engine.physics.PhysicsEngine;
import com.base.engine.physics.PremadeObjects.StaticSphere;
import com.base.engine.physics.RigidBody.ForceGenerators.ConnectedPlanet;
import com.base.engine.physics.RigidBody.ForceGenerators.Planet;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Texture;

public class TestPlanet extends GameObject
{
	public ConnectedPlanet planet;
	private StaticSphere sphere;
	
	public TestPlanet(float gravity, float radius)
	{
		this.planet  = new ConnectedPlanet(this, gravity);
		this.sphere = new StaticSphere(radius);
		
		this.addChild(sphere);
		//GameObject object = new GameObject();
		//Material mat = new Material();
		//mat.addTexture("diffuse", new Texture("test.png"));
		//object.addComponent(new MeshRenderer(Shapes.sphere(), mat));
		//object.getTransform().setScale(radius);
		//this.addChild(object);
		
		PhysicsEngine.addForce(planet);
	}
	
	public void setTexture(Texture texture)
	{
		sphere.setTexture(texture);
	}
}
