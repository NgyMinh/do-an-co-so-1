package main;

import javax.swing.SwingUtilities;
public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				LoginFrame loginFrame = new LoginFrame();
				loginFrame.setVisible(true);
			}
		});
	}
}