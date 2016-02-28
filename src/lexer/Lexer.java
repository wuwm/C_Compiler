package lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class Lexer {
	private PrintStream out;
	private BufferedReader in;
	private char now;
	private int row = 0;
	private int col = 0;
	private String oneline = null;
	boolean isend = false;

	public Lexer(File in, PrintStream out) throws FileNotFoundException {
		this.in = new BufferedReader(new FileReader(in));
		this.out = out;
	}

	public Word nextToken() throws LexerException, IOException {
		Word word = nextTokenValue();
		boolean state = word != null;
		if (out != null) {
			if (state) {
				out.println(String.format("\t%d:%d:%s", word.getRow(), word.getCol(), word.outToMap()));
			} else {
				out.println(row + ":EOF");
			}
		}
		return word;
	}
	public Word nextTokenValue() throws LexerException, IOException {
		while (nextChar()) {
			while (now == ' ' || now == '\r' | now == '\t' | now == '\n') {
				nextChar();
				if (isend)
					return null;
			}
			switch (now) {
			case '{':
				while (nextChar() && now != '}') {
					if (now == '{')
						throw new LexerException(row, col, "不合法的注释");
				}
				if (now != '}')
					throw new LexerException(row, col, "不合法的注释导致错误的结尾");
				else {
					nextChar();
					if (isend) 
						return null;
				}
				break;
			}
			switch (now) {
			case '+':
				return new Word("+", Word.PLUS, row, col);
			case '-':
				return new Word("-", Word.SUB, row, col);
			case '*':
				return new Word("*", Word.MUTL, row, col);
			case '/':
				return new Word("/", Word.DIV, row, col);
			case '=':
				return new Word("=", Word.EQUAL, row, col);
			case '<':
				return new Word("<", Word.LESS, row, col);
			case ';':
				return new Word(";", Word.SEMICOLON, row, col);
			case '(':
				return new Word("(", Word.LEFTBRACKET, row, col);
			case ')':
				return new Word(")", Word.RIGHTBRACKET, row, col);
			case ':':
				if (nextChar() && now == '=')
					return new Word(":=", Word.ASSIGN, row, col - 1);
				else
					throw new LexerException(row, col, ":=符号未正确使用");

			}
			if (Character.isDigit(now)) {
				int startcol = col;
				int value = Character.digit(now, 10);
				while (nextChar() && Character.isDigit(now))
					value = 10 * value + Character.digit(now, 10);
				col--;
				return new Word(Integer.toString(value), Word.INT, row, startcol);

			}
			if (Character.isLetter(now)) {
				int startcol = col;
				StringBuilder sb = new StringBuilder();
				sb.append(now);
				while (nextChar() && (Character.isLetter(now) || Character.isDigit(now)))
					sb.append(now);
				col--;
				String value = sb.toString();
				if (Word.isReserved(value))
					return new Word(value, Word.getReservedValue(value), row, startcol);
				else {
					return new Word(value, Word.VARIABLE, row, startcol);
				}
			}
			if (now == '\n') 
				continue;
			throw new LexerException(row, col, "语言中未定义的字符'" + now + "'");
		}
		return null;
	}

	public int[] getLocation() {
		int[] l = new int[2];
		l[0] = row;
		l[1] = col;
		return l;
	}

	public boolean isEnd() {
		return isend;
	}

	private boolean nextline() throws IOException {
		oneline = in.readLine();
		while (oneline != null && oneline.isEmpty()) {
			row++;
			oneline = in.readLine();
		}
		if (oneline != null) {
			col = 0;
			row++;
			out.printf("Line %d:%s\n", row, oneline.trim());
			oneline = oneline + "\n";
			return true;
		}
		return false;
	}

	private boolean nextChar() throws IOException {
		if (oneline == null || col >= oneline.length()) {
			nextline();
		}
		if (oneline == null) {
			isend = true;
			return false;
		}
		now = oneline.charAt(col);
		col++;
		return true;
	}
}