package by.bsuir.iit.abramov.aipos.ftp.client.util;

import javax.swing.JButton;

public class ExtJButton extends JButton implements ExtJComponent {
	private final Object	controller;
	private final String	caption;

	public ExtJButton(final String caption, final Object container) {

		super(caption);
		controller = container;
		this.caption = caption;
		setFocusable(false);
	}

	public String getCaption() {

		return caption;
	}

	@Override
	public final Object getStoredObject() {

		return controller;
	}
}
