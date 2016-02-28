package semantic;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import lexer.Lexer;
import lexer.LexerException;
import parser.Parser;
import parser.ParserException;
import parser.STable;

public class Program extends Stmt {
	private Stmt seq;
	private STable table;
	private Lexer lex;
	private Parser parse;
	private File inputFile;
	private PrintStream mapFile, gaFile, cFile;

	public Program(String inputFile, String mapFile, String gaFile, String cFile)
			throws LexerException, IOException, ParserException {
		this.inputFile = new File(inputFile);
		this.mapFile = new PrintStream(new File(mapFile));
		this.gaFile = new PrintStream(new File(gaFile));
		this.cFile = new PrintStream(new File(cFile));
		table = new STable();
		lex = new Lexer(this.inputFile, this.mapFile);
		parse = new Parser(lex, table);
		seq = parse.program();
		this.buildTree(this.gaFile);
		this.getCode(this.cFile);
		this.cFile.close();
		this.mapFile.close();
		this.gaFile.close();
		this.setInAttribute(0);
	}

	@Override
	public void getCode(PrintStream out) {
		// TODO Auto-generated method stub
		out.println("#include <stdio.h>");
		out.println();
		out.println("int main() {");
		out.printf("\t%s\n", table.toString());
		seq.getCode(out);
		out.println("\treturn 0;");
		out.println("}");
	}

	@Override
	public void buildTree(PrintStream out) {
		// TODO Auto-generated method stub
		seq.setInAttribute(this.inattribute + 1);
		seq.buildTree(out);
	}

}
