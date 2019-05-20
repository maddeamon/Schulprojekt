package hts.projekt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Equity implements IsSerializable {

	private String equityId;

	private String name;

	private Double price;

	private Company owner;

	public Equity() {
		equityId = null;
		name = null;
		price = null;
		owner = null;
	}

	public Equity(String equityId, String name, Double price, Company owner) {
		super();
		this.equityId = equityId;
		this.name = name;
		this.price = price;
		this.owner = owner;
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Company getOwner() {
		return owner;
	}

	public void setOwner(Company owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "Equity [equityId=" + equityId + ", name=" + name + ", price=" + price + ", owner=" + owner + "]";
	}

}
