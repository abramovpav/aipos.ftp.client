package by.bsuir.iit.abramov.aipos.ftp.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.bsuir.iit.abramov.aipos.ftp.client.controller.Controller;
import by.bsuir.iit.abramov.aipos.ftp.client.model.Model;
import by.bsuir.iit.abramov.aipos.ftp.client.view.Window;

public class Start {

	private static final String	END_LINE	= "\r\n";
	static BufferedReader		reader;
	static BufferedWriter		writer;

	private static Socket getDataConnection(String str) throws UnknownHostException,
			IOException {

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

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(final String[] args) throws UnknownHostException, IOException {

		final Controller controller = new Controller();
		final Window window = new Window(controller);
		final Model model = new Model(controller);
		controller.setModel(model);
		controller.setWindow(window);
		window.setVisible(true);
		/*
		 * Socket socket = new Socket("kakttoc.16mb.com", 21); if
		 * (socket.isConnected()) { System.out.println("connected");
		 * 
		 * reader = new BufferedReader(new
		 * InputStreamReader(socket.getInputStream())); writer = new
		 * BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		 * readGreeting(reader); sendRequest("USER u579185203");
		 * readReply(reader); sendRequest("PASS ftp123258"); readReply(reader);
		 * sendRequest("PWD"); readReply(reader);
		 * 
		 * /*sendRequest("MKD 1"); readReply(reader);
		 */

		/*
		 * sendRequest("CWD 1"); readReply(reader);
		 */

		/*
		 * sendRequest("PASV"); String str = readReply(reader); Socket sock =
		 * getDataConnection(str); if (sock.isConnected()) {
		 * System.out.println("connected"); //sendRequest("STOR example2.txt");
		 * sendRequest("NLST"); readReply(reader);
		 * 
		 * BufferedInputStream istream = new BufferedInputStream(
		 * sock.getInputStream());
		 * 
		 * BufferedOutputStream fi = new BufferedOutputStream(new
		 * FileOutputStream( "c:" + File.separator + "2.txt")); int val;
		 * System.out.println("available = " + istream.available()); while ((val
		 * = istream.read()) != -1) { fi.write(val); System.out.println(":= " +
		 * val); } fi.close();
		 */
		/*
		 * BufferedOutputStream ostream = new
		 * BufferedOutputStream(sock.getOutputStream()); BufferedInputStream fi
		 * = new BufferedInputStream(new FileInputStream("c:" + File.separator +
		 * "example2.txt")); int val; while ((val = fi.read()) != -1) {
		 * ostream.write(val); System.out.println(":= " + val); }
		 * 
		 * ostream.flush(); sock.close(); readGreeting(reader); }
		 * 
		 * sendRequest("PWD"); readReply(reader);
		 * 
		 * sendRequest("RNFR example2.txt"); readReply(reader);
		 * sendRequest("RNTO ex.txt"); readReply(reader);
		 * 
		 * 
		 * sendRequest("QUIT"); readReply(reader); }
		 * 
		 * System.out.println("End");
		 */

	}

	static private List<String> readGreeting(final BufferedReader reader)
			throws IOException {

		final List<String> result = new ArrayList<String>();
		String line;
		do {
			line = reader.readLine();
			result.add(line);
			System.out.println(line);
		} while (reader.ready());
		return result;
	}

	static private String readReply(final BufferedReader reader) throws IOException {

		final String line = reader.readLine();
		System.out.println(line);
		return line;
	}

	private static void sendRequest(final String request) throws IOException {

		Start.writer.write(request + Start.END_LINE);
		System.out.println(request);
		Start.writer.flush();
	}

}
