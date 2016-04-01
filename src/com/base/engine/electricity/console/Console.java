package com.base.engine.electricity.console;

import java.util.ArrayList;

import com.base.engine.components.GameComponent;
import com.base.engine.components.InteractableCollision;
import com.base.engine.components.MeshRenderer;
import com.base.engine.components.attachments.Interactable;
import com.base.engine.components.attachments.Menu;
import com.base.engine.core.GameObject;
import com.base.engine.core.Shapes;
import com.base.engine.core.World;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.collision.Box;
import com.base.engine.physics.collision.Collider;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.MaterialAttachment;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.RenderGenerator;
import com.base.engine.rendering.Texture;

public abstract class Console extends GameObject {
	private int current;
	protected MenuHolder menus;
	private final static RenderGenerator render = new RenderGenerator("Console001.obj", "Console001.png",
			new MaterialAttachment[] { new MaterialAttachment("specularIntensity", 1.0f),
					new MaterialAttachment("specularPower", 30.0f) });
	private MeshRenderer mesh;
	private Collider collider;

	protected final static ConsoleMenu BLANK_SCREEN = new ConsoleMenu();

	public Console() {
		mesh = render.generate();

		collider = new Box(new Vector3f(1f, 1f, 1f));

		// this.getTransform().setScale(.0275f);
		this.addComponent(mesh);
		this.addComponent(collider);
		this.addComponent(new ConsoleControl(collider));

		menus = new MenuHolder();
		this.addChild(menus);
	}

	private class ConsoleControl extends InteractableCollision implements Interactable {
		public ConsoleControl(Collider collider) {
			super(collider);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void interact() {
			open();
		}

	}

	private class MenuHolder extends GameObject {
		ArrayList<ConsoleMenu> menuList;

		public MenuHolder() {
			menuList = new ArrayList<ConsoleMenu>();
		}

		public void add(ConsoleMenu menu) {
			menuList.add(menu);
			this.addChild(menu);
		}

		public ConsoleMenu get(int index) {
			return menuList.get(index);
		}
	}

	public void open() {
		open(current);
	}

	private void open(int menu) {
		World.world.switchMenu(menus.get(current));
	}

	protected void addMenu(ConsoleMenu menu) {
		menus.add(menu);
	}
}
