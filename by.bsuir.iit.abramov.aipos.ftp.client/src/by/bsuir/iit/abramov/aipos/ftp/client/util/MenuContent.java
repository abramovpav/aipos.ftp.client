package by.bsuir.iit.abramov.aipos.ftp.client.util;

public enum MenuContent {
	File("File", MenuItemEnum.MANAGER, MenuItemEnum.EXIT), INFO("Info",
			MenuItemEnum.INFORMATION);

	private final String			section;
	private final MenuItemEnum[]	items;

	private MenuContent(final String section, final MenuItemEnum... items) {

		this.section = section;
		this.items = items;
	}

	public final MenuItemEnum[] getItems() {

		return items;
	}

	public final String getSection() {

		return section;
	}
}
