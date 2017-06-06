package ncd.utils.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ncd.utils.configuration.ScanConfiguration;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class WatchDirectory implements FileListener {

	protected StringProperty	filename		= new SimpleStringProperty("");
	protected ScanConfiguration	configuration	= new ScanConfiguration();

	public WatchDirectory() throws FileSystemException, InterruptedException {
		this.configuration.read();
	}

	public void startListening() throws FileSystemException, InterruptedException {
		final FileSystemManager fsManager = VFS.getManager();
		final File file = new File(((ScanConfiguration) configuration).getScan().getPath());
		final FileObject listendir = fsManager.toFileObject(file);

		DefaultFileMonitor fileMonitor = new DefaultFileMonitor(this);
		fileMonitor.addFile(listendir);
		fileMonitor.start();

	}

	public void fileCreated(FileChangeEvent fce) throws Exception {
		throw new NotImplementedException();
	}

	public void fileDeleted(FileChangeEvent fce) throws Exception {
		throw new NotImplementedException();
	}

	public void fileChanged(FileChangeEvent fce) throws Exception {
		this.readFile();
	}

	public void readFile() {
		File file = new File(((ScanConfiguration) this.configuration).getScan().getPath());
		if (!file.exists()) return;
		try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
			this.filename.set(br.readLine());
			System.out.println("File read: " + this.filename.get());
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getScanFile() {
		return filename.get();
	}

	public void setScanFile(String scanFile) {
		this.filename.set(scanFile);
	}

	public StringProperty scanFileProperty() {
		return filename;
	}

	public void writeConfiguration() {
		this.configuration.write();
	}

}
