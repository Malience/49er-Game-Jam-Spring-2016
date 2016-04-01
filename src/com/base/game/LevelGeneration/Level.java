package com.base.game.LevelGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.base.engine.core.World;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Texture;

public class Level {
	private final static int SPACE = 0;
	private final static int UNASSIGNED = 1;
	private final static int HALLWAY = 2;
	private final static int AIRLOCK = 3;
	private final static int THRUSTER = 4;
	private final static int DOOR = 5;
	private final static int DORM = 10;
	private final static int JANITOR = 11;
	private final static int MESSHALL = 12;
	private final static int KITCHEN = 13;
	private final static int LAUNDRY = 14;
	private final static int BATHROOM = 15;
	private final static int COMMON = 16;
	private final static int ENGINEERING = 20;
	private final static int GENERATOR = 21;
	private final static int STORAGE = 22;
	private final static int MEDBAY = 30;
	private final static int LAB = 40;
	private final static int ARMORY = 50;
	private final static int BRIG = 51;
	private final static int WEAPON = 52;
	private final static int AI = 70;
	private final static int COMPUTER = 71;
	private final static int MAINFRAME = 72;
	private final static int BRIDGE = 80;
	private final static int POD = 90;

	private int[][] map;
	RoomRegistry[] registry;

	// public static void main(String [] args)
	// {
	// Level level = new Level(20, 20);
	// level.initRooms(4,3,3,2); //Integer values 0-7 : 0-3 : 0-3 : 0+
	// System.out.println(level.toString());
	// //Ship ship = level.constructShip(new Vector3f(0,0,0));
	// }

	public Level(int width, int height) {
		// Width must be even
		if (width % 2 != 0)
			width++;

		map = new int[height][width];
	}

	public static int tries = 10;

	public void initRooms(int shipSize) {
		initRooms(shipSize, 0);
	}

	public void initRooms(int shipSize, int techLevel) {
		initRooms(shipSize, techLevel, 0);
	}

	public void initRooms(int shipSize, int techLevel, int militaryLevel) {
		initRooms(shipSize, techLevel, 0, 0);
	}

