package by.bsuir.iit.abramov.aipos.ftp.client.model;

import java.io.IOException;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;

public class Model {

	private final Controller	controller;
	private Connection			connection;

	public Model(final Controller controller) {

		this.controller = controller;
	}

	public void connect(final String host, final int port, final String user,
			final String pass) throws IOException {

		connection = new Connection(controller, host, port, user, pass);
		final Thread thread = new Thread(connection);
		thread.start();
	}

	public void updateFileList() {

		try {
			connection.updateList();
		} catch (final IOException e) {
			e.printStackTrace();
			return;
		}
	}
}
