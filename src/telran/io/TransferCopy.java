package telran.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TransferCopy extends Copy {

	public TransferCopy(String scrFilePath, String destFilePath, boolean overwrite) {
		super(scrFilePath, destFilePath, overwrite);
		setCopyName("TransferCopy");
	}

	@Override
	public void copyRun() throws Exception {
		try (InputStream input = new FileInputStream(getScrFilePath()); 
				OutputStream output = new FileOutputStream(getDestFilePath());) {
			input.transferTo(output);
		}

	}

}
