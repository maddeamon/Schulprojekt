package hts.projekt.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import hts.projekt.shared.Equity;
import hts.projekt.shared.Wallet;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StockSimulator implements EntryPoint {

	private static ServiceAsync service = GWT.create(Service.class);

	private static Wallet ACTIVE_WALLET;

	private static List<Equity> availableEquities;

	private static LayoutPanel contentPanel = new LayoutPanel();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		startLogin();
		RootLayoutPanel.get().add(contentPanel);
	}

	public static void startLogin() {
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
		contentPanel.clear();
		contentPanel.add(LoginUI.getInstance());
	}

	public static void startSimulator(Wallet wallet) {
		ACTIVE_WALLET = wallet;
		contentPanel.clear();
		contentPanel.add(SimulatorUI.getInstance());
	}

	public static void login(String username, String password) {
		service.login(username, password, new AsyncCallback<Wallet>() {
			@Override
			public void onFailure(Throwable throwable) {
				Window.alert(throwable.getMessage());
			}

			@Override
			public void onSuccess(Wallet wallet) {
				StockSimulator.startSimulator(wallet);
			}
		});
	}

	public static void signUp(String username, String password) {

		// is used for testing
		if (username.trim().equalsIgnoreCase("test")) {
			Window.alert("User test is already in use.");
		}

		service.signUp(username, password, new AsyncCallback<Wallet>() {
			@Override
			public void onFailure(Throwable throwable) {
				Window.alert(throwable.getMessage());
			}

			@Override
			public void onSuccess(Wallet wallet) {
				StockSimulator.startSimulator(wallet);
			}
		});
	}

	public static List<Equity> getAvailableEquities() {
		return StockSimulator.availableEquities;
	}

	public static Wallet getActiveWallet() {
		return ACTIVE_WALLET;
	}

	public static void setActiveWallet(Wallet wallet) {
		ACTIVE_WALLET = wallet;
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
