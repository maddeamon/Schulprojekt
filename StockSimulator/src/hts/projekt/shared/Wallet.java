package hts.projekt.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Wallet implements IsSerializable {

	private String username;

	private Long walletId;

	private List<Equity> ownedEquities;

	private Integer savings;

	private String currency;

	public Wallet() {
		username = null;
		walletId = null;
		ownedEquities = new ArrayList<>();
		savings = null;
		currency = null;
	}

	public Wallet(String username, Long walletId, List<Equity> ownedEquities, Integer savings,
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

	public List<Equity> getEquities() {
		return ownedEquities;
	}

	public void setEquities(List<Equity> ownedEquities) {
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

		ownedEquities.add(equity);

	}

	@Override
	public String toString() {
		return "Wallet [username=" + username + ", walletId=" + walletId + ", ownedEquities=" + ownedEquities
				+ ", savings=" + savings + ", currency=" + currency + "]";
	}

}
