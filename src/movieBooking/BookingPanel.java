package movieBooking;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalToggleButtonUI;

public class BookingPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Color background = new Color(245, 245, 249);
	private Color purple = new Color(88, 68, 132);
	private Color palePurple = new Color(239, 235, 247);
	private Color text = new Color(36, 36, 47);
	private Color line = new Color(223, 222, 232);
	private Color bookedColour = new Color(150, 151, 163);
	private Color error = new Color(165, 40, 52);
	private BookingManager bookingManager;
	private Showing showing;
	private String username;
	private boolean availabilityLoaded;
	private boolean confirmed;
	private ArrayList<String> selectedSeats = new ArrayList<>();
	private ArrayList<JToggleButton> seatButtons = new ArrayList<>();
	private CardLayout pages = new CardLayout();
	private JPanel seatMap = new JPanel(new GridBagLayout());
	private JLabel film = label(" ", 21);
	private JLabel genre = label(" ", 14);
	private JLabel date = label(" ", 14);
	private JLabel time = label(" ", 14);
	private JLabel standardCount = label("Standard: 0 x \u20ac10.00", 14);
	private JLabel reclineCount = label("Recline: 0 x \u20ac15.00", 14);
	private JLabel count = label("Seats selected: 0", 14);
	private JLabel total = label("Total: \u20ac0.00", 22);
	private JLabel message = label(" ", 13);
	private JTextArea seatNames = new JTextArea(3, 16);
	private JTextArea receipt = new JTextArea();
	private JButton confirmButton = button("Confirm booking", "confirmBooking", true);
	private JButton refreshButton = button("Refresh seats", "refreshSeats", false);

	public BookingPanel(BookingManager bookingManager, ActionListener backAction) {
		this.bookingManager = bookingManager;
		setLayout(pages);
		add(buildSeatScreen(backAction), "seats");
		add(buildConfirmation(backAction), "confirmed");
		confirmButton.addActionListener(this);
		refreshButton.addActionListener(this);
		confirmButton.setEnabled(false);
	}

	private JPanel buildSeatScreen(ActionListener backAction) {
		JPanel screen = new JPanel(new BorderLayout(20, 16));
		screen.setBackground(background);
		screen.setBorder(new EmptyBorder(20, 28, 20, 28));
		JPanel heading = new JPanel(new BorderLayout(0, 7));
		heading.setOpaque(false);
		heading.add(label("Your booking", 28), BorderLayout.NORTH);
		heading.add(label("2  Choose seats     >     3  Confirm booking", 13), BorderLayout.SOUTH);
		screen.add(heading, BorderLayout.NORTH);

		JPanel mapBox = new JPanel(new BorderLayout(0, 12));
		mapBox.setBackground(Color.WHITE);
		mapBox.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(line), new EmptyBorder(18, 16, 18, 16)));
		JLabel cinemaScreen = label("SCREEN", 14);
		cinemaScreen.setHorizontalAlignment(SwingConstants.CENTER);
		cinemaScreen.setOpaque(true);
		cinemaScreen.setBackground(palePurple);
		cinemaScreen.setBorder(new EmptyBorder(8, 0, 8, 0));
		mapBox.add(cinemaScreen, BorderLayout.NORTH);
		seatMap.setBackground(Color.WHITE);
		buildSeatMap();
		JScrollPane mapScroll = new JScrollPane(seatMap);
		mapScroll.setBorder(null);
		mapScroll.getVerticalScrollBar().setUnitIncrement(16);
		mapBox.add(mapScroll, BorderLayout.CENTER);
		JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		legend.setBackground(Color.WHITE);
		legend.add(legend("Available", palePurple));
		legend.add(legend("Selected", purple));
		legend.add(legend("Booked", bookedColour));
		mapBox.add(legend, BorderLayout.SOUTH);
		screen.add(mapBox, BorderLayout.CENTER);
		screen.add(buildSummary(), BorderLayout.EAST);

		JPanel bottom = new JPanel(new BorderLayout(0, 8));
		bottom.setOpaque(false);
		message.setName("seatMessage");
		bottom.add(message, BorderLayout.NORTH);
		JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		controls.setOpaque(false);
		JButton back = button("Back to showings", "backToShowings", false);
		back.addActionListener(backAction);
		controls.add(back);
		controls.add(refreshButton);
		bottom.add(controls, BorderLayout.SOUTH);
		screen.add(bottom, BorderLayout.SOUTH);
		return screen;
	}

	private JPanel buildSummary() {
		JPanel summary = new JPanel();
		summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
		summary.setBackground(Color.WHITE);
		summary.setPreferredSize(new Dimension(270, 0));
		summary.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(line), new EmptyBorder(14, 14, 14, 14)));
		seatNames.setName("selectedSeats");
		prepareTextArea(seatNames, 13);
		seatNames.setText("No seats selected");
		JScrollPane selected = new JScrollPane(seatNames);
		selected.setBorder(BorderFactory.createLineBorder(line));
		count.setName("seatCount");
		total.setName("bookingTotal");
		JComponent[] details = { film, genre, date, time, standardCount, reclineCount, count, selected, total, confirmButton };
		for (JComponent detail : details) {
			detail.setAlignmentX(Component.LEFT_ALIGNMENT);
			detail.setMaximumSize(new Dimension(Integer.MAX_VALUE, detail.getPreferredSize().height));
			summary.add(detail);
			summary.add(Box.createVerticalStrut(6));
		}
		return summary;
	}

	private void buildSeatMap() {
		int gridRow = 0;
		for (char row = 'A'; row <= 'F'; row++) {
			if (row == 'A' || row == 'E') {
				String section = row == 'A' ? "Standard - \u20ac10.00 per seat" : "Recline - \u20ac15.00 per seat";
				GridBagConstraints heading = new GridBagConstraints();
				heading.gridx = 0;
				heading.gridy = gridRow++;
				heading.gridwidth = 10;
				heading.insets = new Insets(5, 0, 5, 0);
				seatMap.add(label(section, 13), heading);
			}
			GridBagConstraints position = new GridBagConstraints();
			position.gridy = gridRow++;
			position.insets = new Insets(2, 3, 2, 3);
			position.gridx = 0;
			seatMap.add(label(String.valueOf(row), 14), position);
			for (int number = 1; number <= 8; number++) {
				if (number == 5) {
					position.gridx = 5;
					seatMap.add(Box.createHorizontalStrut(16), position);
				}
				String seat = "" + row + number;
				JToggleButton seatButton = new JToggleButton(seat);
				seatButton.setUI(new MetalToggleButtonUI() {
					@Override
					protected Color getSelectColor() {
						return purple;
					}
					@Override
					protected Color getDisabledTextColor() {
						return text;
					}
				});
				seatButton.setName("seat" + seat);
				seatButton.putClientProperty("seat", seat);
				seatButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
				seatButton.setPreferredSize(new Dimension(43, 30));
				seatButton.setBorder(BorderFactory.createLineBorder(line));
				seatButton.setOpaque(true);
				seatButton.setContentAreaFilled(true);
				seatButton.addActionListener(this);
				seatButtons.add(seatButton);
				position.gridx = number <= 4 ? number : number + 1;
				seatMap.add(seatButton, position);
			}
		}
	}

	private JPanel buildConfirmation(ActionListener backAction) {
		JPanel confirmation = new JPanel(new BorderLayout(0, 20));
		confirmation.setBackground(background);
		confirmation.setBorder(new EmptyBorder(24, 28, 28, 28));
		confirmation.add(label("Booking confirmed", 28), BorderLayout.NORTH);
		receipt.setName("bookingReceipt");
		prepareTextArea(receipt, 18);
		receipt.setBorder(new EmptyBorder(25, 25, 25, 25));
		JScrollPane receiptScroll = new JScrollPane(receipt);
		receiptScroll.setBorder(BorderFactory.createLineBorder(line));
		confirmation.add(receiptScroll, BorderLayout.CENTER);
		JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		controls.setOpaque(false);
		JButton next = button("Back to showings", "newBookingButton", false);
		next.addActionListener(backAction);
		controls.add(next);
		confirmation.add(controls, BorderLayout.SOUTH);
		return confirmation;
	}

	private void prepareTextArea(JTextArea area, int size) {
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setFont(new Font("Segoe UI", Font.PLAIN, size));
		area.setForeground(text);
		area.setBackground(Color.WHITE);
	}

	private JLabel label(String value, int size) {
		JLabel label = new JLabel(value);
		label.setFont(new Font("Segoe UI", Font.PLAIN, size));
		label.setForeground(text);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		label.setMaximumSize(new Dimension(Integer.MAX_VALUE, size + 12));
		return label;
	}

	private JButton button(String value, String name, boolean primary) {
		JButton button = new JButton(value);
		button.setName(name);
		button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		button.setForeground(primary ? Color.WHITE : purple);
		button.setBackground(primary ? purple : Color.WHITE);
		button.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(primary ? purple : line), new EmptyBorder(11, 16, 11, 16)));
		return button;
	}

	private JLabel legend(String value, Color colour) {
		JLabel label = label("  " + value + "  ", 11);
		label.setOpaque(true);
		label.setBackground(colour);
		label.setForeground(colour.equals(purple) ? Color.WHITE : text);
		label.setBorder(new EmptyBorder(5, 0, 5, 0));
		return label;
	}

	public void setShowing(Showing showing, String username) {
		this.showing = showing;
		this.username = username;
		confirmed = false;
		film.setText(showing.getFilm());
		genre.setText(showing.getGenre());
		date.setText("Date: " + showing.getDate().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")));
		time.setText("Time: " + showing.getTime());
		loadSeats();
		pages.show(this, "seats");
		revalidate();
		repaint();
	}

	private void loadSeats() {
		selectedSeats.clear();
		availabilityLoaded = false;
		ArrayList<String> booked = new ArrayList<>();
		message.setForeground(text);
		try {
			booked = bookingManager.getBookedSeats(showing);
			availabilityLoaded = true;
			message.setText("Select any available seats. The row determines the ticket type and price.");
		} catch (IOException | ClassNotFoundException ex) {
			message.setForeground(error);
			message.setText("Could not load seats. Check bookings.dat, then select Refresh seats.");
		}
		int availableCount = 0;
		for (JToggleButton button : seatButtons) {
			String seat = (String) button.getClientProperty("seat");
			boolean unavailable = booked.contains(seat);
			boolean available = availabilityLoaded && !unavailable;
			button.setSelected(false);
			button.setEnabled(available);
			button.setBackground(unavailable || !availabilityLoaded ? bookedColour : palePurple);
			button.setForeground(text);
			String state = !availabilityLoaded ? "Availability unknown" : unavailable ? "Booked" : "Available";
			String type = BookingManager.getSeatType(seat);
			String description = seat + " - " + type + " - "
					+ BookingManager.formatPrice(BookingManager.getTicketPrice(type)) + " - " + state;
			button.setToolTipText(description);
			button.getAccessibleContext().setAccessibleName("Seat " + description);
			if (available) availableCount++;
		}
		if (availabilityLoaded && availableCount == 0) {
			message.setText("No seats available for this showing. Please choose another showing.");
		}
		updateTotal();
	}

	private void updateTotal() {
		selectedSeats.clear();
		int standard = 0;
		int recline = 0;
		for (JToggleButton button : seatButtons) {
			if (button.isEnabled() && button.isSelected()) {
				String seat = (String) button.getClientProperty("seat");
				selectedSeats.add(seat);
				if (BookingManager.getSeatType(seat).equals("Standard")) standard++;
				else recline++;
			}
		}
		count.setText("Seats selected: " + selectedSeats.size());
		standardCount.setText("Standard: " + standard + " x " + BookingManager.formatPrice(BookingManager.getTicketPrice("Standard")));
		reclineCount.setText("Recline: " + recline + " x " + BookingManager.formatPrice(BookingManager.getTicketPrice("Recline")));
		seatNames.setText(selectedSeats.isEmpty() ? "No seats selected" : String.join(", ", selectedSeats));
		total.setText("Total: " + BookingManager.formatPrice(BookingManager.getTotalPrice(selectedSeats)));
		confirmButton.setEnabled(availabilityLoaded && !confirmed && !selectedSeats.isEmpty());
	}

	private void confirmBooking() {
		if (!confirmButton.isEnabled() || confirmed) return;
		try {
			Booking booking = bookingManager.bookSeats(username, showing, selectedSeats);
			confirmed = true;
			confirmButton.setEnabled(false);
			receipt.setText("Reference: " + booking.getReference() + "\n\n" + showing.getFilm() + "\n"
					+ "Date: " + showing.getDate().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")) + "\n"
					+ "Time: " + showing.getTime() + "\n\n" + "Ticket types: " + booking.getTicketType() + "\n"
					+ "Seats: " + String.join(", ", booking.getSeats()) + "\n"
					+ "Total: " + BookingManager.formatPrice(booking.getTotal()));
			receipt.setCaretPosition(0);
			pages.show(this, "confirmed");
		} catch (IllegalArgumentException ex) {
			loadSeats();
			message.setForeground(error);
			message.setText(ex.getMessage());
		} catch (IOException | ClassNotFoundException ex) {
			loadSeats();
			message.setForeground(error);
			message.setText("Could not save your booking. Check bookings.dat and try again.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == confirmButton) {
			confirmBooking();
		} else if (source == refreshButton) {
			if (showing != null && !confirmed) loadSeats();
		} else if (source instanceof JToggleButton) {
			JToggleButton button = (JToggleButton) source;
			button.setBackground(button.isSelected() ? purple : palePurple);
			button.setForeground(button.isSelected() ? Color.WHITE : text);
			updateTotal();
		}
	}
}
