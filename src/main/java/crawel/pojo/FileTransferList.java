package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FileTransferList {

	private List<FileTransfer> fileTransfers;

	public FileTransferList() {
		this.fileTransfers = new ArrayList<FileTransfer>();
	}

	public FileTransferList(List<FileTransfer> collect) {
		this.fileTransfers = collect;
	}

	public void addFileTransfer(FileTransfer shop) {
		fileTransfers.add(shop);
	}

}
