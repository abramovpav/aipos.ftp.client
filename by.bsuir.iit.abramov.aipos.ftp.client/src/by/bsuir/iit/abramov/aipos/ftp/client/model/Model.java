package by.bsuir.iit.abramov.aipos.ftp.client.model;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;

public class Model {

	private final Controller	controller;
	private Connection			connection;

	public Model(final Controller controller) {

		this.controller = controller;
	}

	public void connect(final String host, final int port, final String user,
			final String pass) {

		connection = new Connection(controller, host, port, user, pass);
		final Thread thread = new Thread(connection);
		thread.start();
	}
}
