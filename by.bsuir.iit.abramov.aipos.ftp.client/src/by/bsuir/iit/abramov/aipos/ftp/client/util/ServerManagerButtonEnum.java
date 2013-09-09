package by.bsuir.iit.abramov.aipos.ftp.client.util;

public enum ServerManagerButtonEnum {

	CONNECT("Connect", true), OK("OK", true), CANCEL("Cancel", true), NEW("New", false), RENAME(
			"Rename", false), DELETE("Delete", false);

	private final String	caption;
	private final boolean	controlButton;

	ServerManagerButtonEnum(final String caption, final boolean controlButton) {

		this.caption = caption;
		this.controlButton = controlButton;
	}

	public final String getCaption() {

		return caption;
	}

	public final boolean isControlButton() {

		return controlButton;
	}
}
