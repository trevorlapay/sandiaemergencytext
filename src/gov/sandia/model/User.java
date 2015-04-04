package gov.sandia.model;

public class User {
	public User(String number, String provider) {
		super();
		this.number = number;
		this.provider = provider;
	}
	private String number;
	private String provider;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
}
