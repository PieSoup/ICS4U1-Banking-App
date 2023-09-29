package main;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {
	
	public static JFrame window;
	
	public static void main(String[] args) {
		
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setPreferredSize(new Dimension(250, 200));
		window.setTitle("Epic Banking App");
		
		BankPanel panel = new BankPanel();
		window.add(panel);
		panel.Init();
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
	}
}
