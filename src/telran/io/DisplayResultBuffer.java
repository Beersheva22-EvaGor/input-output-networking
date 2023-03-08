package telran.io;

public class DisplayResultBuffer extends DisplayResult {

	long bufferSize;
	public DisplayResultBuffer(long fileSize, long copyTime, long bufferSize) {
		super(fileSize, copyTime);
		this.bufferSize = bufferSize;
	}
	
	public DisplayResultBuffer(DisplayResult displayResult, long bufferSize) {
		super(displayResult.getFileSize(), displayResult.getCopyTime());
		this.bufferSize = bufferSize;
	}

	@Override
	public String toString() {
		return super.toString()+String.format(", buffer size = %d", bufferSize);
	}
}
