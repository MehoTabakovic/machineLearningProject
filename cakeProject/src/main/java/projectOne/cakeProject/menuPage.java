package projectOne.cakeProject;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




public class menuPage extends JFrame{

	
	menuPage(){
		
		//font
		Font f=new Font("Serif",Font.BOLD,30);
		
		
		//header
		JPanel heading;
		heading=new JPanel();

		heading.setBackground(new Color(255,255,255,30));
		heading.setBounds(0,0,900,60);
		JLabel name=new JLabel("Cake Shop Analyzer");
		name.setForeground(Color.WHITE);
		name.setBounds(200,50,400,50);
		name.setFont(f);
		heading.add(name);
		
		
		//panel  250,
		
		JPanel mainPanel=new JPanel();
		mainPanel.setLayout(null);
		setSize(400,350);
		mainPanel.setBackground(new Color(255,255,255,30));
		mainPanel.setBounds(420,140,400,350);
		
		//panel-username
		JButton option1=new JButton("Cake shop insights");
		option1.setBounds(50,50,300,50);
		option1.setForeground(Color.WHITE);
		option1.setBackground(new Color(210,180,140));
		mainPanel.add(option1);
		
		//panel-password
		JButton option2=new JButton("Average cake prices");
		option2.setBounds(50,150,300,50);
		option2.setBackground(new Color(210,180,140));
		option2.setForeground(Color.WHITE);
		mainPanel.add(option2);
		
	
				
		option1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new searchFiltersPage().setVisible(true);
						menuPage.menuPage.setVisible(false);
					}
				});
				
		option2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new priceFilter().setVisible(true);
						menuPage.menuPage.setVisible(false);
					}
				});
		
		JLabel text=new JLabel("App covers cake shops in the UK");
		text.setBounds(110,200,300,50);
		text.setForeground(Color.WHITE);
		mainPanel.add(text);

		//frame
		setSize(900,600);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//background
		ImageIcon background_image=new ImageIcon(Toolkit.getDefaultToolkit().getImage("./src/main/resources/images/newPozadina.jpg"));
		Image img=background_image.getImage();
		Image temp_image=img.getScaledInstance(900,600, Image.SCALE_SMOOTH);
		background_image=new ImageIcon(temp_image);
		JLabel background=new JLabel("",background_image,JLabel.CENTER);
		background.setBounds(0,0,900,600);
		background.add(mainPanel);
		background.add(heading);
		add(background);
		
		setVisible(true);
		
	}
	public static menuPage menuPage = new menuPage();
	public static void main(String args[]) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menuPage.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
