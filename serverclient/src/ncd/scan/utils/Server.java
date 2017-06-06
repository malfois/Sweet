package ncd.scan.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ncd.beamline.Diagnostic;
import ncd.scan.client.ScanFitGUI;
import ncd.scan.configuration.Communication;
import ncd.scan.configuration.ServerFiles;
import ncd.scan.utils.Message.Type;
import ncd.utils.configuration.FileConfiguration;
import ncd.utils.file.Xml;
import ncd.utils.system.OSUtils;

public class Server {

	private static volatile Server	INSTANCE;

	private String					prefix;

	private BooleanProperty			running			= new SimpleBooleanProperty(false);
	private BooleanProperty			connected		= new SimpleBooleanProperty(false);

	private StringProperty			scanFileName	= new SimpleStringProperty();
	private StringProperty			diagnostic		= new SimpleStringProperty();

	private Connection				connection;
	private Thread					connectionThread;

	private Xml<ServerFiles>		server			= new Xml<>(ServerFiles.DEFAULT_FILE, new ServerFiles());

	private Server(String prefix) {
		this.prefix = prefix;
		this.initialise();
	}

	private Server() {
	}

	private void initialise() {

		FileConfiguration xmlFile = this.server.getXmlFile();
		if (!xmlFile.getFile().exists()) {
			System.out.println(this.prefix + " Server file " + xmlFile.getCanonicalPath() + " does NOT exist");
			System.out.println(this.prefix + " No connection to the server ");
			return;
		}

		server.read();
		if (!server.getConfiguration().getServerConnection().exists()) {
			System.out.println(this.prefix + " File " + xmlFile.getCanonicalPath() + "containing information about communication to the server does NOT exist");
			System.out.println(this.prefix + " No connection to the server ");
			return;
		}

		Xml<Communication> communicationFile = new Xml<>(server.getConfiguration().getServerConnection(), new Communication());
		communicationFile.read();
		Communication communication = communicationFile.getConfiguration();
		if (!this.sameNetwork(communication.getHostName())) return;

		if (OSUtils.ListOfProcess(server.getConfiguration().getJarFile().getFilename()).size() > 0) {
			this.running.set(true);
		} else {
			this.start();
		}

		if (this.running.get()) {
			this.connect();
		}

	}

	private Boolean sameNetwork(String serverHostName) {
		try {
			InetAddress thisPC = InetAddress.getLocalHost();
			byte[] pcBytes = thisPC.getAddress();
			InetAddress serverPC = InetAddress.getByName(serverHostName);
			byte[] serverBytes = serverPC.getAddress();
			if (pcBytes[2] != serverBytes[2]) {
				System.out.println(this.prefix + " Cannot reached the server");
				return false;
			}
			return true;
		} catch (UnknownHostException ex) {
			System.out.println("Hostname can not be resolved");
			return false;
		}
	}

	public String getDiagnostic() {
		if (this.diagnostic.get() == null || this.diagnostic.get().length() == 0) {
			this.diagnostic.set(Diagnostic.getDefault());
		}
		return this.diagnostic.get();
	}

	public ServerFiles getServerFiles() {
		return this.server.getConfiguration();
	}

	public Xml<ServerFiles> getServerConfigurationFile() {
		return this.server;
	}

	public StringProperty scanFileNameProperty() {
		return scanFileName;
	}

	public StringProperty diagnosticProperty() {
		return diagnostic;
	}

	public Boolean isRunning() {
		return this.running.getValue();
	}

	public Boolean isConnected() {
		return this.connected.getValue();
	}

	public BooleanProperty runningProperty() {
		return this.running;
	}

	public BooleanProperty connectedProperty() {
		return this.connected;
	}

	public Boolean start() {
		System.out.println(this.prefix + " Starting the server");
		ProcessBuilder pb = new ProcessBuilder(this.server.getConfiguration().getCommandLine());
		try {
			Process process = pb.start();
			StartServer startServer = new StartServer(process);
			Thread startServerThread = new Thread(startServer);
			startServerThread.start();
			return true;
		} catch (IOException e) {
			System.out.println(this.prefix + " " + e.getMessage());
			return false;
		}
	}

	public void send(String message) {
		if (this.connection == null) {
			System.out.println(this.prefix + " [ERROR] No connection to the server");
			return;
		}
		this.connection.send(message);
	}

