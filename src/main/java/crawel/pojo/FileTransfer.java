package crawel.pojo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class FileTransfer {

	private String server;
	private int port;

	private String username;

	private String password;

	private String path;

	private String fileName;

	private long fileSize;

}
