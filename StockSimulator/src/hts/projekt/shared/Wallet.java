package hts.projekt.shared;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Wallet implements IsSerializable {

	private String username;

	private Long walletId;

	private Map<Equity, Integer> ownedEquities;

	private Double savings;

	private String currency;

	public Wallet(String username, Long walletId, Map<Equity, Integer> ownedEquities, Double savings, String currency) {
		this.username = username;
		this.walletId = walletId;
		this.ownedEquities = ownedEquities;
		this.savings = savings;
		this.currency = currency;
	}

	public Wallet() {
		username = null;
		walletId = null;
		ownedEquities = new HashMap<>();
		savings = null;
		currency = null;
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

	public Double getSavings() {
		return savings;
	}

	public void setSavings(Double savings) {
		this.savings = savings;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
