package by.bsuir.iit.abramov.aipos.ftp.client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import by.bsuir.iit.abramov.aipos.ftp.client.util.Operation;

public class Connection extends Thread {// implements Runnable {

	private static final int	ECHO_OFF	= 0;
	private static final int	ECHO_ON		= 1;

	private static final String	END_LINE	= "\r\n";

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
	private boolean				login;
	private boolean				PASV;
	private boolean				command_CDUP;
	private boolean				command_CWD;
	private boolean				command_UpdateList;
	private String				CWD_path;

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
		final List<String> list = getListOfFiles();
		controller.setFileList(list);
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
			login = true;
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
		final List<String> list = getListOfFiles();
		controller.setFileList(list);
	}

	private void echoOFF() {

		echo = Connection.ECHO_OFF;
	}

	private void echoON() {

		echo = Connection.ECHO_ON;
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

	private List<String> getListOfFiles() {

		echoOFF();
		final List<String> result = new ArrayList<String>();
		sendRequest(Operation.PASV.getOperation());
		final String reply = readReply(iostream);
		final Socket miniSocket = getDataConnection(reply);
		if (miniSocket == null) {
			final String error = "miniSocket == null";
			log(error);
			return result;
		}
		if (miniSocket.isConnected()) {
			log("Data connection established");
			sendRequest(Operation.NLST.getOperation());
			result.addAll(readListOfFiles(miniSocket));
		}
		echoON();
		return result;
	}

	private final String hideString(final String text) {

		String result = "";
		for (int i = 1; i < text.length(); i++) {
			result += "*";
		}
		return result;
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
	}

	private List<String> readListOfFiles(final Socket sock) {

		final List<String> result = new ArrayList<String>();
		if (sock != null) {
			BufferedReader istream = null;
			try {
				istream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			} catch (final IOException e) {
				// e.printStackTrace();
				log("Unable extract stream from data socket");
			}

			result.addAll(readReplies(istream));
			try {
				sock.close();
			} catch (final IOException e) {
				// e.printStackTrace();
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
				echoOFF();
				log("wait for read stream");
				while (!reader.ready()) {
					;
				}
				log("try to read stream");
				echoON();
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

	private String readReply(final BufferedReader reader) {

		if (reader != null) {
			try {
				echoOFF();
				log("wait for read stream");
				while (!reader.ready()) {
					;
				}
				log("try to read stream");
				echoON();
				final String line = reader.readLine();
				log(line);
				return line;
			} catch (final IOException e) {
				// e.printStackTrace();
				log("Unable to read stream. Try again");
			}
		}
		return null;
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
			if (command_CWD) {
				if (!"".equals(CWD_path)) {
					CWD(CWD_path);
				}
				CWD_path = "";
				command_CWD = false;
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

	public void taskChangeDirectory(final String path) {

		command_CWD = true;
		CWD_path = path;
	}

	public void taskChangeDirectoryUP() {

		command_CDUP = true;
	}

	public void taskUpdateList() {

		command_UpdateList = true;
	}

	private void updateList() {

		final List<String> fileList = getListOfFiles();
		controller.setFileList(fileList);
	}

}
