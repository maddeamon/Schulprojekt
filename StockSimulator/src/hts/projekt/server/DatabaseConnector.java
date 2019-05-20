package hts.projekt.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import hts.projekt.shared.Company;
import hts.projekt.shared.Equity;
import hts.projekt.shared.User;
import hts.projekt.shared.Wallet;

public class DatabaseConnector {

	private static final String URL = "jdbc:mysql://yannickstrelow.de:3306/ni1952001_1sql5";

	private static final String USERNAME = "ni1952001_1sql5";

	private static final String PASSWORD = "htsprojekt";

	private static Connection con;

	private static ResultSet getResultFromDatabase(String sql) {
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rs;
	}

	private static void changeDataOnDatabase(String sql) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			Statement stmt = con.createStatement();
			stmt.execute(sql);
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static User getUserByName(String username) {
		List<User> response = new ArrayList<>();

		ResultSet rs = getResultFromDatabase("SELECT * FROM User WHERE Benutzername='" + username + "';");

		try {
			while (rs.next()) {
				if (rs.getString("Benutzername") != null) {
					response.add(new User(rs.getString("Benutzername"), rs.getString("Passwort")));
				}
			}

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response.stream().findAny().orElse(null);
	}

	public static User insertNewUser(User user) {
		changeDataOnDatabase("INSERT INTO User (Benutzername, Passwort) VALUES ('" + user.getUsername() + "', '"
				+ user.getPassword() + "');");

		return user;
	}

	public static void removeUser(String username) {
		changeDataOnDatabase("DELETE FROM User WHERE Benutzername='" + username + "';");
	}

	public static List<Equity> getAllEquities() {
		List<Equity> equities = new ArrayList<>();

		ResultSet rs = getResultFromDatabase(
				"SELECT * FROM Aktie a JOIN Firma_Aktie fa on a.Aktie_ID=fa.Aktie_ID JOIN Firma f on fa.Firma_ID=f.Firma_ID");

		try {
			while (rs.next()) {
				equities.add(new Equity(rs.getString("Aktie_ID"), rs.getString("AktieBezeichnung"),
						rs.getDouble("Preis"), new Company(rs.getLong("Firma_ID"), rs.getString("FirmaName"))));
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return equities;
	}

	public static Wallet getWallet(String username) {
		List<Wallet> wallets = new ArrayList<>();

		try {

			ResultSet rs = getResultFromDatabase("SELECT * FROM Wallet WHERE Benutzername=" + username);
			while (rs.next()) {
				wallets.add(new Wallet(rs.getString("username"), rs.getLong("Wallet_ID"), null,
						rs.getDouble("Guthaben"), rs.getString("Währung")));
			}

			for (Wallet wallet : wallets) {
				ResultSet result = getResultFromDatabase(
						"SELECT * FROM Wallet_Aktie WHERE Wallet_ID=" + wallet.getWalletId());
				while (result.next()) {
					// wallet.setEquities(equities);
				}
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return wallets.stream().findFirst().orElse(null);
	}

	public static Wallet getWallet(Long walletId) {
		List<Wallet> wallets = new ArrayList<>();

		try {

			ResultSet rs = getResultFromDatabase("SELECT * FROM Wallet WHERE Wallet_ID=" + walletId);
			while (rs.next()) {
				wallets.add(new Wallet(rs.getString("username"), rs.getLong("Wallet_ID"), null,
						rs.getDouble("Guthaben"), rs.getString("Währung")));
			}

			for (Wallet wallet : wallets) {
				ResultSet result = getResultFromDatabase(
						"SELECT * FROM Wallet_Aktie WHERE Wallet_ID=" + wallet.getWalletId());
				while (result.next()) {
					// wallet.setEquities(equities);
				}
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return wallets.stream().findFirst().orElse(null);
	}

	public static Double getCurrentPrice(Long equityId) {
		ResultSet rs = getResultFromDatabase("SELECT price FROM Aktie WHERE Aktie_ID=" + equityId);

		try {
			Double price = rs.getDouble(0);
			con.close();
			return price;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void sellEquity(Long walletId, String equityId) {

	}

	public static void buyEquity(Long walletId, String equityId) {

	}

	public static void updatePrice(String equityId, Double price) {

	}

}
