package movieBooking;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BookingManagerTest {

	@TempDir
	Path temporaryFolder;

	@Test
	void seatRowsDetermineTicketTypeAndPrice() {
		assertEquals("Standard", BookingManager.getSeatType("A1"));
		assertEquals("Recline", BookingManager.getSeatType("F8"));
		assertEquals(1000, BookingManager.getTicketPrice("Standard"));
		assertEquals(1500, BookingManager.getTicketPrice("Recline"));
		assertEquals(2500, BookingManager.getTotalPrice(new ArrayList<>(Arrays.asList("A1", "E1"))));
	}

	@Test
	void bookingSavesSeatsCreatesReferenceAndRecordsConfirmationEmail() throws Exception {
		File bookingFile = temporaryFolder.resolve("bookings.dat").toFile();
		BookingManager manager = new BookingManager(bookingFile);
		Showing showing = new Showing("Dune part 3", "Sci-Fi", LocalDate.now().plusDays(1), LocalTime.of(20, 0));
		ArrayList<String> seats = new ArrayList<>(Arrays.asList("A1", "E1"));

		Booking booking = manager.bookSeats("kian", "kian@example.com", showing, seats);

		assertTrue(booking.getReference().matches("[A-Z0-9]{8}"));
		assertEquals(2500, booking.getTotal());
		assertEquals(seats, manager.getBookedSeats(showing));
		String email = new String(Files.readAllBytes(temporaryFolder.resolve("bookings.dat.emails.txt")),
				StandardCharsets.UTF_8);
		assertTrue(email.contains("To: kian@example.com"));
		assertTrue(email.contains("Reference: " + booking.getReference()));
		assertTrue(email.contains("Film: Dune part 3"));
		assertTrue(email.contains("Seats: A1, E1"));
	}

	@Test
	void bookingRejectsASeatThatIsAlreadyBooked() throws Exception {
		BookingManager manager = new BookingManager(temporaryFolder.resolve("bookings.dat").toFile());
		Showing showing = new Showing("Street Fighter", "Action", LocalDate.now().plusDays(1), LocalTime.of(18, 30));
		manager.bookSeats("kian", "kian@example.com", showing, new ArrayList<>(Arrays.asList("A1")));

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> manager.bookSeats("another", "another@example.com", showing,
						new ArrayList<>(Arrays.asList("A1"))));
		assertEquals("Seat A1 is no longer available. Please choose again.", exception.getMessage());
	}

	@Test
	void savedBookingsOnlyIncludeTheRequestedUser() throws Exception {
		File bookingFile = temporaryFolder.resolve("bookings.dat").toFile();
		BookingManager manager = new BookingManager(bookingFile);
		Showing showing = new Showing("Dune part 3", "Sci-Fi", LocalDate.now().plusDays(1), LocalTime.of(20, 0));
		Booking aliceBooking = manager.bookSeats("alice", showing, new ArrayList<>(Arrays.asList("A1")));
		manager.bookSeats("bob", showing, new ArrayList<>(Arrays.asList("A2")));

		BookingManager reopenedManager = new BookingManager(bookingFile);
		ArrayList<Booking> savedBookings = reopenedManager.getBookingsForUser("alice");

		assertEquals(1, savedBookings.size());
		assertEquals(aliceBooking.getReference(), savedBookings.get(0).getReference());
	}

	@Test
	void emailValidationRejectsInvalidAddresses() {
		assertTrue(BookingManager.isValidEmail("customer@example.com"));
		assertFalse(BookingManager.isValidEmail("not-an-email"));
		assertFalse(BookingManager.isValidEmail("customer@"));
	}
}
