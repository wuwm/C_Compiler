package lexer;

public class LexerException extends Exception {
	public LexerException(int row, int col, String mes) {
		super(String.format("LexerError:(%d,%d):%s", row, col, mes));
	}
}
