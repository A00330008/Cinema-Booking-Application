package movieBooking;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new CinemaGUI();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							"The saved accounts could not be loaded.\n"
							+ "Check that users.dat is available and readable.\n"
							+ "Your account file has not been replaced.",
							"Athlone Cinema", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
