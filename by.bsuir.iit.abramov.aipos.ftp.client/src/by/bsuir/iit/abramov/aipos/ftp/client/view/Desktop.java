package by.bsuir.iit.abramov.aipos.ftp.client.view;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;
import by.bsuir.iit.abramov.aipos.ftp.client.listeners.JListMouseListener;
import by.bsuir.iit.abramov.aipos.ftp.client.model.ListModel;
import by.bsuir.iit.abramov.aipos.ftp.client.util.FileListItem;

public class Desktop {
	private final Window	parent;
	private final JPanel	mainPanel;
	private final JPanel	headerPanel;
	private final JPanel	filePanel;
	private final Logger	logger;
	private final FileList	fileList;

	public Desktop(final Window parent) {

		this.parent = parent;
		mainPanel = new JPanel();
		headerPanel = new DesktopHeaderPanel(this);
		filePanel = new JPanel();
		logger = new Logger();
		final ListModel listModel = new ListModel();
		fileList = new FileList();
		initialize();
	}

	public void addLogLine(final String text) {

		logger.addLine(text);
	}

	public void clearFileList() {

		fileList.clear();
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

	public final Object[] getSelectedValues() {

		return fileList.getSelectedValues();
	}

	private void initFilePanel() {

		fileList.setVisibleRowCount(30);
		fileList.addMouseListener(new JListMouseListener(getController(), fileList));

		final JScrollPane scrollList = new JScrollPane(fileList);

		filePanel.add(scrollList);
		fileList.setSize(500, 500);

	}

	private void initialize() {

		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(headerPanel);
		final JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		mainPanel.add(panel);
		final JScrollPane scrollList = new JScrollPane(logger);
		panel.add(scrollList);
		panel.add(filePanel);
		logger.setEditable(false);
		addLogLine("Hello, man!");
		initFilePanel();
	}

	public void setFilesInList(final List<FileListItem> arg0) {

		fileList.clear();
		fileList.addFiles(arg0);
	}

}
