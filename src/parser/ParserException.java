package parser;

public class ParserException extends Exception {
	ParserException(int row, int col, String mes) {
		super(String.format("ParseError:(%d,%d):%s", row, col, mes));
	}
}
