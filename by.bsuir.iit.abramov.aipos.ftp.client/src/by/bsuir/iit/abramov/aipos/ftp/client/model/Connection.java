package by.bsuir.iit.abramov.aipos.ftp.client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;
import by.bsuir.iit.abramov.aipos.ftp.client.util.FileListItem;
import by.bsuir.iit.abramov.aipos.ftp.client.util.Operation;

public class Connection implements Runnable {

	private static final String	ACCEPTED_DATA_CONNECTION	= "150";
	private static final String	CODE_RETR_SUCCESS			= Connection.ACCEPTED_DATA_CONNECTION;
	private static final String	CODE_PASV_SUCCESS			= "227";
	private static final int	MODE_RETR					= 2;
	private static final int	MODE_LIST					= 1;
	private static final int	MODE_NLST					= 0;
	private static final int	SLEEP_TIME					= 10;
	private static final int	ECHO_OFF					= 0;
	private static final int	ECHO_ON						= 1;

	private static final String	END_LINE					= "\r\n";

	private Socket				socket;
	private final String		host;
	private final int			port;
	private final String		user;
	private final String		pass;
	private BufferedReader		iostream;
	private BufferedWriter		outstream;
	private final Controller	controller;
	private int					echo;
	private boolean				command_login;
	private boolean				command_logout;
	private boolean				login;
	private boolean				PASV;
	private boolean				command_CDUP;
	private boolean				command_CWD;
	private boolean				command_RETR;
	private boolean				command_UpdateList;
	private boolean				command_delete;
	private boolean				command_STORE;
	private String				CWD_path;
	private String				RETR_serverPath;
	private String				RETR_path;
	private String				STORE_path;
	private String				STORE_serverPath;
	private String				DELETE_path;

	public Connection(final Controller controller, final String host, final int port,
			final String user, final String pass) {

		this.controller = controller;
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
		echo = 1;
		CWD_path = "";
		command_login = true;
	}

	private void CDUP() {

		sendRequest(Operation.CDUP.getOperation());
		readReplies(iostream);
		updateList();
	}

	private boolean checkLine(final String line, final String successCode) {

		final String substrings[] = line.split(" ");
		final String code = substrings[0];
		return successCode.equals(code);
	}

	private void clearFileList() {

		controller.clearFileList();
	}

	private FileReader createFileReader(final Socket sock, final String path) {

		FileReader reader = null;
		try {
			reader = new FileReader(new File(path));
		} catch (final IOException e1) {
			log("Unable to create FileOutputStream");
			try {
				sock.close();
			} catch (final IOException e2) {
				log("Error: unable to close data socket");
			}
			echoON();
		}
		return reader;
	}

	private FileWriter createFileWriter(final Socket sock, final String path) {

		FileWriter writer = null;
		try {
			writer = new FileWriter(new File(path));
		} catch (final IOException e1) {
			log("Unable to create FileOutputStream");
			try {
				sock.close();
			} catch (final IOException e2) {
				log("Error: unable to close data socket");
			}
			echoON();
		}
		return writer;
	}

	private void createSocket() {

		try {
			socket = new Socket(host, port);
		} catch (final UnknownHostException e) {
			e.printStackTrace();
			log("Unable to connect: check login data");
		} catch (final IOException e) {
			e.printStackTrace();
			log("Unable to connect. Try later");
		}
	}

	private void createSocketAndLogin() {

		createSocket();
		if (socket.isConnected()) {
			log("Control connection established");
			extractStreams();
			readReplies(iostream);
			login();

		} else {
			log("Unable to connect");
		}
	}

	private void CWD(final String path) {

		sendRequest(Operation.CWD + " " + path);
		readReplies(iostream);
		updateList();
	}

	private void DELETE(final String path) {

		sendRequest(Operation.DELE.getOperation() + " " + path);
		readReplies(iostream);
	}

	private void echoOFF() {

		echo = Connection.ECHO_OFF;
	}

	private void echoON() {

		echo = Connection.ECHO_ON;
	}