	public void initRooms(int shipSize, int techLevel, int militaryLevel, int additional) {
		long seed = -7438611395090746717L;
		// long seed = new Random().nextLong();
		System.out.println(seed);
		Random r = new Random(seed); // REPLACE WITH World.r AFTER TESTING!!!!!
		ArrayList<Integer> pool = new ArrayList<Integer>();

		int[] rooms = new int[shipSize + additional + 2];
		rooms[rooms.length - 1] = POD;

		switch (militaryLevel) {
		case 3:
			pool.add(WEAPON);
		case 2:
			pool.add(ARMORY);
		case 1:
			pool.add(BRIG);
		case 0:
			break;
		default:
			new Exception("Invalid Tech Level").printStackTrace();
		}

		switch (techLevel) {
		case 3:
			pool.add(AI);
			pool.add(COMPUTER);
			pool.add(MAINFRAME);
		case 2:
			pool.add(LAB);
		case 1:
			pool.add(MEDBAY);
			pool.add(KITCHEN);
			pool.add(MESSHALL);
		case 0:
			pool.add(COMMON);
			pool.add(LAUNDRY);
			pool.add(JANITOR);
			pool.add(BATHROOM);
			break;
		default:
			new Exception("Invalid Tech Level").printStackTrace();
		}

		switch (shipSize) {
		case 7:
			rooms[7] = pool.get(r.nextInt(pool.size()));
		case 6:
			pool.add(STORAGE);
			rooms[6] = pool.get(r.nextInt(pool.size()));
		case 5:
			pool.add(ENGINEERING);
			rooms[5] = pool.get(r.nextInt(pool.size()));
		case 4:
			rooms[4] = pool.get(r.nextInt(pool.size()));
		case 3:
			rooms[3] = DORM;
			rooms[2] = GENERATOR;
			rooms[1] = pool.get(r.nextInt(pool.size()));
			rooms[0] = BRIDGE;
			break;
		default:
			System.err.println("INVALID NUMBER OF ROOMS!");
			new Exception("Invalid number of rooms").printStackTrace();
		}

		if (additional > 0) {
			// higher of additional rooms and rooms left
			for (int i = shipSize; i < rooms.length - 1; i++) {
				rooms[i] = pool.get(r.nextInt(pool.size()));
			}
		}

		registry = new RoomRegistry[rooms.length];

		// First we place the bridge and the thrusters
		final int bmaxh = 4, bminh = 2, bmaxw = 6, bminw = 2;
		int halfw = map[0].length / 2;
		int halfh = map.length / 2;
		int bw = r.nextInt(bmaxw - bminw) + bminw;
		int bh = r.nextInt(bmaxh - bminh) + bminh;

		// Places the bridge
		place(halfw - bw, halfw, map.length, map.length - bh, BRIDGE, true, false);
		registry[0] = new RoomRegistry(halfw - bw, halfw + bw, map.length, map.length - bh, 0);
		registry[0].room = BRIDGE;

		int th = 2, tw = 4;
		int tx = r.nextInt(map[0].length / 4);
		int ty = r.nextInt(map.length / 4);
		place(tx, tx + tw, ty, ty + th, THRUSTER, true, false);

		int h = r.nextInt(3) + 2;
		place(tx, tx + tw, ty + th + 1, ty + th + 1 + h, UNASSIGNED, true, false);
		registry[1] = new RoomRegistry(tx, tx + tw, ty + th + 1, ty + th + 1 + h, 0);
		registry[2] = new RoomRegistry(map[0].length - tx, map[0].length - (tx + tw), ty + th + 1, ty + th + 1 + h, 1);

		int done = 10;
		int i = 3;
		while (i < rooms.length) {
			// If there is only one left
			if (done < 0) {
				tries--;
				if (tries < 0) {
					new Exception("Room Creation Failed!").printStackTrace();
					System.exit(0);
				}
				map = new int[map.length][map[0].length];
				initRooms(shipSize, techLevel, militaryLevel, additional);
			}
			if (rooms.length - i == 1) {
				int yh = r.nextInt(map.length - bh - 5);
				int yheight = r.nextInt(3) + 2;
				int hw = r.nextInt(2) + 1;
				if (axisPlace(hw, yheight, yh, i, registry))
					i++;
				else
					done--;
			} else {
				if (r.nextBoolean()) {
					int nw = r.nextInt(3) + 2;
					int nh = r.nextInt(3) + 2;
					int j = 10;
					while (j > 0) {
						int nx = r.nextInt(map[0].length - nw);
						int ny = r.nextInt(map.length - nh);
						if (randomPlace(nx, ny, nw, nh, i, registry)) {
							i += 2;
							break;
						} else
							j--;
					}
					if (!(j > 0))
						done--;
				} else {
					int yh = r.nextInt(map.length - bh - 5);
					int yheight = r.nextInt(3) + 2;
					int hw = r.nextInt(2) + 1;
					if (axisPlace(hw, yheight, yh, i, registry))
						i++;
					else
						done--;
				}
			}
		}

		// System.out.println(this.toString());

		boolean checked[] = new boolean[registry.length];
		checked[0] = true;
		int miny = map.length;
		for (i = 1; i < registry.length; i++) {
			registry[i].setRoom(rooms[i]);
			if (registry[i].y1 < miny)
				miny = registry[i].y1;
		}

		// System.out.println(this.toString());

		int max = map.length - bh - 1;
		for (int y = max; y > miny; y--) {
			if (!tileCheck(halfw, map.length - y - 1)) {
				RoomRegistry room = findRoom(map[map.length - y - 1][halfw], registry);
				int temp = room.frame();
				place(halfw - 1, halfw + 1, y + 1, max + 1, HALLWAY, false, false);
				max = temp - 1;
				y = temp - 2;
				checked[room.id] = true;
			}
		}
		if (tileCheck(halfw, map.length - (miny + 1))) {
			place(halfw - 1, halfw + 1, miny + 1, max + 1, HALLWAY, false, false);
		}

		// System.out.println(this.toString());

		for (i = 0; i < registry.length; i++) {
			if (!checked[i]) {
				int j = 0;
				// If the room has been framed go ahead and skip to the next
				// tile
				if (map[map.length - registry[i].y2 - 1][registry[i].x1] == HALLWAY)
					j++;
				int y = registry[i].y1 + (registry[i].y2 - registry[i].y1) / 2;
				if (registry[i].x1 < halfw) {
					int start = registry[i].x2;
					for (int x = start + j; x < halfw + 2; x++) {
						if (!tileCheck(x, map.length - y - 1)) {
							place(start, x, y, y + 1, HALLWAY, false, false);
							if (map[map.length - y][x] != HALLWAY) {
								RoomRegistry room = findRoom(map[map.length - y][x]);
								room.frame();
							}
							checked[i] = true;
							break;
						}
					}
				} else {
					int start = registry[i].x1 - 1;
					for (int x = start - j; x > halfw - 2; x--) {
						if (!tileCheck(x, map.length - y - 1)) {
							place(x + 1, start + 1, y, y + 1, HALLWAY, false, false);
							if (map[map.length - y][x] != HALLWAY) {
								RoomRegistry room = findRoom(map[map.length - y][x]);
								room.frame();
							}
							checked[i] = true;
							break;
						}
					}
				}
			}
		}
	}

