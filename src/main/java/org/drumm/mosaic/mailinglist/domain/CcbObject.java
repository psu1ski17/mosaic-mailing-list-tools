package org.drumm.mosaic.mailinglist.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CcbObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// "Last Name","First Name",Campus,"Mailing Street","Mailing City","Mailing State","Mailing Zip","Family Primary Contact"
	protected String lastName;
	protected String firstName;
	protected String Campus;
	protected String mailingStreet;
	protected String mailingCity;
	protected String mailingState;
	protected String mailingZip;
	protected String familyPrimaryContact;

	@JsonProperty("Last Name")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("Last Name")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("First Name")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("First Name")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("Campus")
	public String getCampus() {
		return Campus;
	}

	@JsonProperty("Campus")
	public void setCampus(String campus) {
		Campus = campus;
	}

	@JsonProperty("Mailing Street")
	public String getMailingStreet() {
		return mailingStreet;
	}

	@JsonProperty("Mailing Street")
	public void setMailingStreet(String mailingStreet) {
		this.mailingStreet = mailingStreet;
	}

	@JsonProperty("Mailing City")
	public String getMailingCity() {
		return mailingCity;
	}

	@JsonProperty("Mailing City")
	public void setMailingCity(String mailingCity) {
		this.mailingCity = mailingCity;
	}

	@JsonProperty("Mailing State")
	public String getMailingState() {
		return mailingState;
	}

	@JsonProperty("Mailing State")
	public void setMailingState(String mailingState) {
		this.mailingState = mailingState;
	}

	@JsonProperty("Mailing Zip")
	public String getMailingZip() {
		return mailingZip;
	}

	@JsonProperty("Mailing Zip")
	public void setMailingZip(String mailingZip) {
		this.mailingZip = mailingZip;
	}

	@JsonProperty("Family Primary Contact")
	public String getFamilyPrimaryContact() {
		return familyPrimaryContact;
	}

	@JsonProperty("Family Primary Contact")
	public void setFamilyPrimaryContact(String familyPrivaryContact) {
		this.familyPrimaryContact = familyPrivaryContact;
	}

	@Override
	public String toString() {
		return "CcbFormat [lastName=" + lastName + ", firstName=" + firstName
				+ ", Campus=" + Campus + ", mailingStreet=" + mailingStreet
				+ ", mailingCity=" + mailingCity + ", mailingState="
				+ mailingState + ", mailingZip=" + mailingZip
				+ ", familyPrimaryContact=" + familyPrimaryContact + "]";
	}

	public String toAddressString() {
		return this.getMailingStreet() + " " + this.getMailingCity() + ", "
				+ this.getMailingState() + " " + this.getMailingZip();
	}
}
