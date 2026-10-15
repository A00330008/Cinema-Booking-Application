package movieBooking;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class UserManager {

	private ArrayList<User> users = new ArrayList<>();
	private File userFile;

	public UserManager() throws IOException, ClassNotFoundException {
		this(new File("users.dat"));
	}

	public UserManager(File userFile) throws IOException, ClassNotFoundException {
		this.userFile = userFile;
		loadUsers();
	}

	private void loadUsers() throws IOException, ClassNotFoundException {
		if (!userFile.exists()) {
			return;
		}

		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(userFile))) {
			Object savedUsers = input.readObject();
			if (!(savedUsers instanceof ArrayList<?>)) {
				throw new IOException("The account file is not in the expected format.");
			}

			for (Object savedUser : (ArrayList<?>) savedUsers) {
				if (!(savedUser instanceof User)) {
					throw new IOException("The account file contains an invalid account.");
				}
				users.add((User) savedUser);
			}
		}
	}

	private void saveUsers() throws IOException {
		// Finish writing the new file before replacing the previous saved accounts.
		File temporaryFile = new File(userFile.getPath() + ".tmp");
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(temporaryFile))) {
			output.writeObject(users);
		}
		Files.move(temporaryFile.toPath(), userFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}

	public boolean usernameExists(String username) {
		for (User user : users) {
			if (user.getUsername().equalsIgnoreCase(username.trim())) {
				return true;
			}
		}
		return false;
	}

	public void signUp(String fullName, String username, String password) throws IOException {
		if (fullName.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty()) {
			throw new IllegalArgumentException("Please complete all fields.");
		}
		if (usernameExists(username)) {
			throw new IllegalArgumentException("That username is already taken. Please choose another.");
		}

		User user = new User(fullName.trim(), username.trim(), password);
		users.add(user);
		try {
			saveUsers();
		} catch (IOException ex) {
			users.remove(user);
			throw ex;
		}
	}

	public User login(String username, String password) {
		for (User user : users) {
			if (user.getUsername().equalsIgnoreCase(username.trim()) && user.checkPassword(password)) {
				return user;
			}
		}
		return null;
	}
}
