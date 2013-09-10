package by.bsuir.iit.abramov.aipos.ftp.client.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ExtJButton;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ExtJComponent;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ExtJMenuItem;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ServerManagerTextField;
import by.bsuir.iit.abramov.aipos.ftp.client.view.Manager;

public class ManagerConnectButtonListener implements ActionListener {

	@Override
	public void actionPerformed(final ActionEvent e) {

		Object obj = e.getSource();
		if (!ExtJButton.class.equals(obj.getClass())
				&& !ExtJMenuItem.class.equals(obj.getClass())) {
			System.out.println(this.getClass() + ": " + obj.getClass() + " instead of "
					+ ExtJButton.class);
			return;
		}

		final ExtJComponent component = (ExtJComponent) obj;
		obj = component.getStoredObject();
		if (obj == null) {
			System.out.println(this.getClass() + ": null instead of " + Manager.class);
			throw new NullPointerException();
		} else if (!Manager.class.equals(obj.getClass())) {
			System.out.println(this.getClass() + ": " + obj.getClass() + " instead of "
					+ Manager.class);
			return;
		}
		final Manager manager = (Manager) obj;
		if (manager.isCorrectDataInFields()) {
			String host, port_str, user, pass;
			int port;
			host = manager.getTextFromTextField(ServerManagerTextField.HOST);
			port_str = manager.getTextFromTextField(ServerManagerTextField.PORT);
			port = Integer.parseInt(port_str);
			user = manager.getTextFromTextField(ServerManagerTextField.USER);
			pass = manager.getTextFromTextField(ServerManagerTextField.PASS);

			final Controller controller = manager.getController();
			if (controller != null) {
				try {
					controller.connect(host, port, user, pass);
				} catch (final IOException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(manager,
							"Unable to connect/read list of files");
					return;
				}
			} else {
				System.out.println(this.getClass() + ": controller == null");
			}

			manager.setVisible(false);
			manager.dispose();
		} else {
			JOptionPane.showMessageDialog(null, "Enter correct data!");
		}
	}

}
