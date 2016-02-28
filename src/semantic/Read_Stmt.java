package semantic;

import java.io.PrintStream;

public class Read_Stmt extends Stmt {
	private LeafExp var;

	public Read_Stmt(LeafExp var) {
		this.var = var;
	}

	@Override
	public void getCode(PrintStream out) {
		// TODO Auto-generated method stub
		this.printTab(out);
		out.print("scanf(\"%d\", &");
		out.print(var.getCode());
		out.print(");\n");
	}

	@Override
	public void buildTree(PrintStream out) {
		// TODO Auto-generated method stub
		this.printTab(out);
		out.printf("read:%s\n", var.getCode());
	}

}
