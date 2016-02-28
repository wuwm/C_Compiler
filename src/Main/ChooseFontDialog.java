package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

public class ChooseFontDialog extends JDialog{
	
	private Integer[] FontSize = new Integer[]{
			10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40};
	private Color[] FontColor = new Color[]{
			Color.BLACK,Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW};
	private String[] ColorStr = new String[]{
			"黑色","蓝色","红色","绿色","黄色"
	};
	private String[] style = new String[]{"正常","加粗","倾斜","加粗、倾斜"};
	
	private JComboBox cb1 = null;
	private JComboBox cb2 = null;
	private JComboBox cb3 = null;
	
	private JButton OK = null;
	private JButton Canel = null;
	
	private Container container = null;
	
	private Box mainBox = Box.createVerticalBox();
	private Box box1 = Box.createHorizontalBox();
	private Box box2 = Box.createHorizontalBox();
	private Box box3 = Box.createHorizontalBox();
	private Box box4 = Box.createHorizontalBox();

	public ChooseFontDialog(){
		container = getContentPane();
		
		OK = new JButton("确定");
		Canel = new JButton("取消");
		
		cb1 = new JComboBox(FontSize);
		cb2 = new JComboBox(ColorStr);
		cb3 = new JComboBox(style);
		cb1.setBorder(BorderFactory.createTitledBorder("选择字体的大小："));
		cb2.setBorder(BorderFactory.createTitledBorder("选择字体的颜色："));
		cb3.setBorder(BorderFactory.createTitledBorder("选择字体的风格："));
		
		box1.add(Box.createHorizontalStrut(20));
		box1.add(cb1);
		box1.add(Box.createHorizontalStrut(20));
		
		box2.add(Box.createHorizontalStrut(20));
		box2.add(cb2);
		box2.add(Box.createHorizontalStrut(20));
		
		box3.add(Box.createHorizontalStrut(20));
		box3.add(cb3);
		box3.add(Box.createHorizontalStrut(20));
		
		box4.add(Box.createHorizontalGlue());
		box4.add(OK);
		box4.add(Box.createHorizontalStrut(10));
		box4.add(Canel);
		box4.add(Box.createHorizontalGlue());
		
		mainBox.add(Box.createVerticalStrut(10));
		mainBox.add(box1);
		mainBox.add(Box.createVerticalStrut(10));
		mainBox.add(box2);
		mainBox.add(Box.createVerticalStrut(10));
		mainBox.add(box3);
		mainBox.add(Box.createVerticalStrut(20));
		mainBox.add(box4);
		mainBox.add(Box.createVerticalStrut(10));
		
		container.add(mainBox,BorderLayout.CENTER);
		setAlwaysOnTop(true);
		setResizable(false);
		setBounds(500, 250, 300, 320);
		setVisible(true);
		
		setAction();
	}
	
	private void setAction(){
		OK.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				changeFont();
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
	
	private void Stop(){
		dispose();
	}
	
	private void changeFont(){
		int size = (int)cb1.getSelectedItem();
		Color color = FontColor[cb2.getSelectedIndex()];
		int style =(int)cb3.getSelectedIndex();
		myTextArea.color = color;
		myTextArea.font = myTextArea.font.deriveFont(style,(float)size);
		for(int i=0;i<Window.tabPane.getComponentCount();i++){
			myScrollPane sp = (myScrollPane)Window.tabPane.getComponent(i);
			myTextArea ta = sp.getTextArea();
			ta.changeFont();
		}
	}
}
