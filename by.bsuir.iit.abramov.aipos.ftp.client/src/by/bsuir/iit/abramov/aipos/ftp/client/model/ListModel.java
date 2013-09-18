package by.bsuir.iit.abramov.aipos.ftp.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import by.bsuir.iit.abramov.aipos.ftp.client.util.FileListItem;

public class ListModel extends AbstractListModel {// extends DefaultListModel {
	private final List<Object>	list;

	public ListModel() {

		super();
		list = new ArrayList<Object>();
	}

	public void addAll(final List<FileListItem> arg0) {

		final int index0 = getSize();
		list.addAll(arg0);
		final int index1 = getSize();
		fireIntervalAdded(this, index0, index1);
	}

	public void addElement(final Object obj) {

		list.add(obj);
		final int index = getSize();
		fireIntervalAdded(this, index, index);
	}

	public void clear() {

		final int index1 = list.size() - 1;
		list.clear();
		if (index1 >= 0) {
			fireIntervalRemoved(this, 0, index1);
		}
	}

	public final Object get(final int index) {

		return list.get(index);
	}

	@Override
	public Object getElementAt(final int index) {

		return list.get(index);
	}

	@Override
	public int getSize() {

		return list.size();
	}
}
