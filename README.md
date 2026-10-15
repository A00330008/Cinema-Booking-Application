# Athlone Cinema

This is my Cinema Booking Application project for college. I'm building it in Java using Eclipse and Swing for the GUI.

## Progress so far

For Sprint 1 (CBA-1), I've added the basic GUI and the login and sign-up screens. Users can create an account, log in and log out. Account details are saved in a file called `users.dat`, so they can log in again after closing the program.

The forms check for missing details, usernames that are already taken and passwords that don't match.

After logging in, the user can see the films, their genres and showing times. There is also a choice between Standard and Recline tickets. The booking part still needs to be added, so these options don't make a booking yet.

## Films

- Dune part 3 (Sci-Fi)
- Resident Evil (Horror)
- Street Fighter (Action)
- Avengers Doomsday (Superhero)
- Primetime (Drama)

The showing times are examples and use the 24-hour format.

## Running the application

1. Open the project in Eclipse. If changes aren't showing, select the project and press F5.
2. Open `Main.java` in the `movieBooking` package.
3. Right-click and select **Run As > Java Application**.
4. Choose **Sign up** to create an account, then use those details to log in.

The application uses Java 8 or later. No extra libraries or database are needed. Account details are stored locally, including passwords, so use a made-up password for testing. The account file and compiled files are excluded from Git.

## Next steps

The next part of the project is to build the booking features, including choosing a showing, selecting seats and saving a booking.
