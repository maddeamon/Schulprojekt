package hts.projekt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Company implements IsSerializable {

	private Long companyId;

	private String name;

	public Company() {
		companyId = null;
		name = null;
	}

	public Company(Long companyId, String name) {
		super();
		this.companyId = companyId;
		this.name = name;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Company [companyId=" + companyId + ", name=" + name + "]";
	}

}
