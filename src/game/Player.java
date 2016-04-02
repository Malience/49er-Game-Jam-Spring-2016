package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_I;

import com.base.engine.atmospherics.AtmosphericsComponent;
import com.base.engine.audio.AudioEngine;
import com.base.engine.components.Listener;
import com.base.engine.components.attachments.Atmospherical;
import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.components.attachments.Updatable;
import com.base.engine.components.movenlook.Camera;
import com.base.engine.components.movenlook.DualBoundedLook;
import com.base.engine.components.movenlook.PlanetLook;
import com.base.engine.components.movenlook.JumpMove;
import com.base.engine.components.movenlook.LockedYMove;
import com.base.engine.components.movenlook.MoveComponent;
import com.base.engine.components.movenlook.PlanetJump;
import com.base.engine.core.GameObject;
import com.base.engine.core.World;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.PhysicsEngine;
import com.base.engine.physics.RigidBody.RigidBody;
import com.base.engine.physics.RigidBody.ForceGenerators.ConnectedPlanet;
import com.base.engine.physics.collision.Primitive;
import com.base.engine.physics.collision.Sphere;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Window;
import com.base.engine.rendering.UI.UIBarRect;
import com.base.engine.rendering.UI.UIBarRectBoundedScaled;
import com.base.engine.rendering.UI.UIInventory;
import com.base.engine.rendering.UI.UIInventoryItem;
import com.base.engine.rendering.UI.UIInventoryItemRect;
import com.base.engine.rendering.UI.UIInventoryItemRectSpecific;
import com.base.engine.rendering.UI.UIObject;
import com.base.engine.rendering.UI.UIRect;
import com.base.engine.rendering.UI.UIText;

public class Player extends GameObject {
	boolean alive = true;
	Camera camera;
	RigidBody body;
	Primitive collider;
	public UIInventory inventory;
	Vitals vitals;
	OxygenTank tank = null;
	PlanetLook look;
	PlanetJump jump;

	public Player() {
		camera = new Camera((float) Math.toRadians(70.0f), (float) Window.getWidth() / (float) Window.getHeight(),
				0.01f, 1000.0f);
		body = new RigidBody(20, .2f, 0);
		collider = new Sphere(4);
		// collider1 = new Box(new Vector3f(1,2,1));
		// collider = new Capsule(1,2);
		// FreeMove move = new FreeMove(25);
		LockedYMove move = new LockedYMove(20);
		// FreeLook look = new FreeLook(0.5f);
		// StandardLook look = new StandardLook(0.5f);
		// InteractionTest test = new InteractionTest();
		jump = new PlanetJump(40, body);
		inventory = new UIInventory(this);
		UIInventoryItem item = new UIInventoryItemRect("bricks.png", new Vector2f(30, 30));
		inventory.add(item);

		UIInventoryItem item2 = new UIInventoryItemRectSpecific("bricks2.png", new Vector2f(30, 30), "Head");
		inventory.add(item2);

		inventory.deactivate();
		AtmosphericEffects atmos = new AtmosphericEffects(this);

		vitals = new Vitals();
		RenderingEngine.addUI(vitals);

		body.attach(this);
		collider.attach(body);

		RenderingEngine.mainCamera = camera;
		World.world.focus = this;

		this.addComponent(camera);
		this.addComponent(body);
		this.addComponent(collider);
		// this.addComponent(collider2);
		this.addComponent(move);
		// this.addComponent(look);
		// this.addComponent(test);
		this.addComponent(jump);
		this.addComponent(atmos);

		//PhysicsEngine.addForce(body, "Gravity");
		//PhysicsEngine.addForce(body, "planet1");


		look = new PlanetLook(0.5f, null);

		this.addComponent(look);

		
		inventory.setAction("Inventory", GLFW_KEY_I);
		RenderingEngine.ui.addChild(inventory);
		World.menu.addMenu(inventory);
		vitals.generate();
	}

	private class Vitals extends UIObject {
		UIObject hploc = new UIObject();
		UIObject o2loc = new UIObject();
		UIObject healthloc = new UIObject();
		UIObject oxygenloc = new UIObject();
		UIObject backloc = new UIObject();
		UIObject hnumloc = new UIObject();
		UIObject onumloc = new UIObject();

		UIText hp = new UIText("HP", 20);
		UIText o2 = new UIText("O2", 20);

