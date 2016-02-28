package semantic;

import java.io.PrintStream;

public class Stmt_Sequence extends Stmt {
	private Stmt left;
	private Stmt right;

	public Stmt_Sequence(Stmt left, Stmt right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public void getCode(PrintStream out) {
		// TODO Auto-generated method stub
		left.getCode(out);
		if (right != null)
			right.getCode(out);
	}

	@Override
	public void buildTree(PrintStream out) {
		// TODO Auto-generated method stub
		left.setInAttribute(inattribute);
		left.buildTree(out);
		right.setInAttribute(inattribute);
		right.buildTree(out);
	}

}
