package com.school.shared;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import com.school.constant.ApplicationConstant;
import com.school.utils.Logger;

public class SchoolInfo implements ApplicationConstant {
	private static SchoolInfo instance;
	private String name;
	private String subtitle;
	private Image logo;
	private String iconPath = "config/report/logo.png";
	private String address1;
	private String address2;
	private String idCardTitle;
	private static long lastReadConf;

	private SchoolInfo() {
		loadConf();
	}

	private void defaultInitialize() {
		idCardTitle = "Student Identity Card";
		name = "T.C. Jain H.S School";
		subtitle = "Hindi and English Medium School";
		address1 = "Pinahat Road Ambah ";
		address2 = "Dist-Morena 476111,Phone:67890987";
		try {
			logo = ImageIO.read(new File(iconPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static SchoolInfo getInstance() {
		if (instance == null) {
			instance = new SchoolInfo();
		}
		return instance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public Image getLogo() {
		// Image image = null;
		if (logo == null) {
			try {
				File file = new File(iconPath);
				logo = ImageIO.read(file);
			} catch (Exception e) {
				Logger.EXCEPTION
						.log(Level.WARNING, "Error while read image", e);
			}
		}
		return logo;
	}

	public void setLogo(Image logo) {
		this.logo = logo;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public static void setInstance(SchoolInfo instance) {
		SchoolInfo.instance = instance;
	}

	public String getIdCardTitle() {
		return idCardTitle;
	}

	public void setIdCardTitle(String idCardTitle) {
		this.idCardTitle = idCardTitle;
	}

	// -------------------
	private void loadConf() {
		File file = new File(SCHOOL_CONF_FILE);
		if (file.lastModified() < lastReadConf) {
			return;
		}
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			prop.load(fis);
			lastReadConf = System.currentTimeMillis();
		} catch (IOException e) {
			Logger.EXCEPTION.log(Level.WARNING,
					"Error While reading Application Configuration" + e);
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					Logger.EXCEPTION.log(Level.WARNING, "Error While closing"
							+ e);
				}
		}
		name = prop.getProperty(SCHOOL_NAME, DEFAULT_SCHOOL_NAME);
		subtitle = prop.getProperty(SCHOOL_SUB_TITLE, DEFAULT_SCHOOL_SUB_TITLE);
		address1 = prop.getProperty(SCHOOL_ADDRESS_LINE1,
				DEFAULT_SCHOOL_ADDRESS_LINE1);
		address2 = prop.getProperty(SCHOOL_ADDRESS_LINE2,
				DEFAULT_SCHOOL_ADDRESS_LINE2);
		iconPath = prop.getProperty(SCHOOL_LOG_PATH, DEFAULT_SCHOOL_LOG_PATH);
		idCardTitle = prop.getProperty(SCHOOL_IDCARD_TITLE,
				DEFAULT_SCHOOL_IDCARD_TITLE);
	}

	public void save() {
		Properties prop = new Properties();
		prop.setProperty(SCHOOL_NAME, name);
		prop.setProperty(SCHOOL_SUB_TITLE, subtitle);
		prop.setProperty(SCHOOL_ADDRESS_LINE1, address1);
		prop.setProperty(SCHOOL_ADDRESS_LINE2, address2);
		prop.setProperty(SCHOOL_LOG_PATH, iconPath);
		prop.setProperty(SCHOOL_IDCARD_TITLE, idCardTitle);
		FileOutputStream fio = null;
		try {
			fio = new FileOutputStream(new File(
					ApplicationConstant.SCHOOL_CONF_FILE));
			prop.store(fio, "Updated");

		} catch (IOException e) {
			Logger.EXCEPTION.log(Level.WARNING,
					"Error While Writing Application Configuration" + e);
		} finally {
			if (fio != null) {
				try {
					fio.close();
				} catch (IOException e) {
					Logger.EXCEPTION.log(Level.WARNING, "Error While closing"
							+ e);
				}
			}
		}
	}
}
