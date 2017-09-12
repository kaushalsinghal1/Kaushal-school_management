package com.school.hiebernate;

import org.hibernate.HibernateException;

public class DBOFacotry {
	private static volatile SearchDBManager searchDBManager;

	public static SearchDBManager getSearchDBManager() {
		if (searchDBManager == null) {
			searchDBManager = new HiebernateSearchDBManager();
		}
		return searchDBManager;
	}
	public static void initializeDBConnection() throws Exception {
		HiebernetUtill.getSessionFactory();
	}


}
