package telran.io;

import java.time.Duration;
import java.time.LocalDateTime;

public class FilesCopyBuilder {
	static public Copy build(String type, String[] args) {
		Copy copy = null;
		try {
			switch (type.toLowerCase()) {
			case "filecopy":
				copy = new FileCopy(args[0], args[1], Boolean.parseBoolean(args[2]));
				break;
			case "transfercopy":
				copy = new TransferCopy(args[0], args[1], Boolean.parseBoolean(args[2]));
				break;
			default:
				if (args.length > 3) {
				copy = new BufferCopy(args[0], args[1], Boolean.parseBoolean(args[2]), Integer.parseInt(args[3]));
				} else {
					copy = new BufferCopy(args[0], args[1], Boolean.parseBoolean(args[2]));
				}
				break;
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return copy;
	}

	public static void main(String[] args) {
		final String path = "C:\\Users\\ewgen\\Downloads\\android-studio-2021.2.1.16-windows.exe";
		final String endFile = "android-studio-2021.2.1.16-windows1.exe";

		// FileCopy
		Copy copy = build("FILEcopy", new String[] { path, endFile, "true" });
		copy.runNDisplayResults();

		// TransferCopy
		copy = build("TRANSFERcopy", new String[] { path, endFile, "true" });
		copy.runNDisplayResults();

	
		// BufferCopy
		copy = build("BUFFERcopy", new String[] { path, endFile, "true" });
		copy.runNDisplayResults();
		
		// Find optimal buffer
		timeCopyingOnCashSize(path, endFile);
	}

	private static void timeCopyingOnCashSize(final String path, final String endFile) {
		Copy copy;
		int maxPow = 31;
		for (int i = 10; i < maxPow; i++) {
			copy = build("BUFFERcopy", new String[] { path, endFile, "true", (int)Math.pow(2, i) +""});
			LocalDateTime start = LocalDateTime.now();
			try {
				copy.copyRun();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			System.out.printf("buffer = 2^%d, time = %d (sec)\n", i, Duration.between(start, LocalDateTime.now()).getSeconds());
		}
	}
	
	
}
