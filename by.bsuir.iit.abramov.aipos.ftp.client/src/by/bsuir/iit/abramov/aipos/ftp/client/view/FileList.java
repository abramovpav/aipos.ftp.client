package by.bsuir.iit.abramov.aipos.ftp.client.view;

import java.io.File;
import java.util.List;

import javax.swing.JList;

import by.bsuir.iit.abramov.aipos.ftp.client.model.ListModel;

public class FileList extends JList {
	private boolean		vertical;
	private ListModel	listModel;

	public FileList() {

		super();
		initialize();
	}

	public FileList(final File[] files) {

		super(files);
		initialize();
	}

	public FileList(final javax.swing.ListModel model) {

		super(model);
		initialize();
	}

	public void addElement(final String element) {

		listModel.addElement(element);
	}

	public void addList(final List<String> list) {

		for (int i = 0; i < list.size(); i++) {
			addElement(list.get(i));
		}
	}

	public void clear() {

		listModel.clear();
	}

	private void initialize() {

		listModel = new ListModel();
		setModel(listModel);
		vertical = true;
		setCellRenderer(new FileRenderer(!vertical));

		if (!vertical) {
			setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
			setVisibleRowCount(-1);
		} else {
			setVisibleRowCount(30);
		}
	}
}
