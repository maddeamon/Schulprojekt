package hts.projekt.client;

import java.util.Collections;
import java.util.stream.Collectors;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import hts.projekt.shared.Equity;

public class SimulatorUI extends LayoutPanel {

	private static SimulatorUI INSTANCE;

	private ListDataProvider<Equity> availableEquities = new ListDataProvider<>();

	private ListDataProvider<Equity> ownedEquities = new ListDataProvider<>();

	private DataGrid<Equity> ownedEquityTable;

	private DataGrid<Equity> availableEquityTable;

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
		Label savings = new Label(StockSimulator.getActiveWallet().getSavings().toString()
				+ StockSimulator.getActiveWallet().getCurrency());
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
		ownedEquityTable = createEquitiesTable();

		ownedEquities.setList(StockSimulator.getActiveWallet().getEquities() == null ? Collections.emptyList()
				: StockSimulator.getActiveWallet().getEquities().keySet().stream().collect(Collectors.toList()));

		ownedEquities.addDataDisplay(ownedEquityTable);

		ownedEquityPanel.add(owned);
		ownedEquityPanel.add(ownedEquityTable);

		return ownedEquityPanel;
	}

	private VerticalPanel initializeAvailablePanel() {
		VerticalPanel availableEquityPanel = new VerticalPanel();
		Label available = new Label("Available Equities: ");
		availableEquityTable = createEquitiesTable();

		availableEquities.setList(StockSimulator.getAvailableEquities());
		availableEquities.addDataDisplay(availableEquityTable);

		availableEquityPanel.add(available);
		availableEquityPanel.add(availableEquityTable);

		return availableEquityPanel;
	}

	private DataGrid<Equity> createEquitiesTable() {
		DataGrid<Equity> equitiesTable = new DataGrid<>();

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

		@SuppressWarnings("unchecked")
		Column<Equity, ActionCell<Equity>> buyColumn = new Column<Equity, ActionCell<Equity>>(
				new ActionCell("Buy", new ActionCell.Delegate<Equity>() {

					@Override
					public void execute(Equity equity) {
						StockSimulator.buyEquity(equity);

					}
				})) {

			@Override
			public ActionCell<Equity> getValue(Equity object) {
				// TODO Auto-generated method stub
				return null;
			}

		};

		@SuppressWarnings("unchecked")
		Column<Equity, ActionCell<Equity>> sellColumn = new Column<Equity, ActionCell<Equity>>(
				new ActionCell("Sell", new ActionCell.Delegate<Equity>() {

					@Override
					public void execute(Equity equity) {
						StockSimulator.buyEquity(equity);

					}
				})) {

			@Override
			public ActionCell<Equity> getValue(Equity object) {
				// TODO Auto-generated method stub
				return null;
			}

		};

		equitiesTable.addColumn(nameColumn, "Equity");
		equitiesTable.addColumn(ownerColumn, "Company");
		equitiesTable.addColumn(priceColumn, "Price");
		equitiesTable.addColumn(currencyColumn, "Currency");
		equitiesTable.addColumn(buyColumn);
		equitiesTable.addColumn(sellColumn);

		equitiesTable.setMinimumTableWidth(140, Unit.EM);

		return equitiesTable;
	}

}