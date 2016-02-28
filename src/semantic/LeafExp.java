package semantic;

import java.io.PrintStream;

import lexer.Word;

public class LeafExp extends Exp {
	private Word value;

	public LeafExp(Word word) {
		this.value = word;
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return value.toString();
	}

	@Override
	public void buildTree(PrintStream out) {
		// TODO Auto-generated method stub
		this.printTab(out);
		if(value.getTag()==Word.VARIABLE)
			out.printf("id:%s\n",value.toString());
		else out.printf("const:%s\n",value.toString());
	}

}
