package com.base.engine.rendering;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import java.util.ArrayList;
import java.util.HashMap;

import com.base.engine.components.attachments.LightAttachment;
import com.base.engine.components.attachments.Renderable;
import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.components.lighting.BaseLight;
import com.base.engine.components.movenlook.Camera;
import com.base.engine.core.CoreEngine;
import com.base.engine.core.GameObject;
import com.base.engine.core.World;
import com.base.engine.core.math.Transform;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.MeshLoading.ResourceManagement.MappedValues;
import com.base.engine.rendering.UI.UIComponent;
import com.base.engine.rendering.UI.UIElement;
import com.base.engine.rendering.UI.UIGaugePerpetualSpin;
import com.base.engine.rendering.UI.UIObject;

public class RenderingEngine extends MappedValues {
	World world;

	public static Camera mainCamera;

	private ArrayList<LightAttachment> lights;
	private LightAttachment activeLight;

	public static GameObject ui;
	private Shader UIShader;
	private Shader straight;

	private HashMap<String, Integer> samplerMap;
	private Shader forwardAmbient;

	private static ArrayList<Renderable> renders;
	private static ArrayList<UIRenderable> uiRenders;

	private static ArrayList<Renderable> highlight;
	private static ArrayList<UIRenderable> uiHighlight;

	// private Shader shadowShader;
	// private ShadowFBO shadowMapFBO;

	public RenderingEngine() {
		super();
		world = World.world;

		samplerMap = new HashMap<String, Integer>();
		samplerMap.put("diffuse", 0);
		// samplerMap.put("shadowMap", 0);

		addVector3f("ambient", new Vector3f(0.1f, 0.1f, 0.1f));

		forwardAmbient = new Shader("forward-ambient");
		// shadowShader = new Shader("shadowShader");
		straight = new Shader("straight");

		// shadowMapFBO = new ShadowFBO();

		ui = new UIObject();
		world.addToBucket(ui);
		UIShader = new Shader("UI");

		highlight = new ArrayList<Renderable>();
		uiHighlight = new ArrayList<UIRenderable>();

		UIObject object = new UIObject();
		// UIElement component = new UIBarRectBoundedIncrement("red.png", 400,
		// 10, true);
		UIElement component = new UIGaugePerpetualSpin("vOxygenGauge001.png", 100, 80, 100, 100);
		object.getTransform().setPos(new Vector3f(200, 300, 0));
		object.addComponent(component);
		component.generate();

		// addUI(object);

		glClearColor(world.clearColor[0], world.clearColor[1], world.clearColor[2], world.clearColor[3]);

		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);

		glEnable(GL_DEPTH_CLAMP);

