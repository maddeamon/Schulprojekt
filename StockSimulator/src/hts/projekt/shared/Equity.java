package hts.projekt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Equity implements IsSerializable {

	private String equityId;

	private String name;

	private Integer price;

	private String currency;

	private Company owner;

	public Equity() {
		equityId = null;
		name = null;
		price = null;
		owner = null;
		currency = null;
	}

	public Equity(String equityId, String name, Integer price, Company owner, String currency) {
		super();
		this.equityId = equityId;
		this.name = name;
		this.price = price;
		this.owner = owner;
		this.currency = currency;
	}

	public String getEquityId() {
		return equityId;
	}

	public void setEquityId(String equityId) {
		this.equityId = equityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Company getOwner() {
		return owner;
	}

	public void setOwner(Company owner) {
		this.owner = owner;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "Equity [equityId=" + equityId + ", name=" + name + ", price=" + price + ", currency=" + currency
				+ ", owner=" + owner + "]";
	}

}
