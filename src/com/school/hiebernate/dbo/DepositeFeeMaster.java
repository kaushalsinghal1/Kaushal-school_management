package com.school.hiebernate.dbo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DEPOSITE_FEE_MASTER")
public class DepositeFeeMaster {
	@Id
	@Column(name = "DEPOSITE_FEE_ID")
	private int depositeFeeId;
	@OneToOne
	@JoinColumn(name = "STUDENT_ID")
	private StudentDetails studentDetails;
	private String cachierName;
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
	
	public int getBusFee() {
		return busFee;
	}

	public void setBusFee(int busFee) {
		this.busFee = busFee;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "TOTAL_AMOUNT")
	private int totalAmount;

	@Column(name = "DEPOSITE_DATE", length = 19)
	private Date depositeDate;

	@Column(name = "UPDATED_DATE")
	private Date updateDate;

	@Column(name = "DELETED_DATE")
	private Date deleteDate;

	public int getDepositeFeeId() {
		return depositeFeeId;
	}

	public void setDepositeFeeId(int depositeFeeId) {
		this.depositeFeeId = depositeFeeId;
	}

	public String getCachierName() {
		return cachierName;
	}

	public void setCachierName(String cachierName) {
		this.cachierName = cachierName;
	}

	public StudentDetails getStudentDetails() {
		return studentDetails;
	}

	public void setStudentDetails(StudentDetails studentDetails) {
		this.studentDetails = studentDetails;
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

	public Date getDepositeDate() {
		return depositeDate;
	}

	public void setDepositeDate(Date depositeDate) {
		this.depositeDate = depositeDate;
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

}
