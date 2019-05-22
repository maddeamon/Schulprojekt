package hts.projekt.client;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;

import hts.projekt.shared.Equity;

public class SimulatorUI extends SplitLayoutPanel {

	private static SimulatorUI INSTANCE;

	private ListDataProvider<Equity> availableEquities = new ListDataProvider<>();

	private ListDataProvider<Equity> ownedEquities = new ListDataProvider<>();

	private DataGrid<Equity> ownedEquityTable = new DataGrid<>();

	private DataGrid<Equity> availableEquityTable = new DataGrid<>();

	private DockLayoutPanel ownedEquitiesPanel;

	private DockLayoutPanel availableEquitiesPanel;

	private HorizontalPanel userDataPanel;

	private SimulatorUI() {
		initializeUI();
	}

	public static SimulatorUI getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SimulatorUI();
		}

		return INSTANCE;
	}
	
	public static SimulatorUI forceNewInstance() {
		INSTANCE = new SimulatorUI();
		return INSTANCE;
	}

	private void initializeUI() {

		initializeUserPanel();
		initializeOwnedPanel();
		initializeAvailablePanel();
		
		ownedEquities.setList(StockSimulator.getActiveWallet().getEquities() == null ? Collections.emptyList()
				: StockSimulator.getActiveWallet().getEquities());

		availableEquities.setList(StockSimulator.getAvailableEquities());

		getElement().getStyle().setBackgroundImage("url('ressources/wallstreet.png')");
		addNorth(userDataPanel, 50);
		addSouth(availableEquitiesPanel, 100);
		add(ownedEquitiesPanel);


	}

	private void initializeUserPanel() {
		userDataPanel = new HorizontalPanel();
		Label username = new Label(StockSimulator.getActiveWallet().getUser());
		Double savings = (double) (StockSimulator.getActiveWallet().getSavings() / 100);
		Label savingsLabel = new Label(savings + StockSimulator.getActiveWallet().getCurrency());
		Button logout = new Button("log out");
		logout.addClickHandler(clickEvent -> StockSimulator.startLogin());

		userDataPanel.add(username);
		userDataPanel.add(savingsLabel);
		userDataPanel.add(logout);
	}

	private void initializeOwnedPanel() {
		ownedEquitiesPanel = new DockLayoutPanel(Unit.EM);
		Label owned = new Label("Owned Equities: ");
		ownedEquityTable = createEquitiesTable();

		Button sell = new Button();
		sell.setText("Sell");
		sell.addClickHandler(clickEvent -> StockSimulator
				.sellEquity(getSelectedId(ownedEquities, ownedEquityTable.getKeyboardSelectedRow())));

		ownedEquitiesPanel.addNorth(owned, 2);
		ownedEquitiesPanel.addSouth(sell, 2);
		ownedEquitiesPanel.add(ownedEquityTable);

		ownedEquities.addDataDisplay(ownedEquityTable);
	}

	private void initializeAvailablePanel() {
		availableEquitiesPanel = new DockLayoutPanel(Unit.EM);
		Label available = new Label("Available Equities: ");
		availableEquityTable = createEquitiesTable();

		Button buy = new Button();
		buy.setText("Buy");
		buy.addClickHandler(clickEvent -> StockSimulator
				.buyEquity(getSelectedId(availableEquities, availableEquityTable.getKeyboardSelectedRow())));

		availableEquities.addDataDisplay(availableEquityTable);

		availableEquitiesPanel.addNorth(available, 2);
		availableEquitiesPanel.addSouth(buy, 2);
		availableEquitiesPanel.add(availableEquityTable);
	}

	private DataGrid<Equity> createEquitiesTable() {
		DataGrid<Equity> equitiesTable = new DataGrid<>();

		TextColumn<Equity> idColumn = new TextColumn<Equity>() {

			@Override
			public String getValue(Equity object) {
				return object.getEquityId();
			}

		};

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
				Integer price = equity.getPrice() / 100;
				return price.toString();
			}
		};

		TextColumn<Equity> currencyColumn = new TextColumn<Equity>() {
			@Override
			public String getValue(Equity equity) {
				return equity.getCurrency();
			}
		};

		equitiesTable.addColumn(idColumn, "ID");
		equitiesTable.addColumn(nameColumn, "Equity");
		equitiesTable.addColumn(ownerColumn, "Company");
		equitiesTable.addColumn(priceColumn, "Price");
		equitiesTable.addColumn(currencyColumn, "Currency");

		equitiesTable.setMinimumTableWidth(140, Unit.EM);

		return equitiesTable;
	}

	public void setOwnedEquities(List<Equity> equities) {
		ownedEquities.setList(equities);
	}

	private Equity getSelectedId(ListDataProvider<Equity> provider, Integer selectedRow) {
		return provider.getList().get(selectedRow);
	}

}