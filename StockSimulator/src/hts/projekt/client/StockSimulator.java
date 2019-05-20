package hts.projekt.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import hts.projekt.shared.Equity;
import hts.projekt.shared.User;
import hts.projekt.shared.Wallet;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockSimulator implements EntryPoint {

	private static ServiceAsync service = GWT.create(Service.class);

	private static User ACTIVE_USER;

	private static Wallet ACTIVE_WALLET;

	private static List<Equity> availableEquities;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		startLogin();
	}

	public static void startLogin() {
		RootLayoutPanel.get().add(LoginUI.getInstance());
	}

	public static void startSimulator(User user) {
		ACTIVE_USER = user;
		service.triggerUpdatePrices(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Technical Error");

			}

			@Override
			public void onSuccess(Boolean result) {
				// should never be triggered

			}

		});
		RootLayoutPanel.get().add(SimulatorUI.getInstance());
	}

	public static void login(String username, String password) {
		service.login(username, password, new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable throwable) {
				Window.alert(throwable.getMessage());
			}

			@Override
			public void onSuccess(User user) {
				StockSimulator.startSimulator(user);
			}
		});
	}

	public static void signUp(String username, String password) {

		// is used for testing
		if (username.trim().equalsIgnoreCase("test")) {
			Window.alert("User test is already in use.");
		}

		service.signUp(username, password, new AsyncCallback<User>() {
			@Override
			public void onFailure(Throwable throwable) {
				Window.alert(throwable.getMessage());
			}

			@Override
			public void onSuccess(User user) {
				StockSimulator.startSimulator(user);
			}
		});
	}

	public static List<Equity> getAvailableEquities() {
		service.getAllEquities(new AsyncCallback<List<Equity>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(List<Equity> result) {
				StockSimulator.availableEquities = result;
			}
		});

		return StockSimulator.availableEquities;
	}

	public static User getActiveUser() {
		return ACTIVE_USER;
	}

	public static Wallet getActiveWallet() {
		service.getWallet(ACTIVE_USER, new AsyncCallback<Wallet>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Technical Error");
			}

			@Override
			public void onSuccess(Wallet result) {
				StockSimulator.ACTIVE_WALLET = result;
			}

		});

		return ACTIVE_WALLET;
	}

	public static void sellEquity(Equity equity) {
		service.sellEquity(ACTIVE_WALLET.getWalletId(), equity.getEquityId(), new AsyncCallback<Wallet>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Technical Error");
			}

			@Override
			public void onSuccess(Wallet result) {
				StockSimulator.ACTIVE_WALLET = result;
			}

		});
	}

	public static void buyEquity(Equity equity) {
		service.buyEquity(ACTIVE_WALLET.getWalletId(), equity.getEquityId(), new AsyncCallback<Wallet>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Technical Error");
			}

			@Override
			public void onSuccess(Wallet result) {
				StockSimulator.ACTIVE_WALLET = result;
			}

		});
	}

}
