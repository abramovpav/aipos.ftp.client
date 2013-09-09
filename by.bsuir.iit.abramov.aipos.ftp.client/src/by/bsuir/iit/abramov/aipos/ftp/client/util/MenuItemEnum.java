package by.bsuir.iit.abramov.aipos.ftp.client.util;

public enum MenuItemEnum {

	MANAGER("MANaGER"), EXIT("EXIT"), INFORMATION("GETINFORMATION");

	private String	name;

	MenuItemEnum(final String name) {

		this.name = name;
	}

	public final String getName() {

		return name;
	}
}
