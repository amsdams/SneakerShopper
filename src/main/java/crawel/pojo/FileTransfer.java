package crawel.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileTransfer {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileTransfer.class);

	private String server;
	private int port;

	private String username;

	private String password;

	private String path;

	private String fileName;

	private long fileSize;

	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public String getPassword() {
		return password;
	}

	public String getPath() {
		return path;
	}

	public int getPort() {
		return port;
	}

	public String getServer() {
		return server;
	}

	public String getUsername() {
		return username;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "FileTransfer [server=" + server + ", port=" + port + ", username=" + username + ", password=" + password
				+ ", path=" + path + ", fileName=" + fileName + ", fileSize=" + fileSize + "]";
	}

}
