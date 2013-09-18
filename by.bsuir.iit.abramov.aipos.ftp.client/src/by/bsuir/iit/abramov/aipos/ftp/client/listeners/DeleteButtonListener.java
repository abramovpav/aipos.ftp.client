package by.bsuir.iit.abramov.aipos.ftp.client.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ExtJButton;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ExtJComponent;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ExtJMenuItem;
import by.bsuir.iit.abramov.aipos.ftp.client.util.FileListItem;

public class DeleteButtonListener implements ActionListener {

	@Override
	public void actionPerformed(final ActionEvent e) {

		Object obj = e.getSource();
		if (!ExtJButton.class.equals(obj.getClass())
				&& !ExtJMenuItem.class.equals(obj.getClass())) {
			System.out.println(this.getClass() + ": " + obj.getClass() + " instead of "
					+ ExtJButton.class + " or " + ExtJMenuItem.class);
			return;
		}

		final ExtJComponent component = (ExtJComponent) obj;
		obj = component.getStoredObject();
		if (obj == null) {
			System.out.println(this.getClass() + ": null instead of " + Controller.class);
			throw new NullPointerException();
		} else if (!Controller.class.equals(obj.getClass())) {
			System.out.println(this.getClass() + ": " + obj.getClass() + " instead of "
					+ Controller.class);
			return;
		}
		final Controller controller = (Controller) obj;
		final Object[] selectedObjects = controller.getSelectedValues();
		if (selectedObjects.length <= 0) {
			JOptionPane.showMessageDialog(null, "No item is selected");
			return;
		}
		for (final Object object : selectedObjects) {
			if (FileListItem.class.equals(object.getClass())) {
				final FileListItem item = (FileListItem) object;
				System.out.println(item.getName());
				if (!item.isDirectory()) {
					controller.delete(item.getName());
				} else {
					System.out.println("it's a directory");
				}
			} else {
				System.out.println("Wrong type of the selected item");
			}
		}

		controller.updateFileList();

	}

}
