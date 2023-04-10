package telran.mygit;

import java.io.Serializable;
import java.util.Objects;

public record FileAttributes (String fileName, long size, String dateCreation,  String dateModification) implements Serializable{

	public FileAttributes{
		Objects.requireNonNull(fileName);
		Objects.requireNonNull(size);
		Objects.requireNonNull(dateCreation);
	}

}
