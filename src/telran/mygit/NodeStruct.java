package telran.mygit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record NodeStruct<T, M extends IMessage>( String parent, T data, M message) implements Serializable {
	public static List<String> children= new ArrayList<>();
public NodeStruct{
	Objects.requireNonNull(data);
	Objects.requireNonNull(message);
	Objects.requireNonNull(parent);
}
}
