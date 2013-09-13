package by.bsuir.iit.abramov.aipos.ftp.client.view;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JList;

import by.bsuir.iit.abramov.aipos.ftp.client.model.ListModel;

public class FileList extends JList {
	private boolean					vertical;
	private ListModel				listModel;
	private Map<Object, Boolean>	map;

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

	public void clearMap() {

		map.clear();
	}

	public boolean containsKey(final Object key) {

		return map.containsKey(key);
	}

	private void initialize() {

		map = new HashMap<Object, Boolean>();
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

		if (map.containsKey(obj)) {
			return map.get(obj);
		}
		return false;
	}

	public void put(final Object item, final Boolean bool) {

		map.put(item, bool);
	}
}
