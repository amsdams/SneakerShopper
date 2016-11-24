package crawel.pojo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FileTransfer {

	private String server;
	private int port;

	private String username;

	private String password;

	private String path;

	private String fileName;

	private long fileSize;
	
	public FileTransfer(){
		
	}

}
