package com.base.engine.rendering;

import java.nio.Buffer;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.math.Vector3f;

public class MaterialAttachment {
	private String name;
	private Object data;
	private String type;

	public MaterialAttachment(String name, Buffer buffer) {
		this.name = name;
		data = buffer;
		type = "buffer";
	}

	public MaterialAttachment(String name, Vector3f vector3f) {
		this.name = name;
		data = vector3f;
		type = "vector3f";
	}

	public MaterialAttachment(String name, float floatValue) {
		this.name = name;
		data = new Float(floatValue);
		type = "float";
	}

	public MaterialAttachment(String name, int intValue) {
		this.name = name;
		data = new Integer(intValue);
		type = "int";
	}

	public MaterialAttachment(String name, boolean boolValue) {
		this.name = name;
		data = new Boolean(boolValue);
		type = "boolean";
	}

	public MaterialAttachment(String name, Texture texture) {
		this.name = name;
		data = texture;
		type = "texture";
	}

	public void apply(Material material) {
		switch (type) {
		case "buffer":
			material.addBuffer(name, (Buffer) data);
			break;
		case "vector3f":
			material.addVector3f(name, (Vector3f) data);
			break;
		case "float":
			material.addFloat(name, (float) data);
			break;
		case "int":
			material.addInt(name, (int) data);
			break;
		case "boolean":
			material.addBoolean(name, (boolean) data);
			break;
		case "texture":
			material.addTexture(name, (Texture) data);
			break;
		default:
			CoreEngine.exit(0);
		}
	}
}
