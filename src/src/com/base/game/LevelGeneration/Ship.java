package com.base.game.LevelGeneration;

import java.util.ArrayList;

import com.base.engine.core.GameObject;
import com.base.engine.core.math.Vector3f;
import com.base.engine.electricity.LightingControl;
import com.base.engine.electricity.RoomPowerUnit;

public class Ship extends GameObject
{
	ArrayList<Room> shipRooms;
	ArrayList<Floor> hallwayFloors;
	ArrayList<Wall> hallwayWalls;
	
	public Ship(){this(0,0,0);}
	public Ship(float x, float y, float z){this(new Vector3f(x,y,z), 0, 0);}
	public Ship(Vector3f loc, int x, int y)
	{
		shipRooms = new ArrayList<Room>();
		hallwayFloors = new ArrayList<Floor>();
		hallwayWalls = new ArrayList<Wall>();
		this.getTransform().setPos(loc);
		this.addChild(new LightingControl(new RoomPowerUnit(300,300)));
	}
	
	public void generate()
	{
		
	}
	
	public void generateRooms()
	{
		this.generate();
		for(Room room : shipRooms)
		{
			room.generate();
		}
	}
	
	public int createRoom(){return createRoom(0,0,0);}
	public int createRoom(float x, float y, float z){return createRoom(new Vector3f(x,y,z), 0, 0, 0);}
	public int createRoom(int x, int y){return createRoom(new Vector3f(0,0,0), x, y, 0);}
	public int createRoom(Vector3f loc, int x, int y, int roomID)
	{
		Room room = new Room(loc, x, y, roomID);
		shipRooms.add(room);
		this.addChild(room);
		return shipRooms.indexOf(room);
	}
	
	public void addHallway(Wall wall)
	{
		hallwayWalls.add(wall);
		this.addChild(wall);
	}
	
	public void addHallway(Floor floor)
	{
		hallwayFloors.add(floor);
		this.addChild(floor);
	}
	
	public void addWall(int roomID, Wall wall, boolean door)
	{
		Room room = shipRooms.get(roomID);
		//wall.getTransform().setPos(room.getPosition().sub(wall.getPosition()));
		room.addWall(wall, door);
	}
	
	public void addFloor(int roomID, Floor floor)
	{
		Room room = shipRooms.get(roomID);
		//floor.getTransform().setPos(room.getPosition().sub(floor.getPosition()));
		room.addFloor(floor);
	}
	
	public boolean hasRoom(int room)
	{
		return room < shipRooms.size();
	}
}
