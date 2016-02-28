package Main;

import javax.swing.JScrollPane;

public class myScrollPane extends JScrollPane{
	
	private myTextArea area = null;
	private resTextArea resarea = null;
	
	public myScrollPane(resTextArea resarea){
		super(resarea);
		this.resarea = resarea;
	}
	
	public myScrollPane(myTextArea area){
		super(area);
		this.area = area;
	}
	
	public void Save(){
		area.Save(); 
	}
	
	public void Run(){
		area.Run();
	}
	
	public myTextArea getTextArea(){
		return area;
	}
	
	public resTextArea getResTextArea(){
		return resarea;
	}
}
