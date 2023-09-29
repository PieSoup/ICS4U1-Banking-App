package objects;

import main.Helpers;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private double chequingBal;
	private double savingsBal;
	private Date date;
	
	public User(String username, String password, double chequingBal, double savingsBal, Date date) {
		
		this.username = username;
		this.password = password;
		this.chequingBal = chequingBal;
		this.savingsBal = savingsBal;
		this.date = date;
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
	public void setDate(Date date){
		this.date = date;
	}
	public double addInterest(){
		Date currentTime = new Date();
		long diffMs = currentTime.getTime() - date.getTime();
		long diffSec = diffMs / 1000;
		long min = diffSec / 60;
		double bal = savingsBal;
		for(int i = 0; i < min; i++){
			bal *= 1.01;
		}
		return Helpers.round(bal, 2);
	}
}
