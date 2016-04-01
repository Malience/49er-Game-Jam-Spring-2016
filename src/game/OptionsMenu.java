package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.UI.UIMenu;
import com.base.engine.rendering.UI.UIRect;
import com.base.engine.rendering.UI.UITextButtonRect;

public final class OptionsMenu extends UIMenu {
	private static String buttonTexture = "bricks2.png";
	private static String textFont = "timesNewRoman.png";

	private static float buttonWidth = 75;
	private static float buttonHeight = 50;
	private static float textSize = 64;

	private CloseButton closeButton;

	public OptionsMenu() {
		super();
		this.setAction("Options", GLFW_KEY_ESCAPE);
		UIRect rect = new UIRect("test.png", 100, 200);
		closeButton = new CloseButton("Exit");

		this.addChild(closeButton);

		this.addComponent(rect);
		this.getTransform().setPos(new Vector3f(400, 300, 0));
		System.out.println(closeButton.getTransform().getTransformedPos());
		closeButton.generate();
		rect.generate();
	}

	private abstract class OptionsButton extends UITextButtonRect {

		public OptionsButton(String text) {
			super(buttonTexture, buttonWidth, buttonHeight, textFont, text, textSize);
			// TODO Auto-generated constructor stub
		}
	}

	private class CloseButton extends OptionsButton {

		public CloseButton(String text) {
			super(text);
		}

		@Override
		protected void press() {
			CoreEngine.exit(1);
		}
	}
}
