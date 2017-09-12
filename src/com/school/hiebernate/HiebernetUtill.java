package com.school.hiebernate;

import java.io.File;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HiebernetUtill {
	private static final SessionFactory SESSION_FACTORY = buildSessionFactory();
	private static final String HIEBERNET_CONFIG = "config/hibernate.cfg.xml";

	private static SessionFactory buildSessionFactory()
			throws HibernateException {
		Configuration configuration = new Configuration();
		if (new File(HIEBERNET_CONFIG).exists()) {
			configuration.configure(new File(HIEBERNET_CONFIG));
		} else {
			configuration.configure();
		}
		// configuration.configure();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.buildServiceRegistry();
		SessionFactory sessionFactory = configuration
				.buildSessionFactory(serviceRegistry);
		return sessionFactory;
	}

	public static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}

}
