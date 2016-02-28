package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JTextPane;

public class resTextArea extends JTextPane{
	
	public static int GA = 1;
	public static int MAP = 2;
	public static int C = 3;

	public resTextArea(){
		setEditable(true);
	}
	
	public void printError(String str){
		setText(getText()+"\n"+str);
	}
	
	public void printFile(File file){
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
}
