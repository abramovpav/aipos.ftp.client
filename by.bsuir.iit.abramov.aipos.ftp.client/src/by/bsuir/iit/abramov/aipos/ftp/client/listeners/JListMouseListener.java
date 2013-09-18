package by.bsuir.iit.abramov.aipos.ftp.client.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JList;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;
import by.bsuir.iit.abramov.aipos.ftp.client.view.FileList;

public class JListMouseListener implements MouseListener {

	private final JList			list;
	private final Controller	controller;

	public JListMouseListener(final Controller controller, final JList list) {

		this.list = list;
		this.controller = controller;
	}

	@Override
	public void mouseClicked(final MouseEvent e) {

		if (e.getClickCount() != 2) {
			return;
		}
		if (list == null) {
			System.out.println(this.getClass() + ": list == null");
			return;
		}
		final Object selectedItem = list.getSelectedValue();
		final int sel = list.getSelectedIndex();
		if (FileList.class.equals(list.getClass())) {
			final FileList fileList = (FileList) list;
			if (fileList.isDirectory(selectedItem)) {
				controller.CWD(selectedItem);
			}
		}

	}

	@Override
	public void mouseEntered(final MouseEvent e) {

		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(final MouseEvent e) {

		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(final MouseEvent e) {

		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(final MouseEvent e) {

		// TODO Auto-generated method stub

	}

}
