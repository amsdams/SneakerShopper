package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

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

	public List<FileTransfer> getFileTransfers() {
		return fileTransfers;
	}

	public void setFileTransfers(List<FileTransfer> shops) {
		this.fileTransfers = shops;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (FileTransfer s: this.fileTransfers){
			sb.append(s.toString());
		}
		return "FileTransferList [fileTransfers=" + sb.toString() + "]";
	}
}
