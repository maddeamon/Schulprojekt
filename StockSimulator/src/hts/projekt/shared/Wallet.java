package hts.projekt.shared;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Wallet implements IsSerializable {

	private String username;

	private Long walletId;

	private Map<Equity, Integer> ownedEquities;

	private Integer savings;

	private String currency;

	public Wallet() {
		username = null;
		walletId = null;
		ownedEquities = new HashMap<>();
		savings = null;
		currency = null;
	}

	public Wallet(String username, Long walletId, Map<Equity, Integer> ownedEquities, Integer savings,
			String currency) {
		this.username = username;
		this.walletId = walletId;
		this.ownedEquities = ownedEquities;
		this.savings = savings;
		this.currency = currency;
	}

	public String getUser() {
		return username;
	}

	public void setUser(String username) {
		this.username = username;
	}

	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}

	public Map<Equity, Integer> getEquities() {
		return ownedEquities;
	}

	public void setEquities(Map<Equity, Integer> ownedEquities) {
		this.ownedEquities = ownedEquities;
	}

	public Integer getSavings() {
		return savings;
	}

	public void setSavings(Integer savings) {
		this.savings = savings;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void addEquity(Equity equity) {
		if (ownedEquities.containsKey(equity)) {
			ownedEquities.put(equity, ownedEquities.get(equity) + 1);
		} else {
			ownedEquities.put(equity, 1);
		}
	}

	@Override
	public String toString() {
		return "Wallet [username=" + username + ", walletId=" + walletId + ", ownedEquities=" + ownedEquities
				+ ", savings=" + savings + ", currency=" + currency + "]";
	}

}
