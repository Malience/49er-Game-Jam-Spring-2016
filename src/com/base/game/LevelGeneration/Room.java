package com.base.game.LevelGeneration;

import java.util.ArrayList;
import java.util.Random;

import com.base.engine.core.GameObject;
import com.base.engine.core.math.Vector3f;
import com.base.engine.electricity.RoomPowerUnit;

public class Room extends GameObject {
	private ArrayList<Wall> walls;
	private int roomID;
	private int x, y;
	private RoomPowerUnit rpu;

	public Room() {
		this(0, 0, 0);
	}

	public Room(float x, float y, float z) {
		this(new Vector3f(x, y, z), 0, 0, 0);
	}

	public Room(Vector3f loc, int x, int y, int roomID) {
		this.getTransform().setPos(loc);
		this.roomID = roomID;
		this.x = x;
		this.y = y;
		walls = new ArrayList<Wall>();
		rpu = new RoomPowerUnit(1000, 1000);
	}

	public void generate() {
		ArrayList<Wall> possibleDoors = new ArrayList<Wall>();
		for (Wall wall : walls) {

			if (wall.attachment.equals("Door")) {
				possibleDoors.add(wall);

			}
		}

		if (possibleDoors.size() > 0) {
			int numDoors = 1;
			int roomsize = x * y;
			if (roomsize > 9 && new Random().nextBoolean())
				numDoors++;
			if (numDoors > possibleDoors.size())
				numDoors = possibleDoors.size();
			int doorConvert = 0;
			for (int i = 0; i < numDoors; i++) {
				doorConvert = new Random().nextInt(possibleDoors.size());
				this.addChild(new Door(walls.remove(walls.indexOf(possibleDoors.remove(doorConvert))))); // WOOOOOOOOOOOOOOOOO
			}

		}

		for (Wall wall : walls) {
			switch (wall.attachment) {
			case "RPU":
			}
			this.addChild(wall);
		}
	}

	public void addWall(Wall wall) {
		addWall(wall, false);
	}

	public void addWall(Wall wall, boolean door) {
		if (door)
			wall.attachment = "Door";
		this.walls.add(wall);
	}

	public void addFloor(Floor floor) {
		this.addChild(floor);
	}
}
