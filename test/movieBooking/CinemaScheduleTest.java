package movieBooking;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class CinemaScheduleTest {

	@Test
	void searchFindsFilmNamesWithoutCaringAboutCapitalLetters() {
		LocalDate date = LocalDate.of(2026, 11, 20);
		CinemaSchedule schedule = new CinemaSchedule(date);

		ArrayList<Showing> results = schedule.findShowings(date, "DUNE");

		assertEquals(3, results.size());
		for (Showing showing : results) {
			assertEquals("Dune part 3", showing.getFilm());
		}
	}

	@Test
	void searchOnlyReturnsShowingsOnTheSelectedDate() {
		LocalDate firstDate = LocalDate.of(2026, 11, 20);
		CinemaSchedule schedule = new CinemaSchedule(firstDate);
		LocalDate requestedDate = firstDate.plusDays(1);

		ArrayList<Showing> results = schedule.findShowings(requestedDate, "Street Fighter");

		assertEquals(3, results.size());
		for (Showing showing : results) {
			assertEquals(requestedDate, showing.getDate());
			assertEquals("Street Fighter", showing.getFilm());
		}
	}

	@Test
	void searchReturnsNothingForAnUnavailableFilm() {
		CinemaSchedule schedule = new CinemaSchedule(LocalDate.of(2026, 11, 20));

		assertTrue(schedule.findShowings(LocalDate.of(2026, 11, 20), "Nonexistent Film").isEmpty());
	}

    @Test
    void genreFilterReturnsOnlyFilmsInTheRequestedGenre() {
        LocalDate date = LocalDate.of(2026, 11, 20);
        CinemaSchedule schedule = new CinemaSchedule(date);

        ArrayList<Showing> results = schedule.findShowingsByGenre(date, "Horror");

        assertEquals(3, results.size());
        for (Showing showing : results) {
            assertEquals("Horror", showing.getGenre());
        }
    }
}
