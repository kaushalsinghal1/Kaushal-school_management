package com.school.hiebernate.dbo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "STUDENT_FEE_DETAILS")
public class StudentFeeDetails {
	@Id
	@Column(name = "STUDENT_FEE_ID")
	private int studentFeeId;
	@OneToOne
	@JoinColumn(name = "STUDENT_ID")
	private StudentDetails studentDetails;
	@OneToOne
	@JoinColumn(name = "SESSION_ID")
	private SessionDetails sessionDetails;
	@OneToOne
	@JoinColumn(name = "CLASS_ID")
	private ClassDetails classDetails;
	// admission
	@Column(name = "ADMISSION_FEE")
	private int admissionFee;

	@Column(name = "ADMISSION_FEE_DISCOUNT")
	private int admissionFeeDiscount;
	@Column(name = "ADMISSION_FEE_DEPOSIE")
	private int admissionFeeDeposite;
	// tution
	@Column(name = "TUITION_FEE")
	private int tuitionFee;
	@Column(name = "TUITION_FEE_DISCOUNT")
	private int tuitionFeeDiscount;
	@Column(name = "TUITION_FEE_DEPOSITE")
	private int tuitionFeeDeposite;
	// activity
	@OneToOne
	@JoinColumn(name = "SESSION_CLASS_FEE_ID")
	private SessionClassFeeDetails sessionClassFeeDetails;
	public SessionClassFeeDetails getSessionClassFeeDetails() {
		return sessionClassFeeDetails;
	}

	public void setSessionClassFeeDetails(
			SessionClassFeeDetails sessionClassFeeDetails) {
		this.sessionClassFeeDetails = sessionClassFeeDetails;
	}

//	@Transient//fields, it will not persistence in DB
	@Column(name = "ACTIVITY_FEE")
	private int activityFee;
	@Column(name = "ACTIVITY_FEE_DISCOUNT")
	private int activityFeeDiscount;
	@Column(name = "ACTIVITY_FEE_DEPOSITE")
	private int activityFeeDeposite;

	// bus
	@Column(name = "BUS_FEE")
	private int busFee;
	@Column(name = "BUS_FEE_DISCOUNT")
	private int busFeeDiscount;
	@Column(name = "BUS_FEE_DEPOSITE")
	private int busFeeDeposite;

	@Column(name = "ADDED_DATE", length = 19)
	private Date addDate;

	@Column(name = "LAST_DEPOSITE_DATE")
	private Date lastDepositeDate;

	public Date getLastDepositeDate() {
		return lastDepositeDate;
	}

	public void setLastDepositeDate(Date lastDepositeDate) {
		this.lastDepositeDate = lastDepositeDate;
	}

	@Column(name = "UPDATED_DATE")
	private Date updateDate;

	@Column(name = "DELETED_DATE")
	private Date deleteDate;

	public int getStudentFeeId() {
		return studentFeeId;
	}

