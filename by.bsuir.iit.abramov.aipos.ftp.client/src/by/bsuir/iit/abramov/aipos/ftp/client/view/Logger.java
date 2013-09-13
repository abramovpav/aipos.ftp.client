package by.bsuir.iit.abramov.aipos.ftp.client.view;

import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;

public class Logger extends JTextPane {

	private static final String	LINE_END	= "\r\n";

	public Logger() {

		super();
		final DefaultCaret caret = (DefaultCaret) getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

	public void addLine(final String text) {

		setText(getText() + text + Logger.LINE_END);
	}
}
