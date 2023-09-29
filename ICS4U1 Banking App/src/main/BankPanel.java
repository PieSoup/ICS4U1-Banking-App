package main;
// Import libraries
import objects.User;

import java.util.ArrayList;
import java.util.Date;

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
import java.text.DecimalFormat;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class BankPanel extends JPanel implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4279164923156128223L;
	
	// Variables for user management
	ArrayList<User> users = new ArrayList<User>();
	String userPath = "ICS4U1 Banking App/res/users.ser";
	
	// Variables for GUI functionality
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
	JButton withdrawal;
	JButton transfer;
	
	JLabel balance;
	JLabel amountLabel;
	JLabel recipient;
	JLabel accountType;


	JTextField accountTransfer;
	JButton submit;

	// Variables for keeping track of certain states and important information
	int currentUser = 0;
	BankState state = null;
	BankState prevState = null;
	double amountToTransfer;

	@SuppressWarnings("unchecked")
	public void Init() { // Method to initialize all of the GUI elements and deserialize the users

		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userPath));
			users = (ArrayList<User>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// Define and add the elements to the GUI
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
		
		recipient = new JLabel("Recipient:");
		add(recipient);

		accountTransfer = new JTextField(16);
		add(accountTransfer);

		accountType = new JLabel("Account transferring to:");
		add(accountType);

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
		amount.addKeyListener(new KeyHandler(new char[] {'0','1','2','3','4','5','6','7','8','9','.',}));
		add(amount);
		
		deposit = new JButton("Deposit");
		deposit.addActionListener(this);
		add(deposit);
		
		withdrawal = new JButton("Withdrawal");
		withdrawal.addActionListener(this);
		add(withdrawal);
		
		transfer = new JButton("Transfer");
		transfer.addActionListener(this);
		add(transfer);

		showLoginScreen(); // Show the login screen
	}
	private void showLoginScreen() { // Login screen method
		
		clearScreen(); // Clear the screen
		
		// Set all of the required elements to visible
		nameLabel.setVisible(true);
		nameInput.setVisible(true);
		passLabel.setVisible(true);
		passInput.setVisible(true);
		loginButton.setVisible(true);
		signupButton.setVisible(true);
		
	}
	private void showAccountScreen() { // Account screen method
		
		clearScreen(); // Clear the screen
		
		// Set all of the required elements to visible
		chequing.setVisible(true);
		savings.setVisible(true);
		
	}
	private void showUserScreen() { // User Interface screen method
		
		clearScreen(); // Clear the screen
		
		// Calculate interest since last time account has been viewed
		users.get(currentUser).setSavingsBal(users.get(currentUser).addInterest());
		users.get(currentUser).setDate(new Date());
		serializeUsers();

		// Set all of the required elements to visible
		this.balance.setVisible(true);
		amountLabel.setVisible(true);
		amount.setVisible(true);
		deposit.setVisible(true);
		withdrawal.setVisible(true);
		transfer.setVisible(true);
		// Set the balance value according to the account they are in
		if(state == BankState.Chequing) {
			// Chequing
			this.balance.setText(String.format("%-30s", "Balance: " + String.valueOf(new DecimalFormat("0.00").format((Helpers.round(users.get(currentUser).getChequingBal(), 2))))));
		}
		else if(state == BankState.Savings) {
			// Savings
			this.balance.setText(String.format("%-30s", "Balance: " + String.valueOf(new DecimalFormat("0.00").format(Helpers.round(users.get(currentUser).getSavingsBal(), 2)))));
		}

	}

	private void showTransferScreen(){ // Transfer money screen method

		clearScreen(); // Clear the screen

		// Set the requried elements to visible
		recipient.setVisible(true);
		accountTransfer.setVisible(true);
		accountType.setVisible(true);
		chequing.setVisible(true);
		savings.setVisible(true);

	}
	private void transfer(){ // Transfer method
		// Get the user they are trying to send money to
		for(User u : users){
			if(u.getUsername().equalsIgnoreCase(accountTransfer.getText())){
				// Send the money to the username and account that was specified and subtract the money from the current account
				state = prevState;
				if(state == BankState.Chequing){ 
					u.setChequingBal(u.getChequingBal() + amountToTransfer);
					users.get(currentUser).setChequingBal(users.get(currentUser).getChequingBal() - amountToTransfer);
				}
				else if(state == BankState.Savings){
					u.setSavingsBal(u.getSavingsBal() + amountToTransfer);
					users.get(currentUser).setSavingsBal(users.get(currentUser).getSavingsBal() - amountToTransfer);
				}
				serializeUsers(); // Reserialze the users to update the values
				showUserScreen(); // Go back to the main UI screen
			}
		}
	}
	private void clearScreen(){ // Clear screen method
		// Loop through all the components and set them to invisible
		for(Component cp : getComponents()) {
			cp.setVisible(false);
		}
	}
	private void serializeUsers(){ // Serialize users method
		try {
			// Write the users to the users.ser file using ObjectOutputStream
			FileOutputStream fos = new FileOutputStream(userPath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(users);
			oos.close();
		} catch (IOException e1) { // Catch any errors
			e1.printStackTrace();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) { // Method that is run whenever a button is pressed
		String s = e.getActionCommand();
		if(s.equals("Login")) {
			// When the login button is pressed, check the enter credentials and compare them to the current users
			for(User u : users) {
				if(nameInput.getText().equalsIgnoreCase(u.getUsername()) && passInput.getText().equals(u.getPassword())) {
					// If they match with a user, log them in and go to the account screen
					currentUser = users.indexOf(u);
					showAccountScreen();
				}
			}
		}
		else if(s.equals("Sign Up")) {
			// First check if the boxes are empty or if the username already exists and skip the code if so
			if(!nameInput.getText().equals("") && !passInput.getText().equals("")) {
				for(User u : users) {
					if(nameInput.getText().equals(u.getUsername())) {
						return;
					}
				}
				// Add the new user to the list of users and serialize the list
				users.add(new User(nameInput.getText(), passInput.getText(), 0, 0, new Date()));
				currentUser = users.size() - 1;
				serializeUsers();
				
				showAccountScreen(); // Show the account screen
			}
		}
		// Either transfer money or show the user screen depending on the current state
		// When showing the user screen, show the correct account (chequing or savings)
		else if(s.equals("Chequing")) {
			if(state == BankState.Transfer){
				transfer();
			}
			else{
				state = BankState.Chequing;
				showUserScreen();
			}
		}
		else if(s.equals("Savings")) {
			if(state == BankState.Transfer){
				transfer();
			}
			else{
				state = BankState.Savings;
				showUserScreen();
			}
		}
		else if(s.equals("Deposit")) {
			// When deposit button is pressed, add the entered money into the specifie account and update the text
			if(state == BankState.Chequing) {
				users.get(currentUser).setChequingBal(Double.parseDouble(amount.getText()) + users.get(currentUser).getChequingBal());
				balance.setText(String.format("%-30s", "Balance: " + String.valueOf(new DecimalFormat("0.00").format(Helpers.round(users.get(currentUser).getChequingBal(), 2)))));
			}
			else if(state == BankState.Savings){
				users.get(currentUser).setSavingsBal(Double.parseDouble(amount.getText()) + users.get(currentUser).getSavingsBal());
				balance.setText(String.format("%-30s", "Balance: " + String.valueOf(new DecimalFormat("0.00").format(Helpers.round(users.get(currentUser).getSavingsBal(), 2)))));
			}
			serializeUsers(); // Reserialize the users
		}
		else if(s.equals("Withdrawal")){
			// When the withdrawal button is pressed, remove money from the specified account and update the balance text
			if(state == BankState.Chequing){
				users.get(currentUser).setChequingBal(users.get(currentUser).getChequingBal() - Double.parseDouble(amount.getText()));
				balance.setText(String.format("%-30s", "Balance: " + String.valueOf(new DecimalFormat("0.00").format(Helpers.round(users.get(currentUser).getChequingBal(), 2)))));
			}
			else if(state == BankState.Savings){
				users.get(currentUser).setSavingsBal(users.get(currentUser).getSavingsBal() - Double.parseDouble(amount.getText()));
				balance.setText(String.format("%-30s", "Balance: " + String.valueOf(new DecimalFormat("0.00").format(Helpers.round(users.get(currentUser).getSavingsBal(), 2)))));
			}
			serializeUsers(); // Reserialize the users
		}
		else if(s.equals("Transfer")){
			// If the transfer button is pressed, set the state to transfer and show the transfer screen
			prevState = state;
			state = BankState.Transfer;
			amountToTransfer = Double.parseDouble(amount.getText()); // Also store the amount to transfer
			showTransferScreen();
		}
	}
}
