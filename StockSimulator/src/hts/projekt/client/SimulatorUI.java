package hts.projekt.client;

import java.util.stream.Collectors;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import hts.projekt.shared.Equity;

public class SimulatorUI extends LayoutPanel {

	private static SimulatorUI INSTANCE;

	private SimulatorUI() {
		initializeUI();
	}

	public static SimulatorUI getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SimulatorUI();
		}

		return INSTANCE;
	}

	private void initializeUI() {

		VerticalPanel mainPanel = new VerticalPanel();

		HorizontalPanel userPanel = initializeUserPanel();
		VerticalPanel ownedEquitiesPanel = initializeOwnedPanel();
		VerticalPanel availableEquitiesPanel = initializeAvailablePanel();

		mainPanel.add(userPanel);
		mainPanel.add(ownedEquitiesPanel);
		mainPanel.add(availableEquitiesPanel);

		getElement().getStyle().setBackgroundColor("red");
		add(mainPanel);
	}

	private HorizontalPanel initializeUserPanel() {
		HorizontalPanel userPanel = new HorizontalPanel();
		Label username = new Label(StockSimulator.getActiveWallet().getUser());
		Label savings = new Label("savings");
		// Label savings = new
		// Label(StockSimulator.getActiveWallet().getSavings().toString()
		// + StockSimulator.getActiveWallet().getCurrency());
		Button logout = new Button("log out");
		logout.addClickHandler(clickEvent -> StockSimulator.startLogin());

		userPanel.add(username);
		userPanel.add(savings);
		userPanel.add(logout);

		return userPanel;
	}

	private VerticalPanel initializeOwnedPanel() {
		VerticalPanel ownedEquityPanel = new VerticalPanel();
		Label owned = new Label("Owned Equities: ");
		DataGrid<Equity> ownedEquitiesTable = createEquitiesTable();

		if (StockSimulator.getActiveWallet() != null) {
			ownedEquitiesTable.setRowData(
					StockSimulator.getActiveWallet().getEquities().keySet().stream().collect(Collectors.toList()));
		}

		ownedEquityPanel.add(owned);
		ownedEquityPanel.add(ownedEquitiesTable);

		return ownedEquityPanel;
	}

	private VerticalPanel initializeAvailablePanel() {
		VerticalPanel availableEquityPanel = new VerticalPanel();
		Label available = new Label("Available Equities: ");
		DataGrid<Equity> availableEquitiesTable = createEquitiesTable();

		availableEquitiesTable.setRowData(StockSimulator.getAvailableEquities());

		availableEquityPanel.add(available);
		availableEquityPanel.add(availableEquitiesTable);

		return availableEquityPanel;
	}

	private DataGrid<Equity> createEquitiesTable() {
		DataGrid<Equity> ownedEquitiesTable = new DataGrid<>();

		TextColumn<Equity> nameColumn = new TextColumn<Equity>() {
			@Override
			public String getValue(Equity equity) {
				return equity.getName();
			}
		};

		TextColumn<Equity> ownerColumn = new TextColumn<Equity>() {
			@Override
			public String getValue(Equity equity) {
				return equity.getOwner().getName();
			}
		};

		TextColumn<Equity> priceColumn = new TextColumn<Equity>() {
			@Override
			public String getValue(Equity equity) {
				return equity.getPrice().toString();
			}
		};

		Button sell = new Button("sell");
		sell.addClickHandler(clickEvent -> {
			Equity equity = new Equity();
			StockSimulator.sellEquity(equity);
		});

		ownedEquitiesTable.addColumn(nameColumn, "Equity");
		ownedEquitiesTable.addColumn(ownerColumn, "Company");
		ownedEquitiesTable.addColumn(priceColumn, "Price");
		return ownedEquitiesTable;
	}

}