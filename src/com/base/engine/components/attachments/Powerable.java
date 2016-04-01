package com.base.engine.components.attachments;

public interface Powerable extends ComponentAttachment {
	public default int power(float delta) {
		return 0;
	}
}
