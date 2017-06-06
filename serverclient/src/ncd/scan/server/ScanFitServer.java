package ncd.scan.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ncd.scan.configuration.Communication;
import ncd.scan.utils.Message;
import ncd.scan.utils.Message.Type;
import ncd.utils.file.Xml;

public class ScanFitServer {

	private List<ServerThread>	serverThreads	= new ArrayList<>();

	public final static String	PREFIX			= "SERVER [" + ScanFitServer.class.getSimpleName() + "] ";

	private Xml<Communication>	configuration;

	public static void main(String[] args) {
		new ScanFitServer();
	}

	public ScanFitServer() {
		Runtime.getRuntime().addShutdownHook(new ExitServer());

		boolean listeningSocket = true;

		configuration = new Xml<>(Communication.DEFAULT_FILE, new Communication());

		ServerSocket serverSocket;
		// We need a try-catch because lots of errors can be thrown
		try {

			InetAddress ip = InetAddress.getLocalHost();
			System.out.println(PREFIX + "Your current IP address : " + ip);
			System.out.println(PREFIX + "Your current Host Name : " + ip.getHostAddress());
			System.out.println(PREFIX + "Your current Local Host : " + InetAddress.getLocalHost().getHostName());

			File file = new File(".");
			String path = file.getCanonicalPath();
			System.out.println(PREFIX + "Your current path : " + path);

			int port = this.findFreePort();

			this.configuration.getConfiguration().setPort(port);
			this.configuration.getConfiguration().setHostName(InetAddress.getLocalHost().getHostName());
			configuration.write();
			System.out.println(PREFIX + "Configuration file written : " + configuration.getXmlFile().getCanonicalPath());

			serverSocket = new ServerSocket(port);
			System.out.println(PREFIX + "started at: " + new Date());
			System.out.println(PREFIX + "Waiting for client on port " + port);

			// Loop that runs server functions
			while (listeningSocket) {
				// Wait for a client to connect
				Socket clientSocket = serverSocket.accept();
				System.out.println(PREFIX + "The client" + " " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " is connected ");
				ServerThread server = new ServerThread(clientSocket);
				server.start();
				this.serverThreads.add(server);
			}
		} catch (IOException exception) {
			System.out.println(PREFIX + "-ERROR- " + exception);
		}
	}

	/*
	 * 
	 * to broadcast a message to all Clients
	 */
	private synchronized void broadcast(String message, String name) {
		// we loop in reverse order in case we would have to remove a Client
		// because it has disconnected
		for (int i = this.serverThreads.size(); --i >= 0;) {
			ServerThread serverThread = this.serverThreads.get(i);
			// try to write to the Client if it fails remove it from the list
			if (!serverThread.sendMessage(message, name)) {
				this.serverThreads.remove(i);
				serverThread.close();
			}
		}
	}

	public void stopAll() {
		for (ServerThread th : this.serverThreads) {
			th.interrupt();
		}
		this.serverThreads.clear();
		this.configuration.getXmlFile().deleteFile();
		System.exit(0);
	}

	private int findFreePort() {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(0);
			socket.setReuseAddress(true);
			int port = socket.getLocalPort();
			try {
				socket.close();
			} catch (IOException e) {
				// Ignore IOException on close()
			}
			return port;
		} catch (IOException e) {
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		throw new IllegalStateException(PREFIX + "Could not find a free port");
	}

	private class ServerThread extends Thread {

		private String			name;

		private Socket			clientSocket;
		private String			clientName;

		private BufferedReader	fromClient;
		private PrintWriter		toClient;
		private String			fromClientString;

		public ServerThread(Socket clientSocket) {
			super("ServerThread");
			this.clientSocket = clientSocket;
			this.clientName = clientSocket.getInetAddress() + ":" + clientSocket.getPort();
		}

		public void run() {

			// Create the streams
			try {
				fromClient = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
				toClient = new PrintWriter(this.clientSocket.getOutputStream(), true);

				// Tell the client that he/she has connected
				toClient.println("You have connected at: " + new Date());

				while (true) {
					// This will wait until a line of text has been sent
					fromClientString = fromClient.readLine();

					if (fromClientString == null) {
						this.clientSocket.close();
						System.out.println(PREFIX + "The client" + " " + getClientName() + " is disconnected ");
						break;
					} else {
						System.out.println(PREFIX + " RECEIVED from " + getClientName() + " - " + fromClientString);
						Type type = Message.getType(fromClientString);
						System.out.println(PREFIX + " Type RECEIVED from " + getClientName() + " - " + type);
						switch (type) {
						case NAME:
							this.name = Message.get(fromClientString);
							break;
						case SCAN_FILE:
							broadcast(fromClientString, this.name);
							break;
						case DIAGNOSTIC:
							broadcast(fromClientString, this.name);
							break;
						case RESULTS:
							System.out.println(fromClientString);
							break;
						case STOP:
							broadcast(Message.get(fromClientString), this.name);
							stopAll();
							break;
						default:
							break;
						}
					}
				}
			} catch (IOException e) {
				if (e instanceof SocketException) {
					System.out.println(PREFIX + "The client" + " " + getClientName() + " is disconnected ");
					try {
						this.clientSocket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					System.out.println(e);
					e.printStackTrace();
				}
				return;
			}
		}

		private String getClientName() {
			String name = this.clientName;
			if (this.name != null) {
				name = name + "[" + this.name + "]";
			}
			return name;
		}

		/*
		 * Write a String to the Client output stream
		 */
		private boolean sendMessage(String msg, String name) {
			// if Client is still connected send the message to it
			if (!this.clientSocket.isConnected()) {
				close();
				return false;
			}
			if (name.equals(this.name)) return true;
			System.out.println(PREFIX + " SENDING to " + getClientName() + " - " + msg);
			toClient.flush();
			toClient.println(msg);

			return true;
		}

		// try to close everything
		private void close() {
			// try to close the connection
			try {
				if (this.fromClient != null) this.fromClient.close();
				if (this.toClient != null) this.toClient.close();
				if (this.clientSocket != null) this.clientSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class ExitServer extends Thread {

		public void run() {
			broadcast(Type.STOP.name(), "");
		}
	}
}
