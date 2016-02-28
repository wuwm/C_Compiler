package lexer;

import java.util.HashMap;

public class Word {
	public static final int NORMAL = 256;
	public static final int INT = 257; 
	public static final int IF = 258; 
	public static final int THEN = 259; 
	public static final int ELSE = 260;
	public static final int END = 261; 
	public static final int REPEAT = 262;
	public static final int UNTIL = 263; 
	public static final int READ = 264; 
	public static final int WRITE = 265; 
	public static final int ASSIGN = 266; 
	public static final int PLUS = 267; 
	public static final int SUB = 268; 
	public static final int MUTL = 269; 
	public static final int DIV = 270; 
	public static final int EQUAL = 271; 
	public static final int LESS = 272; 
	public static final int VARIABLE = 273; 
	public static final int LEFTBRACKET = 274;
	public static final int RIGHTBRACKET = 275;
	public static final int SEMICOLON = 276;
	public static final HashMap<String, Integer> map = new HashMap<String, Integer>();

	static {
		map.put("if", IF);
		map.put("then", THEN);
		map.put("else", ELSE);
		map.put("end", END);
		map.put("repeat", REPEAT);
		map.put("until", UNTIL);
		map.put("read", READ);
		map.put("write", WRITE);
	}

	public static final String getOperator(int tag) {
		switch (tag) {
		case ASSIGN:
			return "=";
		case EQUAL:
			return "==";
		case PLUS:
			return "+";
		case SUB:
			return "-";
		case MUTL:
			return "*";
		case DIV:
			return "/";
		case LESS:
			return "<";
		default:
			return null;
		}
	}

	public static final boolean isReserved(Word word) {
		return word.getTag() <= Word.WRITE && word.getTag() >= IF;
	}

	public static final boolean isReserved(String word) {
		return map.containsKey(word);
	}

	public static final int getReservedValue(String word) {
		return map.get(word);
	}

	private int tag;
	private String value;
	private int row;
	private int col;

	public Word(Word word) {
		setWord(word);
	}

	public void setWord(Word word) {
		if (word != null) {
			this.tag = word.getTag();
			this.value = word.getValue();
			this.row = word.getRow();
			this.col = word.getCol();
		}
	}

	public Word(String value, int tag, int row, int col) {
		this.setValue(value);
		this.setTag(tag);
		this.setRow(row);
		this.setCol(col);
	}

	public String outToMap() {
		if (Word.isReserved(value))
			return "reserved word: " + value;
		if (tag == Word.INT)
			return "integer : " + value;
		if (tag == Word.VARIABLE)
			return "ID, name=" + value;
		return "symbol:  " + value;

	}

	@Override
	public String toString() {
		return this.value;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
}