package com.school.console;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.banti.framework.core.Account;
import com.banti.framework.platform.Activator;
import com.banti.framework.platform.Application;
import com.banti.framework.platform.LicenseManager;
import com.banti.framework.platform.LoginListener;
import com.banti.framework.platform.LogoutListener;
import com.banti.framework.platform.licence.AbstractPrefsActivator;
import com.banti.framework.platform.licence.PrefsLicenseManager;
import com.banti.framework.theme.ToolKit;

public class LicenceManagement implements LoginListener, LogoutListener {
	private Activator mainActivator;

	public LicenceManagement(Activator mainActivator) {
		this.mainActivator = mainActivator;
	}

	@Override
	public void loginSucceeded(Account account) {
		boolean activate=true;
		if (mainActivator instanceof AbstractPrefsActivator) {
			AbstractPrefsActivator activator = (AbstractPrefsActivator) mainActivator;
			if (!activator.isMainActivated()) {
				if (activator.isTrialRegistered()) {
					if (activator.isTrialActivated()) {
						int i = showWarning("You are using trial version that will expire "
								+ activator.getRemainingTrialDays()
								+ ".\n Do you want to register");
						if (i == 0) {
							showAndRegisterLicence();
						}
					} else {
						int i = showWarning("Trial version is expired .\n Do you want to register");
						if (i == 0) {
							if (!showAndRegisterLicence()) {
								logoutApp();
								activate=false;
							}
						} else {
							logoutApp();
							activate=false;
						}
					}
				} else {
					String enteredCode = LicenseManager.trialCodePreFix
							+ System.currentTimeMillis();
					PrefsLicenseManager prefsLicenseManager = new PrefsLicenseManager();
					prefsLicenseManager.registerTrial(activator, enteredCode);
					int i = showWarning("You are using trial version that will be expired in "
							+ activator.getTrial()
							+ " days.\n Do you want to register");
					if (i == 0) {
						showAndRegisterLicence();
					}
				}
			}
		}
		if(activate){
			new SetUpApplication().loginSucceeded(account);
		}
	}

	private void logoutApp() {
//		SchoolMain.getInstance().logOutApplication();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SchoolMain.getInstance().logOutApplication();
			}
		});
	}

	private boolean showAndRegisterLicence() {

		PrefsLicenseManager manager = new PrefsLicenseManager();

		String enteredCode = JOptionPane.showInputDialog(
				ToolKit.getString("LICENSE_ENTRY"), "");
		if (enteredCode == null) {
			return false;
		}

		/*
		 * Calls LicenseManager.register method. The registration is delegated
		 * to LicenseManager.
		 */
		AbstractPrefsActivator activator = (AbstractPrefsActivator) mainActivator;
		boolean entered = manager.register(activator, enteredCode);
		if (entered) {
			JOptionPane.showMessageDialog(Application.Frame,
					ToolKit.getString("MSG_LICENSE_VALID"),
					ToolKit.getString("INFORMATION"),
					JOptionPane.INFORMATION_MESSAGE);
			return true;
		} else {
			JOptionPane.showMessageDialog(Application.Frame,
					ToolKit.getString("MSG_LICENSE_INVALID"),
					ToolKit.getString("INFORMATION"),
					JOptionPane.INFORMATION_MESSAGE);
			return showAndRegisterLicence();
		}
	}

	@Override
	public void loginFailed() {
	}

	@Override
	public void logoutSucceeded(Account account) {
	}


	@Override
	public void logoutFailed() {

	}

	private int showWarning(String message) {
		String[] s = { "Yes", "No" };
		return JOptionPane.showOptionDialog(SchoolMain.Frame, message,
				"Warning", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null, s, s[0]);
	}

}
