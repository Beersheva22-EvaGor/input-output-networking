package telran.net.test;

import java.io.Serializable;

public class Operands implements Serializable {
	private static final long serialVersionUID = 1L;
	private int a;
	private int b;

	public Operands(int a, int b) {
		this.a = a;
		this.b = b;
	}

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

}