	public Ship constructShip(Vector3f loc) {
		Vector3f center = loc;
		Ship ship = new Ship(center, map[0].length, map.length);
		HashMap<Integer, Integer> roomMap = new HashMap<Integer, Integer>();

		for (int x = 0; x < map[0].length; x++)
			for (int y = 0; y < map.length; y++) {
				int tile = map[map.length - y - 1][x];
				if (tile != SPACE) {
					if (tile == HALLWAY) {
						ship.addHallway(createFloor(x, y));
					} else if (tile == THRUSTER) {
						// TODO: THRUSTERS
					} else {
						if (roomMap.get(tile) == null) {
							RoomRegistry room = findRoom(tile);
							int x1 = (room.x2 - room.x1);
							int y1 = (room.y2 - room.y1);
							// roomMap.put(tile,
							// ship.createRoom(convertLoc(x1,y1)));
							roomMap.put(tile, ship.createRoom(x1, y1));
						}

						ship.addFloor(roomMap.get(tile), createFloor(x, y));
					}
				}
			}

		for (int x = 0; x < map[0].length; x++)
			for (int y = 0; y < map.length; y++) {
				int tile = map[map.length - y - 1][x];
				if (x == 0 || y == 0) {
					if (x == 0) {
						if (tile != SPACE) {
							if (tile != HALLWAY)
								ship.addWall(roomMap.get(tile), createWall(x - 1, x, y, y), false);
							else
								ship.addHallway(createWall(x - 1, x, y, y));
						}
					}
					if (y == 0) {
						if (tile != SPACE) {
							if (tile != HALLWAY)
								ship.addWall(roomMap.get(tile), createWall(x, x, y - 1, y), false);
							else
								ship.addHallway(createWall(x, x, y - 1, y));
						}
					}
					continue;
				}

				int tile1 = map[map.length - y - 1][x - 1];
				int tile2 = map[map.length - y][x];

				if (tile != tile1) {
					if ((tile == THRUSTER || tile1 == THRUSTER) && (tile == SPACE || tile1 == SPACE)) {
						// TO-DO: THRUSTERS
					} else if (tile != SPACE && tile != HALLWAY && tile != THRUSTER) {
						if (tile1 == HALLWAY)
							ship.addWall(roomMap.get(tile), createWall(x - 1, x, y, y), true);
						else
							ship.addWall(roomMap.get(tile), createWall(x - 1, x, y, y), false);
					} else if (tile1 != SPACE && tile1 != HALLWAY && tile1 != THRUSTER) {
						if (tile == HALLWAY)
							ship.addWall(roomMap.get(tile1), createWall(x - 1, x, y, y), true);
						else
							ship.addWall(roomMap.get(tile1), createWall(x - 1, x, y, y), false);
					} else {
						ship.addHallway(createWall(x - 1, x, y, y));
					}

				}
				if (tile != tile2) {
					if ((tile == THRUSTER || tile2 == THRUSTER) && (tile == SPACE || tile2 == SPACE)) {
						// TO-DO: THRUSTERS
					} else if (tile != SPACE && tile != HALLWAY && tile != THRUSTER) {
						if (tile2 == HALLWAY)
							ship.addWall(roomMap.get(tile), createWall(x, x, y - 1, y), true);
						else
							ship.addWall(roomMap.get(tile), createWall(x, x, y - 1, y), false);
					} else if (tile2 != SPACE && tile2 != HALLWAY && tile2 != THRUSTER) {
						if (tile == HALLWAY)
							ship.addWall(roomMap.get(tile2), createWall(x, x, y - 1, y), true);
						else
							ship.addWall(roomMap.get(tile2), createWall(x, x, y - 1, y), false);
					} else {
						ship.addHallway(createWall(x, x, y - 1, y));
					}

				}
			}

		Material material = new Material();
		material.addTexture("diffuse", new Texture("ui.png"));
		material.addFloat("specularIntensity", 1);
		material.addFloat("specularPower", 10);

		for (int x = 0; x < map[0].length; x++) {
			int tile = map[0][x];
			if (tile != SPACE) {
				Wall wall = createWall(x, x, map.length, map.length - 1);
				wall.attachment = "Window";
				wall.setMaterial(material);

				if (tile == HALLWAY)
					ship.addHallway(wall);
				else
					ship.addWall(roomMap.get(tile), wall, false);
			}
		}

		return ship;
	}

