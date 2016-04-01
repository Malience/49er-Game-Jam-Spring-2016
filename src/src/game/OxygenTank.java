package game;

import com.base.engine.components.attachments.Draggable;
import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.components.attachments.Updatable;
import com.base.engine.core.math.Vector2f;
import com.base.engine.rendering.UI.UIGaugeBound;
import com.base.engine.rendering.UI.UIInventoryItemRectSpecific;

public class OxygenTank extends UIInventoryItemRectSpecific implements UIRenderable, Draggable, Updatable
{
	float o2;
	UIGaugeBound gauge;

	public OxygenTank(String texturePath, Vector2f halfSize) {
		super(texturePath, halfSize, "Back");
		o2 = 480;
		gauge = new UIGaugeBound("vOxygenGauge001.png", 100, -144, 100, 100, -144, 144);
	}

	public boolean breath(float amount)
	{
		o2 -= amount;
		gauge.rotateCounterClockwise(amount*3/5);
		if(o2 <= 0) return false;
		return true;
	}
	
	public UIGaugeBound getGauge()
	{
		return gauge;
	}

}
