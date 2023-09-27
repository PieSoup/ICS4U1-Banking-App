package main;

import objects.User;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class BankPanel extends JPanel implements ActionListener, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4279164923156128223L;
	
	Set<User> users = new HashSet<>();
	String userPath = "res/users.ser";
	
	JTextField nameInput;
	JTextField passInput;
	
	JLabel nameLabel;
	JLabel passLabel;
	JLabel infoLabel;
	
	JButton loginButton;
	JButton signupButton;
	
	
	JTextField amount;
	
	JButton chequing;
	JButton savings;
	
	JButton deposit;
	JButton withdrawl;
	
	JLabel balance;
	JLabel amountLabel;
	
	int currentUser = 0;
	BankState state = null;
	
	
	
	public void Init() {
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userPath));
			users = (HashSet<User>) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		for(User u : users) {
			System.out.println(u.getUsername());
		}
		nameLabel = new JLabel("Username:");
		add(nameLabel);
		
		nameInput = new JTextField(16);
		add(nameInput);
		
		passLabel = new JLabel("Password:");
		add(passLabel);
		
		passInput = new JTextField(16);
		add(passInput);
		
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		add(loginButton);
		
		signupButton = new JButton("Sign Up");
		signupButton.addActionListener(this);
		add(signupButton);
		
		infoLabel = new JLabel();
		add(infoLabel);
		
		chequing = new JButton("Chequing");
		chequing.addActionListener(this);
		add(chequing);
		
		savings = new JButton("Savings");
		savings.addActionListener(this);
		add(savings);
		
		balance = new JLabel();
		balance.setFont(new Font("Verdana", Font.BOLD, 16));
		add(balance);
		
		amountLabel = new JLabel("Amount:");
		add(amountLabel);
		
		amount = new JTextField(12);
		add(amount);
		
		deposit = new JButton("Deposit");
		add(deposit);
		
		withdrawl = new JButton("Withdrawl");
		add(withdrawl);
		
		showLoginScreen();
	}
	private void showLoginScreen() {
		
		for(Component cp : getComponents()) {
			cp.setVisible(false);
		}
		
		nameLabel.setVisible(true);
		nameInput.setVisible(true);
		passLabel.setVisible(true);
		passInput.setVisible(true);
		loginButton.setVisible(true);
		signupButton.setVisible(true);
		
		
		
	}
	private void showAccountScreen() {
		
		for(Component cp : getComponents()) {
			cp.setVisible(false);
		}
		
		chequing.setVisible(true);
		savings.setVisible(true);
		
	}
	private void showUserScreen(double cBalance, double sBalance) {
		
		for(Component cp : getComponents()) {
			cp.setVisible(false);
		}
		
		this.balance.setVisible(true);
		amountLabel.setVisible(true);
		amount.setVisible(true);
		deposit.setVisible(true);
		withdrawl.setVisible(true);
		if(state == BankState.Chequing) {
			this.balance.setText("Balance: " + String.valueOf(cBalance));
		}
		else if(state == BankState.Savings) {
			this.balance.setText("Balance: " + String.valueOf(sBalance));
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		if(s.equals("Login")) {
			for(User u : users) {
				if(nameInput.getText().equalsIgnoreCase(u.getUsername()) && passInput.getText().equalsIgnoreCase(u.getPassword())) {
					//currentUser = u;
					showAccountScreen();
				}
			}
		}
		else if(s.equals("Sign Up")) {
			if(!nameInput.getText().equals("") && !passInput.getText().equals("")) {
				for(User u : users) {
					if(nameInput.getText().equals(u.getUsername())) {
						return;
					}
				}
				users.add(new User(nameInput.getText(), passInput.getText(), 0, 0));
				currentUser = users.size() - 1;
				try {
					FileOutputStream fos = new FileOutputStream(userPath);
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(users);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				showAccountScreen();
			}
		}
		else if(s.equals("Chequing")) {
			state = BankState.Chequing;
			//showUserScreen(currentUser.getChequingBal(), currentUser.getSavingsBal());
		}
		else if(s.equals("Savings")) {
			state = BankState.Savings;
			//showUserScreen(currentUser.getChequingBal(), currentUser.getSavingsBal());
		}
		else if(s.equals("Deposit")) {
			if(state == BankState.Chequing) {
				
			}
		}
	}
	
	
}
