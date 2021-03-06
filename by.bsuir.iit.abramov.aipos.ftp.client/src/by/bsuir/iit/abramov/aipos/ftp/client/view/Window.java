package by.bsuir.iit.abramov.aipos.ftp.client.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;

import javax.swing.JFrame;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;
import by.bsuir.iit.abramov.aipos.ftp.client.util.FileListItem;

public class Window extends JFrame {
	private static final String	TITLE	= "Title";
	private final Controller	controller;
	private final Desktop		desktop;
	private final MenuBar		menuBar;

	public Window(final Controller controller) {

		super(Window.TITLE);
		this.controller = controller;
		desktop = new Desktop(this);
		menuBar = new MenuBar(this);
		getContentPane().add(desktop.getPanel());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setJMenuBar(menuBar);
		setMinimumSize(new Dimension(800, 600));
	}

	public void addLogLine(final String text) {

		desktop.addLogLine(text);
	}

	public void clearFileList() {

		desktop.clearFileList();
	}

	public final Controller getController() {

		return controller;
	}

	public Object[] getSelectedValues() {

		return desktop.getSelectedValues();
	}

	public void openManager() {

		final Manager manager = new Manager(controller);
		manager.setModal(true);
		manager.setVisible(true);
	}

	public void setFilesInList(final List<FileListItem> arg0) {

		desktop.setFilesInList(arg0);
	}
}
