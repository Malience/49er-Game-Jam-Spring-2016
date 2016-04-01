package com.base.engine.rendering.UI;

import java.util.ArrayList;

import com.base.engine.components.attachments.ComponentAttachment;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;

import game.Player;

public class UIContainer extends UIMenu {
	public Player player;

	ArrayList<GenericSlot> slots;
	UIObject genericInventory;
	UIObject genericArealoc;
	UIRect genericArea;

	public UIContainer(Player player) {
		super();

		this.player = player;

		slots = new ArrayList<GenericSlot>();

		genericInventory = new UIObject();

		this.getTransform().setPos(new Vector3f(600, 300, 0));

		this.addChild(genericInventory);
		genericInventory.getTransform().setPos(new Vector3f(0, 0, 0));

	}

	private class GenericSlot extends UIObject {
		UIInventorySlot slot;

		public GenericSlot() {
			Vector2f halfSize = new Vector2f(32, 32);
			slot = new UIInventorySlot(halfSize);
			UIRect rect = new UIRect("ui-item.png", halfSize);
			rect.priority = 2;

			this.addComponent(slot);
			this.addComponent(rect);
		}
	}

	public boolean add(UIInventoryItem item) {
		for (GenericSlot slot : slots) {
			if (slot.slot.item == null) {
				slot.slot.item = item;
				item.current = slot.slot;
				return true;
			}
		}
		return false;
	}

	private static final float spacing = 5;
	private static final float itemDim = 64;
	private static final float rowLength = 5;

	public void addSlots(int num) {
		for (int i = 0; i < num; i++) {
			GenericSlot newSlot = new GenericSlot();
			slots.add(newSlot);
			genericInventory.addChild(newSlot);
		}
		if (genericArea == null) {
			genericArea = new UIRect("ui.png", new Vector2f(0, 0));
			genericArealoc = new UIObject();
			genericArealoc.addComponent(genericArea);
			genericInventory.addChild(genericArealoc);
			genericArea.priority = 1;
		}

		int rows = (int) Math.ceil(slots.size() / rowLength);
		float horizontal = (itemDim + spacing) * rowLength + spacing;
		float vertical = (itemDim + spacing) * rows + spacing;
		if (rows == 1)
			horizontal = (itemDim + spacing) * slots.size() + spacing;

		genericArea.halfSize = new Vector2f(horizontal / 2, vertical / 2);
		genericArealoc.getTransform().setPos(new Vector3f(horizontal / 2, -vertical / 2, 0));
		genericArea.generate();

		int first = slots.size() - num;
		for (int i = first; i < slots.size(); i++) {
			horizontal = spacing + (itemDim + spacing) * (i % rowLength);
			vertical = spacing + (itemDim + spacing) * ((float) Math.floor(i / rowLength));
			slots.get(i).getTransform().setPos(new Vector3f((horizontal + itemDim / 2), -(vertical + itemDim / 2), 0));
		}
	}

	public ArrayList<ComponentAttachment> getAllComponentsOf(String ca) {
		ArrayList<ComponentAttachment> out = super.getAllComponentsOf(ca);
		UIInventoryItem item = null;
		for (GenericSlot slot : slots) {
			item = slot.slot.item;
			if (item != null) {
				Class[] classes = item.getClass().getInterfaces();
				for (int i = 0; i < classes.length; i++) {
					if (classes[i].getSimpleName().equals(ca))
						out.add(item);
				}
			}
		}
		return out;
	}

	public static Vector3f invpos = null;
	public static Vector3f invnewpos = new Vector3f(200, 300, 0);

	@Override
	public void openAction() {
		player.inventory.activate();
		if (invpos == null)
			invpos = new Vector3f(player.inventory.getTransform().getPos());
		player.inventory.getTransform().setPos(invnewpos);

	}

	@Override
	public void closeAction() {
		player.inventory.deactivate();
		player.inventory.getTransform().setPos(invpos);
	}
}
