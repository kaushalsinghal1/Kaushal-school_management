package com.school.hiebernate.dbo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "STUDENT_REGISTRATION")
public class StudentDetails implements Cloneable {
	@Id
	@Column(name = "STUDENT_ID")
	private int studentId;
	@Column(name = "REGISTRATION_NUMBER")
	private String registrationNumber;
	@OneToOne
	@JoinColumn(name = "SESSION_ID")
	private SessionDetails sessionDetails;
	@OneToOne
	@JoinColumn(name = "CLASS_ID")
	private ClassDetails classDetails;
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "LAST_NAME")
	private String lastName;
	@Column(name = "FATHER_NAME")
	private String fatherName;
	@Column(name = "MOTHER_NAME")
	private String motherName;
	@Column(name = "DATE_OF_BIRTH")
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	@Column(name = "PARENTS_OCCUPATION")
	private String parentsOccupation;
	@Column(name = "MOBILE")
	private String mobile;
	@Column(name = "PHONE")
	private String phone;
	@Column(name = "EMAIL_ID")
	private String emailId;
	@Column(name = "ADDRESS")
	private String address;
	@OneToOne
	@JoinColumn(name = "IMAGE_ID")
	private StudentImageDetails studentImageDetails;

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public SessionDetails getSessionDetails() {
		return sessionDetails;
	}

	public void setSessionDetails(SessionDetails sessionDetails) {
		this.sessionDetails = sessionDetails;
	}

	public ClassDetails getClassDetails() {
		return classDetails;
	}

	public void setClassDetails(ClassDetails classDetails) {
		this.classDetails = classDetails;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getParentsOccupation() {
		return parentsOccupation;
	}

	public void setParentsOccupation(String parentsOccupation) {
		this.parentsOccupation = parentsOccupation;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	public StudentImageDetails getStudentImageDetails() {
		return studentImageDetails;
	}

	public void setStudentImageDetails(StudentImageDetails studentImageDetails) {
		this.studentImageDetails = studentImageDetails;
	}

	@Column(name = "CITY")
	private String city;
	@Column(name = "STATE")
	private String state;

	@Column(name = "ADDED_DATE", length = 19)
	private Date addDate;

	@Column(name = "UPDATED_DATE")
	private Date updateDate;

	@Column(name = "DELETED_DATE")
	private Date deleteDate;

	@Override
	public Object clone() throws CloneNotSupportedException {
		StudentDetails details = new StudentDetails();
		details.setFirstName(firstName);
		details.setLastName(lastName);
		details.setFatherName(fatherName);
		details.setMotherName(motherName);
		details.setMobile(mobile);
		details.setEmailId(emailId);
		details.setStudentImageDetails(studentImageDetails);
		details.setClassDetails(classDetails);
		details.setSessionDetails(sessionDetails);
		details.setAddress(address);
		details.setCity(city);
		details.setPhone(phone);
		details.setEmailId(emailId);
		details.setParentsOccupation(parentsOccupation);
		details.setState(state);

		return details;
	}
}
