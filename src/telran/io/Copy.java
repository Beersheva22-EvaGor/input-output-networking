package telran.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class Copy {
	private String scrFilePath;
	private String destFilePath;
	private boolean overwrite;
	private LocalDateTime start;
	private String copyName = "Copying";
	
	protected void setCopyName(String copyName) {
		this.copyName = copyName;
	}
	
	public String getScrFilePath() {
		return scrFilePath;
	}	

	public boolean isOverwrite() {
		return overwrite;
	}

	public String getDestFilePath() {
		return destFilePath;
	}

	public Copy(String scrFilePath, String destFilePath, boolean overwrite) {
		this.scrFilePath = scrFilePath;
		this.destFilePath = destFilePath;
		this.overwrite = overwrite;
	}
	
	protected long copySize(String fileName) {
		long res = 0;
		try {
			res = Files.size(Path.of(fileName));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return res;
	}
	
	DisplayResult getDisplayResult() {
		long mseconds = Duration.between(start, LocalDateTime.now()).getSeconds();
		return  new DisplayResult(copySize(getDestFilePath()), mseconds) ;
	}
	
	protected abstract void copyRun() throws Exception;
	
	public void runNDisplayResults() {
		start = LocalDateTime.now();
		try {
			System.out.println(copyName + " is running...");
			if (Files.exists(Path.of(destFilePath)) && !overwrite) {
				throw new Exception("Destination file exists, rewriting's prohibited");
			}
			copyRun();
			System.out.println(getDisplayResult().toString()+ "\n");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}
