package movieBooking;

import java.awt.Dimension;
import java.io.IOException;
import javax.swing.*;

public class CinemaGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	public CinemaGUI() throws IOException, ClassNotFoundException {
		super("Athlone Cinema");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(new CinemaPanel(new UserManager()));
		pack();
		setMinimumSize(new Dimension(940, 750));
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
