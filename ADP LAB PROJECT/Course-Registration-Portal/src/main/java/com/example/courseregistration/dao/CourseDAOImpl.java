package com.example.courseregistration.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.example.courseregistration.config.HibernateUtil;
import com.example.courseregistration.model.Course;

@Repository
public class CourseDAOImpl implements CourseDAO {

	private final SessionFactory sessionFactory;

    public CourseDAOImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }
    
	@Override
	public void saveCourse(Course course) {
		Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
	}

	@Override
	public List<Course> getAllCourses() {
		try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Course", Course.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

	@Override
	public void updateCourse(Course course) {
		Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
	}

	@Override
	public void deleteCourse(Long id) {
		Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Course course = session.get(Course.class, id);
            if (course != null) {
                session.remove(course);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
	}
	
	@Override
	public Course getCourseById(Long id) {
		try (Session session = sessionFactory.openSession()) {
            return session.get(Course.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

}
