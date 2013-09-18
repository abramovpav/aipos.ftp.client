package by.bsuir.iit.abramov.aipos.ftp.client.model;

import java.io.IOException;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;
import by.bsuir.iit.abramov.aipos.ftp.client.util.FileListItem;

public class Model {

	private final Controller	controller;
	private Connection			connection;

	public Model(final Controller controller) {

		this.controller = controller;
	}

	public void CDUP() {

		connection.taskChangeDirectoryUP();
	}

	public void connect(final String host, final int port, final String user,
			final String pass) throws IOException {

		connection = new Connection(controller, host, port, user, pass);
		final Thread thread = new Thread(connection);
		thread.start();
	}

	public void CWD(final Object path) {

		if (path == null) {
			errorMessage();
			return;
		}
		if (FileListItem.class.equals(path.getClass())) {
			connection.taskChangeDirectory(((FileListItem) path).getName());
		} else if (String.class.equals(path.getClass())) {
			connection.taskChangeDirectory((String) path);
		} else {
			errorMessage();
		}
	}

	private void errorMessage() {

		controller.addLogLine("Wrong path");
		System.out.println("Wrong path");
	}

	public void quit() {

		connection.taskQUIT();
	}

	public void RETR(final String serverPath, final String path) {

		if ("".equals(serverPath)) {
			errorMessage();
			return;
		}
		connection.taskRETRFile(serverPath, path);
	}

	public void updateFileList() {

		connection.taskUpdateList();
	}
}
