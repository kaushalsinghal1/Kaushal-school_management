package com.school.hiebernate.dbo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CLASS_DETAILS")
public class ClassDetails {
	@Id
	@Column(name = "CLASS_ID")
	private int classId;

	@Column(name = "DISPLAY_SESSION")
	private String displaySession;
	@Column(name = "CLASS_NAME")
	private String className;

	@Column(name = "ADDED_DATE")
	private Date addDate;

	@Column(name = "UPDATED_DATE")
	private Date updateDate;

	@Column(name = "DELETED_DATE")
	private Date deleteDate;

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
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

	public String getDisplaySession() {
		return displaySession;
	}

	public void setDisplaySession(String displaySession) {
		this.displaySession = displaySession;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public String toString(){
		return className;
	}

}
