package Main;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class myPopupMenu extends JPopupMenu{
	
	private JMenuItem[] Fileitems = null;
	private JMenuItem[] Treeitems = null;
	private JMenuItem[] resTabitems = null;
	
	private String[] FileitemsName = new String[]{"关闭"};
	private String[] TreeDiritemsName = new String[]{"新建文件","新建目录","删除"};
	private String[] TreeFileitemsName = new String[]{"删除","运行"};
	private String[] resTabitemName = new String[]{"清空"};
	
	private String place = null;
	private String filename = null;
	private int index;
	private MouseEvent event = null;
	
	public myPopupMenu(){
		resTabitems = new JMenuItem[resTabitemName.length];
		addMenuItem(resTabitems,resTabitemName);
		setResTabAction();
	}

	public myPopupMenu(String place,int index){
		this.place = place;
		this.index = index;
		Fileitems = new JMenuItem[FileitemsName.length];
		addMenuItem(Fileitems,FileitemsName);
		setFileAction();
	}
	
	public myPopupMenu(String filename,String place){
		this.filename = filename;
		this.place = place;
		File file = new File(place);
		if(file.isDirectory()){
			Treeitems = new JMenuItem[TreeDiritemsName.length];
			addMenuItem(Treeitems,TreeDiritemsName);
			setTreeDirAction();
		}
		else{
			Treeitems = new JMenuItem[TreeFileitemsName.length];
			addMenuItem(Treeitems,TreeFileitemsName);
			setTreeFileAction();
		}
	}
	
	private void addMenuItem(JMenuItem[] items,String[] names){
		for(int i=0;i<items.length;i++){
			items[i] = new JMenuItem(names[i]);
			add(items[i]);
		}
	}
	
	private void setFileAction(){
		Fileitems[0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				myScrollPane s = (myScrollPane)Window.tabPane.getComponentAt(index);
				myTextArea a = s.getTextArea();
				if(!a.isSaved()){
					int choose = JOptionPane.showConfirmDialog(
							null,"文件未保存，是否保存？","提示",JOptionPane.YES_NO_CANCEL_OPTION);
					switch(choose){
					case 0:
						a.Save();
						Window.tabPane.remove(index);
						break;
					case 1:
						Window.tabPane.remove(index);
						break;
					case 2:
						break;
					}
				}
				else{
					Window.tabPane.remove(index);
				}
			}
			
		});
	}
	
	private void setTreeDirAction(){
		Treeitems[0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				InputWorkPlaceDialog dialog = new InputWorkPlaceDialog(
						InputWorkPlaceDialog.FILE,place+"\\");
				dialog.show();
			}
			
		});
		
		Treeitems[1].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				InputWorkPlaceDialog dialog = new InputWorkPlaceDialog(
						InputWorkPlaceDialog.DIRECTORY,place+"\\");
				dialog.show();
			}
			
		});
		
		Treeitems[2].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				DeleteFileOrDir();
			}
			
		});
		
	}
	
	private void setTreeFileAction(){
		Treeitems[0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				DeleteFileOrDir();
			}
			
		});
		
		Treeitems[1].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Window.addTab(filename,place);
				Window.Run();
			}
			
		});
	}
	
	private void setResTabAction(){
		resTabitems[0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Window.Clear();
			}
			
		});
	}
	
	private void DeleteFileOrDir(){
		File file = new File(place);
		if(!isInInfo()){
			if(file.isDirectory() && file.listFiles().length != 0  ){
				JOptionPane.showMessageDialog(null, "文件夹中包含其他文件，不能删除！","错误！",JOptionPane.YES_OPTION);
			}
			else{
				file.delete();
				int index = -1;
				for(int i=0;i<Window.tabPane.getTabCount();i++){
					if(Window.tabPane.getToolTipTextAt(i).equals(place)){
						index = i;
						break;
					}
				}
				if(index>=0)
					Window.tabPane.remove(index);
				int location = Window.splitPane.getDividerLocation();
				Window.operateTree(location);
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "该文件夹中为根文件，不能删除！","错误！",JOptionPane.YES_OPTION);
		}
	}
	
	private boolean isInInfo(){
		String[] workplaces = null;
		try {
			Scanner in = new Scanner(new File("C:\\workplace\\info.txt"));
			while(in.hasNextLine()){
				String str = in.nextLine();
				workplaces = str.split(" ");
			}
			for(int i=0;i<workplaces.length;i++){
				if(place.equals("C:\\workplace\\"+workplaces[i])){
					return true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void setMouseEvent(MouseEvent event){
		this.event = event;
	}
}
