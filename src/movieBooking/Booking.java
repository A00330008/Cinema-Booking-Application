package movieBooking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class Booking implements Serializable {

	private static final long serialVersionUID = 1L;
	private String reference;
	private String username;
	private String emailAddress;
	private Showing showing;
	private String ticketType;
	private ArrayList<String> seats;
	private int ticketPrice;
	private int totalPrice;

	public Booking(String username, Showing showing, ArrayList<String> seats) {
		this(username, "", showing, seats);
	}

	public Booking(String username, String emailAddress, Showing showing, ArrayList<String> seats) {
		reference = UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
		this.username = username;
		this.emailAddress = emailAddress;
		this.showing = showing;
		this.seats = new ArrayList<>(seats);
		totalPrice = BookingManager.getTotalPrice(seats);
		boolean standard = false;
		boolean recline = false;
		for (String seat : seats) {
			if (BookingManager.getSeatType(seat).equals("Standard")) standard = true;
			else recline = true;
		}
		ticketType = standard && recline ? "Standard and Recline" : standard ? "Standard" : "Recline";
	}

	public String getReference() {
		return reference;
	}

	public String getUsername() {
		return username;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public Showing getShowing() {
		return showing;
	}

	public String getTicketType() {
		return ticketType;
	}

	public ArrayList<String> getSeats() {
		return new ArrayList<>(seats);
	}

	public int getTotal() {
		// Older saved bookings used one ticket price for every seat.
		return totalPrice > 0 ? totalPrice : seats.size() * ticketPrice;
	}
}
