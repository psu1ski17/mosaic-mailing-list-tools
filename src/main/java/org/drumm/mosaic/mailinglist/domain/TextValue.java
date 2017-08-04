package org.drumm.mosaic.mailinglist.domain;

import java.io.Serializable;

public class TextValue implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	private int value;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "TextValue [text=" + text + ", value=" + value + "]";
	}

}
