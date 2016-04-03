package com.base.engine.physics;

import java.util.ArrayList;
import java.util.HashMap;

import com.base.engine.components.attachments.BroadCollidable;
import com.base.engine.components.attachments.Collidable;
import com.base.engine.components.attachments.Physical;
import com.base.engine.core.CoreEngine;
import com.base.engine.core.World;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.RigidBody.RigidBody;
import com.base.engine.physics.RigidBody.ForceGenerators.ForceGenerator;
import com.base.engine.physics.RigidBody.ForceGenerators.ForceRegistry;
import com.base.engine.physics.RigidBody.ForceGenerators.Gravity;
import com.base.engine.physics.collision.Collider;
import com.base.engine.physics.collision.CollisionData;
import com.base.engine.physics.collision.CollisionDetector;
import com.base.engine.physics.collision.Contact;
import com.base.engine.physics.collision.ContactResolver;
import com.base.engine.physics.collision.broadphase.BoundingCollider;
import com.base.engine.physics.collision.broadphase.BroadCollisionDetector;

public class PhysicsEngine extends Thread {
	World world;
	ArrayList<Physical> physicalComponents;
	ArrayList<Collidable> collidableComponents;
	ArrayList<BroadCollidable> broadphase;
	ArrayList<RigidBody> bodies;
	BroadCollisionDetector broadDetector;
	CollisionDetector detector;
	ContactResolver resolver;
	CollisionData data;
	Contact[] contacts;
	int maxContacts;
	int iterations;
	boolean calculateIterations;
	static HashMap<String, ForceGenerator> forces;
	static ForceRegistry registry;

	public PhysicsEngine() {
		world = World.world;
		detector = new CollisionDetector();
		maxContacts = 30;
		iterations = 40;
		data = new CollisionData(maxContacts);
		contacts = new Contact[maxContacts];
		resolver = new ContactResolver(iterations);
		calculateIterations = (iterations == 0);
		bodies = new ArrayList<RigidBody>();
		forces = new HashMap<String, ForceGenerator>();
		registry = new ForceRegistry();

		forces.put("Gravity", new Gravity(new Vector3f(0, -20, 0)));
	}

	public void startFrame() {
		for (RigidBody body : bodies) {
			body.clearAccumulators();
			body.calculateDerivedData();
		}
	}

	public void gather() {
		CoreEngine.debugBreak();
		physicalComponents = world.getPhysical();
		collidableComponents = world.getCollidable();
		broadphase = world.getBroadCollidables();
		bodies.clear();
		for (Physical physic : physicalComponents) {
			if (physic.isActive())
				bodies.add(physic.getBody());
		}
	}

	public void integrate(float delta) {
		registry.updateForces(delta);
		for (Physical object : physicalComponents) {
			if (object.isActive())
				object.integrate(delta);
		}
	}

	public void simulate(float delta) {
		for (Physical object : physicalComponents) {
			if (object.isActive())
				object.simulate(delta);
		}
	}

	public void generateContacts(float delta) {
		for (int i = 0; i < collidableComponents.size(); i++) {
			for (int j = i + 1; j < collidableComponents.size(); j++) {
				detector.checkCollision(collidableComponents.get(i).getPrimitive(),
						collidableComponents.get(j).getPrimitive(), data);
			}
		}

		// for(int i = 0; i < broadphase.size(); i++)
		// {
		// for(int j = i + 1; j < broadphase.size(); j++)
		// {
		// broadphase(broadphase.get(i).getCollider(),
		// broadphase.get(j).getCollider());
		// }
		// }
	}

	public void broadphase(BoundingCollider one, BoundingCollider two) {
		if (broadDetector.checkCollision(one, two)) {
			ArrayList<Collider> colliders1 = one.getColliders();
			ArrayList<Collider> colliders2 = two.getColliders();
			for (int i = 0; i < colliders1.size(); i++) {
				for (int j = 0; j < colliders2.size(); j++) {
					detector.checkCollision(colliders1.get(i).getPrimitive(), colliders2.get(j).getPrimitive(), data);
				}
			}

			for (BoundingCollider second : two.getSubColliders()) {
				broadphase(one, second);
			}

			for (BoundingCollider first : one.getSubColliders()) {
				broadphase(two, first);
			}
		}
	}

	public void handleCollisions(float delta) {
		if (calculateIterations) {
			resolver.setIterations(0);
		}
		int i = data.contactCount;
		contacts = data.getContacts();
		resolver.resolveContacts(contacts, i, delta);
	}

	public static void addForce(RigidBody body, String forceName) {
		ForceGenerator force = forces.get(forceName);
		if (force != null)
			registry.add(body, force);
	}

	public static void addForce(RigidBody body, ForceGenerator force) {
		String forceName = addForce(force);
		force = forces.get(forceName);
		if (force != null)
			registry.add(body, force);
	}

	public static String addForce(ForceGenerator force) {
		String forceName = force.getClass().getSimpleName();
		ForceGenerator test = forces.get(forceName);
		int i = -1;
		while (test != null) {
			test = forces.get(forceName + ++i);
		}
		if(i != -1)
		{
			forces.put(forceName + i, force);
			return forceName + i;
		}
		else{
			forces.put(forceName, force);
			return forceName;
		}
	}

	public static void addForce(ForceGenerator force, String forceName) {
		forces.put(forceName, force);
	}
	
	public static void attachForce(RigidBody body, ForceGenerator force) {
		if (force != null)
			registry.add(body, force);
	}

	public static void removeForce(RigidBody body, String forceName) {
		ForceGenerator force = forces.get(forceName);
		if (force != null)
			registry.remove(body, force);
	}

	public static void removeForce(RigidBody body, ForceGenerator force) {
		String forceName = getForce(force);
		force = forces.get(forceName);
		if (force != null)
			registry.remove(body, force);
	}
	
	public static ForceGenerator getForce(String forceName) {
		return forces.get(forceName);
	}
	
	public static String getForce(ForceGenerator force) {
		String forceName = force.getClass().getSimpleName();
		ForceGenerator test = forces.get(forceName);
		int i = -1;
		while (test != force) {
			test = forces.get(forceName + ++i);
		}
		return forceName + i;
	}

	public static void removeForce(ForceGenerator force, String forceName) {
		forces.remove(forceName, force);
	}

	public static void printInfo(RigidBody body) {
		System.out.println(body.toString() + ": A" + body.getAcceleration() + ": V" + body.getVelocity() + ": R"
				+ body.getRotation());
	}
}
