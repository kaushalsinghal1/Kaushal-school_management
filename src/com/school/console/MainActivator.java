package com.school.console;

import com.banti.framework.platform.LicenseManager;
import com.banti.framework.platform.licence.AbstractPrefsActivator;

public class MainActivator extends AbstractPrefsActivator {
	private final String MODULE_NAME = LicenseManager.MAIN_LICENCE;

	// vesion V10
	private final static String HEADER = "SCHV10"; // '-' add in super

	public MainActivator() {
		super(HEADER);
	}

	@Override
	public String getModuleName() {
		return MODULE_NAME;
	}

	@Override
	public boolean isNoLicense() {
		return false;
	}

	@Override
	protected int getTrialPeriond() {
		return ApplicationConf.getInstance().getTrialPeriod();
	}

}
