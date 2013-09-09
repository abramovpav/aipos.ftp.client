package by.bsuir.iit.abramov.aipos.ftp.client.view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import by.bsuir.iit.abramov.aipos.ftp.client.util.ExtJMenuItem;
import by.bsuir.iit.abramov.aipos.ftp.client.util.Factory;
import by.bsuir.iit.abramov.aipos.ftp.client.util.MenuContent;
import by.bsuir.iit.abramov.aipos.ftp.client.util.MenuItemEnum;

public class MenuBar extends JMenuBar {
	private final Window	parent;

	public MenuBar(final Window parent) {

		super();
		this.parent = parent;
		initialize();
	}

	private void createMenuBlock(final MenuContent menuContent) {

		final JMenu menu = new JMenu(menuContent.getSection());
		for (final MenuItemEnum name : menuContent.getItems()) {
			final ExtJMenuItem mItem = new ExtJMenuItem(name.getName(),
					parent.getController());
			menu.add(mItem);
			mItem.addActionListener(Factory.getMenuItemController(name));
		}
		add(menu);
	}

	private void initialize() {

		for (final MenuContent menu : MenuContent.values()) {
			createMenuBlock(menu);
		}
	}
}
