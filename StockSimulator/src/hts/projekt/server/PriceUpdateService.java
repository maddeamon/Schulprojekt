package hts.projekt.server;

import hts.projekt.shared.Equity;

public class PriceUpdateService {

	public static void updatePrice(Equity equity) {
		Double oldPrice = equity.getPrice();
		Double newPrice = calculateNewPrice(oldPrice);

		DatabaseConnector.updatePrice(equity.getEquityId(), newPrice);
	}

	private static Double calculateNewPrice(Double oldPrice) {
		return Math.random();
	}
}
