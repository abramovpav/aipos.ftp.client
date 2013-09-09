package by.bsuir.iit.abramov.aipos.ftp.client.model;

import java.io.BufferedInputStream;
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

public class Connection implements Runnable {

	private static final String	END_LINE	= "\r\n";

	private Socket				socket;
	private final String		host;
	private final int			port;
	private final String		user;
	private final String		pass;
	private BufferedReader		iostream;
	private BufferedWriter		outstream;
	private final Controller	controller;

	public Connection(final Controller controller, final String host, final int port,
			final String user, final String pass) {

		this.controller = controller;
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
	}

	private Socket getDataConnection(String str) throws UnknownHostException, IOException {

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
		System.out.println("= " + ip);
		final int port = Integer.parseInt(s[4]) * 256 + Integer.parseInt(s[5]);
		System.out.println("ip = " + ip);
		System.out.println("port = " + port);
		final Socket sock = new Socket(ip, port);
		return sock;
	}

	private final String hideString(final String text) {

		String result = "";
		for (int i = 1; i < text.length(); i++) {
			result += "*";
		}
		return result;
	}

	private List<String> readGreeting(final BufferedReader reader) throws IOException {

		final List<String> result = new ArrayList<String>();
		String line;
		do {
			line = reader.readLine();
			result.add(line);
			System.out.println(line);
			controller.addLogLine(line);
		} while (reader.ready());
		return result;
	}

	private void readNLST(final Socket sock) throws IOException {

		final BufferedInputStream istream = new BufferedInputStream(sock.getInputStream());

		final BufferedOutputStream fi = new BufferedOutputStream(new FileOutputStream(
				"2.txt"));
		int val;
		while ((val = istream.read()) != -1) {
			fi.write(val);
		}
		fi.close();
		sock.close();

		readGreeting(iostream);
	}

	private String readReply(final BufferedReader reader) throws IOException {

		final String line = reader.readLine();
		System.out.println(line);
		controller.addLogLine(line);
		return line;
	}

	@Override
	public void run() {

		try {
			socket = new Socket(host, port);
			if (socket.isConnected()) {
				controller.addLogLine("Control connection established");
				iostream = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				outstream = new BufferedWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				readGreeting(iostream);
				sendRequest(Operation.USER + " " + user);
				readReply(iostream);
				sendPass(Operation.PASS + " " + pass);
				readReply(iostream);
				sendRequest(Operation.PASV.getOperation());
				final String reply = readReply(iostream);
				final Socket miniSocket = getDataConnection(reply);
				if (miniSocket.isConnected()) {
					controller.addLogLine("Data connection established");
					sendRequest(Operation.NLST.getOperation());
					readNLST(miniSocket);
					readGreeting(iostream);
				}
			} else {
				controller.addLogLine("Unable to connect");
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

		outstream.write(request + Connection.END_LINE);
		final String hide_pass = hideString(request);
		System.out.println(Operation.PASS + " " + hide_pass);
		controller.addLogLine(Operation.PASS + " " + hide_pass);
		outstream.flush();
	}

	private void sendRequest(final String request) throws IOException {

		outstream.write(request + Connection.END_LINE);
		System.out.println(request);
		controller.addLogLine(request);
		outstream.flush();
	}

}
