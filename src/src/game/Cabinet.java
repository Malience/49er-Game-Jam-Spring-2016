package game;


import com.base.engine.components.InteractableCollision;
import com.base.engine.components.MeshRenderer;
import com.base.engine.components.attachments.Interactable;
import com.base.engine.core.GameObject;
import com.base.engine.core.World;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.collision.Box;
import com.base.engine.physics.collision.Collider;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.UI.UIContainer;
import com.base.engine.rendering.UI.UIInventoryItem;

public class Cabinet extends GameObject
{
	Box box;
	Interact ic;
	MeshRenderer renderer;
	Vector3f halfSize;
	UIContainer container;
	
	public Cabinet()
	{
		halfSize = new Vector3f(1,1,1);
		box = new Box(halfSize);
		box.setActive(false);
		ic = new Interact(box);
		Material mat = new Material();
		mat.addTexture("diffuse", new Texture("test.png"));
		renderer = new MeshRenderer(new Mesh("cabinet.obj"), mat);
		
		container = new UIContainer((Player)World.world.focus);
		container.addSlots(1);
		
		World.world.addMenu(container);
		RenderingEngine.addUI(container);
		
		UIInventoryItem item3 = new OxygenTank("vOxygenGauge001.png", new Vector2f(30,30));
		container.add(item3);
		
		this.addComponent(box);
		this.addComponent(ic);
		this.addComponent(renderer);
	}
	
	private class Interact extends InteractableCollision implements Interactable
	{

		public Interact(Collider collider) {
			super(collider);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void interact()
		{
			World.world.switchMenu(container);
		}
		
	}
}
