package com.base.engine.electricity.console;

import com.base.engine.components.attachments.Controlable;
import com.base.engine.components.attachments.Menu;
import com.base.engine.components.attachments.UIRenderable;
import com.base.engine.core.GameObject;
import com.base.engine.core.math.Transform;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.electricity.RoomPowerUnit;
import com.base.engine.rendering.UI.UIButtonRect;
import com.base.engine.rendering.UI.UIObject;
import com.base.engine.rendering.UI.UIRect;
import com.base.engine.rendering.UI.UISliderCapped;

public class LightingConsole extends PoweredConsole
{
	private boolean auto;
	private LightingControls controls;
	
	public LightingConsole(RoomPowerUnit rpu){this(rpu, RoomPowerUnit.SUBSYSTEM);}
	public LightingConsole(RoomPowerUnit rpu, float powerLevel) {
		super(rpu, powerLevel);
		auto = true;
		controls = new LightingControls();
		//this.addChild(controls);
		this.addMenu(controls);
	}
	
	public float get(int i)
	{
		return controls.get(i);
	}
	
	private class LightingControls extends ConsoleMenu implements Menu
	{
		LinkedSlider[] sliders;
		UIRect autoback;
		
		public LightingControls()
		{
			super();
			Vector2f size = new Vector2f(12, 128);
			Vector2f sliderSize = new Vector2f(16, 8);
			String sliderTexture = "red.png";
			
			sliders = new LinkedSlider[]
					{
							new LinkedSlider(sliderTexture, size, sliderSize, RoomPowerUnit.LIGHTING, false),
							new LinkedSlider(sliderTexture, size, sliderSize, RoomPowerUnit.LOWLIGHTS, false),
							new LinkedSlider(sliderTexture, size, sliderSize, RoomPowerUnit.DARKLIGHTS, false)
					};
			
			sliders[0].setMinimum(sliders[1]);
			sliders[1].setMaximum(sliders[0]);
			sliders[1].setMinimum(sliders[2]);
			sliders[2].setMaximum(sliders[1]);
			
			
			sliders[0].getTransform().setPos(new Vector3f(-128,20,0));
			sliders[1].getTransform().setPos(new Vector3f(0,20,0));
			sliders[2].getTransform().setPos(new Vector3f(128,20,0));
			
			this.addChild(sliders[0]);
			this.addChild(sliders[1]);
			this.addChild(sliders[2]);
			
			Vector3f buttonpos = new Vector3f(0, -128, 0);
			Vector2f buttonsize = new Vector2f(64,32);
			
			autoback = new UIRect("green.png", buttonsize.add(5));
			autoback.priority = 11;
			
			UIObject button = new UIObject();
			
			button.addComponent(autoback);
			button.addComponent(new Toggle("ui-auto.png", buttonsize));
			button.getTransform().setPos(buttonpos);
			
			this.addChild(button);
		}
		
		public float get(int i)
		{
			return sliders[i].get();
		}
		
		private class Toggle extends UIButtonRect implements UIRenderable, Controlable
		{

			public Toggle(String texturePath, Vector2f halfSize) {
				super(texturePath, halfSize);
				this.priority = 12;
			}
			
			@Override
			public void press()
			{
				auto = !auto;
				System.out.println(auto);
				autoback.setActive(auto);
			}
		}
		
		private class LinkedSlider extends UISliderCapped
		{
			LinkedSlider maximum;
			LinkedSlider minimum;
			
			LinkedSlider(String texturePath, Vector2f size, Vector2f sliderSize, float start, boolean horizontal) {
				super(texturePath, size, sliderSize, start, horizontal, 1, 0);
			}
			
			public void setMaximum(LinkedSlider maximum)
			{
				this.maximum = maximum;
				this.setMax(maximum.get());
			}
			public void setMinimum(LinkedSlider minimum)
			{
				this.minimum = minimum;
				this.setMin(minimum.get());
			}
			
			@Override
			public void change(Vector2f loc)
			{
				if(!auto)
				{
					super.change(loc);
					
					if(maximum != null) maximum.setMin(this.get());
					if(minimum != null) minimum.setMax(this.get());
				}
			}
		}
	}
	
}
