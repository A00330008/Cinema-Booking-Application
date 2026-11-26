package movieBooking;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class BookingManager {

	private static final Object bookingLock = new Object();
	private File bookingFile;
	private File confirmationEmailFile;

	public BookingManager() {
		this(new File("bookings.dat"));
	}

	public BookingManager(File bookingFile) {
		this.bookingFile = bookingFile;
		confirmationEmailFile = new File(bookingFile.getPath() + ".emails.txt");
	}

	public static boolean isValidEmail(String emailAddress) {
		return emailAddress != null && emailAddress.trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
	}

	public static ArrayList<String> getSeats() {
		ArrayList<String> seats = new ArrayList<>();
		for (char row = 'A'; row <= 'F'; row++) {
			for (int number = 1; number <= 8; number++) {
				seats.add("" + row + number);
			}
		}
		return seats;
	}

	public static String getSeatType(String seat) {
		if (seat == null || !seat.matches("[A-F][1-8]")) {
			throw new IllegalArgumentException("That seat does not exist.");
		}
		return seat.charAt(0) <= 'D' ? "Standard" : "Recline";
	}

	public static int getTicketPrice(String ticketType) {
		// Prices are stored in cents so totals stay exact.
		if ("Standard".equals(ticketType)) return 1000;
		if ("Recline".equals(ticketType)) return 1500;
		throw new IllegalArgumentException("Please choose Standard or Recline tickets.");
	}

	public static String formatPrice(int amount) {
		return String.format(Locale.ROOT, "\u20ac%.2f", amount / 100.0);
	}

	public static int getTotalPrice(ArrayList<String> seats) {
		int total = 0;
		for (String seat : seats) {
			total += getTicketPrice(getSeatType(seat));
		}
		return total;
	}

	private ArrayList<Booking> loadBookings() throws IOException, ClassNotFoundException {
		ArrayList<Booking> bookings = new ArrayList<>();
		if (!bookingFile.exists()) return bookings;
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(bookingFile))) {
			Object saved = input.readObject();
			if (!(saved instanceof ArrayList<?>)) {
				throw new IOException("The booking file is not in the expected format.");
			}
			for (Object item : (ArrayList<?>) saved) {
				if (!(item instanceof Booking)) {
					throw new IOException("The booking file contains an invalid booking.");
				}
				bookings.add((Booking) item);
			}
		}
		return bookings;
	}

	private void saveBookings(ArrayList<Booking> bookings) throws IOException {
		File temporaryFile = new File(bookingFile.getPath() + ".tmp");
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(temporaryFile))) {
			output.writeObject(bookings);
		}
		try {
			Files.move(temporaryFile.toPath(), bookingFile.toPath(),
					StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
		} catch (AtomicMoveNotSupportedException ex) {
			Files.move(temporaryFile.toPath(), bookingFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	private ArrayList<String> bookedSeats(Showing showing, ArrayList<Booking> bookings) {
		ArrayList<String> seats = new ArrayList<>();
		for (Booking booking : bookings) {
			Showing saved = booking.getShowing();
			if (saved.getFilm().equals(showing.getFilm()) && saved.getDate().equals(showing.getDate())
					&& saved.getTime().equals(showing.getTime())) {
				seats.addAll(booking.getSeats());
			}
		}
		return seats;
	}

	public ArrayList<String> getBookedSeats(Showing showing) throws IOException, ClassNotFoundException {
		synchronized (bookingLock) {
			return bookedSeats(showing, loadBookings());
		}
	}

	public ArrayList<Booking> getBookingsForUser(String username) throws IOException, ClassNotFoundException {
		// CBA-6 Red stage: return an empty list until the lookup is implemented.
		return new ArrayList<>();
	}

	public Booking bookSeats(String username, Showing showing, ArrayList<String> seats)
			throws IOException, ClassNotFoundException {
		return bookSeats(username, "", showing, seats);
	}

	public Booking bookSeats(String username, String emailAddress, Showing showing, ArrayList<String> seats)
			throws IOException, ClassNotFoundException {
		String email = emailAddress == null ? "" : emailAddress.trim();
		if (username == null || username.trim().isEmpty()) {
			throw new IllegalArgumentException("Please log in before booking.");
		}
		if (!email.isEmpty() && !isValidEmail(email)) {
			throw new IllegalArgumentException("Enter a valid email address.");
		}
		if (showing == null || showing.getDate().isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Please choose a showing for today or a future date.");
		}
		if (seats == null || seats.isEmpty()) {
			throw new IllegalArgumentException("Please select at least one seat.");
		}
		ArrayList<String> selection = new ArrayList<>();
		for (String seat : seats) {
			getSeatType(seat);
			if (selection.contains(seat)) {
				throw new IllegalArgumentException("The same seat cannot be booked twice.");
			}
			selection.add(seat);
		}

		synchronized (bookingLock) {
			// Recheck under a file lock so two windows cannot confirm the same seat.
			try (RandomAccessFile lockFile = new RandomAccessFile(bookingFile.getPath() + ".lock", "rw");
					FileChannel channel = lockFile.getChannel(); FileLock lock = channel.lock()) {
				ArrayList<Booking> bookings = loadBookings();
				ArrayList<String> unavailable = bookedSeats(showing, bookings);
				for (String seat : selection) {
					if (unavailable.contains(seat)) {
						throw new IllegalArgumentException("Seat " + seat + " is no longer available. Please choose again.");
					}
				}
				Booking booking = new Booking(username.trim(), email, showing, selection);
				bookings.add(booking);
				saveBookings(bookings);
				if (!email.isEmpty()) saveConfirmationEmail(booking);
				return booking;
			}
		}
	}

	private void saveConfirmationEmail(Booking booking) throws IOException {
		String email = "To: " + booking.getEmailAddress() + System.lineSeparator()
				+ "Subject: Athlone Cinema booking confirmation" + System.lineSeparator()
				+ "Reference: " + booking.getReference() + System.lineSeparator()
				+ "Film: " + booking.getShowing().getFilm() + System.lineSeparator()
				+ "Cinema: Athlone Cinema, Screen 1" + System.lineSeparator()
				+ "Date: " + booking.getShowing().getDate() + System.lineSeparator()
				+ "Time: " + booking.getShowing().getTime() + System.lineSeparator()
				+ "Seats: " + String.join(", ", booking.getSeats()) + System.lineSeparator()
				+ "Total: " + formatPrice(booking.getTotal()) + System.lineSeparator()
				+ "----------------------------------------" + System.lineSeparator();
		Files.write(confirmationEmailFile.toPath(), email.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
				StandardOpenOption.APPEND);
	}
}
