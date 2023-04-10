package telran.mygit;

import java.util.*;

public class CommitMessage implements IMessage {
	private static final long serialVersionUID = 1L;
	String message;
	String key;
	private static final int KEY_LENGTH = 7;

	public CommitMessage(String message) {
		this.message = message;
		key = generateKey();
	}

	private String generateKey() {
		String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		StringBuilder key = new StringBuilder();
		Random rnd = new Random();
		while (key.length() < KEY_LENGTH) { 
			int index = (int) (rnd.nextFloat() * symbols.length());
			key.append(symbols.charAt(index));
		}
		String res = key.toString();
		return res;
	}
	
	public String getMessage() {
		return message;
	}

	@Override
	public String getUniqueKey() {
		return key;
	}

	@Override
	public String toString() {
		return String.format("%s - %s", message, key);
	}
}
