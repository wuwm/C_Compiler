package semantic;

import java.io.PrintStream;

public abstract class Node {
	protected int inattribute;

	public abstract void buildTree(PrintStream out);

	public void setInAttribute(int inattribute) {
		this.inattribute = inattribute;
	}

	public void printTab(PrintStream out) {
		for (int i = 0; i < this.inattribute; i++)
			out.print("\t");
	}

	public int getInAttribute() {
		return this.inattribute;
	}
}