	private final static float spacing = World.tileHalfSize * 2;

	public Vector3f convertLoc(float x, float y) {
		float x1 = x * spacing;
		float y1 = y * spacing;
		return new Vector3f(x1, 0, y1);
	}

	public Wall createWall(int x1, int x2, int y1, int y2) {
		// Swap the first value if it is greater than the second value
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (y1 > y2) {
			int temp = y1;
			y1 = y2;
			y2 = temp;
		}

		// If they aren't adjacent, crash
		if ((x2 - x1) != 1 && (y2 - y1) != 1)
			throw new RuntimeException("Tiles aren't adjacent!");

		Wall wall = null;
		if (x2 - x1 == 1) {
			wall = new Wall(convertLoc(x1 + .5f, y1), 90);
		} else {
			wall = new Wall(convertLoc(x1, y1 + .5f), 0);
		}

		wall.getTransform().setPos(wall.getPosition().add(new Vector3f(0, World.tileHalfSize, 0)));
		return wall;
	}

	public Floor createFloor(int x, int y) {
		return new Floor(convertLoc(x, y));
	}

	public boolean randomPlace(int x, int y, int width, int height, int i, RoomRegistry[] registry) {
		if (placeCheck(x, x + width, y, y + height, UNASSIGNED, true, false)) {
			registry[i] = new RoomRegistry(x, x + width, y, y + height, i);
			registry[i + 1] = new RoomRegistry(map[0].length - x, map[0].length - (x + width), y, y + height, i + 1);
			return true;
		}
		return false;
	}

	public boolean axisPlace(int halfw, int height, int y, int i, RoomRegistry[] registry) {
		int w = map[0].length / 2;
		if (placeCheck(w - halfw, w + halfw, y, y + height, UNASSIGNED, false, false)) {
			registry[i] = new RoomRegistry(w - halfw, w + halfw, y, y + height, i);
			return true;
		}
		return false;
	}

	private class RoomRegistry {
		int x1, x2, y1, y2, room, id;
		boolean frame = false;

		public RoomRegistry(int x1, int x2, int y1, int y2, int id) {
			super();
			if (x1 > x2) {
				this.x1 = x2;
				this.x2 = x1;
			} else {
				this.x1 = x1;
				this.x2 = x2;
			}
			if (y1 > y2) {
				this.y1 = y2;
				this.y2 = y1;
			} else {
				this.y1 = y1;
				this.y2 = y2;
			}
			this.id = id;
		}

		public int size() {
			return (int) Math.abs((x1 - x2) * (y1 - y2));
		}

		public void setRoom(int room) {
			this.room = room;
			place(x1, x2, y1, y2, room, false, false);
		}

		public boolean isRoom(int room) {
			return this.room == room;
		}

		public int frame() {
			if (!frame) {
				frame = true;
				int out = y1;
				for (int x = x1 - 1; x <= x2; x++) {
					try {
						if (map[map.length - y1][x] == SPACE) {
							map[map.length - y1][x] = HALLWAY;
						} else {
							RoomRegistry room = findRoom(map[map.length - (y1 - 1)][x]);
							if (room != null) {
								int temp = room.frame();
								if (temp < out)
									out = temp;
							}
						}
					} catch (Exception e) {
					}

					try {
						if (map[map.length - y2 - 1][x] == SPACE) {
							map[map.length - y2 - 1][x] = HALLWAY;
						} else {
							RoomRegistry room = findRoom(map[map.length - y2 - 1][x]);
							if (room != null) {
								int temp = room.frame();
								if (temp < out)
									out = temp;
							}
						}
					} catch (Exception e) {
					}
				}

				for (int y = y1; y < y2; y++) {
					try {
						if (map[map.length - y - 1][x1 - 1] == SPACE) {
							map[map.length - y - 1][x1 - 1] = HALLWAY;
						} else {
							RoomRegistry room = findRoom(map[map.length - y - 1][x1 - 1]);
							if (room != null) {
								int temp = room.frame();
								if (temp < out)
									out = temp;
							}
						}
					} catch (Exception e) {
					}

					try {
						if (map[map.length - y - 1][x2] == SPACE) {
							map[map.length - y - 1][x2] = HALLWAY;
						} else {
							RoomRegistry room = findRoom(map[map.length - y - 1][x2]);
							if (room != null) {
								int temp = room.frame();
								if (temp < out)
									out = temp;
							}
						}
					} catch (Exception e) {
					}
				}
				try {
					if (map[map.length - y2 - 1][x2] == SPACE) {
						map[map.length - y2 - 1][x2] = HALLWAY;
					} else {
						RoomRegistry room = findRoom(map[map.length - y2 - 1][x2]);
						if (room != null) {
							int temp = room.frame();
							if (temp < out)
								out = temp;
						}
					}
				} catch (Exception e) {
				}
				return out;
			}
			return y1;
		}
	}

