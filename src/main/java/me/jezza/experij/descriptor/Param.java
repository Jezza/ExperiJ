package me.jezza.experij.descriptor;

/**
 * @author Jezza
 */
public final class Param {
	public final int index;
	public final int arrayCount;
	public final String data;

	public final int loadCode;
	public final int storeCode;
	public final int returnCode;

	Param(int index, int arrayCount, String data, int loadCode, int storeCode, int returnCode) {
		this.index = index;
		this.arrayCount = arrayCount;
		if (arrayCount == 0) {
			this.data = data;
		} else {
			int length = data.length();
			char[] c = new char[arrayCount + length];
			for (int i = 0; i < arrayCount; i++)
				c[i] = '[';
			for (int i = 0; i < length; i++)
				c[arrayCount + i] = data.charAt(i);
			this.data = new String(c);
		}
		this.loadCode = loadCode;
		this.storeCode = storeCode;
		this.returnCode = returnCode;
	}

	@Override
	public String toString() {
		return data;
	}
}
