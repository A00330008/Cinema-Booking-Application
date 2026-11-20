package movieBooking;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class UserManagerTest {

	@TempDir
	Path temporaryFolder;

	@Test
	void signUpSavesAnAccountThatCanBeLoggedInAgain() throws Exception {
		File userFile = temporaryFolder.resolve("users.dat").toFile();
		UserManager manager = new UserManager(userFile);

		manager.signUp("Kian Rooney", "kian", "cinema123");

		UserManager reopenedManager = new UserManager(userFile);
		User loggedInUser = reopenedManager.login("KIAN", "cinema123");
		assertNotNull(loggedInUser);
		assertEquals("Kian Rooney", loggedInUser.getFullName());
	}

	@Test
	void duplicateUsernamesAreRejectedIgnoringCase() throws Exception {
		UserManager manager = new UserManager(temporaryFolder.resolve("users.dat").toFile());
		manager.signUp("Kian Rooney", "kian", "cinema123");

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> manager.signUp("Another User", "KIAN", "password123"));
		assertEquals("That username is already taken. Please choose another.", exception.getMessage());
	}

	@Test
	void loginFailsWhenThePasswordIsWrong() throws Exception {
		UserManager manager = new UserManager(temporaryFolder.resolve("users.dat").toFile());
		manager.signUp("Kian Rooney", "kian", "cinema123");

		assertNull(manager.login("kian", "wrongPassword"));
	}
}
