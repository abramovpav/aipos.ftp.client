package by.bsuir.iit.abramov.aipos.ftp.client.view;

import javax.swing.JTextPane;

public class Logger extends JTextPane {

	private static final String	LINE_END	= "\r\n";

	public Logger() {

		super();
	}

	public void addLine(final String text) {

		setText(getText() + text + Logger.LINE_END);
	}
}
