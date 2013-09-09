package by.bsuir.iit.abramov.aipos.ftp.client.util;

import javax.swing.JMenuItem;

public class ExtJMenuItem extends JMenuItem implements ExtJComponent {
	private final Object	controller;

	public ExtJMenuItem(final String caption, final Object controller) {

		super(caption);
		this.controller = controller;
	}

	@Override
	public final Object getStoredObject() {

		return controller;
	}
}
