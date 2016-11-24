package crawel.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawel.pojo.FileTransfer;
import crawel.pojo.FileTransferList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileTransferListStorage {

	private static final String ALL_FILE_TRANSFERS_JSON = "allFileTransfers.json";
	private static final FileTransferListStorage instance = new FileTransferListStorage();

	public static FileTransferList get() {
		return get(ALL_FILE_TRANSFERS_JSON);
	}

	public static FileTransferList get(String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		FileTransferList allFileTransfers = new FileTransferList();
		try {
			allFileTransfers = mapper.readValue(new File(fileName), FileTransferList.class);
		} catch (IOException e) {
			log.error("could not open file, creating one", e);
			allFileTransfers = new FileTransferList();
			String server = "yourhost";
			int port = 21;

			String username = "yourusnername";
			String password = "yourpassword";
			String path = "/yourpath/";

			FileTransfer fileTransfer = new FileTransfer();
			fileTransfer.setServer(server);
			fileTransfer.setPort(port);
			fileTransfer.setPath(path);
			fileTransfer.setFileName(fileName);
			fileTransfer.setUsername(username);
			fileTransfer.setPassword(password);

			allFileTransfers.addFileTransfer(fileTransfer);

			put(allFileTransfers);

		}
		return allFileTransfers;

	}

	public static FileTransferListStorage getInstance() {
		return instance;
	}

	public static void print(FileTransferList fileTransferList) {
		for (FileTransfer fileTransfer : fileTransferList.getFileTransfers()) {
			log.info(fileTransfer.toString());
		}
		log.info("printed " + fileTransferList.getFileTransfers().size());
	}

	public static void put(FileTransferList brandList) {

		put(brandList, ALL_FILE_TRANSFERS_JSON);

	}

	public static void put(FileTransferList fileTransferList, String fileName) {

		ObjectMapper mapper = new ObjectMapper();

		try {

			mapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), fileTransferList);

		} catch (JsonGenerationException e) {
			log.error("could not generate json", e);
		} catch (JsonMappingException e) {
			log.error("could not map json", e);
		} catch (IOException e) {
			log.error("could not write file", e);
		}

	}

	public static void putFileTransfer(FileTransfer fileTransfer) {
		final FTPClient ftp = new FTPClient();

		try {
			File f = new File(fileTransfer.getFileName());

			ftp.connect(fileTransfer.getServer(), fileTransfer.getPort());
			int reply = ftp.getReplyCode();
			log.info("reply from ftp after connect {}", reply);

			ftp.login(fileTransfer.getUsername(), fileTransfer.getPassword());
			reply = ftp.getReplyCode();
			log.info("reply from ftp after login {} with {} ", reply, ftp.getSystemType());

			reply = ftp.getReplyCode();
			log.info("reply from ftp gettin systemtype{}", reply);

			ftp.changeWorkingDirectory(fileTransfer.getPassword());
			reply = ftp.getReplyCode();
			log.info("reply from ftp after path change {}", reply);

			ftp.setFileType(FTP.ASCII_FILE_TYPE);
			reply = ftp.getReplyCode();
			log.info("reply from ftp after setting filetype {}", reply);

			ftp.enterLocalPassiveMode();
			reply = ftp.getReplyCode();
			log.info("reply from ftp after setting passive mode {}", reply);

			InputStream input = new FileInputStream(f);
			// ftp.setCopyStreamListener(streamListener);
			ftp.storeFile(fileTransfer.getPath() + "/" + f.getName(), input);
			reply = ftp.getReplyCode();
			log.info("reply from ftp after store file {}", reply);
			input.close();

			for (String s : ftp.listNames(fileTransfer.getPath())) {
				log.info("listing file  {} in path {}", s, fileTransfer.getPath());
			}

			ftp.logout();
			reply = ftp.getReplyCode();
			log.info("reply from ftp after logout {}", reply);
			ftp.disconnect();
			reply = ftp.getReplyCode();
			log.info("reply from ftp after disconnect {}", reply);

		} catch (SocketException e) {
			log.error("socket problem {}", e.getMessage(), e);

		} catch (IOException e) {
			log.error("io problem {}", e.getMessage(), e);
		}
	}

	public static void putTransfers(FileTransferList fileTransferList) {
		for (FileTransfer fileTransfer : fileTransferList.getFileTransfers()) {
			putFileTransfer(fileTransfer);
		}
	}

	private FileTransferListStorage() {
	}
}
