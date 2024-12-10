package com.example.courseregistration.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import com.example.courseregistration.model.*;

import java.util.HashMap;
import java.util.Map;

public class HibernateUtil {
	private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }
    
    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
	        Map<String, Object> settings = new HashMap<>();
	        settings.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
	        settings.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/course_registration");
	        settings.put("hibernate.connection.username", "root");
	        settings.put("hibernate.connection.password","shantanu2002");
	        settings.put("hibernate.hbm2ddl.auto", "update");
	        settings.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
	        settings.put("hibernate.dialect.storage_engine", "innodb");
	        settings.put("hibernate.show_sql", "true");
	
	        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(settings).build();
	        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
	        metadataSources.addPackage("edu.neu.csye6200.courseregistration.model");
	        metadataSources.addAnnotatedClasses(Course.class, Professor.class, Student.class, User.class, UserRole.class);
	
	        Metadata metadata = metadataSources.buildMetadata();
	
	        sessionFactory = metadata.getSessionFactoryBuilder().build();
        }
        return sessionFactory;
    }
}