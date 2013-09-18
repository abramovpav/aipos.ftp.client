package by.bsuir.iit.abramov.aipos.ftp.client.controller;

import java.io.IOException;
import java.util.List;

import by.bsuir.iit.abramov.aipos.ftp.client.model.Model;
import by.bsuir.iit.abramov.aipos.ftp.client.util.FileListItem;
import by.bsuir.iit.abramov.aipos.ftp.client.view.Window;

public class Controller {
	private Model	model;
	private Window	window;

	public void addLogLine(final String text) {

		window.addLogLine(text);
	}

	public void CDUP() {

		model.CDUP();
	}

	public void clearFileList() {

		window.clearFileList();
	}

	public void connect(final String host, final int port, final String user,
			final String pass) throws IOException {

		model.connect(host, port, user, pass);
	}

	public void CWD(final Object path) {

		model.CWD(path);
	}

	public void openManager() {

		window.openManager();
	}

	public void quit() {

		model.quit();
	}

	public void RETR(final String serverPath, final String path) {

		model.RETR(serverPath, path);
	}

	public void setFilesInList(final List<FileListItem> arg0) {

		window.setFilesInList(arg0);
	}

	public void setModel(final Model model) {

		this.model = model;
	}

	public void setWindow(final Window window) {

		this.window = window;
	}

	public void STORE(final String path) {

		// model.STORE(path);
	}

	public void updateFileList() {

		model.updateFileList();
	}
}
