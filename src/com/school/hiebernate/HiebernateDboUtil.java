package com.school.hiebernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.SessionClassFeeDetails;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.hiebernate.dbo.UserLogin;

public class HiebernateDboUtil {

	public static UserLogin getLoginDetails(String account)
			throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(UserLogin.class);
		criteria.add(Restrictions.eq("account", account)); // 2
		@SuppressWarnings("unchecked")
		List<UserLogin> list = (List<UserLogin>) criteria.list();
		session.getTransaction().commit();
		session.close();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public static UserLogin getLoginDetails(String account, String password)
			throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(UserLogin.class);
		criteria.add(Restrictions.eq("account", account)); // 2
		criteria.add(Restrictions.eq("password", password)); // 2
		@SuppressWarnings("unchecked")
		List<UserLogin> list = (List<UserLogin>) criteria.list();
		session.getTransaction().commit();
		session.close();
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	// Sesion Class Fee Details

	private static List<SessionClassFeeDetails> getSessionFeeDetails(
			SessionClassFeeDetails details) throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session
				.createCriteria(SessionClassFeeDetails.class);
		if (details != null) {
			criteria.add(Restrictions.eq("sessionDetails",
					details.getSessionDetails()));
			criteria.add(Restrictions.eq("classDetails",
					details.getClassDetails()));
		}
		criteria.add(Restrictions.isNull("deleteDate"));
		// criteria.addOrder(Order.asc("displayString"));
		@SuppressWarnings("unchecked")
		List<SessionClassFeeDetails> list = (List<SessionClassFeeDetails>) criteria
				.list();
		session.getTransaction().commit();
		session.close();
		return list;
	}

	public static List<SessionClassFeeDetails> getSessionFeeDetails()
			throws HibernateException {
		return getSessionFeeDetails(null);
	}

	public static SessionClassFeeDetails getSessionFeeDetails(
			SessionDetails sessionDetails, ClassDetails classDetails)
			throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session
				.createCriteria(SessionClassFeeDetails.class);
		criteria.add(Restrictions.eq("sessionDetails", sessionDetails));
		criteria.add(Restrictions.eq("classDetails", classDetails));
		criteria.add(Restrictions.isNull("deleteDate"));
		// criteria.addOrder(Order.asc("displayString"));
		@SuppressWarnings("unchecked")
		List<SessionClassFeeDetails> list = (List<SessionClassFeeDetails>) criteria
				.list();
		session.getTransaction().commit();
		session.close();
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public static boolean saveSessionFeeDetails(SessionClassFeeDetails details)
			throws HibernateException {

		List<SessionClassFeeDetails> detailsList = getSessionFeeDetails(details);
		if (detailsList == null || detailsList.size() < 1) {
			int id = getMaxId(SessionClassFeeDetails.class, "sessionFeeId");
			details.setSessionFeeId(++id);
			saveObject(details);
			return true;
		}
		return false;
	}

	public static boolean updateSessionFeeDetails(SessionClassFeeDetails details)
			throws HibernateException {
		List<SessionClassFeeDetails> detailsList = getSessionFeeDetails(details);
		if (detailsList == null || detailsList.size() <= 1) {
			updateObject(details);
			return true;
		}
		return false;
	}

	public static boolean deleteSessionFeeDetails(SessionClassFeeDetails details)
			throws HibernateException {
		updateObject(details);
		return true;
	}

	// Session Details

	private static List<SessionDetails> getSessionDetails(SessionDetails details)
			throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(SessionDetails.class);
		if (details != null) {
			criteria.add(Restrictions.eq("displayString",
					details.getDisplayString()));
		}
		criteria.add(Restrictions.isNull("deleteDate"));
		criteria.addOrder(Order.asc("displayString"));
		@SuppressWarnings("unchecked")
		List<SessionDetails> list = (List<SessionDetails>) criteria.list();
		session.getTransaction().commit();
		session.close();
		return list;
	}

	public static List<SessionDetails> getSessionDetails()
			throws HibernateException {
		return getSessionDetails(null);
	}

	public static boolean saveSessionDetails(SessionDetails details)
			throws HibernateException {

		List<SessionDetails> detailsList = getSessionDetails(details);
		if (detailsList == null || detailsList.size() < 1) {
			int id = getMaxId(SessionDetails.class, "sessionId");
			details.setSessionId(++id);
			saveObject(details);
			return true;
		}
		return false;
	}

	public static boolean updateSessionDetails(SessionDetails details)
			throws HibernateException {
		List<SessionDetails> detailsList = getSessionDetails(details);
		if (detailsList == null || detailsList.size() <= 1) {
			updateObject(details);
			return true;
		}
		return false;
	}

	public static boolean deleteSessionDetails(SessionDetails details)
			throws HibernateException {
		updateObject(details);
		return true;
	}

	// Class Details

	private static List<ClassDetails> getClassDetails(ClassDetails details)
			throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(ClassDetails.class);
		if (details != null) {
			criteria.add(Restrictions.eq("className", details.getClassName()));
		}
		criteria.add(Restrictions.isNull("deleteDate"));
		criteria.addOrder(Order.asc("displaySession"));
		@SuppressWarnings("unchecked")
		List<ClassDetails> list = (List<ClassDetails>) criteria.list();
		session.getTransaction().commit();
		session.close();
		return list;
	}

	public static List<ClassDetails> getClassDetails()
			throws HibernateException {
		return getClassDetails(null);
	}

	public static boolean saveClassDetails(ClassDetails details)
			throws HibernateException {
		List<ClassDetails> detailsList = getClassDetails(details);
		if (detailsList == null || detailsList.size() < 1) {
			int id = getMaxId(ClassDetails.class, "classId");
			details.setClassId(++id);
			saveObject(details);
			return true;
		}
		return false;
	}

	public static boolean updateClassDetails(ClassDetails details)
			throws HibernateException {
		List<ClassDetails> detailsList = getClassDetails(details);
		if (detailsList == null || detailsList.size() <= 1) {
			updateObject(details);
			return true;
		}
		return false;
	}

	public static boolean deleteClassDetails(ClassDetails details)
			throws HibernateException {
		updateObject(details);
		return true;
	}

	public static boolean saveUserUniqueLogin(UserLogin userLogin)
			throws HibernateException {
		UserLogin login = getLoginDetails(userLogin.getAccount());
		if (login == null) {
			int id = getMaxId(UserLogin.class, "userId");
			userLogin.setUserId(++id);
			saveUserLogin(userLogin);
			return true;
		}
		return false;
	}

	private static UserLogin saveUserLogin(UserLogin userLogin)
			throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(userLogin);
		session.getTransaction().commit();
		session.close();
		return userLogin;
	}

	public static UserLogin updateUserLogin(UserLogin userLogin)
			throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		UserLogin userLogin1 = (UserLogin) session.load(UserLogin.class,
				userLogin.getUserId());
		if (userLogin1 != null) {
			session.update(userLogin);
		}
		session.getTransaction().commit();
		session.close();
		return userLogin1;
	}

	public static UserLogin deleteUserLogin(UserLogin userLogin)
			throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		UserLogin userLogin1 = (UserLogin) session.load(UserLogin.class,
				userLogin.getUserId());
		if (userLogin1 != null) {
			session.delete(userLogin);
		}
		session.getTransaction().commit();
		session.close();
		return userLogin1;
	}

	// ----------
	static void saveObject(Object object) {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		object = session.save(object);
		session.getTransaction().commit();
		session.close();
	}

	static void updateObject(Object object) {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(object);
		session.getTransaction().commit();
		session.close();
	}

	static Object getObject(Object c1, String prperty) {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Object obj = session.get(c1.getClass(), prperty);
		session.getTransaction().commit();
		session.close();
		return obj;
	}

	static Session getSession() {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		return sessionFactory.openSession();

	}

	static int getMaxId(Class class1, String property) {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Criteria criteria = session.createCriteria(class1).setProjection(
				Projections.max(property));
		List<Integer> sumList = (List<Integer>) criteria.list();
		if (sumList.size() == 1) {
			return sumList.get(0) == null ? 0 : sumList.get(0);
		}
		return 0;
	}
}
