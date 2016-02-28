package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import lexer.LexerException;
import parser.ParserException;
import semantic.Program;
import Main.SyntaxHighlighter;
import Main.LineNumberBorder;
public class myTextArea extends JTextPane {
	
	private Document doc = null;
	private File file = null; 
	private boolean isSaved = true;
	
	public static Color color = Color.BLACK;
	public static Font font = new Font("Consolas",Font.BOLD,20); 
	
	public myTextArea(String file){
		super();
		this.file = new File(file);	
		changeFont();
		Text();
		doc = this.getDocument();
		doc.addDocumentListener(new SyntaxHighlighter(this));
//		CodeDocument doc1 = new CodeDocument();
		this.setBorder(new LineNumberBorder());
//		
//		 Color comment = new Color(63,197,95);
//		 Color javadoc = new Color(63,95,191);
//		 Color annotation = new Color(100,100,100);
//		 doc1.setCommentColor(comment);
//		 doc1.setJavadocColor(javadoc);
//		 doc1.setAnnotationColor(annotation);
//		 this.setDocument(doc1);
	}
	
	public void showUnSave(){
		if(isSaved){
			for(int i=0;i<Window.tabPane.getTabCount();i++){
				if(file.getPath().equals(Window.tabPane.getToolTipTextAt(i))){
					Window.tabPane.setTitleAt(i, "*"+Window.tabPane.getTitleAt(i));
					break;
				}
			}
			isSaved = false;
		}
	}
	
	private void Text(){
		try {
			Scanner in = new Scanner(file);
			String content = "";
			while(in.hasNextLine()){
				String str = in.nextLine();
				content = content+str;
				if(in.hasNextLine()){
					content = content+"\n";
				}
			}
			setText(content);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Save(){
		String[] text = getText().split("\n");
		for(int i=0;i<text.length;i++){
			System.out.println(text[i]);
		}
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			for(int i=0;i<text.length;i++){
				fileOut.write(text[i].getBytes());
				if(i!=text.length-1){
					fileOut.write(0x0a);
				}
			}
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!isSaved){
			for(int i=0;i<Window.tabPane.getTabCount();i++){
				if(file.getPath().equals(Window.tabPane.getToolTipTextAt(i))){
					Window.tabPane.setTitleAt(
							i, Window.tabPane.getTitleAt(i).substring(
									1,Window.tabPane.getTitleAt(i).length()));
					break;
				}
			}
		}
		isSaved = true;
	}
	public void Run(){
		String basePath = file.getParentFile().getAbsolutePath()+'\\';
		String mainFile = file.getName();
		String[] names = mainFile.split("\\.");
		String name = basePath+mainFile;
		String mapFile = basePath+names[0]+"Map.txt";
		String gaFile = basePath+names[0]+"Ga.txt";
		String cFile = basePath+names[0]+".c";
		try {
			Program p = new Program(name,mapFile,gaFile,cFile);
			printFile(mapFile,resTextArea.MAP);
			printFile(gaFile,resTextArea.GA);
			printFile(cFile,resTextArea.C);
		} catch (LexerException e) {
			// TODO Auto-generated catch block
			printError(e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			printError(e.toString());
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			printError(e.toString());
		}
		int index = Window.splitPane.getDividerLocation();
		Window.operateTree(index);
	}
	
	private void printError(String str){
		myScrollPane sp = (myScrollPane)Window.resTabPane.getComponent(0);
		resTextArea ra = sp.getResTextArea();
		ra.printError(str);
	}
	
	private void printFile(String filePath, int model){
		File file = new File(filePath);
		myScrollPane sp = (myScrollPane)Window.resTabPane.getComponent(model);
		resTextArea ta = sp.getResTextArea();
		ta.printFile(file);
	}
	
	public boolean isSaved(){
		return isSaved;
	}
	
	public void changeFont(){
		setFont(this.font);
		setForeground(this.color);
	}

}
