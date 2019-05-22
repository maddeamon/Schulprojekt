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

	private static boolean changeDataOnDatabase(String sql) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			Statement stmt = con.createStatement();
			stmt.execute(sql);
			con.close();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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

	public static boolean insertNewUser(User user) {
		return changeDataOnDatabase("INSERT INTO User (Benutzername, Passwort) VALUES ('" + user.getUsername() + "', '"
				+ user.getPassword() + "');");
	}

	public static void removeUser(String username) {
		changeDataOnDatabase("DELETE FROM User WHERE Benutzername='" + username + "';");
	}

	public static List<Equity> getAllEquities() {
		List<Equity> equities = new ArrayList<>();

		ResultSet rs = getResultFromDatabase("SELECT * FROM Aktie a JOIN Firma f on a.Firma_ID=f.Firma_ID");

		try {
			while (rs.next()) {
				equities.add(new Equity(rs.getString("Aktie_ID"), rs.getString("AktieBezeichnung"), rs.getInt("Preis"),
						new Company(rs.getLong("Firma_ID"), rs.getString("FirmaName")), rs.getString("Waehrung")));
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

			ResultSet rs = getResultFromDatabase("SELECT * FROM Konto WHERE Benutzername='" + username + "'");
			while (rs.next()) {
				wallets.add(new Wallet(rs.getString("Benutzername"), rs.getLong("Konto_ID"), null,
						rs.getInt("Guthaben"), rs.getString("Waehrung")));
			}

			for (Wallet wallet : wallets) {
				ResultSet result = getResultFromDatabase(
						"SELECT * FROM Konto_Aktie ka JOIN Aktie a on ka.Aktie_ID=a.Aktie_ID JOIN Firma f on a.Firma_ID=f.Firma_ID WHERE ka.Konto_ID="
								+ wallet.getWalletId());
				while (result.next()) {
					wallet.addEquity(new Equity(result.getString("Aktie_ID"), result.getString("AktieBezeichnung"),
							result.getInt("Preis"),
							new Company(result.getLong("Firma_ID"), result.getString("FirmaName")),
							result.getString("Waehrung")));
				}
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Wallet recieved.");

		return wallets.stream().findFirst().orElse(null);
	}

	public static Wallet getWallet(Long walletId) {
		List<Wallet> wallets = new ArrayList<>();

		try {

			ResultSet rs = getResultFromDatabase("SELECT * FROM Konto WHERE Konto_ID=" + walletId);
			while (rs.next()) {
				wallets.add(new Wallet(rs.getString("Benutzername"), rs.getLong("Konto_ID"), null,
						rs.getInt("Guthaben"), rs.getString("Waehrung")));
			}

			for (Wallet wallet : wallets) {
				ResultSet result = getResultFromDatabase(
						"SELECT * FROM Konto_Aktie WHERE Konto_ID=" + wallet.getWalletId());
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

	public static void addNewWallet(User user) {
		changeDataOnDatabase("INSERT INTO Konto (Benutzername, Guthaben, Waehrung) VALUES ('" + user.getUsername()
				+ "', 10000, 'EUR'");
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

	public static void addNewEquity(Equity equity) {
		changeDataOnDatabase("INSERT INTO Aktie (Aktie_ID, AktieBezeichnung, Preis, Waehrung, Firma_ID) VALUES ('"
				+ equity.getEquityId() + "', '" + equity.getName() + "', " + equity.getPrice() + ", '"
				+ equity.getCurrency() + "', '" + equity.getOwner().getCompanyId() + "')");

		changeDataOnDatabase("INSERT INTO Firma (Firma_ID, FirmaName) VALUES ('" + equity.getOwner().getCompanyId()
				+ "' ,'" + equity.getOwner().getName() + "')");
	}

	public static void removeEquity(String equityId, Long companyId) {
		changeDataOnDatabase("DELETE FROM Aktie WHERE Aktie_ID='" + equityId + "'");
		changeDataOnDatabase("DELETE FROM Firma WHERE Firma_ID=" + companyId);
	}

	public static Equity getEquity(String equityId) {
		ResultSet rs = getResultFromDatabase(
				"SELECT * FROM Aktie a JOIN Firma f on a.Firma_ID=f.Firma_ID WHERE a.Aktie_ID='" + equityId + "'");

		try {
			return new Equity(rs.getString("Aktie_ID"), rs.getString("AktieBezeichnung"), rs.getInt("Preis"),
					new Company(rs.getLong("Firma_ID"), rs.getString("FirmaName")), rs.getString("Waehrung"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void sellEquity(Wallet wallet, String equityId) {
		Integer equityPrice = null;
		try {
			equityPrice = getResultFromDatabase("SELECT Preis FROM Aktie WHERE Aktie_ID='" + equityId + "'")
					.getInt("Preis");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		changeDataOnDatabase("DELETE FROM Konto_Aktie WHERE Konto_ID=" + wallet.getWalletId() + " AND Aktie_ID 0 '"
				+ equityId + "'");

		Integer newSavings = wallet.getSavings() + equityPrice;
		changeDataOnDatabase("UPDATE Konto SET Guthaben=" + newSavings + " WHERE Konto_ID=" + wallet.getWalletId());
	}

	public static void buyEquity(Wallet wallet, String equityId) throws Exception {

		Integer equityPrice = null;
		try {
			equityPrice = getResultFromDatabase("SELECT Preis FROM Aktie WHERE Aktie_ID='" + equityId + "'")
					.getInt("Preis");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (equityPrice > wallet.getSavings()) {
			throw new Exception("Not enough savings.");
		}

		changeDataOnDatabase(
				"INSERT INTO Konto_Aktie (Konto_ID, Aktie_ID) VALUES (" + wallet.getWalletId() + ", " + equityId);

		Integer newSavings = wallet.getSavings() - equityPrice;
		changeDataOnDatabase("UPDATE Konto SET Guthaben=" + newSavings + " WHERE Konto_ID=" + wallet.getWalletId());
	}

	public static void updatePrice(String equityId, Integer price) {
		changeDataOnDatabase("UPDATE Aktie SET Preis=" + price + " WHERE Aktie_ID='" + equityId + "'");
	}

}
