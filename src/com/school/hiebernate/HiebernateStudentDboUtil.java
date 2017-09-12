package com.school.hiebernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.DepositeFeeMaster;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.hiebernate.dbo.StudentDetails;
import com.school.hiebernate.dbo.StudentFeeDetails;
import com.school.hiebernate.dbo.StudentImageDetails;

public class HiebernateStudentDboUtil {
	// Student Details

	public static List<StudentDetails> getStudentDetails(StudentDetails details)
			throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(StudentDetails.class);
		if (details != null) {
			if (details.getStudentId() > 0) {
				criteria.add(Restrictions.eq("studentId",
						details.getStudentId()));
			} else {
				criteria.add(Restrictions.eq("firstName",
						details.getFirstName()));
				criteria.add(Restrictions.eq("fatherName",
						details.getFatherName()));
				criteria.add(Restrictions.eq("sessionDetails",
						details.getSessionDetails()));
				criteria.add(Restrictions.eq("classDetails",
						details.getClassDetails()));
			}

		}
		criteria.add(Restrictions.isNull("deleteDate"));
		// criteria.addOrder(Order.asc("displayString"));
		@SuppressWarnings("unchecked")
		List<StudentDetails> list = (List<StudentDetails>) criteria.list();
		session.getTransaction().commit();
		session.close();
		return list;
	}

	public static List<StudentDetails> getStudentDetails(
			ClassDetails selectedClass, SessionDetails selectedSession) {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(StudentDetails.class);
		if (selectedClass != null && selectedClass.getClassId() != 0) {
			criteria.add(Restrictions.eq("classDetails", selectedClass));

		}
		if (selectedSession != null && selectedSession.getSessionId() != 0) {
			criteria.add(Restrictions.eq("sessionDetails", selectedSession));
		}

		criteria.add(Restrictions.isNull("deleteDate"));
		// criteria.addOrder(Order.asc("displayString"));
		@SuppressWarnings("unchecked")
		List<StudentDetails> list = (List<StudentDetails>) criteria.list();
		session.getTransaction().commit();
		session.close();
		return list;

	}

	public static List<StudentDetails> getStudentDetails()
			throws HibernateException {
		return getStudentDetails(null);
	}

	public static StudentDetails saveStudentDetails(StudentDetails details)
			throws HibernateException {

		// List<StudentDetails> detailsList = getStudentDetails(details);
		// if (detailsList == null || detailsList.size() < 1) {
		int id = HiebernateDboUtil.getMaxId(StudentDetails.class, "studentId");
		details.setStudentId(++id);
		HiebernateDboUtil.saveObject(details);
		return details;
		// }
		// return false;
	}

	public static boolean updateStudentDetails(StudentDetails details)
			throws HibernateException {
		List<StudentDetails> detailsList = getStudentDetails(details);
		if (detailsList == null || detailsList.size() <= 1) {
			HiebernateDboUtil.updateObject(details);
			return true;
		}
		return false;
	}

	public static boolean deleteStudentDetails(StudentDetails details)
			throws HibernateException {
		HiebernateDboUtil.updateObject(details);
		return true;
	}

	// ---------------Student Image
	public static StudentImageDetails saveStudentImageDetails(
			StudentImageDetails details) throws HibernateException {

		// List<StudentDetails> detailsList = getStudentDetails(details);
		// if (detailsList == null || detailsList.size() < 1) {
		int id = HiebernateDboUtil.getMaxId(StudentImageDetails.class,
				"imageId");
		details.setImageId(++id);
		HiebernateDboUtil.saveObject(details);
		return details;
	}

	// ---------------Student Image
	public static boolean updateStudentImageDetails(StudentImageDetails details)
			throws HibernateException {
		HiebernateDboUtil.updateObject(details);
		return true;
	}

	// --------------------------StudentFeeDetails
	/**
	 * 
	 * @param details
	 *            StudentDetails
	 * @return
	 * @throws HibernateException
	 */
	public static List<StudentFeeDetails> getStudentFeeDetails(
			StudentDetails details) throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(StudentFeeDetails.class);
		if (details != null) {
			criteria.add(Restrictions.eq("studentDetails", details));
		}
		criteria.add(Restrictions.isNull("deleteDate"));
		List<StudentFeeDetails> list = (List<StudentFeeDetails>) criteria
				.list();
		session.getTransaction().commit();
		session.close();
		return list;
	}

	/*
	 * public static List<StudentDetails> getStudentDetails() throws
	 * HibernateException { return getStudentDetails(null); }
	 */
	public static StudentFeeDetails saveStudentFeeDetails(
			StudentFeeDetails details) throws HibernateException {

		List<StudentFeeDetails> detailsList = getStudentFeeDetails(details
				.getStudentDetails());
		if (detailsList == null || detailsList.size() < 1) {
			int id = HiebernateDboUtil.getMaxId(StudentFeeDetails.class,
					"studentFeeId");
			details.setStudentFeeId(++id);
			HiebernateDboUtil.saveObject(details);
			return details;
		}
		return null;
	}

	public static List<StudentFeeDetails> saveStudentFeeDetailsList(
			List<StudentFeeDetails> details) throws HibernateException {
		for (StudentFeeDetails feeDetails : details) {
			saveStudentFeeDetails(feeDetails);
		}
		return details;
	}

	public static List<StudentDetails> saveStudentDetailsList(
			List<StudentDetails> details) throws HibernateException {
		for (StudentDetails feeDetails : details) {
			saveStudentDetails(feeDetails);
		}
		return details;
	}

	public static boolean updateStudentFeeDetails(StudentFeeDetails details)
			throws HibernateException {
		List<StudentFeeDetails> detailsList = getStudentFeeDetails(details
				.getStudentDetails());
		if (detailsList.size() == 1) {
			HiebernateDboUtil.updateObject(details);
			return true;
		}
		return false;
	}

	public static boolean deleteStudentFeeDetails(StudentFeeDetails details)
			throws HibernateException {
		HiebernateDboUtil.updateObject(details);
		// also need to update student fee details, deposite fee master
		return true;
	}

	// Deposit Fee Master Details

	public static List<DepositeFeeMaster> getDepositeFeeMasterDetails(
			StudentDetails details) throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(DepositeFeeMaster.class);
		if (details != null) {
			criteria.add(Restrictions.eq("studentDetails", details));
		}
		criteria.add(Restrictions.isNull("deleteDate"));
		// criteria.addOrder(Order.asc("displayString"));
		@SuppressWarnings("unchecked")
		List<DepositeFeeMaster> list = (List<DepositeFeeMaster>) criteria
				.list();
		session.getTransaction().commit();
		session.close();
		return list;
	}

	public static List<DepositeFeeMaster> getDepositeFeeMasterDetails()
			throws HibernateException {
		return getDepositeFeeMasterDetails(null);
	}

	public static DepositeFeeMaster saveDepositeFeeMasterDetails(
			DepositeFeeMaster details, StudentFeeDetails feeDetails)
			throws HibernateException {
		int id = HiebernateDboUtil.getMaxId(DepositeFeeMaster.class,
				"depositeFeeId");
		details.setDepositeFeeId(++id);
		Session session = HiebernateDboUtil.getSession();
		session.beginTransaction();
		session.save(details);
		session.update(feeDetails);
		session.getTransaction().commit();
		session.close();
		return details;
		// }
		// return false;
	}

	public static boolean updateDepositeFeeMasterDetails(
			DepositeFeeMaster details) throws HibernateException {

		HiebernateDboUtil.updateObject(details);
		return true;

	}

	public static boolean deleteDepositeFeeMasterDetails(
			DepositeFeeMaster details) throws HibernateException {
		HiebernateDboUtil.updateObject(details);
		return true;
	}

}
