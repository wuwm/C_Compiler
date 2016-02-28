package Main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import lexer.LexerException;
import parser.ParserException;
import semantic.Program;

public class Window extends JFrame{
	
	private String[] MenuStr = new String[]{"文件","运行","控制","工作空间"};
	private String[][] MenuItemsStr = new String[][]{
			{"打开","保存"},
			{"运行"},
			{"字体","替换"},
			{"新建工程","删除工程"}
	};
	private String[] resTabStr = new String[]{"控制台","语法树","词法分析","c文件"};
	private JMenuItem[][] MenuItems = new JMenuItem[4][4];

	private Container container = null;
	private JScrollPane sp = null;
	private myTextArea area = null;
	private JMenuBar MainMenu = null;
	private JFileChooser fileChooser = null;
	
	public static JSplitPane splitPane = null;
	public static JSplitPane childSplitPane = null;
	public static JTree tree = null;
	public static JTabbedPane tabPane = null;
	public static JTabbedPane resTabPane = null;
	
	public Window(){
		super("Tiny语言编译器");
		InitFile();
		container = getContentPane();
		fileChooser = new JFileChooser("C:\\workplace\\");
		tabPane = new JTabbedPane(JTabbedPane.TOP);
		
		initResTabPane();
		setTabPaneAction();
		InitMenu();
		InitSplitPane();
		operateTree(140);
		
		setLayout(new BorderLayout());
		container.add(splitPane,BorderLayout.CENTER);
		setVisible(true);
		setBounds(100,50,1000,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void InitMenu(){
		MainMenu = new JMenuBar();
		for(int i=0;i<MenuItemsStr.length;i++){
			for(int j=0;j<MenuItemsStr[i].length;j++){
				MenuItems[i][j] = new JMenuItem(MenuItemsStr[i][j]);
			}
		}
		SetMenuItemAction();
		for(int i=0;i<MenuStr.length;i++){
			JMenu menu = new JMenu(MenuStr[i]);
			for(int j=0;j<MenuItemsStr[i].length;j++){
				menu.add(MenuItems[i][j]);
			}
			MainMenu.add(menu);
		}
		this.setJMenuBar(MainMenu);
	}
	
	private void SetMenuItemAction(){
		MenuItems[0][0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				showOpenFileDialog();
			}
			
		});
		MenuItems[0][1].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Save();
			}
			
		});
		MenuItems[1][0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Run();
			}
			
		});
		MenuItems[2][0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				ChooseFontDialog dialog = new ChooseFontDialog();
				dialog.show();
			}
			
		});
		MenuItems[2][1].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		MenuItems[3][0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				InputWorkPlaceDialog dialog = new InputWorkPlaceDialog(
						InputWorkPlaceDialog.PROJECT,"C:\\workplace\\");
				dialog.show();
			}
			
		});
		
		MenuItems[3][1].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				OpenWorkPlaceDialog dialog = new OpenWorkPlaceDialog(OpenWorkPlaceDialog.DELETE);
				dialog.show();
			}
			
		});
	}
	
	private void showOpenFileDialog(){ 
		fileChooser.setApproveButtonText("确定");
		fileChooser.setDialogTitle("打开文件");
		int result = fileChooser.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();
			String name = file.getName();
			String path = file.getPath();
			addTab(name,path);
		}
		else if(result == JFileChooser.CANCEL_OPTION){
			
		}
	}
	
	private void setTabPaneAction(){
		tabPane.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getButton() == MouseEvent.BUTTON3){
					String place = tabPane.getToolTipText(e);
					System.out.println(place);
					int index = -1;
					for(int i=0;i<tabPane.getTabCount();i++){
						if(tabPane.getToolTipTextAt(i).equals(place)){
							index = i;
							break;
						}
					}
					if(place != null && !place.equals("")){
						myPopupMenu pop = new myPopupMenu(place,index);
						pop.show(e.getComponent(),e.getX(),e.getY());
					}
				}
			}
		});
		
		resTabPane.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.getButton() == MouseEvent.BUTTON3){
					myPopupMenu pop = new myPopupMenu();
					pop.show(e.getComponent(),e.getX(),e.getY());
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private void InitSplitPane(){
		splitPane = new JSplitPane();
		childSplitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setDividerLocation(140);
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setRightComponent(childSplitPane);
		
		childSplitPane.setContinuousLayout(true);
		childSplitPane.setDividerLocation(400);
		childSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		childSplitPane.setLeftComponent(tabPane);
		childSplitPane.setRightComponent(resTabPane);
	}
	
	private void initResTabPane(){
		resTabPane = new JTabbedPane(JTabbedPane.TOP);
		for(int i=0;i<resTabStr.length;i++){
			resTextArea p = new resTextArea();
			myScrollPane sp = new myScrollPane(p);
			resTabPane.addTab(resTabStr[i],sp);
		}
	}
	
	private void InitFile(){
		Path path = Paths.get("C:/workplace");
		try{
			Files.createDirectory(path);
		}
		catch(IOException e){
			System.out.println("名称重复，创建失败，请重新创建！");
		}
		Path path1 = Paths.get("C:/workplace/info.txt");
		try{
			Files.createFile(path1);
		}
		catch(NoSuchFileException e){
			System.out.println("配置文件创建失败！");
		}
		catch(FileAlreadyExistsException e){
			System.out.println("配置文件创建失败！");
		}
		catch(IOException e){
			System.out.println("配置文件创建失败！");
		}
	}
	
	private void Save(){
		myScrollPane sp = (myScrollPane)tabPane.getSelectedComponent();
		sp.Save();
	}
	
	public static void Run(){
		Clear();
		myScrollPane sp = (myScrollPane)tabPane.getSelectedComponent();
		sp.Run();
	}
	
	public static void Clear(){
		for(int i=0;i<Window.resTabPane.getTabCount();i++){
			myScrollPane sp = (myScrollPane)Window.resTabPane.getComponent(i);
			sp.getResTextArea().setText("");
		}
	}
	
	public static void operateTree(int index){

		File file = new File("C:\\workplace");
		DefaultMutableTreeNode d = new DefaultMutableTreeNode("workplace");
		Node(d,file);
		tree = new JTree(d);
		setTreeAction();
		for(int i=0;i<tree.getRowCount();i++){
			tree.expandRow(i);
		}
		splitPane.setLeftComponent(tree);
		splitPane.setDividerLocation(index);
		tree.updateUI();
			
	}
	
	private static void Node(DefaultMutableTreeNode node,File file){
		File[] list = file.listFiles();
		for(int i=0;i<list.length;i++){
			if(!list[i].getPath().equals( "C:\\workplace\\info.txt")){
				DefaultMutableTreeNode d = new DefaultMutableTreeNode(list[i].getName());
				node.add(d);
				if(list[i].isDirectory()){
					Node(d,list[i]);
				}	
			}
		}
	}
	
	private static void setTreeAction(){
		tree.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				try{
					String path = tree.getPathForLocation(e.getX(), e.getY()).toString();
					String path1 = path.substring(1, path.length()-1);
					String[] pathnodes = path1.split(", ");
					String wplace = "C:";
					for(int i=0;i<pathnodes.length;i++){
						wplace = wplace+"\\"+pathnodes[i];
					}
					System.out.println(wplace);
					if(!wplace.equals("C:\\workplace")){
						File file = new File(wplace);
						if(e.getClickCount() >= 2 && e.getButton() == MouseEvent.BUTTON1){
							if(file.isDirectory()){
								
							}
							else{
								addTab(pathnodes[pathnodes.length-1],wplace);
							}
						}
						else if(e.getButton() == MouseEvent.BUTTON3){
							myPopupMenu pop = new myPopupMenu(pathnodes[pathnodes.length-1],wplace);
							pop.setMouseEvent(e);
							pop.show(e.getComponent(),e.getX(),e.getY());
						}
						else{
							
						}
					}
				}
				catch(Exception ex){
					
				}
			}
		});
	}
	
	public static void addTab(String file,String wplace){
		if(!isContain(wplace)){
			myTextArea area = new myTextArea(wplace);
			myScrollPane sp = new myScrollPane(area);
			tabPane.addTab(file, null, sp, wplace);
		}
		for(int i=0;i<tabPane.getTabCount();i++){	
			if(tabPane.getToolTipTextAt(i).equals(wplace)){
				tabPane.setSelectedIndex(i);
				break;
			}
		}
	}
	
	private static boolean isContain(String wplace){
		for(int i=0;i<tabPane.getTabCount();i++){	
			if(tabPane.getToolTipTextAt(i).equals(wplace)){
				return true;
			}
		}
		return false;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Window();
	}
}
