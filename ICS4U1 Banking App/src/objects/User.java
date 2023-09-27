package objects;

import java.io.Serializable;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private double chequingBal;
	private double savingsBal;
	
	public User(String username, String password, double chequingBal, double savingsBal) {
		
		this.username = username;
		this.password = password;
		this.chequingBal = chequingBal;
		this.savingsBal = savingsBal;
	}

	public double getChequingBal() {
		return chequingBal;
	}

	public void setChequingBal(double chequingBal) {
		this.chequingBal = chequingBal;
	}

	public double getSavingsBal() {
		return savingsBal;
	}

	public void setSavingsBal(double savingsBal) {
		this.savingsBal = savingsBal;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
