package com.base.game;

import com.base.engine.components.MeshRenderer;
import com.base.engine.components.movenlook.FreeLook;
import com.base.engine.components.movenlook.FreeMove;
import com.base.engine.components.movenlook.LookAtComponent;
import com.base.engine.components.movenlook.PlanetWalking;
import com.base.engine.core.Game;
import com.base.engine.core.GameObject;
import com.base.engine.core.World;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.electricity.RoomPowerUnit;
import com.base.engine.electricity.console.LightingConsole;
import com.base.engine.electricity.console.PoweredConsole;
import com.base.engine.physics.PremadeObjects.PlaneObject;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Vertex;
import com.base.game.LevelGeneration.Level;
import com.base.game.LevelGeneration.Ship;

import game.Player;
import game.TestPlanet;

public class TestGame extends Game {
	public void init() {
		world = World.world;
		float fieldDepth = 10.0f;
		float fieldWidth = 10.0f;

		Vertex[] vertices = new Vertex[] {
				new Vertex(new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
				new Vertex(new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
				new Vertex(new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
				new Vertex(new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f)) };

		int indices[] = { 0, 1, 2, 2, 1, 3 };

		Vertex[] vertices2 = new Vertex[] {
				new Vertex(new Vector3f(-fieldWidth / 10, 0.0f, -fieldDepth / 10), new Vector2f(0.0f, 0.0f)),
				new Vertex(new Vector3f(-fieldWidth / 10, 0.0f, fieldDepth / 10 * 3), new Vector2f(0.0f, 1.0f)),
				new Vertex(new Vector3f(fieldWidth / 10 * 3, 0.0f, -fieldDepth / 10), new Vector2f(1.0f, 0.0f)),
				new Vertex(new Vector3f(fieldWidth / 10 * 3, 0.0f, fieldDepth / 10 * 3), new Vector2f(1.0f, 1.0f)) };

		int indices2[] = { 0, 1, 2, 2, 1, 3 };

		Mesh mesh2 = new Mesh(vertices2, indices2, true);

		Mesh mesh = new Mesh(vertices, indices, true);
		Material material = new Material();// new Texture("test.png"), new
											// Vector3f(1,1,1), 1, 8);
		material.addTexture("diffuse", new Texture("test.png"));
		material.addFloat("specularIntensity", 1);
		material.addFloat("specularPower", 8);

		Mesh tempMesh = new Mesh("monkey3.obj");

		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);

		GameObject planeObject = new GameObject();
		planeObject.addComponent(meshRenderer);
		planeObject.getTransform().getPos().set(0, -1, 5);

		// GameObject directionalLightObject = new GameObject();
		// DirectionalLight directionalLight = new DirectionalLight(new
		// Vector3f(0,0,0), 0);
		//// RenderingEngine.dlight = directionalLight;
		//
		// directionalLightObject.addComponent(directionalLight);
		//
		//
		// //addObject(planeObject);
		// addObject(directionalLightObject);
		// addObject(pointLightObject);
		// addObject(spotLightObject);

		// getRootObject().addChild(new GameObject().addComponent(new
		// Camera((float)Math.toRadians(70.0f),
		// (float)Window.getWidth()/(float)Window.getHeight(), 0.01f,
		// 1000.0f)));

		GameObject testMesh1 = new GameObject().addComponent(new MeshRenderer(mesh2, material));
		GameObject testMesh2 = new GameObject().addComponent(new MeshRenderer(mesh2, material));
		GameObject testMesh3 = new GameObject().addComponent(new FreeLook(0.5f)).addComponent(new FreeMove(10))
				.addComponent(new LookAtComponent()).addComponent(new MeshRenderer(tempMesh, material));

		testMesh1.getTransform().getPos().set(0, 2, 0);
		testMesh1.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), 0.4f));

		testMesh2.getTransform().getPos().set(0, 0, 5);

		testMesh1.addChild(testMesh2);
		// getRootObject()
		// GameObject cameraObject = new GameObject();
		// Camera camera = new Camera((float)Math.toRadians(70.0f),
		// (float)Window.getWidth()/(float)Window.getHeight(), 0.01f, 1000.0f);
		// GameComponent freelook = new FreeLook(0.5f);
		// GameComponent freeMove = new FreeMove(10);

		// world.add(cameraObject);

		// RenderingEngine.mainCamera = camera;

		// cameraObject.addComponent(freelook).addComponent(freeMove).addComponent(camera);
		// world.focus = cameraObject;
		// world.add(cameraObject);
		// testMesh2.addChild(cameraObject);

		// addObject(testMesh2);

		// addObject(testMesh1);
		// addObject(testMesh3);

		testMesh3.getTransform().getPos().set(5, 5, 5);
		testMesh3.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float) Math.toRadians(-70.0f)));

		// addObject(new GameObject().addComponent(new MeshRenderer(new
		// Mesh("monkey3.obj"), material)));

		// directionalLight.getTransform().setRot(new Quaternion(new
		// Vector3f(1,0,0), (float)Math.toRadians(-45)));

		// BoxObject box1 = new BoxObject(30, .2f, .3f);
		// BoxObject box2 = new BoxObject(45, .5f, .3f);
		// BoxObject box3 = new BoxObject(45, .5f, .3f);
		// BoxObject box4 = new BoxObject(45, .5f, .3f);
		PlaneObject plane = new PlaneObject(new Vector3f(0, 1, 0), 0);
		// SphereObject sphere = new SphereObject(10, .5f, .5f, 1);
		// SphereObject sphere2 = new SphereObject(10, .5f, .5f, 1);
		// BallWaterfall waterfall = new BallWaterfall(20, 1, 1, 1, .2f);
		//
		// box1.getTransform().setPos(new Vector3f(-25,28,0));
		// box2.getTransform().setPos(new Vector3f(-25,20,0));
		// box3.getTransform().setPos(new Vector3f(-25,20,20));
		// box4.getTransform().setPos(new Vector3f(-25,23,-4));
		plane.getTransform().setPos(new Vector3f(-25, 20, 0));
		// sphere.getTransform().setPos(new Vector3f(-25, 10, 1));
		// sphere2.getTransform().setPos(new Vector3f(-25, 10, 0));
		// waterfall.getTransform().setPos(new Vector3f(-25, 20, 0));
		// //
		// //box1.getRigidBody().addVelocity(new Vector3f(0,0,15));
		// box3.getRigidBody().addVelocity(new Vector3f(0,0,-40));
		// sphere.getRigidBody().addVelocity(new Vector3f(-20,0,0));
		// sphere2.getRigidBody().addVelocity(new Vector3f(20,0,0));
		// world.add(box1);
		// world.add(box2);
		// world.add(box3);
		// world.add(box4);
		world.add(plane);
		// world.add(sphere);
		// world.add(sphere2);
		// //world.add(waterfall);
		//
		// PhysicsEngine.addForce(sphere.getRigidBody(), "Gravity");
		// PhysicsEngine.addForce(sphere2.getRigidBody(), "Gravity");
		// PhysicsEngine.addForce(box1.getRigidBody(), "Gravity");
		// PhysicsEngine.addForce(box2.getRigidBody(), "Gravity");
		// PhysicsEngine.addForce(box3.getRigidBody(), "Gravity");
		// PhysicsEngine.addForce(box4.getRigidBody(), "Gravity");

		

		// world.add(new Wall(new Vector3f(10,2.5f,10)));
		// world.add(new Floor(new Vector3f(5,.5f,10)));
		// world.add(new Floor(new Vector3f(5,1f,15)));
		// world.add(new Floor(new Vector3f(5,1.5f,20)));
		// world.add(new Floor(new Vector3f(5,2f,25)));
		// Cabinet it = new Cabinet();
		// it.getTransform().setPos(new Vector3f(1.4f,0,4));
		// world.add(it);

