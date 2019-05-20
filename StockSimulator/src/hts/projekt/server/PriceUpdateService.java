package hts.projekt.server;

import hts.projekt.shared.Equity;

public class PriceUpdateService {

	public static void updatePrice(Equity equity) {
		Integer oldPrice = equity.getPrice();
		Integer newPrice = calculateNewPrice(oldPrice);

		DatabaseConnector.updatePrice(equity.getEquityId(), newPrice);
	}

	private static Integer calculateNewPrice(Integer oldPrice) {
		return (int) Math.random();
	}
}
