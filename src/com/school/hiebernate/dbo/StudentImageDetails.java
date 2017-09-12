package com.school.hiebernate.dbo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "STUDENT_IMAGE_DETAILS")
public class StudentImageDetails {
	@Id
	@Column(name = "IMAGE_ID")
	private int imageId;
	@Lob
	@Column(name = "PHOTO")
	private byte[] photo;
	@Column(name = "OLD_SCHOOL_INFO")
	private String oldSchoolName;
	@Column(name = "OLD_SCHOOL_CLASS")
	private String className;
	@Column(name = "OLD_SCHOOL_YEAR")
	private String year;
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

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getOldSchoolName() {
		return oldSchoolName;
	}

	public void setOldSchoolName(String oldSchoolName) {
		this.oldSchoolName = oldSchoolName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

}
