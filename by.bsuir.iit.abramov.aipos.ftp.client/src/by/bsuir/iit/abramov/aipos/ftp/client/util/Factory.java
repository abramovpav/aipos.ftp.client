package by.bsuir.iit.abramov.aipos.ftp.client.util;

import java.awt.event.ActionListener;

import by.bsuir.iit.abramov.aipos.ftp.client.listeners.CDUPButtonListener;
import by.bsuir.iit.abramov.aipos.ftp.client.listeners.DeleteButtonListener;
import by.bsuir.iit.abramov.aipos.ftp.client.listeners.DisconnectButtonListener;
import by.bsuir.iit.abramov.aipos.ftp.client.listeners.ManagerConnectButtonListener;
import by.bsuir.iit.abramov.aipos.ftp.client.listeners.OpenManagerButtonListener;
import by.bsuir.iit.abramov.aipos.ftp.client.listeners.RefreshButtonListener;
import by.bsuir.iit.abramov.aipos.ftp.client.listeners.STOREButtonListener;

public class Factory {
	static public ActionListener getButtonController(final Button eButton) {

		switch (eButton) {
			case MANAGER:
				return new OpenManagerButtonListener();
			case DISCONNECT:
				return new DisconnectButtonListener();
			case CWD:
				return new CDUPButtonListener();
			case STORE:
				return new STOREButtonListener();
			case DELETE:
				return new DeleteButtonListener();
			case REFRESH:
				return new RefreshButtonListener();
			default:
				return null;
		}
	}

	static public ActionListener getManagerButtonsController(
			final ServerManagerButtonEnum eButton) {

		switch (eButton) {
			case CONNECT:
				return new ManagerConnectButtonListener();
			case OK:
				return null;
			case CANCEL:
				return null;
			case NEW:
				return null;
			case DELETE:
				return null;
			case RENAME:
				return null;
			default:
				return null;
		}
	}

	static public ActionListener getMenuItemController(final MenuItemEnum menu) {

		switch (menu) {
			case MANAGER:
				return new OpenManagerButtonListener();
			case EXIT:
				return null;
			case INFORMATION:
				return null;
			default:
				return null;
		}
	}
}
