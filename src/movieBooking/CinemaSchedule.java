package movieBooking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;

public class CinemaSchedule {

	private ArrayList<Showing> showings = new ArrayList<>();

	public CinemaSchedule() {
		this(LocalDate.now());
	}

	public CinemaSchedule(LocalDate firstDate) {
		// Sample timetable for the next two weeks, starting when the program opens.
		for (int day = 0; day < 14; day++) {
			LocalDate date = firstDate.plusDays(day);
			addShowings("Dune part 3", "Sci-Fi", date, new String[] { "14:00", "17:00", "20:00" });
			if (day % 2 == 0) {
				addShowings("Resident Evil", "Horror", date, new String[] { "16:30", "19:00", "21:30" });
			}
			addShowings("Street Fighter", "Action", date, new String[] { "13:30", "16:00", "18:30" });
			addShowings("Avengers Doomsday", "Superhero", date, new String[] { "14:30", "17:30", "20:30" });
			if (day % 2 == 0) {
				addShowings("Primetime", "Drama", date, new String[] { "15:00", "17:45", "20:15" });
			}
		}
	}

	private void addShowings(String film, String genre, LocalDate date, String[] times) {
		for (String time : times) {
			showings.add(new Showing(film, genre, date, LocalTime.parse(time)));
		}
	}

	public ArrayList<Showing> findShowings(LocalDate date) {
		return findShowings(date, "");
	}

	public ArrayList<Showing> findShowings(LocalDate date, String filmName) {
		String searchName = filmName.trim().toLowerCase(Locale.ROOT);
		ArrayList<Showing> results = new ArrayList<>();
		for (Showing showing : showings) {
			if (showing.getDate().equals(date) && showing.getFilm().toLowerCase(Locale.ROOT).contains(searchName)) {
				results.add(showing);
			}
		}
		return results;
	}
}
