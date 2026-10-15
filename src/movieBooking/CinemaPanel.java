package movieBooking;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CinemaPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Color background = new Color(245, 245, 249);
	private Color surface = Color.WHITE;
	private Color text = new Color(36, 36, 47);
	private Color muted = new Color(101, 102, 119);
	private Color purple = new Color(88, 68, 132);
	private Color palePurple = new Color(239, 235, 247);
	private Color line = new Color(223, 222, 232);
	private Color error = new Color(165, 40, 52);

	private UserManager userManager;
	private User currentUser;
	private CardLayout screens = new CardLayout();
	private JPanel screenPanel = new JPanel(screens);
	private CardLayout accountCards = new CardLayout();
	private JPanel accountPanel = new JPanel(accountCards);
	private JPanel homePanel = new JPanel(new BorderLayout(20, 20));

	private JButton loginTab = button("Log in", "loginTab", false);
	private JButton signUpTab = button("Sign up", "signUpTab", false);
	private JButton loginButton = button("Log in", "loginButton", true);
	private JButton signUpButton = button("Create account", "signUpButton", true);
	private JButton logoutButton = button("Log out", "logoutButton", false);
	private JTextField loginUsername = new JTextField();
	private JPasswordField loginPassword = new JPasswordField();
	private JTextField fullName = new JTextField();
	private JTextField signUpUsername = new JTextField();
	private JPasswordField signUpPassword = new JPasswordField();
	private JPasswordField confirmPassword = new JPasswordField();
	private JLabel loginMessage = label(" ", 13, error);
	private JLabel signUpMessage = label(" ", 13, error);
	private JLabel sessionLabel = label("Not signed in", 12, muted);
	private JLabel welcomeLabel = label("Welcome", 14, text);

	public CinemaPanel(UserManager userManager) {
		this.userManager = userManager;
		setLayout(new BorderLayout());
		setBackground(background);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setPreferredSize(new Dimension(1000, 760));

		JPanel header = panel(new BorderLayout(), surface);
		header.setBorder(new EmptyBorder(18, 28, 18, 28));
		header.add(label("ATHLONE CINEMA", 21, purple), BorderLayout.WEST);
		header.add(label("Your next cinema visit starts here", 13, muted), BorderLayout.EAST);
		add(header, BorderLayout.NORTH);

		screenPanel.setBackground(background);
		screenPanel.add(buildAccountScreen(), "account");
		screenPanel.add(buildHomeScreen(), "home");
		add(screenPanel, BorderLayout.CENTER);

		JPanel footer = panel(new BorderLayout(), surface);
		footer.setBorder(new EmptyBorder(11, 28, 11, 28));
		footer.add(sessionLabel, BorderLayout.WEST);
		footer.add(label("Athlone Cinema", 12, muted), BorderLayout.EAST);
		add(footer, BorderLayout.SOUTH);

		loginTab.addActionListener(this);
		signUpTab.addActionListener(this);
		loginButton.addActionListener(this);
		signUpButton.addActionListener(this);
		logoutButton.addActionListener(this);
		loginUsername.addActionListener(this);
		loginPassword.addActionListener(this);
		confirmPassword.addActionListener(this);
		showAccountCard("login");
	}

	private JPanel panel(LayoutManager layout, Color colour) {
		JPanel panel = new JPanel(layout);
		panel.setBackground(colour);
		return panel;
	}

	private JLabel label(String value, int size, Color colour) {
		JLabel label = new JLabel(value);
		label.setFont(new Font("Segoe UI", Font.PLAIN, size));
		label.setForeground(colour);
		return label;
	}

	private JButton button(String value, String name, boolean primary) {
		JButton button = new JButton(value);
		button.setName(name);
		button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		button.setForeground(primary ? Color.WHITE : purple);
		button.setBackground(primary ? purple : surface);
		button.setOpaque(true);
		button.setContentAreaFilled(true);
		button.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(primary ? purple : line), new EmptyBorder(10, 20, 10, 20)));
		return button;
	}

	private JPanel buildAccountScreen() {
		JPanel outside = panel(new GridBagLayout(), background);
		outside.setBorder(new EmptyBorder(22, 28, 22, 28));
		JPanel accountBox = panel(new GridLayout(1, 2), surface);
		accountBox.setPreferredSize(new Dimension(880, 574));
		accountBox.setBorder(BorderFactory.createLineBorder(line));

		JPanel introduction = panel(new GridBagLayout(), purple);
		introduction.setBorder(new EmptyBorder(36, 30, 36, 30));
		introduction.add(label("Welcome to Athlone Cinema", 24, Color.WHITE));
		accountBox.add(introduction);

		JPanel right = panel(new BorderLayout(0, 20), surface);
		right.setBorder(new EmptyBorder(26, 28, 26, 28));
		JPanel tabs = panel(new GridLayout(1, 2, 8, 0), surface);
		tabs.add(loginTab);
		tabs.add(signUpTab);
		right.add(tabs, BorderLayout.NORTH);
		accountPanel.setBackground(surface);
		accountPanel.add(buildLoginForm(), "login");
		accountPanel.add(buildSignUpForm(), "signup");
		right.add(accountPanel, BorderLayout.CENTER);
		accountBox.add(right);
		outside.add(accountBox);
		return outside;
	}

	private JPanel formPanel() {
		JPanel form = panel(null, surface);
		form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
		return form;
	}

	private void addText(JPanel form, String value, int size, Color colour) {
		JLabel heading = label(value, size, colour);
		heading.setAlignmentX(Component.LEFT_ALIGNMENT);
		form.add(heading);
	}

	private void addField(JPanel form, String title, JTextField field, String name) {
		addText(form, title, 13, text);
		form.add(Box.createVerticalStrut(5));
		field.setName(name);
		field.getAccessibleContext().setAccessibleName(title);
		field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(line), new EmptyBorder(6, 10, 6, 10)));
		field.setBackground(surface);
		field.setForeground(text);
		field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		field.setPreferredSize(new Dimension(300, 35));
		field.setAlignmentX(Component.LEFT_ALIGNMENT);
		form.add(field);
		form.add(Box.createVerticalStrut(11));
	}

	private void addSubmit(JPanel form, JLabel message, JButton submit) {
		message.setAlignmentX(Component.LEFT_ALIGNMENT);
		message.setMinimumSize(new Dimension(0, 20));
		message.setPreferredSize(new Dimension(360, 20));
		message.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		form.add(message);
		form.add(Box.createVerticalStrut(10));
		submit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		submit.setAlignmentX(Component.LEFT_ALIGNMENT);
		form.add(submit);
		form.add(Box.createVerticalGlue());
	}

	private JPanel buildLoginForm() {
		JPanel form = formPanel();
		addText(form, "Welcome back", 27, text);
		form.add(Box.createVerticalStrut(8));
		addText(form, "Log in to your cinema account.", 13, muted);
		form.add(Box.createVerticalStrut(28));
		addField(form, "Username", loginUsername, "loginUsername");
		addField(form, "Password", loginPassword, "loginPassword");
		addSubmit(form, loginMessage, loginButton);
		return form;
	}

	private JPanel buildSignUpForm() {
		JPanel form = formPanel();
		addText(form, "Create your account", 25, text);
		form.add(Box.createVerticalStrut(7));
		addText(form, "Sign up once, then log in on your next visit.", 13, muted);
		form.add(Box.createVerticalStrut(18));
		addField(form, "Full name", fullName, "fullName");
		addField(form, "Username", signUpUsername, "signUpUsername");
		addField(form, "Password", signUpPassword, "signUpPassword");
		addField(form, "Confirm password", confirmPassword, "confirmPassword");
		addSubmit(form, signUpMessage, signUpButton);
		return form;
	}

	private JPanel buildHomeScreen() {
		homePanel.setBackground(background);
		homePanel.setBorder(new EmptyBorder(0, 28, 20, 28));
		JPanel top = panel(new BorderLayout(0, 23), background);
		JPanel navigation = panel(new BorderLayout(), surface);
		navigation.setBorder(new EmptyBorder(10, 14, 10, 14));
		navigation.add(label("Book tickets", 15, purple), BorderLayout.WEST);
		navigation.add(logoutButton, BorderLayout.EAST);
		top.add(navigation, BorderLayout.NORTH);
		JPanel heading = panel(new BorderLayout(0, 7), background);
		heading.add(label("What would you like to see?", 27, text), BorderLayout.NORTH);
		heading.add(welcomeLabel, BorderLayout.CENTER);
		heading.add(label("1  Choose showing     >     2  Choose seats     >     3  Confirm booking", 13, muted),
				BorderLayout.SOUTH);
		top.add(heading, BorderLayout.CENTER);
		homePanel.add(top, BorderLayout.NORTH);

		JPanel movieList = panel(new GridLayout(5, 1, 0, 10), background);
		movieList.add(movieCard("Dune part 3", "Sci-Fi", new String[] { "14:00", "17:00", "20:00" }));
		movieList.add(movieCard("Resident Evil", "Horror", new String[] { "16:30", "19:00", "21:30" }));
		movieList.add(movieCard("Street Fighter", "Action", new String[] { "13:30", "16:00", "18:30" }));
		movieList.add(movieCard("Avengers Doomsday", "Superhero", new String[] { "14:30", "17:30", "20:30" }));
		movieList.add(movieCard("Primetime", "Drama", new String[] { "15:00", "17:45", "20:15" }));
		JScrollPane scroll = new JScrollPane(movieList);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		homePanel.add(scroll, BorderLayout.CENTER);

		JPanel ticketPanel = panel(new BorderLayout(0, 15), surface);
		ticketPanel.setPreferredSize(new Dimension(220, 0));
		ticketPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(line), new EmptyBorder(20, 18, 20, 18)));
		JPanel ticketText = panel(new GridLayout(0, 1, 0, 10), surface);
		ticketText.add(label("Your visit", 20, text));
		ticketText.add(label("Athlone Cinema", 14, muted));
		ticketText.add(label("Ticket type", 13, text));
		JComboBox<String> ticketType = new JComboBox<>(new String[] { "Standard", "Recline" });
		ticketType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		ticketType.setName("ticketType");
		ticketType.getAccessibleContext().setAccessibleName("Ticket type");
		ticketText.add(ticketType);
		ticketPanel.add(ticketText, BorderLayout.NORTH);
		homePanel.add(ticketPanel, BorderLayout.EAST);
		return homePanel;
	}

	private JPanel movieCard(String title, String genre, String[] times) {
		JPanel card = panel(new BorderLayout(12, 0), surface);
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(line), new EmptyBorder(11, 16, 11, 16)));
		JPanel description = panel(new GridLayout(2, 1, 0, 2), surface);
		description.add(label(title, 17, text));
		description.add(label(genre, 12, muted));
		card.add(description, BorderLayout.CENTER);
		JPanel showtimes = panel(new FlowLayout(FlowLayout.RIGHT, 6, 12), surface);
		for (String time : times) {
			JLabel timeLabel = label(time, 13, purple);
			timeLabel.setOpaque(true);
			timeLabel.setBackground(palePurple);
			timeLabel.setBorder(new EmptyBorder(7, 9, 7, 9));
			showtimes.add(timeLabel);
		}
		card.add(showtimes, BorderLayout.EAST);
		return card;
	}

	private void showAccountCard(String name) {
		accountCards.show(accountPanel, name);
		loginTab.setBackground(name.equals("login") ? palePurple : surface);
		signUpTab.setBackground(name.equals("signup") ? palePurple : surface);
		loginMessage.setText(" ");
		signUpMessage.setText(" ");
		loginPassword.setText("");
		signUpPassword.setText("");
		confirmPassword.setText("");
		if (name.equals("login")) {
			loginUsername.requestFocusInWindow();
		} else {
			fullName.requestFocusInWindow();
		}
	}

	private void login() {
		char[] passwordCharacters = loginPassword.getPassword();
		String password = new String(passwordCharacters);
		Arrays.fill(passwordCharacters, '\0');
		loginMessage.setForeground(error);
		if (loginUsername.getText().trim().isEmpty() || password.trim().isEmpty()) {
			loginMessage.setText("Please enter your username and password.");
			return;
		}

		User user = userManager.login(loginUsername.getText(), password);
		loginPassword.setText("");
		if (user == null) {
			loginMessage.setText("Username or password is incorrect.");
			loginPassword.requestFocusInWindow();
			return;
		}

		currentUser = user;
		welcomeLabel.setText("Welcome, " + currentUser.getFullName() + ".");
		sessionLabel.setText("Signed in as " + currentUser.getUsername());
		screens.show(screenPanel, "home");
	}

	private void signUp() {
		char[] passwordCharacters = signUpPassword.getPassword();
		char[] confirmationCharacters = confirmPassword.getPassword();
		String password = new String(passwordCharacters);
		String confirmation = new String(confirmationCharacters);
		Arrays.fill(passwordCharacters, '\0');
		Arrays.fill(confirmationCharacters, '\0');

		if (fullName.getText().trim().isEmpty() || signUpUsername.getText().trim().isEmpty()
				|| password.trim().isEmpty() || confirmation.trim().isEmpty()) {
			signUpMessage.setText("Please complete all fields.");
			return;
		}
		if (!password.equals(confirmation)) {
			signUpMessage.setText("Your passwords do not match.");
			return;
		}

		try {
			userManager.signUp(fullName.getText(), signUpUsername.getText(), password);
			loginUsername.setText(signUpUsername.getText().trim());
			fullName.setText("");
			signUpUsername.setText("");
			showAccountCard("login");
			loginMessage.setForeground(purple);
			loginMessage.setText("Account created. You can now log in.");
			loginPassword.requestFocusInWindow();
		} catch (IllegalArgumentException ex) {
			signUpMessage.setText(ex.getMessage());
		} catch (IOException ex) {
			signUpMessage.setText("Could not save your account. Please try again.");
		}
	}

	private void logout() {
		currentUser = null;
		loginUsername.setText("");
		sessionLabel.setText("Not signed in");
		screens.show(screenPanel, "account");
		showAccountCard("login");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object target = e.getSource();
		if (target == loginTab) {
			showAccountCard("login");
		} else if (target == signUpTab) {
			showAccountCard("signup");
		} else if (target == loginButton || target == loginUsername || target == loginPassword) {
			login();
		} else if (target == signUpButton || target == confirmPassword) {
			signUp();
		} else if (target == logoutButton) {
			logout();
		}
	}
}
