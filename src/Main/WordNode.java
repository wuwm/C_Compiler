package Main;

public class WordNode {
	
	private int start;
	private int length;
	
	public WordNode(int start,int end){
		this.start = start;
		this.length = end-start+1;
	}
	
	
	public int getStart(){
		return start;
	}
	
	public int getLength(){
		return length;
	}
}
