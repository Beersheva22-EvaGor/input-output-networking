package telran.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileCopy extends Copy {

	public FileCopy(String scrFilePath, String destFilePath, boolean overwrite) {
		super(scrFilePath, destFilePath, overwrite);
		setCopyName("FileCopy");
	}


	@Override
	protected void copyRun() throws Exception {
			Path dest = Path.of(getDestFilePath());
			Files.copy(Path.of(getScrFilePath()), dest,
					isOverwrite() ? StandardCopyOption.REPLACE_EXISTING : null);

	}

}
