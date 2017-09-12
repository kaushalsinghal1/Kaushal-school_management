package com.banti.framework.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.banti.framework.utils.CSVParser;

/**
 * This is a ValueObject that keeps a name, a passwd and a local host of the
 * account. This is an immutable object. Only when this constructor is called,
 * the name, passwd and his client host are specfied. So there is no setter
 * method.
 */
public final class Account implements Cloneable, Serializable {
	private String name;
	private String passwd;
	private String role;
	private long addDate;
	private long updateDate;
	private long pwUpdateDate;
	private Map<String, Object> accountDetails = new HashMap<String, Object>();

	/**
	 * Public Constructor. This is an immutable object. Only when this
	 * constructor is called, the name, passwd, his client address, server
	 * address which he connected and role are specified.
	 * 
	 * @param name
	 *            a name of this account.
	 * @param passwd
	 *            a password of this account
	 * @param role
	 *            a privilege given to a user
	 */
	public Account(String name, String passwd, String role) {

		this.name = name;
		this.passwd = passwd;
		this.role = role;
	}

	public Account(String name, String passwd, String role, long addDate,
			long updateDate, long pwUpdateDate) {
		this.name = name;
		this.passwd = passwd;
		this.role = role;
		this.addDate = addDate;
		this.updateDate = updateDate;
		this.pwUpdateDate = pwUpdateDate;
	}

	/**
	 * Returns a name of this account.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns an encoded password of this account.
	 * 
	 * @return encoded password.
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 * Set password.
	 * 
	 * @param passwd
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	public long getAddDate() {
		return addDate;
	}

	public void setAddDate(long addDate) {
		this.addDate = addDate;
	}

	public long getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(long updateDate) {
		this.updateDate = updateDate;
	}

	public long getPwUpdateDate() {
		return pwUpdateDate;
	}

	public void setPwUpdateDate(long pwUpdateDate) {
		this.pwUpdateDate = pwUpdateDate;
	}

	public void setAccountDetails(String constant, Object obj) {
		synchronized (accountDetails) {
			accountDetails.put(constant, obj);
		}
	}

	public Object getAccountDetails(String constant) {
		synchronized (accountDetails) {
			return accountDetails.get(constant);
		}
	}

	public boolean isActionPermitted(String actionName) {
		if (isSuperUser()) {
			return true;
		}
		Object o = null;

		// synchronized (accountDetails) {
		// o = accountDetails.get(ALLOWED_OPERATIONS);
		// }
		if (!(o instanceof Set)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Set<String> s = (Set<String>) o;
		return s.contains(actionName);
	}

	public boolean isSuperUser() {
		//
		return true;
		// return (Constants.ROLE_ADMINISTRATOR.equals(getRole()) &&
		// Constants.DOMAIN_TOP.equals(getDomain()));
	}

	public boolean isAdmin() {
		return false;
	}

	public String serialize() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(",");
		sb.append(passwd);
		sb.append(",");
		if (role != null) {
			sb.append(role);
		}
		sb.append(",");
		sb.append(addDate);
		sb.append(",");
		sb.append(updateDate);
		sb.append(",");
		sb.append(pwUpdateDate);
		return sb.toString();
	}

	public static Account deserialize(String s) {
		if (s == null) {
			return null;
		}
		String[] chunks = CSVParser.parse(s);

		if (chunks.length != 5) {
			return null;
		}
		try {
			return new Account(chunks[0], chunks[1], chunks[2],
					Long.parseLong(chunks[3]), Long.parseLong(chunks[4]),
					Long.parseLong(chunks[5]));

		} catch (NumberFormatException e) {
		}
		return null;
	}

	public Object clone() {
		Account a = new Account(name, passwd, role);
		synchronized (accountDetails) {
			a.accountDetails.putAll(accountDetails);
		}
		a.setAddDate(addDate);
		a.setUpdateDate(updateDate);
		a.setPwUpdateDate(pwUpdateDate);
		return a;
	}

}
