package com.banti.framework.platform.licence;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.banti.framework.platform.Activator;
import com.banti.framework.platform.LicenseManager;

/**
 * This abstract Activator can activate the relevant Plug-in based on the
 * License Code from "java.util.prefs".
 */
public abstract class AbstractPrefsActivator implements Activator {

	// abstract public String getModuleName();

	protected String header;

	private boolean userMode;
	protected int TRIALDAYS = 1;
	public static String EXPIRE_TRIAL = "SCH0-000000000000";

	public AbstractPrefsActivator() {
		this(true);
	}

	/**
	 * Instantiates itself without a header. Then no-header license code is
	 * available like;<br>r
	 * <font color="green"><b>"PLUGIN-7E2649-22D77C066738ED6C-0"</b></font>
	 * 
	 */
	public AbstractPrefsActivator(boolean userMode) {
		super();
		header = "";
		this.userMode = userMode;
		setTrial();
	}

	public AbstractPrefsActivator(String header) {
		this(header, true);
	}

	protected abstract int getTrialPeriond();

	private void setTrial() {
		TRIALDAYS = getTrialPeriond();
	}
	public int getTrial() {
		return TRIALDAYS ;//= getTrialPeriond();
	}

	/**
	 * Instantiates itself with a header. Then a license code like<br>
	 * <font color="blue"><b>"712A30-84406D7778FC3F3A-0"</b></font><br>
	 * is available if LicenseManager generates <br>
	 * <font color="red"><b>"EX1-712A30-84406D7778FC3F3A-0"</b></font><br>
	 * .
	 * 
	 * @param header
	 *            a specified header like "EX1".
	 */
	public AbstractPrefsActivator(String header, boolean userMode) {
		this.header = header + "-";
		this.userMode = userMode;
		setTrial();
	}

	/**
	 * Returns true if the relevant Plugin's license code is registered in
	 * "java.util.prefs".
	 * 
	 * @see com.cysols.framework.platform.Activator#isActivated()
	 */
	public final boolean isActivated() {
		String myLicense = this.retrieveCode(getClass());
		return this.validate(myLicense) ? true : isTrialActivated();
	}

	public final boolean isMainActivated() {
		String myLicense = this.retrieveCode(getClass());
		return this.validate(myLicense);
	}

	/**
	 * 
	 * @return true if trial is registered
	 * @return false otherwise
	 */
	public final boolean isTrialRegistered() {
		String code = this.retrieveCode(getClass());
		if (code == null || "".equals(code)) {
			return false;
		}
		return true;
	}

	public final boolean isTrialActivated() {
		String myLicense = this.retrieveCode(getClass());
		return this.validateTrial(myLicense);
	}

	public final String getRemainingTrialDays() {
		String myLicense = this.retrieveCode(getClass());
		try {
			String s = myLicense.substring(LicenseManager.trialCodePreFix
					.length());
			long time = Long.parseLong(s);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(time);
			cal.add(Calendar.DATE, TRIALDAYS);

			long dif = cal.getTimeInMillis() - System.currentTimeMillis();
			int days = (int) (dif / (1000 * 3600 * 24));
			return days + " days";

		} catch (NumberFormatException e) {

		}
		return "";
	}

	private boolean validateTrial(String myLicense) {
		if (myLicense == null || "".equals(myLicense)
				|| EXPIRE_TRIAL.equals(myLicense)) {
			return false;
		}
		try {
			String s = myLicense.substring(LicenseManager.trialCodePreFix
					.length());
			long time = Long.parseLong(s);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(time);
			cal.add(Calendar.DATE, TRIALDAYS);
			if (cal.getTimeInMillis() >= System.currentTimeMillis()) {
				return true;
			} else {
				PrefsLicenseManager licenseManager = new PrefsLicenseManager();
				licenseManager.registerTrial(this, EXPIRE_TRIAL);
			}
		} catch (NumberFormatException e) {

		}
		return false;
	}

	public final boolean validate(String licenseCode) {
		try {
			PrefsLicenseDelegator delegator = new PrefsLicenseDelegator(
					userMode);
			return delegator.decryptLicense(header + licenseCode);
		} catch (InvalidKeyException e) {
			return false;
		} catch (InvalidKeySpecException e) {
			return false;
		} catch (NoSuchAlgorithmException e) {
			return false;
		} catch (NoSuchPaddingException e) {
			return false;
		} catch (IllegalStateException e) {
			return false;
		} catch (IllegalBlockSizeException e) {
			return false;
		} catch (BadPaddingException e) {
			return false;
		}
	}

	/**
	 * Returns false always because this class can activate based on
	 * LicenseCode.
	 * 
	 * @see com.cysols.framework.platform.Activator#isNoLicense()
	 */
	public boolean isNoLicense() {
		return false;
	}

	/**
	 * Retrieves License Code from System preference by using
	 * java.util.pref.Prefernces. Then, "Code" is used as a key. The code is
	 * retrieved from Not User preference But from System preference. Activator
	 * itself is used for specifying a node in a hierarchical collection of
	 * preference data.
	 * 
	 * @param activator
	 *            - Activation instance itself. It is almost used like
	 *            "retrieveCode(this)" by the concrete Activator that extends
	 *            this class.
	 * @return - String License Code from system preference.
	 * @throws SecurityException
	 *             - if a security manager is present and it denies
	 */
	protected final String retrieveCode(Class activator)
			throws SecurityException {
		String storedCode = "";

		if (activator == null)
			return storedCode;

		PrefsLicenseDelegator delegator = new PrefsLicenseDelegator(userMode);
		String key = activator.getName();
		try {
			storedCode = delegator.read(activator, key);
			if (storedCode == null)
				storedCode = "";
		} catch (RuntimeException e) {
			storedCode = "";
		}
		return storedCode;
	}

}