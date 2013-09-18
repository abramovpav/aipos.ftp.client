package by.bsuir.iit.abramov.aipos.ftp.client.util;

public enum Button {
	MANAGER("Manager", true), DISCONNECT("Disconnect", true), CWD("CWD", true), STORE(
			"Store", true), DELETE("Delete", true), REFRESH("Refresh", true);

	private final String	caption;
	private final boolean	visible;

	Button(final String caption, final boolean show) {

		this.caption = caption;
		visible = show;
	}

	public final String getCaption() {

		return caption;
	}

	public final boolean isVisible() {

		return visible;
	}
}
