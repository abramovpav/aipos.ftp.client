package by.bsuir.iit.abramov.aipos.ftp.client.model;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
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

	public Connection(final Controller controller, final String host, final int port,
			final String user, final String pass) {

		this.controller = controller;
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
		echo = 1;
	}

	private void echoOFF() {

		echo = Connection.ECHO_OFF;
	}

	private void echoON() {

		echo = Connection.ECHO_ON;
	}

	private Socket getDataConnection(String str) throws UnknownHostException, IOException {

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
		final Socket sock = new Socket(ip, port);
		return sock;
	}

	public List<String> getListOfFiles() throws IOException {

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

	private void login() throws IOException {

		sendRequest(Operation.USER + " " + user);
		readReply(iostream);
		sendPass(Operation.PASS + " " + pass);
		readReply(iostream);
	}

	private List<String> readListOfFiles(final Socket sock) throws IOException {

		final List<String> result = new ArrayList<String>();
		if (sock != null) {
			final BufferedReader istream = new BufferedReader(new InputStreamReader(
					sock.getInputStream()));

			final BufferedOutputStream fi = new BufferedOutputStream(
					new FileOutputStream("list.txt"));

			result.addAll(readReplies(istream));
			fi.close();
			sock.close();
			readReplies(iostream);

		}
		return result;
	}

	private List<String> readReplies(final BufferedReader reader) throws IOException {

		final List<String> result = new ArrayList<String>();
		if (reader != null) {
			String line;
			do {
				line = reader.readLine();
				result.add(line);
				log(line);
			} while (reader.ready());
		}
		return result;

	}

	private String readReply(final BufferedReader reader) throws IOException {

		if (reader != null) {
			final String line = reader.readLine();
			log(line);
			return line;
		} else {
			return null;
		}
	}

	@Override
	public void run() {

		System.out.println("tadam");
		try {
			socket = new Socket(host, port);
			if (socket.isConnected()) {
				controller.addLogLine("Control connection established");
				iostream = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				outstream = new BufferedWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				readReplies(iostream);
				login();

			} else {
				log("Unable to connect");
			}
		} catch (final UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	private void sendPass(final String request) throws IOException {

		if (outstream != null) {
			outstream.write(request + Connection.END_LINE);
			final String hide_pass = hideString(request);
			log(Operation.PASS + " " + hide_pass);
			outstream.flush();
		}
	}

	private void sendRequest(final String request) throws IOException {

		if (outstream != null) {
			outstream.write(request + Connection.END_LINE);
			log(request);
			outstream.flush();
		}
	}

	public void updateList() throws IOException {

		final List<String> fileList = getListOfFiles();
		controller.setFileList(fileList);
	}

}
