package org.drumm.mosaic.mailinglist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CcbTransaction {
	private String name;
	private String campus;
	private String id;
	private String mobilePhone;
	private String homePhone;
	private String email;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String coaCategory;
	private String date;

	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	@JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("Campus")
	public String getCampus() {
		return campus;
	}

	@JsonProperty("Campus")
	public void setCampus(String campus) {
		this.campus = campus;
	}

	@JsonProperty("Ind ID")
	public String getId() {
		return id;
	}

	@JsonProperty("Ind ID")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("Mobile Phone")
	public String getMobilePhone() {
		return mobilePhone;
	}

	@JsonProperty("Mobile Phone")
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@JsonProperty("Home Phone")
	public String getHomePhone() {
		return homePhone;
	}

	@JsonProperty("Home Phone")
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	@JsonProperty("Email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("Email")
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("Street")
	public String getStreet() {
		return street;
	}

	@JsonProperty("Street")
	public void setStreet(String street) {
		this.street = street;
	}

	@JsonProperty("City")
	public String getCity() {
		return city;
	}

	@JsonProperty("City")
	public void setCity(String city) {
		this.city = city;
	}

	@JsonProperty("State")
	public String getState() {
		return state;
	}

	@JsonProperty("State")
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("Zip")
	public String getZip() {
		return zip;
	}

	@JsonProperty("Zip")
	public void setZip(String zip) {
		this.zip = zip;
	}

	@JsonProperty("COA Category")
	public String getCoaCategory() {
		return coaCategory;
	}

	@JsonProperty("COA Category")
	public void setCoaCategory(String coaCategory) {
		this.coaCategory = coaCategory;
	}

	@JsonProperty("Date")
	public String getDate() {
		return date;
	}

	@JsonProperty("Date")
	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((campus == null) ? 0 : campus.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((coaCategory == null) ? 0 : coaCategory.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((homePhone == null) ? 0 : homePhone.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((mobilePhone == null) ? 0 : mobilePhone.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((zip == null) ? 0 : zip.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CcbTransaction other = (CcbTransaction) obj;
		if (campus == null) {
			if (other.campus != null)
				return false;
		} else if (!campus.equals(other.campus))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (coaCategory == null) {
			if (other.coaCategory != null)
				return false;
		} else if (!coaCategory.equals(other.coaCategory))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (homePhone == null) {
			if (other.homePhone != null)
				return false;
		} else if (!homePhone.equals(other.homePhone))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (mobilePhone == null) {
			if (other.mobilePhone != null)
				return false;
		} else if (!mobilePhone.equals(other.mobilePhone))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (zip == null) {
			if (other.zip != null)
				return false;
		} else if (!zip.equals(other.zip))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CcbTransaction [name=" + name + ", campus=" + campus + ", id="
				+ id + ", mobilePhone=" + mobilePhone + ", homePhone="
				+ homePhone + ", email=" + email + ", street=" + street
				+ ", city=" + city + ", state=" + state + ", zip=" + zip
				+ ", coaCategory=" + coaCategory + ", date=" + date + "]";
	}
}