	private BufferedReader extractInputStream(final Socket sock) {

		BufferedReader istream = null;
		try {
			istream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch (final IOException e) {
			log("Unable extract stream from data socket");
			try {
				sock.close();
			} catch (final IOException e1) {
				log("Error: unable to close data socket");
			}
			echoON();
		}
		return istream;
	}

	private BufferedWriter extractOutputStream(final Socket sock) {

		BufferedWriter ostream = null;
		try {
			ostream = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (final IOException e) {
			log("Unable extract stream from data socket");
			try {
				sock.close();
			} catch (final IOException e1) {
				log("Error: unable to close data socket");
			}
			echoON();
		}
		return ostream;
	}

	private void extractStreams() {

		try {
			iostream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outstream = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
		} catch (final IOException e) {
			e.printStackTrace();
			log("Error: can't get stream");
		}
	}

	private Socket getDataConnection(String str) {

		if (str == "" || str == null) {
			return null;
		}

		final Pattern pattern = Pattern.compile("(\\d+, *)+\\d+");
		final Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			str = matcher.group();
		}

		final String s[] = str.split(",");
		String ip = "";
		for (int i = 0; i < 4; i++) {

			ip += s[i];
			if (i < 3) {
				ip += ".";

			}
		}
		final int port = Integer.parseInt(s[4]) * 256 + Integer.parseInt(s[5]);
		System.out.println("ip = " + ip);
		System.out.println("port = " + port);
		Socket sock = null;
		try {
			sock = new Socket(ip, port);
		} catch (final UnknownHostException e) {
			// e.printStackTrace();
			log("Unable to open data stream(wrong host)");
		} catch (final IOException e) {
			// e.printStackTrace();
			log("Unable to open data stream");
		}
		return sock;
	}

	private final List<FileListItem> getListOfFiles(final int mode) {

		echoOFF();
		final List<FileListItem> result = new ArrayList<FileListItem>();
		final Socket miniSocket = PASV();
		if (miniSocket == null) {
			final String error = "miniSocket == null";
			log(error);
			return result;
		}
		if (miniSocket.isConnected()) {
			log("Data connection established");
			if (mode == Connection.MODE_NLST) {
				sendRequest(Operation.NLST.getOperation());
			} else if (mode == Connection.MODE_LIST) {
				sendRequest(Operation.LIST.getOperation());
			}
			if ("".equals(readReply(iostream, Connection.ACCEPTED_DATA_CONNECTION))) {
				return result;
			}
			result.addAll(readListOfFiles(miniSocket, mode));
		}
		echoON();
		return result;
	}

	private final String getSuccessCode(final Operation operation) {

		switch (operation) {
			case RETR:
				return "";
			default:
				return "";
		}
	}

	private final String hideString(final String text) {

		String result = "";
		for (int i = 1; i < text.length(); i++) {
			result += "*";
		}
		return result;
	}

	private boolean isDirectory(final String[] sublines) {

		return sublines[0].charAt(0) == 'd';
	}

	private boolean isEchoOn() {

		return echo == Connection.ECHO_ON;
	}

	private void log(final String text) {

		System.out.println(text);
		if (isEchoOn()) {
			controller.addLogLine(text);
		}
	}

	private void login() {

		sendRequest(Operation.USER + " " + user);
		readReplies(iostream);
		sendPass(Operation.PASS + " " + pass);
		readReplies(iostream);
		login = true;
		command_UpdateList = true;
	}

	private Socket PASV() {

		sendRequest(Operation.PASV.getOperation());
		final String reply = readReply(iostream, Connection.CODE_PASV_SUCCESS);
		final Socket miniSocket = getDataConnection(reply);
		return miniSocket;
	}

	private void prepareToReadStream(final BufferedReader reader) throws IOException {

		echoOFF();
		log("Wait for read stream");
		double t = 0;
		double limit = 1.0;
		while (!reader.ready()) {
			sleep();
			t += Connection.SLEEP_TIME;
			if (t / 2000 >= limit) {
				log("Waiting for long. Cancel the operation and try again");
				limit += 1;
				if (Math.abs(10000 - t) < Connection.SLEEP_TIME * 2) {
					t = 0;
					limit = 1.0;
				}
			}
		}
		log("Stream ready. Try to read stream");
		echoON();
	}

	private void quit() {

		sendRequest(Operation.QUIT.getOperation());
		readReplies(iostream);
		clearFileList();
	}

	private final List<FileListItem> readListFile(final BufferedReader reader,
			final int mode) {

		final List<FileListItem> result = new ArrayList<FileListItem>();
		if (reader != null) {
			String line, name = "";
			boolean directory = false;;
			try {
				System.out.println("begin reading");
				prepareToReadStream(reader);
				while (reader.ready()) {
					directory = false;
					line = reader.readLine();
					if (mode == Connection.MODE_NLST) {
						name = line;
					} else if (mode == Connection.MODE_LIST) {
						final String sublines[] = line.split(" ");
						name = sublines[sublines.length - 1];
						directory = isDirectory(sublines);
					}
					result.add(new FileListItem(name, directory));
				}
			} catch (final IOException e) {
				e.printStackTrace();
				log("Unable to read stream. Try again");
			}
		}
		return result;
	}

	private final List<FileListItem> readListOfFiles(final Socket sock, final int mode) {

		final List<FileListItem> result = new ArrayList<FileListItem>();
		if (sock != null) {
			BufferedReader istream = null;
			try {
				istream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			} catch (final IOException e) {
				log("Unable extract stream from data socket");
				try {
					sock.close();
				} catch (final IOException e1) {
					log("Error: unable to close data socket");
				}
				echoON();
				return result;
			}
			result.addAll(readListFile(istream, mode));

			try {
				sock.close();
			} catch (final IOException e) {
				echoOFF();
				log("Error: unable to close data socket");
				echoON();
			}
			readReplies(iostream);

		}
		return result;
	}

	private List<String> readReplies(final BufferedReader reader) {

		final List<String> result = new ArrayList<String>();
		if (reader != null) {
			String line;
			try {
				prepareToReadStream(reader);
				while (reader.ready()) {
					line = reader.readLine();
					result.add(line);
					log(line);
				}
			} catch (final IOException e) {
				e.printStackTrace();
				log("Unable to read stream. Try again");
			}
		}
		return result;

	}

	private String readReply(final BufferedReader reader, final String successCode) {

		if (reader != null) {
			try {
				prepareToReadStream(reader);
				final String line = reader.readLine();
				log(line);
				if (checkLine(line, successCode)) {
					return line;
				}
			} catch (final IOException e) {
				// e.printStackTrace();
				log("Unable to read stream. Try again");
			}
		}
		return "";
	}

	private void RETR(final String serverPath, final String path) {

		final Socket dataSocket = PASV();
		if (dataSocket == null) {
			log("dataSocket == null");
			return;
		}
		if (dataSocket.isConnected()) {
			log("Data connection established");
			sendRequest(Operation.RETR + " " + serverPath);
			final String reply = readReply(iostream, Connection.CODE_RETR_SUCCESS);
			System.out.println("before save");
			if ("".equals(reply)) {
				return;
			}
			saveFile(dataSocket, serverPath, path);
			readReplies(iostream);
		}
	}

	@Override
	public void run() {

		while (true) {

			if (command_login) {
				if (!login) {
					createSocketAndLogin();
				}
				command_login = false;
			}
			if (command_logout) {
				if (login) {
					quit();
				}
				command_logout = false;
			}
			if (command_CWD) {
				if (!"".equals(CWD_path)) {
					CWD(CWD_path);
				}
				CWD_path = "";
				command_CWD = false;
			}
			if (command_RETR) {
				if (!"".equals(RETR_serverPath)) {
					RETR(RETR_serverPath, RETR_path);
				}
				RETR_path = "";
				RETR_serverPath = "";
				command_RETR = false;
			}
			if (command_STORE) {
				if (!"".equals(STORE_path)) {
					STOR(STORE_serverPath, STORE_path);
				}
				command_STORE = false;
			}
			if (command_delete) {
				if (!"".equals(DELETE_path)) {
					DELETE(DELETE_path);
				}
				command_delete = false;
			}
			if (command_CDUP) {
				CDUP();
				command_CDUP = false;
			}
			if (command_UpdateList) {
				updateList();
				command_UpdateList = false;
			}

		}
	}

	private void saveFile(final BufferedReader reader, final FileWriter writer) {

		if (reader != null) {
			try {
				prepareToReadStream(reader);
				int val;
				while ((val = reader.read()) != -1) {
					writer.write(val);
				}
				writer.flush();
			} catch (final IOException e) {
				log("Unable to save file. Try again");
			}
		}
	}

	private void saveFile(final Socket sock, final String serverPath, final String path) {

		if (sock != null) {
			BufferedReader istream = null;
			FileWriter writer = null;
			istream = extractInputStream(sock);
			writer = createFileWriter(sock, path);
			if (istream != null && writer != null) {
				saveFile(istream, writer);
			}
			try {
				sock.close();
				writer.close();
			} catch (final IOException e) {
				echoOFF();
				log("Error: unable to close socket connection");
				echoON();
			}
		}
	}

	private void sendPass(final String request) {

		if (outstream != null) {
			try {
				outstream.write(request + Connection.END_LINE);
				final String hide_pass = hideString(request);
				log(Operation.PASS + " " + hide_pass);
				outstream.flush();
			} catch (final IOException e) {
				// e.printStackTrace();
				log("Unable to write in stream");
			}
		}
	}

	private void sendRequest(final String request) {

		if (outstream != null) {
			try {
				outstream.write(request + Connection.END_LINE);
				log(request);
				outstream.flush();
			} catch (final IOException e) {
				e.printStackTrace();
				log("Unable to write in stream");
			}
		}
	}

	private void sleep() {

		try {
			Thread.sleep(Connection.SLEEP_TIME);
		} catch (final InterruptedException e) {
			// e.printStackTrace();
			echoOFF();
			log("Unable to sleep");
			echoON();
		}
	}

	private void STOR(final String serverPath, final String path) {

		final Socket dataSocket = PASV();
		if (dataSocket == null) {
			log("dataSocket == null");
			return;
		}
		if (dataSocket.isConnected()) {
			log("Data connection established");
			sendRequest(Operation.STOR + " " + serverPath);
			final String reply = readReply(iostream, Connection.CODE_RETR_SUCCESS);
			System.out.println("before save");
			if ("".equals(reply)) {
				return;
			}
			storeFile(dataSocket, path);
			readReplies(iostream);
		}
	}

	private void storeFile(final BufferedWriter writer, final FileReader reader) {

		if (reader != null) {
			try {
				int val;
				while ((val = reader.read()) != -1) {
					writer.write(val);
				}
				writer.flush();
			} catch (final IOException e) {
				log("Unable to save file. Try again");
			}
		}
	}

	private void storeFile(final Socket sock, final String path) {

		if (sock != null) {
			BufferedWriter outstream = null;
			FileReader reader = null;
			outstream = extractOutputStream(sock);
			reader = createFileReader(sock, path);
			if (outstream != null && reader != null) {
				storeFile(outstream, reader);
			}
			try {
				sock.close();
				reader.close();
			} catch (final IOException e) {
				echoOFF();
				log("Error: unable to close socket connection");
				echoON();
			}
		}
	}

	public void taskChangeDirectory(final String path) {

		command_CWD = true;
		CWD_path = path;
	}

	public void taskChangeDirectoryUP() {

		command_CDUP = true;
	}

	public void taskDeleteFile(final String path) {

		command_delete = true;
		DELETE_path = path;
	}

	public void taskQUIT() {

		command_logout = true;
	}

	public void taskRETRFile(final String serverPath, final String path) {

		command_RETR = true;
		RETR_serverPath = serverPath;
		RETR_path = path;
	}

	public void taskSTORE(final String serverPath, final String path) {

		command_STORE = true;
		STORE_path = path;
		STORE_serverPath = serverPath;
	}

	public void taskUpdateList() {

		command_UpdateList = true;
	}

	private void updateList() {

		final List<FileListItem> fileList = getListOfFiles(Connection.MODE_LIST);
		controller.setFilesInList(fileList);
	}

}