		UIBarRect health = new UIBarRectBoundedScaled("red.png", 400, 3, 100);
		UIBarRect oxygen = new UIBarRectBoundedScaled("blue.png", 400, 3, 100);

		NumberBar hnum = new NumberBar(health, 15);
		NumberBar onum = new NumberBar(oxygen, 15);

		UIRect background = new UIRect("black.png", 220, 17);

		public Vitals() {
			this.addChild(hploc);
			this.addChild(o2loc);
			hploc.addChild(healthloc);
			o2loc.addChild(oxygenloc);
			this.addChild(backloc);
			healthloc.addChild(hnumloc);
			oxygenloc.addChild(onumloc);

			hploc.addComponent(hp);
			o2loc.addComponent(o2);
			healthloc.addComponent(health);
			oxygenloc.addComponent(oxygen);
			backloc.addComponent(background);
			hnumloc.addComponent(hnum);
			onumloc.addComponent(onum);

			this.getTransform().setPos(0, 0, 0);
			hploc.getTransform().setPos(0, Window.height - 20, 0);
			healthloc.getTransform().setPos(30, 10, 0);
			o2loc.getTransform().setPos(0, Window.height - 35, 0);
			oxygenloc.getTransform().setPos(30, 10, 0);
			backloc.getTransform().setPos(220, Window.height - 17, 0);
			hnumloc.getTransform().setPos(200, -7, 0);
			onumloc.getTransform().setPos(200, -7, 0);

			hp.priority = 2;
			o2.priority = 2;
			health.priority = 1;
			oxygen.priority = 1;
			background.priority = 0;
			hnum.priority = 3;
			onum.priority = 3;
		}

		public UIBarRect getHealth() {
			return health;
		}

		public UIBarRect getOxygen() {
			return oxygen;
		}

		public void addHealth(float amount) {
			health.add(amount);
		}

		public void subHealth(float amount) {
			health.sub(amount);
		}

		public void addOxygen(float amount) {
			oxygen.add(amount);
		}

		public void subOxygen(float amount) {
			oxygen.sub(amount);
		}

		public void generate() {
			hp.generate();
			o2.generate();
			health.generate();
			oxygen.generate();
			background.generate();
		}

		private class NumberBar extends UIText implements UIRenderable, Updatable {
			UIBarRect bar;

			public NumberBar(UIBarRect bar, float size) {
				super("", size);
				this.bar = bar;
			}

			@Override
			public int update(float delta) {
				this.text = "" + bar.getAmount();
				bar.generate();
				return 1;
			}
		}
	}

	private class AtmosphericEffects extends AtmosphericsComponent implements Atmospherical {
		Player player;

		public AtmosphericEffects(Player player) {
			this.player = player;
		}

		@Override
		public int effect(float delta) {
			float damage = (20 - enviro.getAtmosphere().getO2Level()) * 10f;
			if (tank == null) {
				UIInventoryItem backItem = inventory.getSpecificItem("Back");
				if (backItem instanceof OxygenTank) {
					tank = (OxygenTank) backItem;
					UIObject gaugeObject = new UIObject();
					gaugeObject.addComponent(tank.getGauge());
					tank.getGauge().getTransform().setPos(700, 500, 0);
					RenderingEngine.addUI(gaugeObject);
				}
			}
			if (!enviro.isBreathable() && (tank == null || !tank.breath(delta * .4f * damage))) {

				player.vitals.subOxygen(delta * .4f * damage);
				if (player.vitals.getOxygen().getAmount() <= 0) {
					player.vitals.subHealth(delta * .1f * damage);
					if (player.vitals.getHealth().getAmount() <= 0) {
						// System.out.println("Dead");
					}
				}
			} else {
				player.vitals.addOxygen(delta * 20f);
				player.vitals.addHealth(delta * .01f);
			}
			return 1;
		}

	}
	
	public void setPlanet(ConnectedPlanet planet)
	{
		if(this.look.getPlanet() != null) PhysicsEngine.removeForce(body, this.look.getPlanet());
		this.look.setPlanet(planet);
		this.jump.setPlanet(planet);
		PhysicsEngine.addForce(body, planet);
	}
	
	public boolean checkPulse() {
		return alive;
	}

	public int update(float delta) {

		return 1;
	}

	public Camera getCamera() {
		return camera;
	}
}
