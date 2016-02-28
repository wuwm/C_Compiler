package Main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTree;

public class OpenWorkPlaceDialog extends JDialog{
	
	private Container container = null;
	private JLabel label = null;
	private JComboBox cb = null;
	private JButton OK = null;
	private JButton Canel = null;
	private String[] workplaces = null;
	private int model;
	
	private static String file = "C:\\workplace\\info.txt";
	
	public static int OPEN = 1;
	public static int DELETE = 2;
	
	public OpenWorkPlaceDialog(int model){
		super();
		Init();
		this.model = model;
		container = getContentPane();
		label = new JLabel("选择要打开的工作空间：");
		OK = new JButton("确定");
		Canel = new JButton("取消");
		
		if(workplaces == null){
			workplaces = new String[]{""};
		}
		
		cb = new JComboBox(workplaces);
		if(model == OpenWorkPlaceDialog.OPEN)
			cb.setBorder(BorderFactory.createTitledBorder("选择要打开的工作空间："));
		if(model == OpenWorkPlaceDialog.DELETE)	
			cb.setBorder(BorderFactory.createTitledBorder("选择要删除的工作空间："));
		
		Box box1 = Box.createHorizontalBox();
		box1.add(Box.createHorizontalStrut(20));
		box1.add(cb);
		box1.add(Box.createHorizontalStrut(20));
		
		Box box2 = Box.createHorizontalBox();
		box2.add(Box.createHorizontalGlue());
		box2.add(OK);
		box2.add(Box.createHorizontalStrut(10));
		box2.add(Canel);
		box2.add(Box.createHorizontalGlue());
		
		Box mainBox = Box.createVerticalBox();
		mainBox.add(Box.createVerticalStrut(10));
		mainBox.add(box1);
		mainBox.add(Box.createVerticalStrut(20));
		mainBox.add(box2);
		mainBox.add(Box.createVerticalStrut(10));
		
		container.add(mainBox,BorderLayout.CENTER);
		setAlwaysOnTop(true);
		setResizable(false);
		setBounds(500, 250, 300, 170);
		setVisible(true);
		
		OK.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				OpenOrDelete();
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
	
	private void OpenOrDelete(){
		String w = cb.getSelectedItem().toString();
		if(!w.equals("")){
			int index = Window.splitPane.getDividerLocation();
			switch(model){
			case 1:
				Window.operateTree(index);
				break;
			case 2:
				String place = "C:\\workplace\\"+w;
				File file = new File(place);
				int choosen = 0;
				if(file.listFiles().length!=0){
					choosen = JOptionPane.showConfirmDialog(
							this, "该目录包含其他文件，确定删除？", "提示", JOptionPane.YES_NO_OPTION);
				}
				if(choosen == 0){
					System.out.println(place);
					deleteFile(file);
					Window.operateTree(index);
					try {
						FileOutputStream fileOut = new FileOutputStream("C:\\workplace\\info.txt",false);
						fileOut.write("".getBytes());
						fileOut.close();
						FileOutputStream fileOut1 = new FileOutputStream("C:\\workplace\\info.txt",true);
						for(int i=0;i<workplaces.length;i++){
							System.out.println(workplaces[i]);
							if(!workplaces[i].equals(w)){
								fileOut1.write(workplaces[i].getBytes());
								fileOut1.write(' ');
							}
						}
						fileOut1.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			}
		}
		Stop();
	}
	
	private void deleteFile(File file){
		File[] files = file.listFiles();
		if(files.length != 0){
			for(int i=0;i<files.length;i++){
				if(files[i].isDirectory())
					deleteFile(files[i]);
				else
					files[i].delete();
			}
		}
		file.delete();	
	}
	
	private void Stop(){
		this.dispose();
	}
	
	private void Init(){
		try {
			Scanner in = new Scanner(new File(file));
			while(in.hasNextLine()){
				String str = in.nextLine();
				workplaces = str.split(" ");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
