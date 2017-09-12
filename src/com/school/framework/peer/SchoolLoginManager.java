package com.school.framework.peer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;

import com.banti.framework.core.Account;
import com.banti.framework.platform.Application;
import com.banti.framework.platform.LoginManager;
import com.banti.framework.platform.peer.LoginDialog;
import com.banti.framework.theme.ToolKit;
import com.banti.framework.utils.MDEncoder;

public class SchoolLoginManager extends LoginManager {

	private static final String ACCOUNT_FILE = "config/accounts.prop";
	private static final String[] USER_SELECTION = {
			ToolKit.getString("SUPER"), ToolKit.getString("ADMIN"),
			ToolKit.getString("USER") };

	private Map<String, Account> accountMap;

	// private Properties baseProp;

	/**
	 * A constructor, that loads an account file from its local directory. The
	 * account file is put as "config/accounts.prop" which is a binary file. If
	 * the file doesn't exist, LocalLoginManager prepared
	 * <b>"Account: admin, Password: netskate"</b> as its default authenticated
	 * account.
	 */
	public SchoolLoginManager() {
		super();

		try {
			accountMap = this.load();

		} catch (IOException e) {
			accountMap = null;

		} catch (ClassNotFoundException e) {
			accountMap = null;
		}

		if (accountMap == null) {
			accountMap = new HashMap<String, Account>();
			// String validName = "21232F297A57A5A743894A0E4A801FC3";
			// 0CC175B9C0F1B6A831C399E269772661 a
			String validName = "admin";
			// 74D839D98630E280DF752E8939454A6B "admin"
			// String validPasswd = "81ADADFC78A574E76D70D5798EB7AEC9";
			String validPasswd = "74D839D98630E280DF752E8939454A6B";
			Account account = new Account(validName, validPasswd,
					ToolKit.getString("SUPER"), System.currentTimeMillis(),
					System.currentTimeMillis(), System.currentTimeMillis());

			accountMap.put(validName, account);
			// baseProp.setProperty(validName, validPasswd);
		}
	}

	/**
	 * Shows a LoginDialog. A user can enter his account and password. After
	 * that, <a href=
	 * "LocalLoginManager.html#authenticate(com.cysols.framework.platform.core.Account)"
	 * > authenticate(Account)</a> method is called, and he can login to your
	 * application if it is a correct pair of account-password.
	 * 
	 * @param application
	 *            your concrete application implementation.
	 * 
	 * @see com.cysols.framework.platform.LoginManager#login(com.cysols.framework.platform.Application)
	 */
	protected void login(Application application) {
		LoginDialog dialog = new LoginDialog(application,
				super.LOGIN_DIALOG_TITLE);
		dialog.setActionListener(new LoginAction(dialog));
		dialog.setVisible(true);

		super.login(application);
	}

