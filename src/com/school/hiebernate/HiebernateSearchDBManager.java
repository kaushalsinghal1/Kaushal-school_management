package com.school.hiebernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.SessionDetails;
import com.school.hiebernate.dbo.StudentDetails;
import com.school.hiebernate.dbo.StudentFeeDetails;

public class HiebernateSearchDBManager extends SearchDBManager {

	@Override
	public List<StudentDetails> getStudentDetails(String firstName,
			String lastName, String fatherName, ClassDetails selectedClass,
			SessionDetails selectedSession) throws RuntimeException {
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
		if (firstName != null)
			criteria.add(Restrictions.like("firstName", firstName));
		if (lastName != null)
			criteria.add(Restrictions.like("lastName", lastName));
		if (fatherName != null)
			criteria.add(Restrictions.like("fatherName", fatherName));

		criteria.addOrder(Order.asc("firstName"));

		criteria.add(Restrictions.isNull("deleteDate"));
		// criteria.addOrder(Order.asc("displayString"));
		@SuppressWarnings("unchecked")
		List<StudentDetails> list = (List<StudentDetails>) criteria.list();
		session.getTransaction().commit();
		session.close();
		return list;

	}

	@Override
	public List<StudentFeeDetails> getStudentFeeDetails(
			ClassDetails classDetails, SessionDetails sessionDetails)
			throws RuntimeException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(StudentFeeDetails.class);
		if (classDetails != null && classDetails.getClassId() != 0) {
			criteria.add(Restrictions.eq("classDetails", classDetails));
		}
		if (sessionDetails != null && sessionDetails.getSessionId() != 0) {
			criteria.add(Restrictions.eq("sessionDetails", sessionDetails));
		}
		criteria.add(Restrictions.isNull("deleteDate"));
		List<StudentFeeDetails> list = (List<StudentFeeDetails>) criteria
				.list();
		session.getTransaction().commit();
		session.close();
		return list;
	}

}
