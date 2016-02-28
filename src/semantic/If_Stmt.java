package semantic;

import java.io.PrintStream;

public class If_Stmt extends Stmt {
	private Exp exp;
	private Stmt then_stmt;
	private Stmt else_stmt;

	public If_Stmt(Exp exp, Stmt then_stmt) {
		this.exp = exp;
		this.then_stmt = then_stmt;
		this.else_stmt = null;
	}

	public If_Stmt(Exp exp, Stmt then_stmt, Stmt else_stmt) {
		this.exp = exp;
		this.then_stmt = then_stmt;
		this.else_stmt = else_stmt;
	}

	@Override
	public void getCode(PrintStream out) {
		// TODO Auto-generated method stub
		this.printTab(out);
		out.printf("if (%s) {\n", this.exp.getCode());
		this.then_stmt.getCode(out);
		this.printTab(out);
		
		if (this.else_stmt != null) {
			out.println("} else {");
			this.else_stmt.getCode(out);
			this.printTab(out);
			out.print("}\n");
		}
		else out.println("}");
	}

	@Override
	public void buildTree(PrintStream out) {
		// TODO Auto-generated method stub
		this.printTab(out);
		out.println("if");
		exp.setInAttribute(inattribute + 1);
		exp.buildTree(out);
		then_stmt.setInAttribute(inattribute + 1);
		then_stmt.buildTree(out);
		if (else_stmt != null) {
			else_stmt.setInAttribute(inattribute + 1);
			else_stmt.buildTree(out);
		}
	}

}
