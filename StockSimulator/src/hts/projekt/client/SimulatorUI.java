package hts.projekt.client;

import java.util.Collections;
import java.util.stream.Collectors;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.view.client.ListDataProvider;

import hts.projekt.shared.Equity;

public class SimulatorUI extends LayoutPanel {

	private static SimulatorUI INSTANCE;

	private ListDataProvider<Equity> availableEquities = new ListDataProvider<>();

	private ListDataProvider<Equity> ownedEquities = new ListDataProvider<>();

	private DataGrid<Equity> ownedEquityTable;

	private DataGrid<Equity> availableEquityTable;

	private SimpleLayoutPanel ownedEquitiesPanel;

	private SimpleLayoutPanel availableEquitiesPanel;

	private SimpleLayoutPanel userDataPanel;

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

		initializeUserPanel();
		initializeOwnedPanel();
		initializeAvailablePanel();

		getElement().getStyle().setBackgroundColor("#6CDAE7");
		add(userDataPanel);
		add(ownedEquitiesPanel);
		add(availableEquitiesPanel);

		ownedEquities.setList(StockSimulator.getActiveWallet().getEquities() == null ? Collections.emptyList()
				: StockSimulator.getActiveWallet().getEquities().keySet().stream().collect(Collectors.toList()));

		availableEquities.setList(StockSimulator.getAvailableEquities());
	}

	private void initializeUserPanel() {
		userDataPanel = new SimpleLayoutPanel();
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
		ownedEquitiesPanel = new SimpleLayoutPanel();
		Label owned = new Label("Owned Equities: ");
		ownedEquityTable = createEquitiesTable();

		ownedEquitiesPanel.add(owned);
		ownedEquitiesPanel.add(ownedEquityTable);

		ownedEquities.addDataDisplay(ownedEquityTable);
	}

	private void initializeAvailablePanel() {
		availableEquitiesPanel = new SimpleLayoutPanel();
		Label available = new Label("Available Equities: ");
		availableEquityTable = createEquitiesTable();

		availableEquities.addDataDisplay(availableEquityTable);

		availableEquitiesPanel.add(available);
		availableEquitiesPanel.add(availableEquityTable);
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