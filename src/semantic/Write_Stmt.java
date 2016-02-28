package semantic;

import java.io.PrintStream;

public class Write_Stmt extends Stmt {
	private Exp exp;

	public Write_Stmt(Exp exp) {
		this.exp = exp;
	}

	@Override
	public void getCode(PrintStream out) {
		// TODO Auto-generated method stub
		this.printTab(out);
		out.print("printf(\"%d \", ");
		out.print(exp.getCode());
		out.println(");");
	}

	@Override
	public void buildTree(PrintStream out) {
		// TODO Auto-generated method stub
		this.printTab(out);
		out.println("write");
		exp.setInAttribute(inattribute + 1);
		exp.buildTree(out);
	}

}
