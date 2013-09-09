package by.bsuir.iit.abramov.aipos.ftp.client.view;

import javax.swing.JPanel;

import by.bsuir.iit.abramov.aipos.ftp.client.util.Button;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ExtJButton;
import by.bsuir.iit.abramov.aipos.ftp.client.util.Factory;

public class DesktopHeaderPanel extends JPanel {

	private final Desktop	parent;

	public DesktopHeaderPanel(final Desktop parent) {

		this.parent = parent;
		initialize();
	}

	private void createButton(final Button eButton) {

		if (eButton.isVisible()) {
			final ExtJButton button = new ExtJButton(eButton.getCaption(),
					parent.getController());
			add(button);
			button.addActionListener(Factory.getButtonController(eButton));
		}
	}

	private void initialize() {

		for (final Button eButton : Button.values()) {
			createButton(eButton);
		}
	}
}
