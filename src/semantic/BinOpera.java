package semantic;

import java.io.PrintStream;

import lexer.Word;

public class BinOpera extends Exp {
	private Exp left;
	private int opera;
	private Exp right;

	public BinOpera(Exp left, int opera, Exp right) {
		this.left = left;
		this.opera = opera;
		this.right = right;
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return left.getCode() + " " + Word.getOperator(this.opera) + " " + right.getCode();
	}

	@Override
	public void buildTree(PrintStream out) {
		// TODO Auto-generated method stub
		this.printTab(out);
		out.printf("op:%s\n", Word.getOperator(this.opera));
		left.setInAttribute(this.getInAttribute() + 1);
		right.setInAttribute(this.getInAttribute() + 1);
		left.buildTree(out);
		right.buildTree(out);
	}
}
