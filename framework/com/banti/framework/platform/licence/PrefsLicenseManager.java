package com.banti.framework.platform.licence;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import com.banti.framework.platform.Activator;
import com.banti.framework.platform.LicenseManager;
import com.banti.framework.platform.PluginCommand;
import com.banti.framework.platform.PluginModule;

/**
 * This is an concrete <i>LicenseManager</i> class, based on "java.util.prefs".
 * This class can register/delete/read the License Code of the specified
 * PluginCommand by Preference API. This is used with
 * <i>AbstractPrefsActivator</i>. Both implementation are based on
 * "java.util.prefs" package. A default <i>LicenseManager</i> defined in
 * <i>DefaultHelpScheme</i> is this manager.
 */
public class PrefsLicenseManager implements LicenseManager {

	private ArrayList listeners;
	private boolean userMode;
	private boolean trial = true;

	public PrefsLicenseManager() {
		this(true);
	}

	public PrefsLicenseManager(boolean userMode) {
		super();
		listeners = new ArrayList(1);
		this.userMode = userMode;
	}

	public boolean registerTrial(PluginCommand command) {
		if (command == null)
			return false;

		if (!(command.getActivator() instanceof AbstractPrefsActivator))
			return false;
		AbstractPrefsActivator activator = (AbstractPrefsActivator) command
				.getActivator();

		try {
			PrefsLicenseDelegator delegator = new PrefsLicenseDelegator(
					userMode);
			Class c = activator.getClass();
			String enteredCode = trialCodePreFix + System.currentTimeMillis();
			delegator.register(c, c.getName(), enteredCode);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean registerTrial(AbstractPrefsActivator activator, String expire) {
		try {
			PrefsLicenseDelegator delegator = new PrefsLicenseDelegator(
					userMode);
			Class c = activator.getClass();
			delegator.register(c, c.getName(), expire);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean register(AbstractPrefsActivator activator, String enteredCode) {
		if (activator == null)
			return false;
		if (enteredCode == null || enteredCode.equals(""))
			return false;
	//	enteredCode = LicenseManager.trialCodePreFix + enteredCode;

		if (!activator.validate(enteredCode))
			return false;

		try {
			PrefsLicenseDelegator delegator = new PrefsLicenseDelegator(
					userMode);
			Class c = activator.getClass();
			delegator.register(c, c.getName(), enteredCode);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean register(PluginCommand command, String enteredCode) {
		if (command == null)
			return false;
		if (enteredCode == null || enteredCode.equals(""))
			return false;

		if (!(command.getActivator() instanceof AbstractPrefsActivator))
			return false;
		//enteredCode = LicenseManager.trialCodePreFix + enteredCode;
		AbstractPrefsActivator activator = (AbstractPrefsActivator) command
				.getActivator();

		if (!activator.validate(enteredCode))
			return false;

		try {
			PrefsLicenseDelegator delegator = new PrefsLicenseDelegator(
					userMode);
			Class c = activator.getClass();
			delegator.register(c, c.getName(), enteredCode);

			// command.accept(this);
			//
			// PropertyChangeListener[] listeners = activator
			// .getPropertyChangeListeners();
			// for (int i = 0; i < listeners.length; i++) {
			// PropertyChangeListener listener = listeners[i];
			// listener.propertyChange(new PropertyChangeEvent(this,
			// Activator.ACTIVATED, Boolean.valueOf(false), Boolean
			// .valueOf(true)));
			// }

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void visit(PluginCommand command) {
		Activator activator = command.getActivator();
		command.setVisible(activator.isActivated());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cysols.framework.platform.LicenseRegistry#delete(com.cysols.framework
	 * .platform.PluginCommand)
	 */
	public boolean delete(PluginCommand command) {
		if (command == null) {
			return false;
		}
		if (!(command.getActivator() instanceof AbstractPrefsActivator)) {
			return false;
		}

		AbstractPrefsActivator activator = (AbstractPrefsActivator) command
				.getActivator();
		Class c = activator.getClass();
		PrefsLicenseDelegator delegator = new PrefsLicenseDelegator(userMode);
		try {
			delegator.delete(c, c.getName());
			/*
			 * command.accept(this);
			 * 
			 * PropertyChangeListener[] listeners = activator
			 * .getPropertyChangeListeners(); for (int i = 0; i <
			 * listeners.length; i++) { PropertyChangeListener listener =
			 * listeners[i]; listener.propertyChange(new
			 * PropertyChangeEvent(this, AbstractPrefsActivator.ACTIVATED,
			 * Boolean.valueOf(true), Boolean.valueOf(false))); }
			 */

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cysols.framework.platform.LicenseManager#read(com.cysols.framework
	 * .platform.PluginCommand)
	 */
	public String read(PluginCommand command) {
		if (command == null)
			return null;

		if (!(command.getActivator() instanceof AbstractPrefsActivator))
			return null;

		AbstractPrefsActivator activator = (AbstractPrefsActivator) command
				.getActivator();
		return activator.retrieveCode(activator.getClass());
	}

	// Overrides super.addPropertyChangeListener method.
	public void addPropertyChangeListener(PropertyChangeListener pcListener) {
		synchronized (listeners) {
			listeners.add(pcListener);
		}
	}

	public void setEnabled(boolean aFlag) {
		synchronized (listeners) {
			for (int i = 0; i < listeners.size(); i++) {
				PropertyChangeListener listener = (PropertyChangeListener) listeners
						.get(i);
				listener.propertyChange(new PropertyChangeEvent(this,
						"ENABLED", null, new Boolean(aFlag)));
			}
		}
	}

	/**
	 * overridden method of license manager
	 */
	public boolean isResponseNull() {
		return false; // default
	}

	@Override
	public void visit(PluginModule module) {

	}

}
