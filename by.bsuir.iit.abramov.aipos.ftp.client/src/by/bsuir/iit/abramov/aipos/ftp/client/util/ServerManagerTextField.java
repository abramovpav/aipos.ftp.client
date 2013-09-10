package by.bsuir.iit.abramov.aipos.ftp.client.util;

public enum ServerManagerTextField {
	HOST("Host:", "127.0.0.1"/* "kakttoc.16mb.com" */), PORT("Port:", "21"), USER("User:",
			"u579185203"), PASS("Pass:", "ftp123258");
	private final String	label;
	private final String	textField;

	ServerManagerTextField(final String label, final String textField) {

		this.label = label;
		this.textField = textField;
	}

	public final String getLabel() {

		return label;
	}

	public final String getTextField() {

		return textField;
	}
}
