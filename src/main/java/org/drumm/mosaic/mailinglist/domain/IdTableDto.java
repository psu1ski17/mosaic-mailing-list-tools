package org.drumm.mosaic.mailinglist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdTableDto {
	private String lastName;
	private String firstName;
	private String campus;
	private String individualId;
	private String familyId;
	private String familyPosition;
	private String email;

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
		return campus;
	}

	@JsonProperty("Campus")
	public void setCampus(String campus) {
		this.campus = campus;
	}
	@JsonProperty("Individual ID")
	public String getIndividualId() {
		return individualId;
	}
	@JsonProperty("Individual ID")
	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}
	@JsonProperty("Family ID")
	public String getFamilyId() {
		return familyId;
	}
	@JsonProperty("Family ID")
	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}
	@JsonProperty("Family Position")
	public String getFamilyPosition() {
		return familyPosition;
	}
	@JsonProperty("Family Position")
	public void setFamilyPosition(String familyPosition) {
		this.familyPosition = familyPosition;
	}
	@JsonProperty("Email")
	public String getEmail() {
		return email;
	}
	@JsonProperty("Email")
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "IdTableDto [lastName=" + lastName + ", firstName=" + firstName
				+ ", campus=" + campus + ", individualId=" + individualId
				+ ", familyId=" + familyId + ", familyPosition="
				+ familyPosition + ", email=" + email + "]";
	}

}
