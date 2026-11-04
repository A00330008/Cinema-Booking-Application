package movieBooking;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Showing implements Serializable {

	private static final long serialVersionUID = 1L;

	private String film;
	private String genre;
	private LocalDate date;
	private LocalTime time;

	public Showing(String film, String genre, LocalDate date, LocalTime time) {
		this.film = film;
		this.genre = genre;
		this.date = date;
		this.time = time;
	}

	public String getFilm() {
		return film;
	}

	public String getGenre() {
		return genre;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getTime() {
		return time.format(DateTimeFormatter.ofPattern("HH:mm"));
	}
}
