package crawel.pojo;

import java.util.ArrayList;
import java.util.List;

public class FileTransferList {

	private List<FileTransfer> shops;

	public FileTransferList() {
		this.shops = new ArrayList<FileTransfer>();
	}

	public FileTransferList(List<FileTransfer> collect) {
		this.shops = collect;
	}

	public void addFileTransfer(FileTransfer shop) {
		shops.add(shop);
	}

	public List<FileTransfer> getFileTransfers() {
		return shops;
	}

	public void setFileTransfers(List<FileTransfer> shops) {
		this.shops = shops;
	}
}
