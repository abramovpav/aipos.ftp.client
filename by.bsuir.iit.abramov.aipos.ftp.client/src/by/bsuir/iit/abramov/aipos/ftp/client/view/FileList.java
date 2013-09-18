package by.bsuir.iit.abramov.aipos.ftp.client.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;

import by.bsuir.iit.abramov.aipos.ftp.client.model.ListModel;
import by.bsuir.iit.abramov.aipos.ftp.client.util.FileListItem;

public class FileList extends JList {
	private boolean			vertical;
	private ListModel		listModel;
	private List<Object>	map;

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

	public void addElement(final FileListItem element) {

		listModel.addElement(element);
	}

	public void addFiles(final List<FileListItem> arg0) {

		listModel.addAll(arg0);
	}

	public void clear() {

		listModel.clear();
	}

	public void clearMap() {

		map.clear();
	}

	public boolean containsKey(final Object obj) {

		return map.contains(obj);
	}

	private void initialize() {

		map = new ArrayList<Object>();
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

	public boolean isDirectory(final Object obj) {

		if (map.contains(obj)) {
			if (FileListItem.class.equals(obj.getClass())) {
				return ((FileListItem) obj).isDirectory();
			}
		}
		return false;
	}

	public void put(final Object item) {

		map.add(item);
	}
}
