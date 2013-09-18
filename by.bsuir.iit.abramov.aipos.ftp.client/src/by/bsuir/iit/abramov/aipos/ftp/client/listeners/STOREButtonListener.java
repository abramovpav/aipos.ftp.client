package by.bsuir.iit.abramov.aipos.ftp.client.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ExtJButton;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ExtJComponent;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ExtJMenuItem;

public class STOREButtonListener implements ActionListener {

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

		final JFileChooser chooser = new JFileChooser();
		final int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			final File file = chooser.getSelectedFile();
			controller.STORE(file.getAbsolutePath());
		} else {
			System.out.println("cancel retr");
		}

	}

}
