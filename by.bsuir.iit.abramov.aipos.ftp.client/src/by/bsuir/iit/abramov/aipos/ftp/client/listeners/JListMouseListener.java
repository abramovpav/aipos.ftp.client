package by.bsuir.iit.abramov.aipos.ftp.client.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JList;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;
import by.bsuir.iit.abramov.aipos.ftp.client.util.FileListItem;
import by.bsuir.iit.abramov.aipos.ftp.client.view.FileList;

public class JListMouseListener implements MouseListener {

	private final JList			list;
	private final Controller	controller;

	public JListMouseListener(final Controller controller, final JList list) {

		this.list = list;
		this.controller = controller;
	}

	private String extractPathInSting(final Object selectedItem, String serverPath) {

		if (FileListItem.class.equals(selectedItem.getClass())) {
			serverPath = ((FileListItem) selectedItem).getName();
		} else if (String.class.equals(selectedItem.getClass())) {
			serverPath = (String) selectedItem;
		}
		return serverPath;
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
		if (FileList.class.equals(list.getClass())) {
			final FileList fileList = (FileList) list;
			if (fileList.isDirectory(selectedItem)) {
				controller.CWD(selectedItem);
			} else {
				String serverPath = "";
				serverPath = extractPathInSting(selectedItem, serverPath);
				if ("".equals(serverPath)) {
					System.out.println("Empty file's path");
					return;
				}
				final JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(new File(serverPath));
				final int returnVal = chooser.showSaveDialog(list);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					final File file = chooser.getSelectedFile();
					controller.RETR(serverPath, file.getAbsolutePath());
				} else {
					System.out.println("cancel retr");
				}
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
