package telran.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class BufferCopy extends Copy {
	int bufferSize = 1024 * 1024;

	public BufferCopy(String scrFilePath, String destFilePath, boolean overwrite, int... bufferSize) {
		super(scrFilePath, destFilePath, overwrite);
		setCopyName("BufferCopy");
		if (bufferSize.length > 0) {
			this.bufferSize = bufferSize[0];
		}
	}

	@Override
	public void copyRun() throws Exception {
		byte[] bytes = new byte[bufferSize];
		try (FileInputStream fis = new FileInputStream(getScrFilePath());
				FileOutputStream fos = new FileOutputStream(getDestFilePath(), false)) {
			int count;
			while((count = fis.read(bytes)) > -1) {
				fos.write(bytes, 0, count);
			}
		}
	}

	@Override
	DisplayResult getDisplayResult() {		
		return new DisplayResultBuffer(super.getDisplayResult(), bufferSize) ;
	}
}
