package by.bsuir.iit.abramov.aipos.ftp.client.view;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;

public class Desktop {
	private final Window	parent;
	private final JPanel	mainPanel;
	private final JPanel	headerPanel;
	private final Logger	logger;

	public Desktop(final Window parent) {

		this.parent = parent;
		mainPanel = new JPanel();
		headerPanel = new DesktopHeaderPanel(this);
		logger = new Logger();
		initialize();
	}

	public void addLogLine(final String text) {

		logger.addLine(text);
	}

	public final Controller getController() {

		if (parent != null) {
			return parent.getController();
		} else {
			return null;
		}
	}

	public final JPanel getPanel() {

		return mainPanel;
	}

	public final Window getParent() {

		return parent;
	}

	private void initialize() {

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(headerPanel);
		mainPanel.add(logger);
		logger.setEditable(false);
		addLogLine("Hello, man!");
	}
}
