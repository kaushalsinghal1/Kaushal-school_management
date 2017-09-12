package com.school.hiebernate.dbo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SESSION_CLASS_FEE_DETAILS")
public class SessionClassFeeDetails {
	@Id
	@Column(name = "SESSION_FEE_ID")
	private int sessionFeeId;
	@OneToOne
	@JoinColumn(name = "SESSION_ID")
	private SessionDetails sessionDetails;
	@OneToOne
	@JoinColumn(name = "CLASS_ID")
	private ClassDetails classDetails;
	@Column(name = "ADMISSION_FEE")
	private int admissionFee;
	@Column(name = "TUITION_FEE")
	private int tuitionFee;
	@Column(name = "ACTIVITY_FEE")
	private int activityFee;
	@Column(name = "BUS_FEE")
	private int busFee;

	@Column(name = "ADDED_DATE", length = 19)
	private Date addDate;

	@Column(name = "UPDATED_DATE")
	private Date updateDate;

	@Column(name = "DELETED_DATE")
	private Date deleteDate;

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

	public int getSessionFeeId() {
		return sessionFeeId;
	}

	public void setSessionFeeId(int sessionFeeId) {
		this.sessionFeeId = sessionFeeId;
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

	public int getAdmissionFee() {
		return admissionFee;
	}

	public int getBusFee() {
		return busFee;
	}

	public void setBusFee(int busFee) {
		this.busFee = busFee;
	}


	public void setAdmissionFee(int admissionFee) {
		this.admissionFee = admissionFee;
	}

	public int getTuitionFee() {
		return tuitionFee;
	}

	public void setTuitionFee(int tuitionFee) {
		this.tuitionFee = tuitionFee;
	}

	public int getActivityFee() {
		return activityFee;
	}

	public void setActivityFee(int activityFee) {
		this.activityFee = activityFee;
	}

}
