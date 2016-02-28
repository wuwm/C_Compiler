package Main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class InputWorkPlaceDialog extends JDialog{
	
	public static  int PROJECT = 1;
	public static int FILE = 2;
	public static int DIRECTORY = 3;
	
	private JTextField tf = null;
	private JTextField tf1 = null;
	private JLabel label = null;
	private JLabel label1 = null;
	private JButton OK = null;
	private JButton Canel = null;
	
	private Container container = null;
	
	private String name = "";
	private String filepath = null;
	private int model;
	
	private Box mainBox = Box.createVerticalBox();
	private Box box1 = Box.createHorizontalBox();
	private Box box2 = Box.createHorizontalBox();
	private Box box3 = Box.createHorizontalBox();
	private Box boxa = Box.createHorizontalBox();
	private Box boxb = Box.createHorizontalBox();

	public InputWorkPlaceDialog(int model,String path){
		super();
		this.model = model;
		this.filepath = path;
		container = getContentPane();
		initLabel(model);
		
		tf = new JTextField(10);
		tf1 = new JTextField(10);
		OK = new JButton("确定");
		Canel = new JButton("取消");
				
		box1.add(Box.createHorizontalStrut(20));
		box1.add(label);
		box1.add(Box.createHorizontalGlue());
		
		box2.add(Box.createHorizontalStrut(20));
		box2.add(tf);
		box2.add(Box.createHorizontalStrut(20));
		
		box3.add(Box.createHorizontalGlue());
		box3.add(OK);
		box3.add(Box.createHorizontalStrut(10));
		box3.add(Canel);
		box3.add(Box.createHorizontalGlue());
		
		Model();
		SetAction();
	}
	
	private void SetAction(){
		OK.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				name = tf.getText();
				if(name.equals("")||name == null){
					System.out.println("名称不能为空！");
				}
				else{
					Path path = Paths.get(filepath+name);
					int index = Window.splitPane.getDividerLocation();
					try{
						switch(model){
						case 1:
							Files.createDirectory(path);
							Path p = Paths.get(filepath+name+"\\"+tf1.getText());
							Files.createFile(p);
							writeInfo(name);
							Window.operateTree(index);
							break;
						case 2:
							Files.createFile(path);
							Window.operateTree(index);
							break;
						case 3:
							Files.createDirectory(path);
							Window.operateTree(index);
							break;
						}
					}
					catch(IOException e){
						System.out.println("名称重复，创建失败，请重新创建！");
					}
				}
				Stop();
			}
			
		});
		
		Canel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Stop();
			}
			
		});
	}
	
	private void initLabel(int model){
		if(model == PROJECT){
			label = new JLabel("请输入工程名称：");
			label1 = new JLabel("请输入主文件名：");
		}
		else if(model ==FILE){
			label = new JLabel("请输入文件名称：");
		}
		else if(model == DIRECTORY){
			label = new JLabel("请输入目录名称：");
		}
	}
	
	private void Model(){
		
		mainBox.add(Box.createVerticalStrut(10));
		mainBox.add(box1);
		mainBox.add(box2);
		if(model == PROJECT){
			boxa.add(Box.createHorizontalStrut(20));
			boxa.add(label1);
			boxa.add(Box.createHorizontalGlue());
				
			boxb.add(Box.createHorizontalStrut(20));
			boxb.add(tf1);
			boxb.add(Box.createHorizontalStrut(20));
			
			mainBox.add(Box.createVerticalStrut(10));
			mainBox.add(boxa);
			mainBox.add(boxb);
		}
		mainBox.add(Box.createVerticalStrut(20));
		mainBox.add(box3);
		mainBox.add(Box.createVerticalStrut(10));
		
		container.add(mainBox,BorderLayout.CENTER);
		setAlwaysOnTop(true);
		setResizable(false);
		if(model == PROJECT){
			setBounds(500, 250, 300, 220);
		}
		else{
			setBounds(500, 250, 300, 140);
		}
		setVisible(true);
	}
	
	private void Stop(){
		this.dispose();
	}
	
	private void writeInfo(String name){
		try {
			FileOutputStream fileOut = new FileOutputStream("C:\\workplace\\info.txt",true);
			fileOut.write(name.getBytes());
			fileOut.write(' ');
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
