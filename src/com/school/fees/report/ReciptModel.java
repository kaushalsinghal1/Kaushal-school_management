package com.school.fees.report;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.school.constant.ApplicationConstant;
import com.school.hiebernate.dbo.DepositeFeeMaster;
import com.school.shared.SchoolInfo;

public class ReciptModel {
	private DepositeFeeMaster feeMaster;
	private int totalAmt;
	private SchoolInfo schoolInfo = SchoolInfo.getInstance();

	public ReciptModel(DepositeFeeMaster feeMaster) {
		this.feeMaster = feeMaster;
	}

	public DepositeFeeMaster getFeeMaster() {
		return feeMaster;
	}

	public List<String[]> getDetailsAndAmout() {
		totalAmt = 0;
		List<String[]> list = new ArrayList<String[]>();
		String s[] = new String[2];
		s[0] = "Admission Fee";
		s[1] = feeMaster.getAdmissionFee() + "";
		totalAmt += feeMaster.getAdmissionFee();
		list.add(s);
		s = new String[2];
		s[0] = "Tution Fee";
		s[1] = feeMaster.getTuitionFee() + "";
		totalAmt += feeMaster.getTuitionFee();
		list.add(s);
		s = new String[2];
		s[0] = "Activity & Others Fee";
		s[1] = feeMaster.getActivityFee() + "";
		totalAmt += feeMaster.getActivityFee();
		list.add(s);
		s = new String[2];
		s[0] = "Bus Fee";
		s[1] = feeMaster.getBusFee() + "";
		totalAmt += feeMaster.getBusFee();
		list.add(s);
		return list;
	}

	// S.No.,Description,Amount
	public String[] getTableHeaderDetails() {

		String s[] = new String[3];
		s[0] = "S.No.";
		s[1] = "Description";
		s[2] = "Amount";
		return s;
	}

	public String[] getTotAmountWithDes() {
		String s[] = new String[2];
		s[0] = "Grand Total";
		s[1] = totalAmt + "";
		return s;
	}

	public Image getLogo() {
		Image image = null;
		if (schoolInfo.getIconPath() == null
				|| "".equals(schoolInfo.getIconPath().trim())) {
			return null;
		}
		try {
			File f = new File(schoolInfo.getIconPath());
			image = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	public void setFeeMaster(DepositeFeeMaster feeMaster) {
		this.feeMaster = feeMaster;
	}

	public String getFooterString() {
		return "Signature";
	}

	public String getSchoolTitle() {
		return schoolInfo.getName();
	}

	public String getSchoolSubTitle() {
		return schoolInfo.getSubtitle();
	}

	public String getStudentNamewithLabel() {
		StringBuffer sb = new StringBuffer();
		sb.append("Name: ");

		if (feeMaster.getStudentDetails().getFirstName() != null) {
			sb.append(feeMaster.getStudentDetails().getFirstName());
			sb.append(" ");
		}

		if (feeMaster.getStudentDetails().getLastName() != null) {
			sb.append(feeMaster.getStudentDetails().getLastName());
		}

		return sb.toString();
	}

	public String getClassNameWithLabel() {
		return "Class: " + feeMaster.getClassDetails().getClassName();
	}

	public String getSessionNameWithLabel() {
		return "Session: " + feeMaster.getSessionDetails().getDisplayString();
	}

	public String getRecieptNoWithLabel() {
		return "Reciept No:" + feeMaster.getDepositeFeeId();
	}

	public String getDateWithLabel() {
		StringBuffer sb = new StringBuffer();
		sb.append("Date: ");

		if (feeMaster.getDepositeDate() != null) {
			sb.append(ApplicationConstant.sdf_date.format(feeMaster
					.getDepositeDate()));
		}

		return sb.toString();
	}

}
