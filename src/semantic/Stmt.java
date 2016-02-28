package semantic;

import java.io.PrintStream;

public abstract class Stmt extends Node {
	public abstract void getCode(PrintStream out);
}
