package com.shakun.ws.utils;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
	private static final SessionFactory sf;
	static {
		Configuration cf = new Configuration();
		cf.configure();
		try {
			sf = cf.buildSessionFactory();
		} catch (HibernateException he) {
			System.err.println("Session factory creation failed" + he);
			throw new ExceptionInInitializerError(he);
		}
	}
	public static SessionFactory getSessionFactory() {
		return sf;
	}
}
