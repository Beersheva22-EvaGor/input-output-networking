package telran.io;

public class DisplayResult {
	long fileSize;
	long copyTime;
	
	public long getFileSize() {
		return fileSize;
	}
	
	public long getCopyTime() {
		return copyTime;
	}
	
	public DisplayResult(long fileSize, long copyTime) {
		this.fileSize = fileSize;
		this.copyTime = copyTime;
	}
	
	public String toString() {
		return String.format("file size = %.2f (Mb), copy time = %d (ms)", fileSize/1024/1024.0, copyTime);
	}
}
