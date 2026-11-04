# Athlone Cinema

This is my Cinema Booking Application project for college. I'm building it in Java using Eclipse and Swing for the GUI.

## Progress so far

For Sprint 1 (CBA-1), I've added the basic GUI and the login and sign-up screens. Users can create an account, log in and log out. Account details are saved in a file called `users.dat`, so they can log in again after closing the program.

The forms check for missing details, usernames that are already taken and passwords that don't match.

For CBA-2, I've added a search by date. After logging in, the user sees today's films and showing times. They can type a date in dd/mm/yyyy format or use the arrows beside the date, then select Search. Invalid dates and dates in the past aren't accepted.

There is also a movie name search. Typing part of a name, such as "dune" or "avengers", finds matching films for the selected date. Capital letters don't matter. Leaving the name blank shows all films for that day. Press Enter or select Search to see the results.

The user can select a showing time and select Continue to choose their seats.

For CBA-3, I've added a seat map with 48 seats. Rows A-D are Standard at EUR10 per seat and rows E-F are Recline at EUR15 per seat. The map shows available and booked seats, with an aisle down the middle. Clicking a seat chooses its ticket type automatically. Users can select seats from either section in the same booking. The summary shows the number of each type selected and the total updates as seats are selected or deselected.

Selecting Confirm booking saves the booking and shows a summary. Booked seats stay unavailable for that film, date and time, including after restarting the program. Going back without confirming doesn't reserve any seats. Availability is checked again when confirming, so a seat taken by another booking can't be booked twice.

Bookings are stored locally in `bookings.dat`. This file and its temporary and lock files are excluded from Git. A file lock keeps two running copies of the program from confirming the same seat at the same time. If the booking file can't be read or saved, the screen displays an error and doesn't confirm a booking. Refresh seats reloads availability and clears the current selection.

## Films

- Dune part 3 (Sci-Fi)
- Resident Evil (Horror)
- Street Fighter (Action)
- Avengers Doomsday (Superhero)
- Primetime (Drama)

The showing times are examples and use the 24-hour format. The sample timetable covers 14 days starting from the day the application opens. Dune part 3, Street Fighter and Avengers Doomsday show every day. Resident Evil and Primetime show on alternate days, starting with the first day. Searching outside this timetable shows a message saying there are no showings.

## Running the application

1. Open the project in Eclipse. If changes aren't showing, select the project and press F5.
2. Open `Main.java` in the `movieBooking` package.
3. Right-click and select **Run As > Java Application**.
4. Choose **Sign up** to create an account, then use those details to log in.
5. Choose a date and enter a movie name if needed. Select Search, then select a showing time and Continue.
6. Click one or more available seats. The row determines whether each seat is Standard or Recline. Check the total and select Confirm booking.

The application uses Java 8 or later. No extra libraries or database are needed. Account details are stored locally, including passwords, so use a made-up password for testing. The account file and compiled files are excluded from Git.

## Next steps

The next parts of the project include confirmation emails, filtering by genre and viewing saved bookings.
