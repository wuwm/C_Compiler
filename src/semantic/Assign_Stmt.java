package semantic;

import java.io.PrintStream;

public class Assign_Stmt extends Stmt {
	LeafExp left;
	Exp right;

	public Assign_Stmt(LeafExp left, Exp right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public void getCode(PrintStream out) {
		// TODO Auto-generated method stub
		this.printTab(out);
		out.printf("%s = %s;\n", left.getCode(), right.getCode());
	}

	@Override
	public void buildTree(PrintStream out) {
		// TODO Auto-generated method stub
		this.printTab(out);
		out.printf("assign to:%s\n", left.getCode());
		right.setInAttribute(this.getInAttribute() + 1);
		right.buildTree(out);
	}

}