	public void setStudentFeeId(int studentFeeId) {
		this.studentFeeId = studentFeeId;
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

	public int getAdmissionFeeDiscount() {
		return admissionFeeDiscount;
	}

	public void setAdmissionFeeDiscount(int admissionFeeDiscount) {
		this.admissionFeeDiscount = admissionFeeDiscount;
	}

	public int getAdmissionFeeDeposite() {
		return admissionFeeDeposite;
	}

	public void setAdmissionFeeDeposite(int admissionFeeDeposite) {
		this.admissionFeeDeposite = admissionFeeDeposite;
	}

	public int getTuitionFee() {
		return tuitionFee;
	}

	public void setTuitionFee(int tuitionFee) {
		this.tuitionFee = tuitionFee;
	}

	public int getTuitionFeeDiscount() {
		return tuitionFeeDiscount;
	}

	public void setTuitionFeeDiscount(int tuitionFeeDiscount) {
		this.tuitionFeeDiscount = tuitionFeeDiscount;
	}

	public int getTuitionFeeDeposite() {
		return tuitionFeeDeposite;
	}

	public void setTuitionFeeDeposite(int tuitionFeeDeposite) {
		this.tuitionFeeDeposite = tuitionFeeDeposite;
	}

	public int getActivityFee() {
		return activityFee;
	}

	public void setActivityFee(int activityFee) {
		this.activityFee = activityFee;
	}

	public int getActivityFeeDiscount() {
		return activityFeeDiscount;
	}

	public void setActivityFeeDiscount(int activityFeeDiscount) {
		this.activityFeeDiscount = activityFeeDiscount;
	}

	public int getActivityFeeDeposite() {
		return activityFeeDeposite;
	}

	public void setActivityFeeDeposite(int activityFeeDeposite) {
		this.activityFeeDeposite = activityFeeDeposite;
	}

	public int getBusFee() {
		return busFee;
	}

	public void setBusFee(int busFee) {
		this.busFee = busFee;
	}

	public int getBusFeeDiscount() {
		return busFeeDiscount;
	}

	public void setBusFeeDiscount(int busFeeDiscount) {
		this.busFeeDiscount = busFeeDiscount;
	}

	public int getBusFeeDeposite() {
		return busFeeDeposite;
	}

	public void setBusFeeDeposite(int busFeeDeposite) {
		this.busFeeDeposite = busFeeDeposite;
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

	public int getTotalAdmission() {
		return admissionFee - admissionFeeDiscount;
	}

	public int getTotalAdmissionDue() {
		return admissionFee - admissionFeeDiscount - admissionFeeDeposite;
	}

	public int getTotalTutionFee() {
		return tuitionFee - tuitionFeeDiscount;
	}

	public int getTotalTutionDue() {
		return tuitionFee - tuitionFeeDiscount - tuitionFeeDeposite;
	}

	public int getTotalBus() {
		return busFee - busFeeDiscount;
	}

	public int getTotalBusDue() {
		return busFee - busFeeDiscount - busFeeDeposite;
	}

	public int getTotalActivity() {
		return activityFee - activityFeeDiscount;
	}

	public int getTotalActivityDue() {
		return activityFee - activityFeeDiscount - activityFeeDeposite;
	}

	public int getTotalFeeWithOutDiscount() {
		return admissionFee + tuitionFee + activityFee + busFee;
	}

	public int getTotalDiscount() {
		return admissionFeeDiscount + tuitionFeeDiscount + activityFeeDiscount
				+ busFeeDiscount;
	}

	public int getTotalFees() {
		return getTotalFeeWithOutDiscount() - getTotalDiscount();
	}

	public int getTotalDepositedFee() {
		return admissionFeeDeposite + tuitionFeeDeposite + activityFeeDeposite
				+ busFeeDeposite;
	}

	public int getTotalDueFee() {
		return getTotalFees() - getTotalDepositedFee();
	}

	public int[] getPerMonthWithoutAdmission() {
		int fees[] = new int[4];
		fees[0] = 0;
		fees[1] = getTotalTutionDue() - getTotalTutionFee() / 12 > 0 ? getTotalTutionFee() / 12
				: getTotalTutionDue();
		fees[2] = getTotalActivityDue() - getTotalActivity() / 12 > 0 ? getTotalActivity() / 12
				: getTotalActivityDue();
		fees[3] = getTotalBusDue() - getTotalBus() / 12 > 0 ? getTotalBus() / 12
				: getTotalBusDue();
		return fees;
	}

	public int[] getTwoMonthWithoutAdmission() {
		int fees[] = new int[4];
		fees[0] = 0;
		fees[1] = getTotalTutionDue() - getTotalTutionFee() / 6 > 0 ? getTotalTutionFee() / 6
				: getTotalTutionDue();
		fees[2] = getTotalActivityDue() - getTotalActivity() / 6 > 0 ? getTotalActivity() / 6
				: getTotalActivityDue();
		fees[3] = getTotalBusDue() - getTotalBus() / 6 > 0 ? getTotalBus() / 6
				: getTotalBusDue();
		return fees;
	}

	public int[] getQuaterlyWithoutAdmission() {
		int fees[] = new int[4];
		fees[0] = 0;
		fees[1] = getTotalTutionDue() - getTotalTutionFee() / 4 > 0 ? getTotalTutionFee() / 4
				: getTotalTutionDue();
		fees[2] = getTotalActivityDue() - getTotalActivity() / 4 > 0 ? getTotalActivity() / 4
				: getTotalActivityDue();
		fees[3] = getTotalBusDue() - getTotalBus() / 4 > 0 ? getTotalBus() / 4
				: getTotalBusDue();
		return fees;
	}

	public int[] getHalfYearlyWithoutAdmission() {
		int fees[] = new int[4];
		fees[0] = 0;
		fees[1] = getTotalTutionDue() - getTotalTutionFee() / 2 > 0 ? getTotalTutionFee() / 2
				: getTotalTutionDue();
		fees[2] = getTotalActivityDue() - getTotalActivity() / 2 > 0 ? getTotalActivity() / 2
				: getTotalActivityDue();
		fees[3] = getTotalBusDue() - getTotalBus() /2 > 0 ? getTotalBus() / 2
				: getTotalBusDue();
		return fees;
	}

	public int[] getYearlyWithoutAdmission() {
		int fees[] = new int[4];
		fees[0] = 0;
		fees[1] = getTotalTutionFee();
		fees[2] = getTotalActivity();
		fees[3] =  getTotalBus();
		return fees;
	}

	public int[] getPerMonthWithAdmission() {
		int fees[] = new int[4];
		fees[0] = getTotalAdmission() / 12;
		fees[1] = getTotalTutionFee() / 12;
		fees[2] = getTotalActivity() / 12;
		fees[3] =  getTotalBus()/12;
		// int t = (getTotalAdmission() + getTotalActivity() +
		// getTotalAdmission()) / 12;
		return fees;
	}

}
