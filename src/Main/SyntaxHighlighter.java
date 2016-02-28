package Main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class SyntaxHighlighter implements DocumentListener {
	private Set<String> keywords;
	private Set<String> ops;
	private Style keywordStyle;
	private Style normalStyle;
	private Style opStyle;
	private Style eStyle;
	private myTextArea editor;

	public SyntaxHighlighter(myTextArea editor) {
		keywordStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		normalStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		opStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		eStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
		StyleConstants.setForeground(opStyle, Color.RED);
		StyleConstants.setForeground(keywordStyle, Color.BLUE);
		StyleConstants.setForeground(normalStyle, Color.darkGray);
		StyleConstants.setForeground(eStyle, Color.lightGray);
		this.editor = editor;
		keywords = new HashSet<String>();
		ops = new HashSet<String>();
		keywords.add("end");
		keywords.add("if");
		keywords.add("{");
		keywords.add("}");
		keywords.add("repeat");
		keywords.add("read");
		keywords.add("write");
		keywords.add("then");
		keywords.add("until");
		keywords.add("else");
		
		ops.add("+");
		ops.add("-");
		ops.add("*");
		ops.add("/");
		ops.add("(");
		ops.add(")");
		ops.add("=");
		ops.add("<");
		ops.add(">");
		ops.add(":=");
		ops.add("<=");
		ops.add(">=");
		
		ArrayList<WordNode> nodes = new ArrayList<WordNode>();
		String str = editor.getText();
		System.out.println(str);
		String[] words = str.split(" ");
		
		int start = 0;
		int ss = -1;
		int ee = -1;
		for(int i=0;i<words.length;i++){
			if(!words[i].equals("{") && !words[i].startsWith("}")){
				try {
					colouring((StyledDocument) editor.getDocument(), start, words[i].length());
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else{
				System.out.println(words[i].charAt(0));
				if(words[i].charAt(0) == '{'){
					ss = start;
					System.out.println(ss);
				}
				else if(words[i].charAt(0) == '}'){
					ee = start+1;
					System.out.println(ee);
					if(ss != -1){
						WordNode wn = new WordNode(ss,ee);
						nodes.add(wn);
						ss = -1;
						ee = -1;
					}
				}
			}
			start = start + words[i].length() +1;
		}
		System.out.println("aaa");
		for(int i=0;i<nodes.size();i++){
			System.out.println(nodes.get(i).getStart()+" "+nodes.get(i).getLength());
				color((StyledDocument) editor.getDocument(),nodes.get(i).getStart(), nodes.get(i).getLength());
		}
		
	}
	
	private void initWord(DocumentEvent e){
		ArrayList<WordNode> nodes = new ArrayList<WordNode>();
		String str = editor.getText();
		System.out.println(str);
		String[] words = str.split(" ");
		
		int start = 0;
		int ss = -1;
		int ee = -1;
		for(int i=0;i<words.length;i++){
			if(!words[i].equals("{") && !words[i].startsWith("}")){
				try {
					colouring((StyledDocument) e.getDocument(), start, words[i].length());
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else{
				System.out.println(words[i].charAt(0));
				if(words[i].charAt(0) == '{'){
					ss = start;
					System.out.println(ss);
				}
				else if(words[i].charAt(0) == '}'){
					ee = start+1;
					System.out.println(ee);
					if(ss != -1){
						WordNode wn = new WordNode(ss,ee);
						nodes.add(wn);
						ss = -1;
						ee = -1;
					}
				}
			}
			start = start + words[i].length() +1;
		}
		System.out.println("aaa");
		for(int i=0;i<nodes.size();i++){
			System.out.println(nodes.get(i).getStart()+" "+nodes.get(i).getLength());
				color((StyledDocument) e.getDocument(),nodes.get(i).getStart(), nodes.get(i).getLength());
		}
	}
	
	private void color(StyledDocument doc,int pos,int len){
		SwingUtilities.invokeLater(new ColouringTask(doc, pos, len, eStyle));
	}

	public void colouring(StyledDocument doc, int pos, int len) throws BadLocationException {
		int start = indexOfWordStart(doc, pos);
		int end = indexOfWordEnd(doc, pos + len);

		char ch;
		while (start < end) {
			ch = getCharAt(doc, start);
			if (Character.isLetter(ch) || ch == '_'  || ops.contains(String.valueOf(ch)) || keywords.contains(String.valueOf(ch))  || ops.contains(ch) || keywords.contains(ch)) {
				start = colouringWord(doc, start);
			} 
			else{
				SwingUtilities.invokeLater(new ColouringTask(doc, start, 1, normalStyle));
				++start;
			}
		}
	}

	public int colouringWord(StyledDocument doc, int pos) throws BadLocationException {
		int wordEnd = indexOfWordEnd(doc, pos);
		String word = doc.getText(pos, wordEnd - pos);

		if (keywords.contains(word)) {
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, keywordStyle));
		}else{
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, normalStyle));
		}
		if(ops.contains(word)){
			SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, opStyle));
		}

		return wordEnd;
	}

	public char getCharAt(Document doc, int pos) throws BadLocationException {
		return doc.getText(pos, 1).charAt(0);
	}


	public int indexOfWordStart(Document doc, int pos) throws BadLocationException {
		for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos);

		return pos;
	}


	public int indexOfWordEnd(Document doc, int pos) throws BadLocationException {
		for (; isWordCharacter(doc, pos); ++pos);

		return pos;
	}


	public boolean isWordCharacter(Document doc, int pos) throws BadLocationException {
		char ch = getCharAt(doc, pos);
		if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_' || ops.contains(String.valueOf(ch)) || keywords.contains(String.valueOf(ch))  || ops.contains(ch) || keywords.contains(ch)) { return true; }
		return false;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {

	}

	@Override
	public void insertUpdate(DocumentEvent e) {
			initWord(e);
			editor.showUnSave();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		initWord(e);
		editor.showUnSave();
	}

	private class ColouringTask implements Runnable {
		private StyledDocument doc;
		private Style style;
		private int pos;
		private int len;

		public ColouringTask(StyledDocument doc, int pos, int len, Style style) {
			this.doc = doc;
			this.pos = pos;
			this.len = len;
			this.style = style;
		}

		public void run() {
			try {
				doc.setCharacterAttributes(pos, len, style, true);
			} catch (Exception e) {}
		}
	}
}