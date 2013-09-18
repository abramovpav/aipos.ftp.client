package by.bsuir.iit.abramov.aipos.ftp.client.util;

public class FileListItem {

	private final String	name;
	private final Boolean	directory;

	public FileListItem(final String name, final Boolean directory) {

		this.name = name;
		this.directory = directory;
	}

	public final String getName() {

		return name;
	}

	public final Boolean isDirectory() {

		return directory;
	}

}
