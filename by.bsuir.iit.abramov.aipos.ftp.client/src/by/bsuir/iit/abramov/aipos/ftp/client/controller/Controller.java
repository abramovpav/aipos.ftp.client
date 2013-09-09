package by.bsuir.iit.abramov.aipos.ftp.client.controller;

import by.bsuir.iit.abramov.aipos.ftp.client.model.Model;
import by.bsuir.iit.abramov.aipos.ftp.client.view.Window;

public class Controller {
	private Model	model;
	private Window	window;

	public void addLogLine(final String text) {

		window.addLogLine(text);
	}

	public void connect(final String host, final int port, final String user,
			final String pass) {

		model.connect(host, port, user, pass);
	}

	public void openManager() {

		window.openManager();
	}

	public void setModel(final Model model) {

		this.model = model;
	}

	public void setWindow(final Window window) {

		this.window = window;
	}
}
