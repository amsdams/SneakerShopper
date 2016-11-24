package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FileTransferList {

	private List<FileTransfer> fileTransfers;

	public FileTransferList() {
		this.fileTransfers = new ArrayList<>();
	}

	

	public void addFileTransfer(FileTransfer fileTransfer) {
		fileTransfers.add(fileTransfer);
	}

}
