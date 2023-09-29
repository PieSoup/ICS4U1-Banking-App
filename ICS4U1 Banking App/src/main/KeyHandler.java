package main;
// Import necessary libraries
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Extend KeyAdapter to get the keys the user is pressing
public class KeyHandler extends KeyAdapter{

	private char[] allowedChars = {}; // Variable storing the allowed characters
	
	// Constructor to initialize the default allowed characters
	public KeyHandler(char[] ch) {
		this.allowedChars = ch;
	}
	
	// Method to set new allowed characters
	public void setChars(char[] chars) {
		allowedChars = chars;
	}
	// Method that runs whenever a key is pressed
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar(); // Get the key that was pressed

		// If the character is in the allowed characters array, then just return and allow it to be typed
		for(int i = 0; i < allowedChars.length; i++) {
			if(c == allowedChars[i]) {
				return;
			}
		}
		// If it is not in the array, then consume the event and play a beep noise
		java.awt.Toolkit.getDefaultToolkit().beep();
		e.consume();
	}
}