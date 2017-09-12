package com.school.hiebernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.school.hiebernate.dbo.ClassDetails;
import com.school.hiebernate.dbo.DepositeFeeMaster;
import com.school.hiebernate.dbo.SessionDetails;

public class HiebernateDboReprotUtil {

	public static List<DepositeFeeMaster> getFeeMasterDetails(Date start,
			Date end) throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(DepositeFeeMaster.class);
		criteria.add(Restrictions.between("depositeDate", start, end));
		criteria.add(Restrictions.isNull("deleteDate"));
		criteria.addOrder(Order.asc("depositeDate"));
		@SuppressWarnings("unchecked")
		List<DepositeFeeMaster> list = (List<DepositeFeeMaster>) criteria
				.list();
		session.getTransaction().commit();
		session.close();
		return list;
	}

	public static List<DepositeFeeMaster> getFeeMasterDetails(Date start,
			Date end, SessionDetails sessionDetails, ClassDetails classDetails)
			throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(DepositeFeeMaster.class);
		if (sessionDetails != null) {
			criteria.add(Restrictions.eq("sessionDetails", sessionDetails));
		}
		if (classDetails != null) {
			criteria.add(Restrictions.eq("classDetails", classDetails));
		}
		criteria.add(Restrictions.between("depositeDate", start, end));
		criteria.add(Restrictions.isNull("deleteDate"));
		criteria.addOrder(Order.asc("depositeDate"));
		@SuppressWarnings("unchecked")
		List<DepositeFeeMaster> list = (List<DepositeFeeMaster>) criteria
				.list();
		session.getTransaction().commit();
		session.close();
		return list;
	}

	public static long getTotalDepositeFee(Date start, Date end,
			SessionDetails sessionDetails, ClassDetails classDetails)
			throws HibernateException {
		SessionFactory sessionFactory = HiebernetUtill.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(DepositeFeeMaster.class)
				.setProjection(Projections.sum("totalAmount"));

		if (sessionDetails != null) {
			criteria.add(Restrictions.eq("sessionDetails", sessionDetails));
		}
		if (classDetails != null) {
			criteria.add(Restrictions.eq("classDetails", classDetails));
		}

		criteria.add(Restrictions.between("depositeDate", start, end));
		criteria.add(Restrictions.isNull("deleteDate"));
		@SuppressWarnings("unchecked")
		List<Integer> sumList = (List<Integer>) criteria.list();
		session.getTransaction().commit();
		session.close();
		Object i = sumList.get(0);
		if (i == null) {
			return 0;
		}
		long l = (Long) i;
		return l;
	}
}