		glEnable(GL_TEXTURE_2D);
	}

	public void updateUniformStruct(Transform transform, Material material, RenderingEngine renderingEngine,
			String uniformName, String uniformType) {
		throw new IllegalArgumentException(uniformName + " is not a supported type in Rendering Engine");
	}

	public void gather() {
		lights = world.getLightAttachments();
		renders = world.getRenderable();
		uiRenders = world.getUIRenderable();

		int n = uiRenders.size();
		UIRenderable[] ren = new UIRenderable[n];
		for (int i = 0; i < n; i++) {
			ren[i] = uiRenders.get(i);
		}
		n = ren.length;
		for (int i = 0; i < n - 1; i++) {
			int min = i;
			for (int j = i + 1; j < n; j++) {
				if (ren[j].getPriority() < ren[min].getPriority()) {
					min = j;
				}
			}
			if (min != i) {
				UIRenderable swap = ren[i];
				ren[i] = ren[min];
				ren[min] = swap;
			}
		}

		uiRenders = new ArrayList<UIRenderable>();
		for (int i = 0; i < ren.length; i++) {
			uiRenders.add(ren[i]);
		}

		// CoreEngine.debugBreak();
		if (2 + 2 == 4)
			return;

	}

	public void render() {
		// Shadows
		// shadowMapFBO.write();
		//
		// glClear(GL_DEPTH_BUFFER_BIT);
		// DirectionalLight dlight = null;
		// for(BaseLight light : lights)
		// {
		// if(light instanceof DirectionalLight)
		// {
		// dlight = (DirectionalLight) light;
		// break;
		// }
		// }
		// if(dlight != null)
		// {
		// Vector3f lightInvDir = dlight.getDirection();
		//
		// Matrix4f depthProjectionMatrix = new Matrix4f().initOrthographic(-10,
		// 10, -10, 10, -10, 20);
		// Matrix4f depthViewMatrix = new
		// Transform().getLookAtRotation(lightInvDir, new
		// Vector3f(0,1,0)).toRotationMatrix();
		// //Matrix4f depthModelMatrix = new Matrix4f().initIdentity();
		// //Doesn't matter
		// Matrix4f depthMVP = depthProjectionMatrix.mul(depthViewMatrix);
		// dlight.setMVP(depthMVP);
		//
		// samplerMap.put("shadowMap", shadowMapFBO.getShadowMap());
		//
		// activeLight = dlight;
		// object.renderAll(shadowShader, this);
		// }
		// Rendering

		Window.bindAsRenderTarget();
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		renderAll(forwardAmbient);

		glBlendFunc(GL_ONE, GL_ONE);
		glDepthMask(false);
		glDepthFunc(GL_EQUAL);

		for (LightAttachment light : lights) {
			if (!light.isActive())
				continue;
			activeLight = light;
			renderAll(light.getShader());
		}

		glDepthFunc(GL_LESS);
		glDepthMask(true);
		glDisable(GL_BLEND);

		// Outline

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glPolygonMode(GL_BACK, GL_LINE);
		glLineWidth(4);
		glCullFace(GL_FRONT);
		glDepthFunc(GL_LEQUAL);
		glColor3f(255, 255, 0);

		renderAllHighlight(straight);

		glDepthFunc(GL_LESS);
		glCullFace(GL_BACK);
		glPolygonMode(GL_BACK, GL_FILL);
		glDisable(GL_BLEND);

		// UI RENDERING
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		glDepthMask(false);
		// glDepthFunc(GL_EQUAL);

		// UIShader.bind();

		renderAllUI();

		// glDepthFunc(GL_LESS);
		glDepthMask(true);
		glDisable(GL_BLEND);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glPolygonMode(GL_BACK, GL_LINE);
		glLineWidth(20);
		glCullFace(GL_FRONT);
		glDepthFunc(GL_LEQUAL);
		glColor3f(255, 255, 0);

		renderAllUIHighlight(forwardAmbient);

		glDepthFunc(GL_LESS);
		glCullFace(GL_BACK);
		glPolygonMode(GL_BACK, GL_FILL);
		glDisable(GL_BLEND);

		highlight = new ArrayList<Renderable>();
		uiHighlight = new ArrayList<UIRenderable>();
	}

	public static void addRender(UIRenderable ui) {
		uiRenders.add(ui);
	}

	public void renderAll(Shader shader) {
		for (Renderable render : renders) {
			if (render.isActive())
				render.render(shader, this);
		}
	}

	public void renderAllUI() {
		//CoreEngine.debugBreak();
		for (UIRenderable render : uiRenders) {
			if (render.isActive())
				render.render(this);
		}
	}

	public void renderAllHighlight(Shader shader) {
		for (Renderable render : highlight) {
			if (render.isActive())
				render.render(shader, this);
		}
	}

	public void renderAllUIHighlight(Shader shader) {
		for (UIRenderable render : uiHighlight) {
			if (render.isActive())
				render.render(this);
		}
	}

	public static void highlight(Renderable render) {
		highlight.add(render);
	}

	public static void highlight(UIRenderable render) {
		uiHighlight.add(render);
	}

	// private static void clearScreen()
	// {
	// //TODO: Stencil Buffer
	// glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	// }

	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}

	public static void addUI(UIObject uiObject) {
		ui.addChild(uiObject);
	}

	public static void addUI(UIComponent uiComponent) {
		ui.addComponent(uiComponent);
	}

	public void addLight(BaseLight light) {
		lights.add(light);
	}

	public void addCamera(Camera camera) {
		mainCamera = camera;
	}

	public void putSampler(String samplerName, int samplerSlot) {
		samplerMap.put(samplerName, samplerSlot);
	}

	public int getSamplerSlot(String samplerName) {
		return samplerMap.get(samplerName);
	}

	public LightAttachment getActiveLight() {
		return activeLight;
	}

	public Camera getMainCamera() {
		return mainCamera;
	}

	public static void setMainCamera(Camera mc) {
		mainCamera = mc;
	}
}
