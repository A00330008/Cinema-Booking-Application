package movieBooking;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BookingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Color purple = new Color(88, 68, 132);
	private Color text = new Color(36, 36, 47);
	private JLabel film = label(" ", 26);
	private JLabel genre = label(" ", 15);
	private JLabel date = label(" ", 18);
	private JLabel time = label(" ", 18);
	private JLabel ticket = label(" ", 18);

	public BookingPanel(ActionListener backAction) {
		setLayout(new BorderLayout(0, 24));
		setBackground(new Color(245, 245, 249));
		setBorder(new EmptyBorder(24, 28, 28, 28));
		add(label("Your booking", 28), BorderLayout.NORTH);

		JPanel details = new JPanel();
		details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
		details.setBackground(Color.WHITE);
		details.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(223, 222, 232)), new EmptyBorder(30, 30, 30, 30)));
		details.add(label("Selected showing", 16));
		details.add(Box.createVerticalStrut(20));
		details.add(film);
		details.add(Box.createVerticalStrut(8));
		details.add(genre);
		details.add(Box.createVerticalStrut(30));
		details.add(date);
		details.add(Box.createVerticalStrut(15));
		details.add(time);
		details.add(Box.createVerticalStrut(15));
		details.add(ticket);
		add(details, BorderLayout.CENTER);

		JButton back = new JButton("Back to showings");
		back.setName("backToShowings");
		back.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		back.setForeground(purple);
		back.setBackground(Color.WHITE);
		back.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(purple), new EmptyBorder(12, 20, 12, 20)));
		back.addActionListener(backAction);
		JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		controls.setOpaque(false);
		controls.add(back);
		add(controls, BorderLayout.SOUTH);
	}

	private JLabel label(String value, int size) {
		JLabel label = new JLabel(value);
		label.setFont(new Font("Segoe UI", Font.PLAIN, size));
		label.setForeground(text);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		label.setMaximumSize(new Dimension(Integer.MAX_VALUE, size + 12));
		return label;
	}

	public void setShowing(Showing showing, String ticketType) {
		film.setText(showing.getFilm());
		genre.setText(showing.getGenre());
		date.setText("Date: " + showing.getDate().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
		time.setText("Time: " + showing.getTime());
		ticket.setText("Ticket type: " + ticketType);
		revalidate();
		repaint();
	}
}
