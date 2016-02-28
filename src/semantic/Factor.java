package semantic;

import java.io.PrintStream;

public class Factor extends Exp {
	private Exp exp;

	public Factor(Exp exp) {
		this.exp = exp;
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return "(" + exp.getCode() + ")";
	}

	@Override
	public void buildTree(PrintStream out) {
		// TODO Auto-generated method stub
		this.exp.setInAttribute(this.getInAttribute());
		this.exp.buildTree(out);
	}

}
