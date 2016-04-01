package com.base.engine.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import com.base.engine.components.MenuComponent;
import com.base.engine.components.attachments.Animate;
import com.base.engine.components.attachments.Atmospherical;
import com.base.engine.components.attachments.BroadCollidable;
import com.base.engine.components.attachments.Collidable;
import com.base.engine.components.attachments.Controlable;
import com.base.engine.components.attachments.Draggable;
import com.base.engine.components.attachments.DropLocation;
import com.base.engine.components.attachments.EnviromentalComponent;
import com.base.engine.components.attachments.Interactable;
import com.base.engine.components.attachments.LightAttachment;
import com.base.engine.components.attachments.Menu;
import com.base.engine.components.attachments.Physical;
import com.base.engine.components.attachments.Renderable;
import com.base.engine.components.attachments.Tickable;
import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.components.attachments.Updatable;
import com.base.engine.core.math.Point;
import com.base.engine.core.math.SpatialHash;
import com.base.engine.core.math.Tuple;
import com.base.engine.core.math.Vector3f;
import com.base.game.TestGame;

import game.OptionsMenu;

public class World {
	/* WORLD CONSTANTS */
	public final static Vector3f X_AXIS = new Vector3f(1, 0, 0);
	public final static Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	public final static Vector3f Z_AXIS = new Vector3f(0, 0, 1);

	public final static float tileHalfSize = 2.5f;

	public final static float[] clearColor = new float[] { 0f, 1f, 1f, 0f }; // TEST
																				// MODE
	// public final static float[] clearColor = new float[]{0f, 0f, 0f, 0f};
	// //RELEASE MODE

	/*-----------------*/

	public static World world = new World();
	public static long seed;
	public static Random r;

	private SpatialHash spatialHash;
	private ArrayList<GameObject> bucket;
	private ArrayList<Tuple> activeCells;
	public GameObject focus;
	private Game map;
	ArrayList<Updatable> updates;
	ArrayList<Tickable> ticks;
	ArrayList<GameObject> removeList;
	public static MenuComponent menu = new MenuComponent();

	public World() {
		spatialHash = new SpatialHash(100);
		bucket = new ArrayList<GameObject>();
		removeList = new ArrayList<GameObject>();
		map = new TestGame();
	}

	public void init() {
		if (seed == 0)
			seed = new Random().nextLong();
		r = new Random(seed);

		map.init();

		GameObject menuObject = new GameObject();

		menuObject.addComponent(menu);
		this.addToBucket(menuObject);

		OptionsMenu optionsMenu = new OptionsMenu();
		MenuComponent.optionsMenu = optionsMenu;
		this.addToBucket(optionsMenu);
	}

	public void add(GameObject object) {
		spatialHash.insert(object, object.getPosition());
	}

	public void addToBucket(GameObject object) {
		bucket.add(object);
	}

	public void refreshActives() {
		activeCells = spatialHash.getCellsAdjacent(spatialHash.hash(focus.getPosition()));
	}

	public void remove(GameObject object) {
		removeList.add(object);
	}

	public void remove() {
		for (GameObject object : removeList)
			if (!bucket.remove(object))
				spatialHash.remove(object);
	}

	// Convenience Methods!
	public void updateObjects() {
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				object.update();
		}
	}

	public void gather() {
		updates = this.getUpdatable();
		ticks = this.getTickable();
	}

	private float tick = 0;
	private final static float ticklength = 1.0f;

	public void update(float delta) {
		for (Updatable update : updates) {
			if (update.isActive())
				update.update(delta);
		}
		tick += delta;

		if (tick >= ticklength) {
			for (Tickable tick : ticks) {
				if (tick.isActive())
					tick.tick();
				;
			}
			tick -= ticklength;
		}
	}

	public void addMenu(Menu menu) {
		this.menu.addMenu(menu);
	}

	public void switchMenu(Menu menu) {
		this.menu.switchMenu(menu);
	}

	public ArrayList<GameObject> getGameObjects() {
		ArrayList<GameObject> out = new ArrayList<GameObject>();
		ArrayList<Point> points = spatialHash.getAll();
		for (Point point : points) {
			out.addAll(point.getType("GameObject"));
		}
		out.addAll(bucket);
		return out;
	}

	public ArrayList<GameObject> getGameObjects(ArrayList<Tuple> cells) {
		ArrayList<GameObject> out = new ArrayList<GameObject>();
		ArrayList<Point> points = spatialHash.getAll();// get(cells);
		for (Point point : points) {
			out.addAll(point.getType("GameObject"));
		}
		out.addAll(bucket);
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Renderable> getRenderable() {
		ArrayList<Renderable> out = new ArrayList<Renderable>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends Renderable>) object.getAllComponentsOf("Renderable"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<UIRenderable> getUIRenderable() {
		ArrayList<UIRenderable> out = new ArrayList<UIRenderable>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends UIRenderable>) object.getAllComponentsOf("UIRenderable"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Controlable> getControlable() {
		ArrayList<Controlable> out = new ArrayList<Controlable>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends Controlable>) object.getAllComponentsOf("Controlable"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Physical> getPhysical() {
		ArrayList<Physical> out = new ArrayList<Physical>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends Physical>) object.getAllComponentsOf("Physical"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Collidable> getCollidable() {
		ArrayList<Collidable> out = new ArrayList<Collidable>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends Collidable>) object.getAllComponentsOf("Collidable"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<LightAttachment> getLightAttachments() {
		ArrayList<LightAttachment> out = new ArrayList<LightAttachment>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends LightAttachment>) object.getAllComponentsOf("LightAttachment"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Updatable> getUpdatable() {
		ArrayList<Updatable> out = new ArrayList<Updatable>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends Updatable>) object.getAllComponentsOf("Updatable"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Interactable> getInteractables() {
		ArrayList<Interactable> out = new ArrayList<Interactable>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends Interactable>) object.getAllComponentsOf("Interactable"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Animate> getAnimate() {
		ArrayList<Animate> out = new ArrayList<Animate>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends Animate>) object.getAllComponentsOf("Animate"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Draggable> getDraggable() {
		ArrayList<Draggable> out = new ArrayList<Draggable>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends Draggable>) object.getAllComponentsOf("Draggable"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<DropLocation> getDropLocations() {
		ArrayList<DropLocation> out = new ArrayList<DropLocation>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends DropLocation>) object.getAllComponentsOf("DropLocation"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Atmospherical> getAtmospherics() {
		ArrayList<Atmospherical> out = new ArrayList<Atmospherical>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends Atmospherical>) object.getAllComponentsOf("Atmospherical"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<EnviromentalComponent> getEnviromentals() {
		ArrayList<EnviromentalComponent> out = new ArrayList<EnviromentalComponent>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends EnviromentalComponent>) object
						.getAllComponentsOf("EnviromentalComponent"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<BroadCollidable> getBroadCollidables() {
		ArrayList<BroadCollidable> out = new ArrayList<BroadCollidable>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends BroadCollidable>) object.getAllComponentsOf("BroadCollidable"));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Tickable> getTickable() {
		ArrayList<Tickable> out = new ArrayList<Tickable>();
		ArrayList<GameObject> objects = getGameObjects(activeCells);
		for (GameObject object : objects) {
			if (object.isActive())
				out.addAll((Collection<? extends Tickable>) object.getAllComponentsOf("Tickable"));
		}
		return out;
	}
}