//		Level level = new Level(20, 20);
//		level.initRooms(4, 3, 3, 2); // Integer values 0-7 : 0-3 : 0-3 : 0+
//		System.out.println(level.toString());
//
//		Ship ship = level.constructShip(new Vector3f(0, 0, 0));
//		ship.generateRooms();
//		world.add(ship);
//
//		PoweredConsole pc = new LightingConsole(new RoomPowerUnit(1000, 1000));
//		pc.getTransform().setPos(new Vector3f(1.4f, 0, 4));
//		world.add(pc);
		
		TestPlanet planet1 = new TestPlanet(1000, 40);
		planet1.getTransform().setPos(new Vector3f(40,40,40));
		world.add(planet1);
		
		Player player = new Player();
		player.setPlanet(planet1.planet);
		// player.getTransform().setPos(new Vector3f(mainRoomTopCenterPos.x,
		// Room.roomSize.y * 2.0f, mainRoomTopCenterPos.z));
		//player.addComponent(new PlanetWalking(player, planet1.planet));
		// player.getTransform().setPos(new Vector3f(mainRoomTopCenterPos.x,
		// Room.roomSize.y * 1.5f, mainRoomTopCenterPos.z));
		world.addToBucket(player);
		
//		System.out.println("GRAPH\n");
//		IslandGeneration islandGen = new IslandGeneration(9, 3, 0.0f, 4.0f);
//		Graph g = islandGen.getGraph();
//		System.out.println(g.toString());
	}
}
