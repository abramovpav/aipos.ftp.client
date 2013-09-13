package by.bsuir.iit.abramov.aipos.ftp.client.view;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import by.bsuir.iit.abramov.aipos.ftp.client.model.ListModel;

public class FileRenderer extends DefaultListCellRenderer {

	public static String getFileExtention(final String filename) {

		if (filename == null) {
			return "";
		}
		int dotPos = filename.lastIndexOf(".");
		if (dotPos == -1) {
			dotPos = filename.length();
		}
		return filename.substring(dotPos);
	}

	public static String getFileName(final String filename) {

		int dotPos = filename.lastIndexOf(".");
		if (dotPos == -1) {
			dotPos = 0;
		}
		return filename.substring(0, dotPos);
	}

	private final boolean	pad;

	private final Border	padBorder	= new EmptyBorder(3, 3, 3, 3);

	public FileRenderer(final boolean pad) {

		this.pad = pad;
	}

	@Override
	public Component getListCellRendererComponent(final JList list, final Object value,
			final int index, final boolean isSelected, final boolean cellHasFocus) {

		final Component c = super.getListCellRendererComponent(list, value, index,
				isSelected, cellHasFocus);
		final JLabel label = (JLabel) c;
		final String str = (String) value;
		File file;
		FileList fileList = null;
		if (FileList.class.equals(list.getClass())) {
			fileList = (FileList) list;
		}
		final javax.swing.ListModel lstModel = list.getModel();
		ListModel listModel = null;
		if (ListModel.class.equals(lstModel.getClass())) {
			listModel = (ListModel) lstModel;
		}
		try {
			final String fileExtention = FileRenderer.getFileExtention(str);
			file = File.createTempFile("temp", fileExtention);

			label.setText(str);
			if ("".equals(fileExtention)) {
				final ImageIcon icon = new ImageIcon("icons" + File.separator
						+ "folder2.png");
				label.setIcon(icon);

				if (listModel != null && fileList != null) {
					final Object item = listModel.get(index);
					if (!fileList.containsKey(item)) {
						fileList.put(item, true);
					}
				}

			} else {
				// label.setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
			}
			if (pad) {
				label.setBorder(padBorder);
			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return label;
	}
}
