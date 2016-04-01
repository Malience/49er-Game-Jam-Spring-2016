package com.base.engine.rendering.UI;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL15;

import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.core.Util;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Texture;

public class UIGauge extends UIElement implements UIRenderable {
	float length;
	float width = 200;
	float radius;
	float current;
	float pivot;

	private static final Texture pointerTexture = new Texture("vOxygenGauge001Pointer.png");

	public UIGauge(String texturePath, float radius, float start, float length) {
		this(texturePath, radius, start, length, 0);
	}

	public UIGauge(String texturePath, float radius, float start, float length, float pivot) {
		super(texturePath);
		this.radius = radius;
		this.current = start;
		this.length = length;
		this.pivot = pivot;
	}

	public void setPointer(float angle) {
		current = angle;
		generatePointer();
	}

	public void rotatePointer(float angle) {
		current += angle;
		generatePointer();
	}

	@Override
	public void generate() {
		Vector2f position = this.getTransform().getPos().getXY();
		vertices = new Vector2f[] { new Vector2f(position.x + radius, position.y + radius),
				new Vector2f(position.x - radius, position.y + radius),
				new Vector2f(position.x + radius, position.y - radius),
				new Vector2f(position.x - radius, position.y - radius) };

		indices = new int[] { 0, 2, 1, 1, 2, 3 };

		texCoords = new Vector2f[] { new Vector2f(1, 1), new Vector2f(0, 1), new Vector2f(1, 0), new Vector2f(0, 0) };

		generatePointer();
	}

	Vector2f[] pointerVertices;
	Vector2f[] pointerTexcoords;
	int[] pointerIndices;

	public void generatePointer() {
		Vector2f dir = new Vector3f(0, length, 0).rotate(new Vector3f(0, 0, 1), (float) Math.toRadians(current))
				.getXY();
		Vector2f position = this.getTransform().getPos().getXY();
		dir = dir.add(position);

		pointerVertices = new Vector2f[] { new Vector2f(width / 2, length).rotate(current),
				new Vector2f(-width / 2, length).rotate(current), new Vector2f(width / 2, -pivot).rotate(current),
				new Vector2f(-width / 2, -pivot).rotate(current) };

		for (int i = 0; i < pointerVertices.length; i++) {
			pointerVertices[i] = pointerVertices[i].add(position);
		}

		pointerIndices = new int[] { 0, 2, 1, 1, 2, 3 };

		pointerTexcoords = new Vector2f[] { new Vector2f(1, 1), new Vector2f(0, 1), new Vector2f(1, 0),
				new Vector2f(0, 0) };
	}

	@Override
	public int render(RenderingEngine renderingEngine) {
		super.render(renderingEngine);

		pointerTexture.bind();

		int vbo = glGenBuffers();
		int ibo = glGenBuffers();

		FloatBuffer buffer = Util.createFloatBuffer(pointerVertices.length * UISIZE);
		// TODO make cleaner
		for (int i = 0; i < pointerVertices.length; i++) {
			buffer.put((float) pointerVertices[i].getX());
			buffer.put((float) pointerVertices[i].getY());
			buffer.put((float) pointerTexcoords[i].getX());
			buffer.put((float) pointerTexcoords[i].getY());
		}

		buffer.flip();

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 2, GL_FLOAT, false, UISIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, UISIZE * 4, 8);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(pointerIndices), GL_STATIC_DRAW);
		glDrawElements(GL_TRIANGLES, pointerIndices.length, GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);

		glDeleteBuffers(vbo);
		glDeleteBuffers(ibo);
		return 1;
	}
}
