package by.bsuir.iit.abramov.aipos.ftp.client.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.WindowConstants;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ExtJButton;
import by.bsuir.iit.abramov.aipos.ftp.client.util.Factory;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ServerManagerButtonEnum;
import by.bsuir.iit.abramov.aipos.ftp.client.util.ServerManagerTextField;

public class Manager extends JDialog {

	private static final String								CHOOSE_ONE			= "Choose one:";
	private static final int								JTEXTFIELD_COLUMNS	= 10;
	private static final String								TITLE				= "Title";

	private final Controller								controller;
	private final Map<ServerManagerTextField, JTextField>	textFields;

	public Manager(final Controller controller) {

		super();
		this.controller = controller;
		textFields = new HashMap<ServerManagerTextField, JTextField>();
		setTitle(Manager.TITLE);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(500, 400));
		setLayout(new BorderLayout(0, 0));
		createPanels();
	}

	private void createButtonsPanel() {

		final JPanel mainPanel = new JPanel();
		add(mainPanel, BorderLayout.SOUTH);
		for (final ServerManagerButtonEnum eButton : ServerManagerButtonEnum.values()) {
			if (eButton.isControlButton()) {
				final ExtJButton button = new ExtJButton(eButton.getCaption(), this);
				button.addActionListener(Factory.getManagerButtonsController(eButton));
				mainPanel.add(button);
			}
		}
	}

	private void createPanels() {

		createSettingsPanel();
		createServersPanel();
		createButtonsPanel();
		/*
		 * JPanel panel = new JPanel(); panel.setLayout(new BoxLayout(panel,
		 * BoxLayout.Y_AXIS)); JLabel label = new JLabel("Choose one:");
		 * panel.add(label); JList list = new JList(); panel.add(list); JPanel
		 * panel2 = new JPanel(); panel2.setLayout(new BoxLayout(panel2,
		 * BoxLayout.Y_AXIS)); panel2.add(new JButton("1")); panel2.add(new
		 * JButton("2")); panel2.add(new JButton("3")); panel2.add(new
		 * JButton("4")); panel2.add(new JButton("5")); panel2.add(new
		 * JButton("6")); panel.add(panel2); add(panel, BorderLayout.WEST);
		 * 
		 * panel = new JPanel(); add(panel, BorderLayout.CENTER); panel.add(new
		 * JTextField(10)); panel.add(new JButton("7")); panel.add(new
		 * JTextField(10)); panel.add(new JButton("8")); panel.add(new
		 * JTextField(10)); panel.add(new JButton("9")); panel.add(new
		 * JTextField(10));
		 */
	}

	private void createServersPanel() {

		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		add(mainPanel, BorderLayout.CENTER);
		final JLabel label = new JLabel(Manager.CHOOSE_ONE);
		mainPanel.add(label);
		final ListModel listModel = new DefaultListModel();
		final JList list = new JList(listModel);

		list.setVisibleRowCount(5);
		final JScrollPane scrollList = new JScrollPane(list);
		mainPanel.add(scrollList);
		// JPanel buttonPanel = new JPanel();
		// mainPanel.add(buttonPanel);
		for (final ServerManagerButtonEnum eButton : ServerManagerButtonEnum.values()) {
			if (!eButton.isControlButton()) {
				final ExtJButton button = new ExtJButton(eButton.getCaption(), this);
				button.addActionListener(Factory.getManagerButtonsController(eButton));
				mainPanel.add(button);
			}
		}
	}

	private void createSettingsPanel() {

		textFields.clear();
		final JPanel mainPanel = new JPanel();
		add(mainPanel, BorderLayout.EAST);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		JPanel panel;
		JLabel label;
		JTextField textField;
		for (final ServerManagerTextField field : ServerManagerTextField.values()) {
			panel = new JPanel();
			panel.setLayout(new FlowLayout());// BoxLayout(panel,
												// BoxLayout.X_AXIS));
			mainPanel.add(panel);
			label = new JLabel(field.getLabel());
			panel.add(label);
			if (field != ServerManagerTextField.PASS) {
				textField = new JTextField(field.getTextField(),
						Manager.JTEXTFIELD_COLUMNS);
			} else {
				textField = new JPasswordField(field.getTextField(),
						Manager.JTEXTFIELD_COLUMNS);
			}

			panel.add(textField);
			textFields.put(field, textField);
		}
	}

	public final Controller getController() {

		return controller;
	}

	public final String getTextFromTextField(final ServerManagerTextField eField) {

		if (!textFields.containsKey(eField)) {
			System.out.println(this.getClass() + " no such textField");
			return null;
		}

		final JTextField textField = textFields.get(eField);
		if (textField != null) {
			return textField.getText();
		}
		return null;
	}

	public boolean isCorrectDataInFields() {

		return true;
	}
}
