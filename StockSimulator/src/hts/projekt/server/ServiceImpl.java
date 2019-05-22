package hts.projekt.server;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import hts.projekt.client.Service;
import hts.projekt.shared.Equity;
import hts.projekt.shared.User;
import hts.projekt.shared.Wallet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ServiceImpl extends RemoteServiceServlet implements Service {

	private long timer;

	/**
	 * Checks the login data from the user against the database. Throws an exception
	 * if the username does not exist or if the passwords do not match.
	 *
	 * @param username the username entered by the user
	 * @param password the password entered by the user
	 * @return the username from the user
	 * @throws Exception if either username or password is wrong
	 */
	@Override
	public Wallet login(String username, String password) throws Exception {
		System.out.println("Login started for " + username);
		User user = DatabaseConnector.getUserByName(username);

		if (user == null) {
			throw new Exception("Wrong Username");
		}

		if (!securestPasswordHash(password).equals(user.getPassword())) {
			throw new Exception("Wrong Password");
		}

		System.out.println("Login successful.");

		return getWallet(user);
	}

	/**
	 * Inserts a new entry for the user with a new userID and the hashed password.
	 *
	 * @param username the username entered by the user
	 * @param password the password entered by the user
	 * @return the username from the user
	 */
	@Override
	public Wallet signUp(String username, String password) {
		System.out.println("Sign up started...");

		User user = new User();
		user.setUsername(escapeHtml(username));
		user.setPassword(securestPasswordHash(password));

		DatabaseConnector.insertNewUser(user);

		DatabaseConnector.addNewWallet(user);

		System.out.println("Sign up completed.");

		return getWallet(user);
	}

	public void removeUser(String username) {
		System.out.println("Removing user " + username);

		DatabaseConnector.removeUser(username);
	}

	@Override
	public Wallet getWallet(User user) {
		System.out.println("Recieving Wallet...");
		Wallet wallet = DatabaseConnector.getWallet(user.getUsername());
		System.out.println(wallet);
		return wallet;
	}

	@Override
	public List<Equity> getAllEquities() {
		System.out.println("Reading all equities...");
		return DatabaseConnector.getAllEquities();
	}

	@Override
	public Wallet buyEquity(Wallet wallet, String equityId) throws Exception {

		System.out.println(wallet + " is buying equity number " + equityId);

		DatabaseConnector.buyEquity(wallet, equityId);
		return DatabaseConnector.getWallet(wallet.getWalletId());
	}

	@Override
	public Wallet sellEquity(Wallet wallet, String equityId) {
		System.out.printf("{} is selling equity number {}", wallet, equityId);

		DatabaseConnector.sellEquity(wallet, equityId);
		return DatabaseConnector.getWallet(wallet.getWalletId());
	}

	@Override
	public Boolean triggerUpdatePrices() {

		timer = System.currentTimeMillis();

		while (true) {
			if (System.currentTimeMillis() > timer + 60000) {
				System.out.println("Updating all prices...");

				List<Equity> equities = DatabaseConnector.getAllEquities();

				equities.stream().filter(Objects::nonNull).forEach(PriceUpdateService::updatePrice);

				System.out.println("Update completed.");
			}
		}
	}

	public void addNewEquity(Equity equity) {
		System.out.println("Adding new equity...");
		DatabaseConnector.addNewEquity(equity);
	}

	public void removeEquity(String equityId, Long companyId) {
		System.out.println("Removing equity with id " + equityId);
		DatabaseConnector.removeEquity(equityId, companyId);
	}

	public Equity getEquity(String equityId) {
		return DatabaseConnector.getEquity(equityId);
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 *
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	/**
	 * hashes the password using the Pbkdf2 algorithm.
	 *
	 * @param password the password to be hashed
	 * @return the hashed password
	 */
	private String securestPasswordHash(String password) {
		return StringUtils.reverse(password);
	}

}