	public static RoomRegistry findRoom(int room, RoomRegistry[] registry) {
		for (int i = 0; i < registry.length; i++) {
			if (registry[i].isRoom(room))
				return registry[i];
		}
		return null;
	}

	public RoomRegistry findRoom(int room) {
		for (int i = 0; i < registry.length; i++) {
			if (registry[i].isRoom(room))
				return registry[i];
		}
		return null;
	}

	public boolean placeCheck(int x1, int x2, int y1, int y2, int object) {
		return placeCheck(x1, x2, y1, y2, object, true, false);
	}

	public boolean placeCheck(int x1, int x2, int y1, int y2, int object, boolean xsymmetry, boolean ysymmetry) {
		if (tileCheck(x1, x2, y1, y2)) {
			if (xsymmetry && ysymmetry) {
				if (!tileCheck(map[0].length - x1, map[0].length - x2, map.length - y1, map.length - y2))
					return false;
			} else if (xsymmetry) {
				if (!tileCheck(map[0].length - x1, map[0].length - x2, y1, y2))
					return false;
			} else if (ysymmetry) {
				if (!tileCheck(x1, x2, map.length - y1, map.length - y2))
					return false;
			}
			place(x1, x2, y1, y2, object, xsymmetry, ysymmetry);
			return true;
		}
		return false;
	}

	/**
	 * Fills a square from x1,y1 to x2,y2 with object
	 * 
	 * @param x1
	 *            the starting or ending x coord
	 * @param x2
	 *            the starting or ending x coord
	 * @param y1
	 *            the starting or ending y coord
	 * @param y2
	 *            the starting or ending y coord
	 * @param object
	 *            the integer value to change to
	 */
	public void place(int x1, int x2, int y1, int y2, int object) {
		place(x1, x2, y1, y2, object, true, false);
	}

	public void place(int x1, int x2, int y1, int y2, int object, boolean xsymmetry, boolean ysymmetry) {
		// Swap the first value if it is greater than the second value
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (y1 > y2) {
			int temp = y1;
			y1 = y2;
			y2 = temp;
		}

		for (int x = x1; x < x2; x++)
			for (int y = y1; y < y2; y++) {
				map[map.length - y - 1][x] = object;
			}

		if (xsymmetry && ysymmetry) {
			place(map[0].length - x1, map[0].length - x2, map.length - y1, map.length - y2, object, false, false);
			return;
		}
		if (xsymmetry) {
			place(map[0].length - x1, map[0].length - x2, y1, y2, object, false, false);
			return;
		}
		if (ysymmetry) {
			place(x1, x2, map.length - y1, map.length - y2, object, false, false);
			return;
		}
	}

	public void placeFrame(int x1, int x2, int y1, int y2, int object) {
		// Swap the first value if it is greater than the second value
		if (x1 > x2) {
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (y1 > y2) {
			int temp = y1;
			y1 = y2;
			y2 = temp;
		}

		for (int x = x1; x < x2; x++) {
			map[map.length - y1 - 1][x] = object;
			map[map.length - y2 - 1][x] = object;
		}

		for (int y = y1; y < y2; y++) {
			map[map.length - y - 1][x1] = object;
			map[map.length - y - 1][x2] = object;
		}
		map[map.length - y2 - 1][x2] = object;
	}

	public boolean tileCheck(int x1, int x2, int y1, int y2) {
		for (int x = x1; x < x2; x++)
			for (int y = y1; y < y2; y++) {
				if (!tileCheck(x, map.length - y - 1))
					return false;
			}
		return true;
	}

	public boolean tileCheck(int x, int y) {
		return map[y][x] == SPACE;
	}

	@Override
	public String toString() {
		String out = "";
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				out += map[i][j];
				if (j != map[0].length - 1) {
					out += " | ";
				}
			}
			if (i != map.length - 1) {
				out += "\n";
				for (int j = 0; j < map[0].length * 3.5f; j++) {
					out += "-";
				}
				out += "\n";
			}
		}
		return out;
	}
}
