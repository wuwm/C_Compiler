package semantic;

import java.io.PrintStream;

public class Repeat_Stmt extends Stmt {
	private Stmt stmt;
	private Exp exp;

	public Repeat_Stmt(Stmt stmt, Exp exp) {
		this.stmt = stmt;
		this.exp = exp;
	}

	@Override
	public void getCode(PrintStream out) {
		// TODO Auto-generated method stub
		this.printTab(out);
		out.println("do {");
		stmt.getCode(out);
		this.printTab(out);
		out.printf("} while (!(%s));\n", exp.getCode());
	}

	@Override
	public void buildTree(PrintStream out) {
		// TODO Auto-generated method stub
		this.printTab(out);
		out.println("repeat");
		stmt.setInAttribute(inattribute + 1);
		stmt.buildTree(out);
		this.printTab(out);
		out.println("until");
		exp.setInAttribute(inattribute + 1);
		exp.buildTree(out);

	}

}