	public void stop() {
		System.out.println(this.prefix + " Stopping the server");
		send(Type.STOP.name());
		this.running.set(false);
		this.disconnect();
	}

	public void connect() {
		Xml<Communication> communicationFile = new Xml<>(server.getConfiguration().getServerConnection(), new Communication());
		communicationFile.read();
		Communication communication = communicationFile.getConfiguration();

		this.connection = new Connection(communication.getHostName(), communication.getPort());
		connectionThread = new Thread(connection);
		connectionThread.setDaemon(true);
		connectionThread.start();
	}

	public void disconnect() {
		this.connection.disconnect();
		this.connectionThread.interrupt();
		this.connection = null;
	}

	// generate an instance
	private static synchronized Server tryCreateInstance(String prefix) {
		if (INSTANCE == null) {
			INSTANCE = new Server(prefix);
		}
		return INSTANCE;
	}

	/**
	 * Create an instance of this class
	 *
	 * @return the instance of this class
	 */
	public static Server getInstance(String prefix) {
		// use local variable, don't issue 2 reads (memory fences) to 'INSTANCE'
		Server s = INSTANCE;
		if (s == null) {
			// check under lock; move creation logic to a separate method to
			// allow inlining of getInstance()
			s = tryCreateInstance(prefix);
		}
		return s;
	}

	// generate an instance
	private static synchronized Server tryCreateInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Server();
		}
		return INSTANCE;
	}

	/**
	 * Create an instance of this class
	 *
	 * @return the instance of this class
	 */
	public static Server getInstance() {
		// use local variable, don't issue 2 reads (memory fences) to 'INSTANCE'
		Server s = INSTANCE;
		if (s == null) {
			// check under lock; move creation logic to a separate method to
			// allow inlining of getInstance()
			s = tryCreateInstance();
		}
		return s;
	}

	private class Connection implements Runnable {

		private Socket			clientSocket;
		private String			fromServerString;
		private PrintWriter		toServer;
		private BufferedReader	fromServer;

		private String			hostName;
		private int				portNumber;

		public Connection(String hostName, int portNumber) {
			this.hostName = hostName;
			this.portNumber = portNumber;
			this.connect();
		}

		public void connect() {
			try {
				clientSocket = new Socket(hostName, portNumber);
				toServer = new PrintWriter(clientSocket.getOutputStream(), true);
				fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				toServer.println(Type.NAME + " : " + ScanFitGUI.class.getSimpleName());
				connected.set(true);
				System.out.println(prefix + " Server Connected ");

			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println(prefix + " ERROR - " + e.getMessage() + " on " + hostName + "/" + portNumber);
				Thread.currentThread().interrupt();
			}
		}

		protected void send(String message) {
			System.out.println(prefix + " SENT TO SERVER - " + message);
			this.toServer.println(message);
		}

		protected void disconnect() {
			try {
				this.clientSocket.close();
				connected.set(false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				while (true) {
					fromServerString = fromServer.readLine();
					System.out.println(prefix + " RECEIVED from server : " + fromServerString);
					Type type = (fromServerString != null) ? Message.getType(fromServerString) : Type.STOP;
					switch (type) {
					case NAME:
						break;
					case RESULTS:
						break;
					case SCAN_FILE:
						scanFileName.set(Message.get(fromServerString));
						break;
					case DIAGNOSTIC:
						diagnostic.set(Message.get(fromServerString));
						break;
					case STOP:
						disconnect();
						break;
					case UNKNOWN:
						break;
					default:
						break;

					}
				}
			} catch (UnknownHostException e) {
				System.out.println(e);
			} catch (IOException e) {
				System.out.println(prefix + " SERVER not connected");
			}
		}

	}

	private class StartServer implements Runnable {

		private Process process;

		public StartServer(Process process) {
			this.process = process;
		}

		@Override
		public void run() {
			try {
				try (BufferedReader bri = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
					String line = null;

					while ((line = bri.readLine()) != null) {
						System.out.println(line);
						if (line.contains("started at")) {
							System.out.println(prefix + " Server running ");
							running.set(true);

							connect();
						}
					}

					// Check result
					try {
						if (process.waitFor() != 0) {
							if (!process.isAlive()) {
								running.set(false);
							}
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						e.printStackTrace();
					}

				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

	}

}