	/**
	 * Authenticates a login account based on its account file. The account file
	 * is, of course, put on the local machine because this is
	 * LocalLoginManager. A user can login to your application only when his
	 * account and password are passed to the check of this method.
	 * 
	 * @param account
	 *            Account instance who want to login into your application.
	 * @return true if account and password entered by the user is correct;
	 *         false otherwise.
	 * 
	 * @see com.cysols.framework.platform.LoginManager#authenticate(com.cysols.framework.platform.core.Account)
	 */
	protected boolean authenticate(Account account) {
		String key = account.getName();
		String passwd = account.getPasswd();

		try {

			Account existAccount = accountMap.get(key);

			// To judge with out domain - For Enterprise Console and other
			// applications where domain cannot be specified in Login Screen
			if (existAccount == null) {
				int index = key.indexOf("@");
				if (index > 0) {
					String keyWithoutDomain = key.substring(0, index);
					existAccount = accountMap.get(key);
				}
			}

			try {
				passwd = MDEncoder.encode(passwd);
			} catch (NoSuchAlgorithmException e) {
				// do nothing.
			}

			if (existAccount == null) {
				return false;

			} else if (existAccount.getPasswd().equals(passwd)) {
				// AccountSession session = Application.getSession();
				// session.setActiveAccount(account);
				Application.account = account;

				return true;

			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * The process of logout is performed by this method. This method is called
	 * when a logged in user requests logout operation from the menu item or
	 * requests exit operation. Session is maintained by this method.
	 * 
	 * @param application
	 *            application your concrete application implementation.
	 * 
	 * @see com.cysols.framework.platform.LoginManager#logout(com.cysols.framework.platform.Application)
	 */
	protected void logout(Application application) {
		super.logout(application);
	}

	/**
	 * Changes the password of local account. This method changes the password
	 * of the specfied account into its new password string and stores the new
	 * password to the local account file.<br>
	 * True is returned if the password change is done successfully.
	 * 
	 * @param account
	 *            Account instance who want to change his password.
	 * @param newPasswd
	 *            a new pasword.
	 * @return true if the password is changed successfully, false otherwise.
	 * 
	 * @see com.cysols.framework.platform.LoginManager#changePassword(com.cysols.framework.platform.core.Account,
	 *      java.lang.String)
	 */
	public boolean changePassword(Account account, String newPasswd) {
		String key = account.getName();
		String passwd = newPasswd;

		try {
			passwd = MDEncoder.encode(passwd);
			account.setPasswd(passwd);
			accountMap.put(account.getName(), account);
			// baseProp.setProperty(key, passwd);
			this.save(accountMap);

			// AccountSession session = Application.getSession();
			if (account.getName().equals(Application.account.getName())) {

				Application.account.setPasswd(newPasswd);
			}

			return true;

		} catch (NoSuchAlgorithmException e) {
			return false;

		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Returns an array of the loaded accounts.
	 * 
	 * @return Account[] of all local accounts.
	 * 
	 * @see com.cysols.framework.platform.LoginManager#loadAccounts()
	 */
	public Account[] loadAccounts() {
		Account[] accounts = new Account[accountMap.size()];
		Set<String> enu = accountMap.keySet();
		int i = 0;
		for (Iterator<String> iterator = enu.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			accounts[i] = accountMap.get(key);
			i++;

		}
		return accounts;
	}

	/**
	 * Deletes the specified Account from account conf.
	 * 
	 * @param account
	 *            Account to be deleted.
	 * @return true if the specified Account is deleted.
	 * 
	 * @see com.cysols.framework.platform.LoginManager#delete(com.cysols.framework.platform.core.Account)
	 */
	public boolean delete(Account account) {
		String key = account.getName();
		accountMap.remove(key);

		try {
			this.save(accountMap);
			return true;

		} catch (IOException e) {
			return false;
		}
	}

	public Collection<String> loadRoles() {
		return Arrays.asList(USER_SELECTION);
	}

	// ------------------ private methods
	// -------------------------------------------------------------
	@SuppressWarnings("unused")
	private Map<String, Account> load() throws IOException,
			ClassNotFoundException {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		Map<String, Account> map = new HashMap<String, Account>();

		fis = new FileInputStream(ACCOUNT_FILE);
		if (fis != null) {
			ois = new ObjectInputStream(fis);
			readOIS(ois, map);

		} else {
			InputStream is = this.getClass().getResourceAsStream(ACCOUNT_FILE);
			if (is != null) {
				ois = new ObjectInputStream(is);
				readOIS(ois, map);
			} else {
				URL url = getClass().getResource("/" + ACCOUNT_FILE);

				if (url != null) {
					ois = new ObjectInputStream(url.openStream());
					readOIS(ois, map);
					// ois = new ObjectInputStream(url.openStream());
					// Account account = (Account) ois.readObject();
					// map.put(account.getName(), account);
				}
			}
		}

		if (ois != null)
			ois.close();
		if (fis != null)
			fis.close();

		return map;
	}

	private void readOIS(ObjectInputStream ois, Map<String, Account> map)
			throws IOException, ClassNotFoundException {
		Object obj = null;
		try {
			while ((obj = ois.readObject()) != null) {
				Account account = (Account) obj;
				map.put(account.getName(), account);
			}
		} catch (EOFException e) {

		}
	}

	private void save(Properties prop) throws IOException {
		File file = new File(ACCOUNT_FILE);

		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}

		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(prop);

		if (oos != null)
			oos.close();
		if (fos != null)
			fos.close();
	}

	private void save(Map<String, Account> actMap) throws IOException {
		File file = new File(ACCOUNT_FILE);

		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}

		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		for (Iterator<String> iterator = actMap.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			oos.writeObject(actMap.get(key));

		}
		if (oos != null)
			oos.close();
		if (fos != null)
			fos.close();
	}

	private class LoginAction implements ActionListener {

		private LoginDialog dialog;

		private LoginAction(LoginDialog dialog) {
			this.dialog = dialog;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		public void actionPerformed(ActionEvent ae) {
			if (ae == null)
				return;

			String account = dialog.getAccount();
			String passwd = dialog.getPasswd();
			// String localHostAddress =
			// HostsLoader.getInstance().getIPAddress();

			ToolKit tool = ToolKit.getInstance();

			if (account == null || account.trim().equals("")) {
				JOptionPane.showMessageDialog(dialog,
						tool.getString("MSG_NOUSER_ENTRY"),
						tool.getString("WARNING"), JOptionPane.WARNING_MESSAGE);

				return;
			}

			if (passwd == null || passwd.trim().equals("")) {
				JOptionPane.showMessageDialog(dialog,
						tool.getString("MSG_NOPASSWD_ENTRY"),
						tool.getString("WARNING"), JOptionPane.WARNING_MESSAGE);

				return;
			}

			Account accountObj = new Account(account, passwd, "");
			if (authenticate(accountObj)) {
				dialog.dispose();

			} else {
				JOptionPane.showMessageDialog(dialog,
						tool.getString("MSG_LOGIN_FAILED"),
						tool.getString("WARNING"), JOptionPane.WARNING_MESSAGE);

				dialog.clearPasswd();
			}
		}
	}
}
