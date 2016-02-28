package parser;

import java.io.IOException;
import java.io.PrintStream;

import semantic.Assign_Stmt;
import semantic.BinOpera;
import semantic.Exp;
import semantic.Factor;
import semantic.If_Stmt;
import semantic.LeafExp;
import semantic.Read_Stmt;
import semantic.Repeat_Stmt;
import semantic.Stmt;
import semantic.Stmt_Sequence;
import semantic.Write_Stmt;
import lexer.Lexer;
import lexer.LexerException;
import lexer.Word;

public class Parser {
	private Lexer lex;
	private Word word;
	private STable table;
	
	PrintStream out;

	public Parser(Lexer lex, STable table) throws LexerException, IOException {
		this.lex = lex;
		this.table = table;
		next();
	}

	private boolean next() throws LexerException, IOException {
		word = lex.nextToken();
		return word != null;
	}

	public Stmt program() throws ParserException, LexerException, IOException {
		checkEnd();
		Stmt stmt = seq();
		if (isEnd())
			return stmt;
		throw new ParserException(word.getRow(), word.getCol(), "错误的结尾");
	}

	public Stmt seq() throws ParserException, LexerException, IOException {
		checkEnd();
		Stmt left = statement();
		while (true) {
			if (!isEnd() && word.getTag() == Word.SEMICOLON) {
				next();
				left = new Stmt_Sequence(left, statement());
			} else
				return left;
		}
	}

	public Stmt statement() throws ParserException, LexerException, IOException {
		checkEnd();
		Stmt stmt = null;
		if (word != null) {
			switch (word.getTag()) {
			case Word.IF:
				stmt = if_Stmt();
				break;
			case Word.VARIABLE:
				stmt = assign_Stmt();
				break;
			case Word.READ:
				stmt = read_Stmt();
				break;
			case Word.WRITE:
				stmt = write_Stmt();
				break;
			case Word.REPEAT:
				stmt = repeat_Stmt();
				break;
			default:
				throw new ParserException(word.getRow(), word.getCol(),
						String.format("未能识别的语句开头\"%s\"", word.getValue()));
			}
		}
		return stmt;
	}

	public Stmt assign_Stmt() throws ParserException, LexerException, IOException {
		checkEnd();
		LeafExp id = new LeafExp(word);
		table.add(word.getValue());
		next();
		if (!isEnd()) {
			if (word.getTag() == Word.ASSIGN) {
				next();
				return new Assign_Stmt(id, exp());
			} else
				throw new ParserException(word.getRow(), word.getCol(),
						String.format("无法匹配语法规则的符号\"%s\"", word.getValue()));
		} else
			throw new ParserException(word.getRow(), word.getCol(), "无法匹配语法的符号");
	}

	public Stmt if_Stmt() throws ParserException, LexerException, IOException {
		checkEnd();
		Exp exp = null;
		Stmt then_stmt = null, else_stmt = null;
		if (match(Word.IF)) {
			exp = exp();
		}
		if (match(Word.THEN)) {
			then_stmt = seq();
		}
		checkEnd();
		if (word.getTag() == Word.ELSE) {
			next();
			else_stmt = seq();
		}
		if (match(Word.END)) {
			return new If_Stmt(exp, then_stmt, else_stmt);
		}
		throw new ParserException(word.getRow(), word.getCol(), "无法匹配语法的符号");
	}

	public Stmt repeat_Stmt() throws ParserException, LexerException, IOException {
		checkEnd();
		Stmt stmt = null;
		Exp expr = null;
		if (match(Word.REPEAT)) {
			stmt = seq();
		}
		if (match(Word.UNTIL))
			expr = exp();
		return new Repeat_Stmt(stmt, expr);
	}

	public Stmt read_Stmt() throws ParserException, LexerException, IOException {
		checkEnd();
		LeafExp exp;
		if (match(Word.READ)) {
			if (!isEnd() && word.getTag() == Word.VARIABLE) {
				exp = new LeafExp(word);
				table.add(word.getValue());
				next();
				return new Read_Stmt(exp);
			}
		}
		throw new ParserException(word.getRow(), word.getCol(), "无法匹配语法的符号");
	}

	
	public Stmt write_Stmt() throws ParserException, LexerException, IOException {
		checkEnd();
		Exp expr;
		if (match(Word.WRITE)) {
			expr = exp();
			return new Write_Stmt(expr);
		}
		throw new ParserException(word.getRow(), word.getCol(), "无法匹配语法的符号");
	}

	public Exp exp() throws ParserException, LexerException, IOException {
		int op = -1;
		checkEnd();
		Exp left = simple_exp();
		if (isEnd()) {
			return left;
		}
		if (!isEnd()) {
			switch (word.getTag()) {
			case Word.EQUAL:
				op = Word.EQUAL;
				break;
			case Word.LESS:
				op = Word.LESS;
				break;
			default:
				return left;
			}
			next();
			Exp right = simple_exp();
			return new BinOpera(left, op, right);
		}
		return left;
	}

	public Exp simple_exp() throws ParserException, LexerException, IOException {
		checkEnd();
		int op = -1;
		Exp left = term();
		while (true) {
			if (!isEnd()) {
				switch (word.getTag()) {
				case Word.PLUS:

					op = Word.PLUS;
					break;
				case Word.SUB:

					op = Word.SUB;
					break;
				default:
					return left;
				}
				next();
				Exp right = term();
				left = new BinOpera(left, op, right);
			} else
				return left;
		}

	}

	public Exp term() throws ParserException, LexerException, IOException {
		checkEnd();
		int op = -1;
		Exp left = factor();
		while (true) {
			if (!isEnd()) {
				switch (word.getTag()) {
				case Word.MUTL:
					op = Word.MUTL;
					break;
				case Word.DIV:
					op = Word.DIV;
					break;
				default:
					return left;
				}
				next();
				Exp right = factor();
				left = new BinOpera(left, op, right);
			} else
				return left;
		}
	}

	public Exp factor() throws ParserException, LexerException, IOException {
		checkEnd();
		Exp expr;
		switch (word.getTag()) {
		case Word.LEFTBRACKET:
			next();
			expr = new Factor(exp());
			if (word.getTag() != Word.RIGHTBRACKET)
				throw new ParserException(word.getRow(), word.getCol(),
						String.format("不合适的符号\"%s\"导致括号不匹配", word.getValue()));
			next();
			break;
		case Word.INT:
		case Word.VARIABLE:
			expr = new LeafExp(word);
			if (word.getTag() == Word.VARIABLE && !table.isContained(word.getValue()))
				throw new ParserException(word.getRow(), word.getCol(),
						String.format("未初始赋值便使用的字符\"%s\"", word.getValue()));
			next();
			break;

		default:
			throw new ParserException(word.getRow(), word.getCol(), String.format("不符合语法规则的符号\"%s\"", word.getValue()));
		}
		return expr;
	}

	private void checkEnd() throws ParserException {
		if (lex.isEnd()) {
			throw new ParserException(lex.getLocation()[0], lex.getLocation()[1], "不正确的结尾");
		}
	}

	public boolean isEnd() {
		return word == null;
	}

	public boolean match(int tag) throws LexerException, IOException, ParserException {
		if (word != null && word.getTag() == tag) {
			next();
			return true;
		}
		throw new ParserException(word.getRow(), word.getCol(), String.format("不符合语法规则的符号\"%s\"", word.getValue()));
	}
}
