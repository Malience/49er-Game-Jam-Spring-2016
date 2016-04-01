package com.base.engine.rendering.UI;

import java.util.ArrayList;

import com.base.engine.components.attachments.ComponentAttachment;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;

import game.Player;

public class UIInventory extends UIMenu {
	public Player player;

	ArrayList<SpecificSlot> specificSlots;
	ArrayList<GenericSlot> slots;
	UIObject genericInventory;
	UIObject genericArealoc;
	UIRect genericArea;
	Vector2f inventorySize;

	public UIInventory(Player player) {
		super();

		this.player = player;

		specificSlots = new ArrayList<SpecificSlot>();
		slots = new ArrayList<GenericSlot>();

		this.addSpecific("ui-mask.png", "Mask", new Vector3f(64, 64, 0));
		this.addSpecific("ui-head.png", "Head", new Vector3f(0, 64, 0));
		this.addSpecific("ui-torso.png", "Torso", new Vector3f(0, 0, 0));
		this.addSpecific("ui-back.png", "Back", new Vector3f(64, 0, 0));
		this.addSpecific("ui-belt.png", "Belt", new Vector3f(0, -64, 0));

		genericInventory = new UIObject();

		inventorySize = new Vector2f(175, 100);
		UIRect rect = new UIRect("ui.png", inventorySize);
		rect.priority = 1;
		this.addComponent(rect);
		this.getTransform().setPos(new Vector3f(400, 300, 0));

		this.addChild(genericInventory);
		genericInventory.getTransform().setPos(new Vector3f(-inventorySize.x, -inventorySize.y, 0));

		this.addSlots(5);
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

	private class SpecificSlot extends UIObject {
		UIInventorySlotSpecific slot;

		public SpecificSlot(String texturePath, String type) {
			Vector2f halfSize = new Vector2f(32, 32);
			slot = new UIInventorySlotSpecific(halfSize, type);
			UIRect rect = new UIRect(texturePath, halfSize);
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

	public void addSpecific(String texturePath, String type, Vector3f pos) {
		SpecificSlot newSlot = new SpecificSlot(texturePath, type);
		specificSlots.add(newSlot);
		this.addChild(newSlot);
		newSlot.getTransform().setPos(pos);
	}

	public UIInventoryItem getSpecificItem(String type) {
		for (SpecificSlot slot : specificSlots) {
			if (slot.slot.type.equals(type)) {
				return slot.slot.item;
			}
		}
		return null;
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

		for (SpecificSlot slot : specificSlots) {
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
}
